package com.example.jkmusicplayer;

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jkmusicplayer.SongsListRVAdapter.OnviewClickedListner
import java.nio.FloatBuffer


class MainActivity : AppCompatActivity() {
    var CurrentPositon = 0
    var etSearch: EditText? = null
    var count = 0
    var cursor: Cursor? = null
    var songsListRVAdapter: SongsListRVAdapter? = null
    var rvSongsList: RecyclerView? = null
    var btn_search: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CurrentPositon = intent.getIntExtra("CurrentPosition", 0)
        etSearch = findViewById<View>(R.id.et_Serach) as EditText
        btn_search = findViewById<View>(R.id.btn_Search) as ImageButton
        songList
        val premcode: Int
        premcode =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        songList
        Log.d(TAG, "onCreate: Songs Fetched")
        rvSongsList = findViewById<View>(R.id.rv_SongsList) as RecyclerView
        Log.d(TAG, "onCreate: sharuu")
        rvSongsList!!.layoutManager = LinearLayoutManager(this)
        songsListRVAdapter = SongsListRVAdapter(songsLists, this, object : OnviewClickedListner {
            override fun onViewCiclked(SongPath: String?) {
                val Paths = ArrayList<String>()
                val thisSong = songsLists[position]
                val thisBumdle = Bundle()
                thisBumdle.putString("Title", thisSong.Title)
                thisBumdle.putString("SongPath", thisSong.songPath)
                thisBumdle.putString("ArtistName", thisSong.Artist)
                thisBumdle.putString("Minute", thisSong.minlength.toString())
                thisBumdle.putString("Second", thisSong.seclength)
                val i = Intent(this@MainActivity, PlayMusicActivity::class.java)
                i.putExtra("Info", thisBumdle)
                val data = Bundle()
                data.putSerializable("data", songsLists as Serializable)
                data.putInt("position", position)
                i.putExtra("bundle", data)
                startActivity(i)
                val data = Bundle()
                if (count == 0) {
                    count = 1
                } else {
                    PlayMusicActivity.mp.release()
                }
                var pos = 0
                for (j in songsLists.indices) {
                    if (SongPath === songsLists[j].songPath) {
                        pos = j
                        break
                    }
                }
                val flag: Int
                flag = if (pos == CurrentPositon) {
                    1
                } else {
                    0
                }
                data.putInt("flag", flag)
                data.putInt("position", pos)
                i.putExtra("data", data)
                startActivity(i)
            }
        })
        rvSongsList!!.adapter = songsListRVAdapter
        btn_search!!.setOnClickListener {
            etSearch!!.visibility = View.VISIBLE
            etSearch!!.isCursorVisible = true
            etSearch!!.setText("")
        }
        etSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val temp = ArrayList<SongsList>()
                for (i in songsLists.indices) {
                    val thisSong = songsLists[i]
                    if (thisSong.title.contains(s)) {
                        temp.add(thisSong)
                    }
                    songsListRVAdapter!!.SongListUpdated(temp)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    val songList: Unit
        @SuppressLint("Range")
        get() {
            val fullsongpath: MutableList<String> = ArrayList()
            val allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            cursor = managedQuery(allsongsuri, null, selection, null, null)
            if (cursor != null) {
                if (cursor!!.moveToFirst()) {
                    do {
                        val cid =
                            cursor!!.getLong(cursor!!.getColumnIndex(MediaStore.Audio.Media._ID))
                        val name =
                            cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                        val id =
                            cursor!!.getInt(cursor!!.getColumnIndex(MediaStore.Audio.Media._ID))
                        val songPath =
                            cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                        fullsongpath.add(songPath)
                        val artistName = cursor!!.getString(
                            cursor!!
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST)
                        )
                        val artistId = cursor!!.getInt(
                            cursor!!
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                        )
                        val albumName = cursor!!.getString(
                            cursor!!
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM)
                        )
                        val albumId = cursor!!.getInt(
                            cursor!!
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                        )
                        val trackUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            cid
                        )
                        var bitmap: Bitmap? = null
                        val mdr = MediaMetadataRetriever()
                        mdr.setDataSource(applicationContext, trackUri)
                        val data = mdr.embeddedPicture
                        if (data != null) {
                            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                        }
                        val Dur =
                            cursor!!.getInt(cursor!!.getColumnIndex(MediaStore.Audio.Media.DURATION)) / 1000
                        val min = Dur / 60
                        val remSec = Dur - min * 60
                        var x = remSec.toString()
                        if (x.length == 1) {
                            x = "0$x"
                        }
                        songsLists.add(
                            SongsList(
                                name, artistName, songPath, albumName, artistId, albumId, min, x,
                                bitmap!!
                            )
                        )
                    } while (cursor!!.moveToNext())
                }
                cursor!!.close()
            }
        }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Yes Function Will Be Called")
    }

    companion object {
        @JvmField
//        var songsLists: FloatBuffer = TODO("initialize me")
        const val TAG = "Main Activity"
        var songsLists = ArrayList<SongsList>()
    }
}

