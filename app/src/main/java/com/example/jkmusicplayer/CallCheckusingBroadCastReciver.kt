package com.example.jkmusicplayer;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import java.util.*


class CallCheckusingBroadCastReciver : BroadcastReceiver() {
    private var lastState = TelephonyManager.CALL_STATE_IDLE
    private var isIncoming = false
    private var callStart: Date? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            Toast.makeText(context, "Outgoing Call Start", Toast.LENGTH_SHORT).show()
            PlayMusicActivity.mp.pause();
        } else {
            val stateString = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
            var state = 0
            if (stateString == TelephonyManager.EXTRA_STATE_IDLE) {
                Toast.makeText(context, "Idle State", Toast.LENGTH_SHORT).show()
                state = TelephonyManager.CALL_STATE_IDLE
            } else if (stateString == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                Toast.makeText(context, "OffHook", Toast.LENGTH_SHORT).show()
                state = TelephonyManager.CALL_STATE_OFFHOOK
            } else if (stateString == TelephonyManager.EXTRA_STATE_RINGING) {
                Toast.makeText(context, "Ringing", Toast.LENGTH_SHORT).show()
                state = TelephonyManager.CALL_STATE_RINGING
            }
            OnCallStateChanged(context, state)
        }
    }

    fun OnCallStateChanged(context: Context?, state: Int) {
        if (lastState == state) {
            PlayMusicActivity.mp.start()
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStart = Date()
                OnincomingcallStarted(context, callStart)
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                callStart = Date()
                isIncoming = false
                OnoutgoingcallStarted(context, callStart)
            }
            TelephonyManager.CALL_STATE_IDLE -> if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                Onmissedcall(context, callStart)
            } else if (lastState == TelephonyManager.CALL_STATE_OFFHOOK) {
                Toast.makeText(context, "Outgoing Call Has Ended", Toast.LENGTH_SHORT).show()
                OnoutgoingcallEnded(context, callStart, Date())
                if (isIncoming) {
                    OnincomingcallEnded(context, callStart, Date())
                } else {
                    OnoutgoingcallEnded(context, callStart, Date())
                }
            }
        }
        lastState = state
    }

    fun OnincomingcallStarted(context: Context?, dateStart: Date?) {
        Toast.makeText(context, "Incoming Call", Toast.LENGTH_SHORT).show()
        PlayMusicActivity.mp.pause()
    }

    fun OnoutgoingcallStarted(context: Context?, dateStart: Date?) {
        Toast.makeText(context, "Outgoing Call Start ho gyi", Toast.LENGTH_SHORT).show()
        PlayMusicActivity.mp.pause()
    }

    fun OnincomingcallEnded(context: Context?, dateStart: Date?, end: Date?) {
        Toast.makeText(context, "Incoming Call Ended", Toast.LENGTH_SHORT).show()
        PlayMusicActivity.mp.start()
    }

    fun OnoutgoingcallEnded(context: Context?, dateStart: Date?, end: Date?) {
        Toast.makeText(context, "Outgoing Call has Ended", Toast.LENGTH_SHORT).show()
        PlayMusicActivity.mp.start()
    }

    fun Onmissedcall(context: Context?, dateStart: Date?) {
        Toast.makeText(context, "U Missed the Call", Toast.LENGTH_SHORT).show()
        PlayMusicActivity.mp.start()
    }
}
