package bleszerd.com.github.whyspper.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.adapters.AudioAdapter
import bleszerd.com.github.whyspper.controllers.AudioController
import bleszerd.com.github.whyspper.listeners.AudioStateListener
import bleszerd.com.github.whyspper.listeners.AudioTapListener
import bleszerd.com.github.whyspper.models.AudioModel

class BottomAudioActionFragment : Fragment(), AudioStateListener, AudioTapListener {
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
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_audio_action, container, false)

        rootConstraintLayout = view as ConstraintLayout

        openAnimation =
            AnimationUtils.loadAnimation(context, R.anim.open_audio_action_container)

        AudioController.listener = this
        AudioAdapter.listener = this

        imageCover = view.findViewById(R.id.albumImageCover)
        pausePlayButton = view.findViewById(R.id.playPauseActionButton)
        likeButton = view.findViewById(R.id.favoriteActionButton)

        pausePlayButton.setOnClickListener {
            AudioController.togglePlayPauseState()
        }

        likeButton.setOnClickListener {
            AudioController.toggleFavoriteState(likeButton)
        }

        return view
    }

    override fun onAudioStart(context: Context, currentAudio: AudioModel) {
        if (rootConstraintLayout.visibility != View.VISIBLE) {
            rootConstraintLayout.startAnimation(openAnimation)
            rootConstraintLayout.visibility = View.VISIBLE
        }

        pausePlayButton.setImageResource(R.drawable.ic_pause_btn)

        if (currentAudio.albumArt == null) {
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

    override fun onAudioSelected(viewTapped: View, audio: AudioModel) {
        //play content from audioUri
        AudioController.playFromUri(rootConstraintLayout.context, Uri.parse(audio.location))

        //update bottom content to current music
        val imageCover = rootConstraintLayout.findViewById<ImageView>(R.id.albumImageCover)
        val musicTitle = rootConstraintLayout.findViewById<TextView>(R.id.audioTitle)
        val musicArtist = rootConstraintLayout.findViewById<TextView>(R.id.audioArtist)

        imageCover.visibility = View.VISIBLE
        musicTitle.visibility = View.VISIBLE
        musicArtist.visibility = View.VISIBLE

        if (audio.albumArt != null) {
            imageCover.setImageBitmap(audio.albumArt)
        } else {
            imageCover.setImageResource(R.drawable.ic_album)
        }

        musicTitle.text = audio.title
        musicArtist.text = audio.artist

        //set favorite button drawable
        val buttonLikeIcon = rootConstraintLayout.findViewById<ImageView>(R.id.favoriteActionButton)
        val buttonResource =
            if (audio.favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_empty

        buttonLikeIcon.setImageResource(buttonResource)

        //update image button resource
        val playPauseBtn = rootConstraintLayout.findViewById<ImageView>(R.id.playPauseActionButton)
        playPauseBtn.setImageResource(R.drawable.ic_pause_btn)

        //show icons
        buttonLikeIcon.visibility = View.VISIBLE
        playPauseBtn.visibility = View.VISIBLE
    }
}
