package bleszerd.com.github.whyspper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.adapters.MusicAdapter
import bleszerd.com.github.whyspper.models.AudioModel

class MusicListFragment : Fragment() {
    companion object {
        private lateinit var arrayListMusic: List<AudioModel>

        fun newInstance(audioList: List<AudioModel>): MusicListFragment {
            //get arrayListMusic from newInstance constructor
            arrayListMusic = audioList
            return MusicListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater
            .inflate(R.layout.music_list_fragment, container, false)

        //set recyclerview adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MusicAdapter(arrayListMusic)

        return view
    }
}