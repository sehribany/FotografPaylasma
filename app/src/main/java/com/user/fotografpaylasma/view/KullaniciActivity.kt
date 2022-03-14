package com.user.fotografpaylasma.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.user.fotografpaylasma.R
import kotlinx.android.synthetic.main.activity_main.*

class KullaniciActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()

        val guncelKullanici=auth.currentUser
        if(guncelKullanici !=null){
            val intent=Intent(this, HaberlerActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    fun girisYap(view:View){

        auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener {
            if(it.isSuccessful){

                val guncelKullanici=auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldin: ${guncelKullanici}",Toast.LENGTH_LONG).show()

                val intent=Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()


            }
        }.addOnFailureListener {
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    fun kayitOl(view:View){

        val email= emailText.text.toString()
        val password= passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            //asenkron
            if(it.isSuccessful){
                //diğer activiteye gidicez
                val intent= Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            //hata olursa ne olacak
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }



    }
}