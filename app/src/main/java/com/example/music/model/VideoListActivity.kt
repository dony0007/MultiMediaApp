package com.example.music.model


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.adapters.VideoAdapter
import com.example.music.model.VideoItemModel
import com.example.music.R
import java.io.File

class VideoListActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1
    private val REQUEST_CODE_PERMISSION: Int = 123

    companion object{
        lateinit var videoList: ArrayList<VideoItemModel>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)


        if (checkPermission()) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        videoList = getAllVideos()

        var recyclerView = findViewById<RecyclerView>(R.id.videoRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VideoAdapter(applicationContext,videoList)

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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission is denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun getAllVideos(): ArrayList<VideoItemModel>{
        val tempList = ArrayList<VideoItemModel>()
        val projection = arrayOf(Media.TITLE,
            Media._ID, Media.BUCKET_DISPLAY_NAME,
            Media.DATA, Media.DATE_ADDED,
            Media.DURATION)
        val cursor = this.contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, null, null,
            Media.DATE_ADDED + " DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {

                    var columnTitle = cursor?.getColumnIndexOrThrow(Media.TITLE)
                    var columnId = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    var columnPath = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                    var columnDuration = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                    var columnFolder = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

                    var titleC = cursor.getString(columnTitle!!)
                    var idC = cursor.getString(columnId!!)
                    var folderC = cursor.getString(columnFolder!!)
                    var pathC = cursor.getString(columnPath!!)
                    var durationC = cursor.getString(columnDuration!!).toLong()

                    try {
                        var file = File(pathC)
                        var artUri = Uri.fromFile(file)
                        var video = VideoItemModel(title= titleC, id= idC, folderName = folderC, duration = durationC, path = pathC, artUri = artUri)
                        if (file.exists()) tempList.add(video)
                    }catch (e: Exception){}

                }while (cursor.moveToNext())
        cursor?.close()!!
        return  tempList
    }
}