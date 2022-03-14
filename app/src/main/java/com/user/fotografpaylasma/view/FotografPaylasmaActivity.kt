package com.user.fotografpaylasma.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.user.fotografpaylasma.R
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.io.File
import java.util.*

class FotografPaylasmaActivity : AppCompatActivity() {
    var secilenGorsel : Uri?=null
    var secilenBitmap:Bitmap?=null

    private lateinit var storage:FirebaseStorage
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)

        storage=FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()

    }


    fun fotoPaylas(view:View){
        //depo işemler
        ///UUID
        val uuid=UUID.randomUUID()
        val gorselismi="${uuid}.jpg"
        val reference=storage.reference
        val gorselReference= reference.child("images").child(gorselismi)
        if(secilenGorsel !=null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                val yuklenengorselReference=FirebaseStorage.getInstance().reference.child("images").child(gorselismi)
                yuklenengorselReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl= it.toString()
                    val guncelKullaniciEmail= auth.currentUser!!.email.toString()
                    val kullaniciYorumu=yorumText.text.toString()
                    val tarih =Timestamp.now()

                    //veritabanı

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("gorselurl",downloadUrl)
                    postHashMap.put("email",guncelKullaniciEmail)
                    postHashMap.put("yorum",kullaniciYorumu)
                    postHashMap.put("tarih",tarih)
                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun gorselSec(view:View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //izin verildiye eşit değil ise . İzin alınmamış ise
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else{
            //izin varsa
            val galeriintent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriintent,2)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            //izin verilmiş mi  kontrol eder
            if(grantResults.size > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){//verilen sonuçlar dizisi
                //izin verildiyse yapılacaklar
                val galeriintent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriintent,2)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data != null ){
            secilenGorsel=data.data

            if(secilenGorsel !=null){

                if(Build.VERSION.SDK_INT>=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap=ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)
                }else{

                    secilenBitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageView.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



 }