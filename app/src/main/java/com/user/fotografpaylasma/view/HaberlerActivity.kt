package com.user.fotografpaylasma.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.user.fotografpaylasma.model.Post
import com.user.fotografpaylasma.R
import com.user.fotografpaylasma.adapter.HaberRecyclerAdapter
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {
    private  lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseFirestore

    private lateinit var recyclerViewAdapter:HaberRecyclerAdapter
    var postListesi=ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()

        verileriAlma()
        var layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerViewAdapter= HaberRecyclerAdapter(postListesi)
        recyclerView.adapter=recyclerViewAdapter

    }

    fun verileriAlma(){

        database.collection("Post").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null){
                    if(value.isEmpty == false){ //value boş değilse
                        val documents=value.documents
                        postListesi.clear()
                        for(document in documents){
                            val email =document.get("email") as String
                            val yorum=document.get("yorum") as String
                            val gorsel=document.get("gorselurl") as String

                            val indirilenPost= Post(email,yorum,gorsel)
                            postListesi.add(indirilenPost)

                        }
                        recyclerViewAdapter.notifyDataSetChanged()

                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.secenekler_menusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.fotograf_paylas){
            //Foto paylaşma activitesine gidilecek
            val intent=Intent(this, FotografPaylasmaActivity::class.java)
            startActivity(intent)

        }else if(item.itemId== R.id.cikis_yap){
            auth.signOut()
            val intent= Intent(this, KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}