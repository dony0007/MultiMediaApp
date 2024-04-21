package com.example.music

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.adapters.MusicAdapter
import com.example.music.model.MusicListItem
import java.io.File

class MusicActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1

    companion object{
        lateinit var musicList: ArrayList<MusicListItem>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val playPause = findViewById<ImageView>(R.id.imageView5)
        val musicCover = findViewById<ImageView>(R.id.imageView)
        var musicRV = findViewById<RecyclerView>(R.id.recyclerview)

        musicList = getAllAudio()

        if (checkPermission()) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        musicCover.setImageResource(R.drawable.image1)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE),
            0
        )

        var mediaPlayer = MediaPlayer.create(this,R.raw.ringtone)
        playPause.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                playPause.setImageResource(R.drawable.pause)
            } else {
                mediaPlayer.pause()
                playPause.setImageResource(R.drawable.play)
            }
        }

        musicRV.layoutManager = LinearLayoutManager(this)
        musicRV.adapter = MusicAdapter(applicationContext, musicList, mediaPlayer, playPause, musicCover)


    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }
        private fun getAllAudio(): ArrayList<MusicListItem> {
            val tempList = ArrayList<MusicListItem>()
            val selection = MediaStore.Audio.Media.IS_MUSIC+" != 0"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED,MediaStore.Audio.Media.DATA)
            val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
                MediaStore.Audio.Media.DATE_ADDED+ " DESC", null)
            if(cursor != null)
                if(cursor.moveToFirst())
                    do {

                        var columnTitle = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                        var columnId = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                        var columnPath = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                        var columnDuration = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                        var columnAlbum = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                        var columnArtist = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

                        var titleC = cursor.getString(columnTitle!!)
                        var idC = cursor.getString(columnId!!)
                        var albumC = cursor.getString(columnAlbum!!)
                        var artistC = cursor.getString(columnArtist!!)
                        var pathC = cursor.getString(columnPath!!)
                        var durationC = cursor.getString(columnDuration!!).toLong()

                        try {
                            var file = File(pathC)
                            var artUriC = Uri.fromFile(file)
                            var music = MusicListItem(title= titleC, id= idC, album = albumC, duration = durationC, path = pathC, artist = artistC, artUri = artUriC)
                            if (file.exists()) tempList.add(music)
                        }catch (e: Exception){}
                    }while (cursor.moveToNext())
            cursor?.close()!!
            return tempList
        }
    }