package com.example.firebaselogin

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.MediaController
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.load.model.GlideUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_home.*
import java.net.URI


enum class ProviderType{
    BASIC
}
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle: Bundle? =intent.extras
        val email: String? =bundle?.getString("email")
        val provider: String? =bundle?.getString("provider")
        setup(email?: "", provider ?:"")
        abrirCamara_Click()


    }
    private val REQUEST_GALERIA = 1001
    private val REQUEST_CAMARA = 1002
    private val REQUEST_VIDEO = 101
    var imagen: Uri? = null

    private fun setup(email: String, provider: String){
        title =  "Inicio"
        emailTextView.text = email
        providerTextView.text = provider
        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }
    private fun abrirGaleria_Click(){
        btnAbrirGaleria.setOnClickListener(){
            //Verificar version Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //Permiso
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //Pide permiso a Usuario
                    val permisoArchivos = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permisoArchivos, REQUEST_GALERIA)

                }else
                //Tiene permiso
                    mostrarGaleria()
            }else
            //Tiene version de Android 8.1 Oreo hacia abajo
                mostrarGaleria()
        }
    }
    private fun mostrarGaleria(){
        //Mostrar una imagen de la galeria
        val intentGaleria = Intent(Intent.ACTION_PICK)
        intentGaleria.type = "image/*"
        startActivityForResult(intentGaleria,REQUEST_GALERIA)
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_GALERIA ->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mostrarGaleria()
                else
                    Toast.makeText(applicationContext, "No tiene acceso a las imagenes", Toast.LENGTH_SHORT).show()
            }
            REQUEST_CAMARA ->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    abrirCamara()
                else
                    Toast.makeText(applicationContext, "No tiene acceso a la camara", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun abrirCamara_Click(){
        btnAbrirCamara.setOnClickListener(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //Aviso de permiso a Usuario
                    val permisosCamara = arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permisosCamara,REQUEST_CAMARA)
                }else
                    abrirCamara()
            }else
                abrirCamara()
        }
    }

    //Abrir camara y tomar foto
    private fun abrirCamara(){
        val value = ContentValues()
        //Imagen de tipo Media
        value.put(MediaStore.Images.Media.TITLE, "Imagen Nueva")
        imagen = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imagen)
        startActivityForResult(camaraIntent,REQUEST_CAMARA)
    }


}