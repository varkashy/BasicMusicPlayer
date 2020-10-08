package com.example.mymusicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mp: MediaPlayer? = null
    private var currentSong = mutableListOf(R.raw.base_after_base,R.raw.cant_let_go)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        controlSound(currentSong[0])
    }

    private fun controlSound(songId: Int) {
        fab_play.setOnClickListener {
            if(mp == null){
                mp = MediaPlayer.create(this,songId)
                Log.d("Main Activity","ID ${mp!!.audioSessionId}")
                initializeSeekBar()
            }
            mp?.start()
            Log.d("Main Activity","Duration ${mp!!.duration/1000} seconds")
        }

        fab_pause.setOnClickListener {
            if(mp != null){
                mp?.pause()
                Log.d("Main Activity","Paused at ${mp!!.currentPosition/1000} seconds")
            }
        }

        fab_stop.setOnClickListener {
            if(mp != null){
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
                Log.d("Main Activity","Stopped")
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun initializeSeekBar() {
        seekbar.max = mp!!.duration
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object:Runnable {
            override fun run() {
                try {
                    seekbar.progress = mp!!.currentPosition
                    handler.postDelayed(this, 1000)
                }catch (e: Exception){
                    seekbar.progress = 0
                }
            }
        },0)
    }
}