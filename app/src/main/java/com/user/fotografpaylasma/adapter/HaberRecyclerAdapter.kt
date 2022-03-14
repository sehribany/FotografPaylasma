package com.user.fotografpaylasma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.user.fotografpaylasma.R
import com.user.fotografpaylasma.model.Post
import kotlinx.android.synthetic.main.recycler_row.view.*


class HaberRecyclerAdapter(val postList:ArrayList<Post>) : RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>(){

    class PostHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.recycler_row_email.text=postList[position].email
        holder.itemView.recyler_row_yorum.text=postList[position].yorum
        Picasso.get().load(postList[position].url).into(holder.itemView.recycler_row_gorsel)



    }

    override fun getItemCount(): Int {
        return postList.size
    }

}