package bleszerd.com.github.whyspper.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.controllers.AudioController
import bleszerd.com.github.whyspper.controllers.AudioController.AudioListener
import bleszerd.com.github.whyspper.models.AudioModel
import kotlin.reflect.typeOf

class BottomAudioActionContentFragment : Fragment(), AudioListener {
    companion object {
        fun newInstance() = BottomAudioActionContentFragment()
    }

    lateinit var imageCover: ImageView
    lateinit var pausePlayButton: ImageView
    lateinit var likeButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_audio_action_content, container, false)

        AudioController.listener = this

        imageCover = view.findViewById(R.id.albumImageCover)
        pausePlayButton = view.findViewById<ImageView>(R.id.playPauseActionButton)
        likeButton = view.findViewById<ImageView>(R.id.favoriteActionButton)

        pausePlayButton.setOnClickListener {
            AudioController.pauseOrPlay()
        }

        likeButton.setOnClickListener {
            AudioController.handleChangeFavoriteAction(likeButton)
        }

        return view
    }

    override fun onAudioStart(context: Context, currentAudio: AudioModel) {
        pausePlayButton.setImageResource(R.drawable.ic_pause_btn)

        if(currentAudio.albumArt == null){
            imageCover.setColorFilter(R.color.black)
        } else {
            imageCover.clearColorFilter()
        }
        super.onAudioStart(context, currentAudio)
    }

    override fun onAudioPause(currentAudio: AudioModel) {
        pausePlayButton.setImageResource(R.drawable.ic_play_btn)

        super.onAudioPause(currentAudio)
    }

    override fun onAudioEnd() {
        pausePlayButton.setImageResource(R.drawable.ic_play_btn)

        super.onAudioEnd()
    }
}