package bleszerd.com.github.whyspper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.adapters.AudioAdapter
import bleszerd.com.github.whyspper.models.AudioModel

class AudioListFragment : Fragment() {
    companion object {
        private lateinit var arrayListMusic: List<AudioModel>

        fun newInstance(audioList: List<AudioModel>): AudioListFragment {
            //get arrayListMusic from newInstance constructor
            arrayListMusic = audioList
            return AudioListFragment()
        }
    }

    lateinit var recyclerViewRoot: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater
            .inflate(R.layout.fragment_audio_list, container, false)

        //set recyclerview adapter
        recyclerViewRoot = view.findViewById(R.id.recyclerView)
        recyclerViewRoot.layoutManager = LinearLayoutManager(activity)
        recyclerViewRoot.adapter = AudioAdapter(arrayListMusic)

        return view
    }

}