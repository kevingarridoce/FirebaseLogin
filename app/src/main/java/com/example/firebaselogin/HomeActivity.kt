package com.example.firebaselogin

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.load.model.GlideUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_home.*

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



    }
    private fun setup(email: String, provider: String){
        title =  "Inicio"
        emailTextView.text = email
        providerTextView.text = provider
        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }

}