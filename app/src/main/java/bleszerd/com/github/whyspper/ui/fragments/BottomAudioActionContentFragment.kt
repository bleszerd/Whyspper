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

class BottomAudioActionContentFragment : Fragment(), AudioListener {
    companion object {
        fun newInstance() = BottomAudioActionContentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_audio_action_content, container, false)

        AudioController.listener = this

        val imageCover = view.findViewById<ImageView>(R.id.art)
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

    override fun onAudioStart() {
        println("Audio play")
    }
}