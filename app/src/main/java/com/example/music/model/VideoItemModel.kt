package com.example.music.model


import android.net.Uri
import java.time.Duration

data class VideoItemModel(val id:String, val title:String, val duration: Long =0, val folderName: String,
                          val path: String, val artUri: Uri)
