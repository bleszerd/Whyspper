package bleszerd.com.github.whyspper.exceptions

import android.util.Log

/**
Whyspper
17/07/2021 - 11:14
Created by bleszerd.
@author alive2k@programmer.net
 */

class ListenerException: Exception {

    object Constants {
        const val EXCEPTION_TAG = "throwable_listener_key"
    }

    constructor(_class: Class<*>): super(){
        Log.e(Constants.EXCEPTION_TAG, "You must implement the listener for the class $_class before using it")
    }

    constructor(message: String?) : super(message){
        Log.e(Constants.EXCEPTION_TAG, "$message")
    }
}