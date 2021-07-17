package bleszerd.com.github.whyspper.listeners

import android.content.Context
import bleszerd.com.github.whyspper.models.AudioModel

/**
Whyspper
17/07/2021 - 19:07
Created by bleszerd.
@author alive2k@programmer.net
 */
interface AudioStateListener {
    fun onAudioStop(context: Context, currentAudio: AudioModel) {}
    fun onAudioRelease(context: Context, currentAudio: AudioModel) {}
    fun onAudioStart(context: Context, currentAudio: AudioModel) {}
    fun onAudioPause(currentAudio: AudioModel) {}
    fun onAudioChange(context: Context, currentAudio: AudioModel) {}
    fun onAudioResume(currentAudio: AudioModel) {}
    fun onAudioEnd() {}
}