package com.example.icemusic

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.icemusic.data.eventBus.PlaySongEvent
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.databinding.ActivityMainBinding
import com.example.icemusic.netWork.SearchSongWorker
import com.example.icemusic.viewModel.PlaySongBottomTabViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

//@Route(path = "iceMusic/MainActivity")
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    var mediaPlayer:MediaPlayer? = null

    lateinit var binding:ActivityMainBinding

    lateinit var playSongBottomTabViewModel: PlaySongBottomTabViewModel
    var playSongBottomTabVisible:ObservableInt = ObservableInt(View.INVISIBLE)

    var lastSongData: SearchSingleSongData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playSongBottomTabViewModel = ViewModelProvider(this).get(PlaySongBottomTabViewModel::class.java)
        binding.playSongBottomTabVisible = playSongBottomTabVisible
        binding.playSongBottomViewModel = playSongBottomTabViewModel
        EventBus.getDefault().register(this)
        createMediaPlayer()
//        playMusic()

    }

    override fun onResume() {
        super.onResume()
        binding.mainPageFragment.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
            Log.i(TAG,""+destination.label)
            destination.label?.let {
                var name:String = it.toString()
                Log.i(TAG,"name: ${name}")
                var targetName = resources.getString(R.string.nav_label_main_page_fragment)
                if(name==targetName){
                    playSongBottomTabVisible.set(View.INVISIBLE)
                }else {
                    playSongBottomTabVisible.set(View.VISIBLE)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun createMediaPlayer(){
        mediaPlayer = MediaPlayer()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun playMusic(event:PlaySongEvent){
        var curSongData = event.songData
        if(curSongData.equals(lastSongData)){
            playSongBottomTabViewModel.playSwitchFlag.let {
                if(it.get()){
                    mediaPlayer?.start()
                }else mediaPlayer?.pause()
            }
        }else{
            var id = event.songData.songID
            var searchSongWorker = SearchSongWorker()
            var songUrl = searchSongWorker.obtainSongUrl(id)
            mediaPlayer?.let {
                it.stop()
                it.reset()

                it.setDataSource(songUrl)
                it.prepare()
                it.setOnPreparedListener {
                    Log.i(TAG,"start play")
                    it.start()
                    Log.i(TAG,"all time: ${it.duration}")
                    lifecycleScope.launch {
                        playSongBottomTabViewModel.playSwitchFlag.set(true)
                    }
                }
                it.setOnCompletionListener {
                    lifecycleScope.launch {
                        playSongBottomTabViewModel.playSwitchFlag.set(false)
                    }
                    Log.i(TAG,"music completed")
                }
                it.setOnErrorListener { mp, what, extra ->
                    Log.i(TAG,"muisc error")
                    false
                }
            }
            lifecycleScope.launch {
                playSongBottomTabViewModel.songData.set(curSongData)
            }
        }
        lastSongData = curSongData
    }
}