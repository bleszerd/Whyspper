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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_audio_action_content, container, false)

        AudioController.listener = this

        imageCover = view.findViewById(R.id.albumImageCover)
        val pausePlayButton = view.findViewById<ImageButton>(R.id.playPauseActionButton)
        val likeButton = view.findViewById<ImageButton>(R.id.favoriteActionButton)

        pausePlayButton.setOnClickListener {
            AudioController.pauseOrPlay(pausePlayButton)
        }

        likeButton.setOnClickListener {
            AudioController.handleChangeFavoriteAction(likeButton)
        }

        return view
    }

    override fun onAudioStart(context: Context, currentAudio: AudioModel) {
        if(currentAudio.albumArt == null){
            imageCover.setColorFilter(R.color.dark)
        } else {
            imageCover.clearColorFilter()
        }
        super.onAudioStart(context, currentAudio)
    }
}