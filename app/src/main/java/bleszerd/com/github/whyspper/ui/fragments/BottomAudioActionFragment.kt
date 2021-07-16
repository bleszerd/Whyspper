package bleszerd.com.github.whyspper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import bleszerd.com.github.whyspper.R

class BottomAudioActionFragment : Fragment(){
    companion object {
        fun newInstance() = BottomAudioActionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_audio_action, container, false)
    }
}