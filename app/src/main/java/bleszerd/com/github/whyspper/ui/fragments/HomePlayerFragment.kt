package bleszerd.com.github.whyspper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import bleszerd.com.github.whyspper.R

class HomePlayerFragment: Fragment() {
    companion object {
        fun newInstance() = HomePlayerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_player, container, false)

        return view
    }
}