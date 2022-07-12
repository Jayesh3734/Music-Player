package com.example.jkmusicplayer;

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SongsListRVAdapter(
    var songs: ArrayList<SongsList>,
    var context: Context,
    var onviewClickedListner: Any
) :
    RecyclerView.Adapter<SongsListRVAdapter.SongListHolder>() {
    interface OnviewClickedListner {
        fun onViewCiclked(SongPath: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListHolder {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.singview, parent, false)
        return SongListHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SongListHolder, position: Int) {
        val thisSong = songs[position]
        holder.tvTitle.text = thisSong.title
        holder.tvSinger.text = thisSong.artist
        holder.ivPhoto.setImageBitmap(thisSong.bitmap)
        holder.tvDuration.text = thisSong.minlength.toString() + ":" + thisSong.seclength
        holder.thisView.setOnClickListener {
            Log.d("23456", "onClick:  View is Clicked")
            onviewClickedListner.onViewCiclked(thisSong.songPath)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun SongListUpdated(temp: ArrayList<SongsList>) {
        songs = temp
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SongListHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvSinger: TextView
        var tvDuration: TextView
        var thisView: View
        var ivPhoto: ImageView

        init {
            tvTitle = itemView.findViewById<View>(R.id.tv_SongTitle) as TextView
            tvSinger = itemView.findViewById<View>(R.id.tv_Singer) as TextView
            tvDuration = itemView.findViewById<View>(R.id.tv_Duration) as TextView
            ivPhoto = itemView.findViewById<View>(R.id.iv_SongImage) as ImageView
            thisView = itemView
        }
    }
}
