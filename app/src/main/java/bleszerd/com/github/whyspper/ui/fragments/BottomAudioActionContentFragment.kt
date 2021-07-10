package bleszerd.com.github.whyspper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.controllers.AudioController

class BottomAudioActionContentFragment : Fragment() {
    private val audioController = AudioController()

    companion object {
        fun newInstance() = BottomAudioActionContentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_audio_action_content_fragment, container, false)

        val pausePlayButton = view.findViewById<ImageButton>(R.id.playPauseActionButton)
        pausePlayButton.setOnClickListener {
            audioController.pauseOrPlay(pausePlayButton)
        }

        return view
    }
}