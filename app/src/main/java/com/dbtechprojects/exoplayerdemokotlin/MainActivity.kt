package com.dbtechprojects.exoplayerdemokotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.dbtechprojects.exoplayerplayground.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED
import com.google.android.exoplayer2.Player.TimelineChangeReason
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.common.collect.ImmutableList
import java.io.File
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), Player.Listener {
    private lateinit var getContent: ActivityResultLauncher<Intent>





    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var titleTv: TextView
    private lateinit var btVideo: Button
    val concatenatingMediaSource = ConcatenatingMediaSource()
    private lateinit var dataSourceFactory: DataSource.Factory


    private val videos = ArrayList<Video>()



        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            requestStoragePermission()


        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)

        titleTv = findViewById(R.id.title)
        btVideo=findViewById(R.id.btVideo)



            setupPlayer()
             dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))

//            dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))

            val mediaItem = MediaItem.fromUri(Uri.parse(getString(R.string.test_mp3)))

//            player.setMediaSource(concatenatingMediaSource)


//            player = ExoPlayer.Builder(this).build()
//            playerView = findViewById(R.id.video_view)
//            playerView.player = player
            getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val uri = data?.data
                    val fileuri=getFilePathFromContentUri(this,uri!!)
                    val fileUri = Uri.parse("file://$fileuri")
                    // 在这里处理所选视频的Uri

                    if (fileuri != null) {
                        Toast.makeText(this, fileUri.toString(), Toast.LENGTH_SHORT).show()
                        // 处理所选视频的Uri
//                        val mediaItem = MediaItem.fromUri(fileUri)
//                        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))
                        val mediaSource2= ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(fileUri)
//                        val mediaItem = MediaItem.fromUri(fileUri)
//                        player.addMediaItem(mediaItem)
                        concatenatingMediaSource.addMediaSource(mediaSource2)
                        player.setMediaSource(concatenatingMediaSource)
//                        player.addMediaItem(mediaItem)
                        player.prepare()
//                        val mediaItem = MediaItem.fromUri(getString(uri))
//                        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))
//                        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//                        concatenatingMediaSource.addMediaSource(mediaSource)
//                        player.setMediaSource(concatenatingMediaSource)


                    }
                }
            }



//        addMP3()
//        addMP4Files()



//        setFile()//開啟檔案

//            val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//                // 在这里处理所选视频的Uri
//                val mediaItem3 = uri?.let { MediaItem.fromUri(it) }
//                player.addMediaItems(mediaItem3)
//                player.prepare()
//            }
//
//            getContent.launch("video/*")

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
        val projection = arrayOf(MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA)
//        contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            null,
//            null,
//            MediaStore.Video.Media.DATE_ADDED + " DESC"
//        )

        val mediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
        val mediaItem2 = MediaItem.fromUri(getString(R.string.myTestMp4))

//            val path= cursor!!.getString(R.string.myvideo)

            val mediaItem3 = MediaItem.fromUri(getString(R.string.myvideo))




//        val mediaItem3 = MediaItem.fromUri("/storage/emulated/0/Movies/VideoPanda/cpc.mp4")
        val newItems: List<MediaItem> = ImmutableList.of(
            mediaItem,
            mediaItem2 ,
         //   mediaItem3
        )

//        videos.add(Video(mediaItem))

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
//        val mediaItem = MediaItem.fromUri(Uri.parse(getString(R.string.test_mp3)))
//        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))
        val uri1 = Uri.parse(R.string.test_mp3.toString())
//        val uri1 = Uri.parse("file:///storage/emulated/0/Movies/VideoPanda/cpc.mp4")
//        val mediaItem = MediaItem.fromUri(getString(R.string.myvideo))
        val mediaSource1 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri1)
        concatenatingMediaSource.addMediaSource(mediaSource1)
//        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))
//        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//        concatenatingMediaSource.addMediaSource(mediaSource)
        // Set the media item to be played.
//        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(mediaItem)
//                        player.addMediaItem(mediaItem)
//        concatenatingMediaSource.addMediaSource(mediaSource)

//        player.setMediaSource(concatenatingMediaSource)
//        player.setMediaItem(mediaItem)
//        player.setMediaSource(mediaSource)
        player.setMediaSource(concatenatingMediaSource)
        // Prepare the player.
        player.prepare()
    }
     fun setFile() {
        btVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                addCategory(Intent.CATEGORY_OPENABLE)
//                type = "video/*"
//            }

            val dest=Intent.createChooser(intent, "Select")
//            startActivityForResult(dest,0)


            getContent.launch(dest)



//
//            getContent.launch("video/*")
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


    // 請求權限
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissions,1)
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                // 已經授權，可以進行下一步操作
//                // 比如初始化ExoPlayer
//            } else {
//                // 請求權限
//                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
//            }
        }
    }

    fun getFilePathFromContentUri(context: Context, contentUri: Uri): String? {
        val filePath: String?
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    // 授權結果回調
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        if (requestCode == 123 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // 授權成功，可以進行下一步操作
//            // 比如初始化ExoPlayer
//        } else {
//            // 授權失敗，可以給出提示或者其他處理
//        }
//    }



    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onResume() {
        super.onResume()
        setupPlayer()

        addMP3()
//        addMP4Files()
//        getContent
        setFile()//開啟檔案
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
        private val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE" ,
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    }
}


