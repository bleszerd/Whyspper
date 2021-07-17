package bleszerd.com.github.whyspper.listeners

import android.view.View
import bleszerd.com.github.whyspper.models.AudioModel

/**
Whyspper
17/07/2021 - 11:10
Created by bleszerd.
@author alive2k@programmer.net
 */

interface AudioTapListener {
    fun onAudioSelected(viewTapped: View, audio: AudioModel)
}