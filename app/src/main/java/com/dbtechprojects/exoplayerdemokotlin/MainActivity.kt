package com.dbtechprojects.exoplayerdemokotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dbtechprojects.exoplayerplayground.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.common.collect.ImmutableList


class MainActivity : AppCompatActivity(), Player.Listener {



    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var titleTv: TextView
    private lateinit var btVideo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)

        titleTv = findViewById(R.id.title)
        btVideo=findViewById(R.id.btVideo)

        setupPlayer()
        addMP3()
        addMP4Files()



        setFile()//開啟檔案

//        val mediaSources = arrayOfNulls<MediaSource>(paths.length)
//        mediaSources[i] = ProgressiveMediaSource.Factory(FileDataSource.Factory())
//            .createMediaSource(MediaItem.fromUri(paths.get(i)))
//        player.setMediaSource(mediaSources)
//        player.prepare()
//        player.setPlayWhenReady(true)









        // restore playstate on Rotation
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("mediaItem") != 0) {
                val restoredMediaItem = savedInstanceState.getInt("mediaItem")
                val seekTime = savedInstanceState.getLong("SeekTime")
                player.seekTo(restoredMediaItem, seekTime)
                player.play()
            }
        }
    }

    private fun addMP4Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
        val mediaItem2 = MediaItem.fromUri(getString(R.string.myTestMp4))
        val newItems: List<MediaItem> = ImmutableList.of(
            mediaItem,
            mediaItem2
        )
        player.addMediaItems(newItems)
        player.prepare()
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.video_view)
        playerView.player = player
        player.addListener(this)
    }

    private fun addMP3() {
        // Build the media item.
        val mediaItem = MediaItem.fromUri(getString(R.string.test_mp3))
        player.setMediaItem(mediaItem)
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
    }
     fun setFile() {
        btVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            val dest=Intent.createChooser(intent, "Select")
            startActivityForResult(dest,0)
//            val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//                // 在这里处理所选视频的Uri
//                val mediaItem = MediaItem.fromUri(uri!!)
//                player.setMediaItem(mediaItem)
//                player.prepare()
//                // 您可以将其传递给ExoPlayer来播放
//
//            }
//            getContent.launch(intent.toString())

//            onActivityResult(0,dest,intent)


        }
    }


    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onResume() {
        super.onResume()
        setupPlayer()
        addMP3()
        addMP4Files()
    }

    // handle loading
    override fun onPlaybackStateChanged(state: Int) {
        when (state) {
            Player.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE

            }
            Player.STATE_READY -> {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    //get Title from metadata
    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {

        titleTv.text = mediaMetadata.title ?: mediaMetadata.displayTitle ?: "no title found"

    }

    // save details if Activity is destroyed
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: " + player.currentPosition)
        // current play position
        outState.putLong("SeekTime", player.currentPosition)
        // current mediaItem
        outState.putInt("mediaItem", player.currentMediaItemIndex)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onSaveInstanceState: " + player.currentPosition)
    }



    companion object {
        private const val TAG = "MainActivity"
        const val REQUEST_VIDEO_FILE = 1
    }
}