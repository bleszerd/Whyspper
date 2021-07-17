package bleszerd.com.github.whyspper.ui.fragments

import android.animation.AnimatorInflater
import android.content.Context
import android.opengl.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.controllers.AudioController
import bleszerd.com.github.whyspper.controllers.AudioController.AudioListener
import bleszerd.com.github.whyspper.models.AudioModel

class BottomAudioActionFragment : Fragment(), AudioListener{
    companion object {
        fun newInstance() = BottomAudioActionFragment()
    }

    lateinit var imageCover: ImageView
    lateinit var pausePlayButton: ImageView
    lateinit var likeButton: ImageView
    lateinit var rootConstraintLayout: ConstraintLayout
    lateinit var openAnimation: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_audio_action, container, false)

        rootConstraintLayout = view as ConstraintLayout

        openAnimation =
            AnimationUtils.loadAnimation(context, R.anim.open_audio_action_container)

        AudioController.listener = this

        imageCover = view.findViewById(R.id.albumImageCover)
        pausePlayButton = view.findViewById(R.id.playPauseActionButton)
        likeButton = view.findViewById(R.id.favoriteActionButton)

        pausePlayButton.setOnClickListener {
            AudioController.pauseOrPlay()
        }

        likeButton.setOnClickListener {
            AudioController.handleChangeFavoriteAction(likeButton)
        }

        return view
    }

    override fun onAudioStart(context: Context, currentAudio: AudioModel) {
        if(rootConstraintLayout.visibility != View.VISIBLE){
            rootConstraintLayout.startAnimation(openAnimation)
            rootConstraintLayout.visibility = View.VISIBLE
        }

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
