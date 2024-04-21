package com.example.music.model

import android.net.Uri
import java.time.Duration
import java.util.concurrent.TimeUnit

data class MusicListItem(val id:String,val title: String, val album:String, val artist: String,
                         val duration: Long=0, val path: String, val artUri: Uri)

fun formatDuration(duration: Long):String {

    val hours = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS)
    var minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    var seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)

    seconds = seconds - minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    minutes = minutes - hours*TimeUnit.MINUTES.convert(1, TimeUnit.HOURS)

    return String.format("%2d:%2d:%2d", hours, minutes, seconds)
}
