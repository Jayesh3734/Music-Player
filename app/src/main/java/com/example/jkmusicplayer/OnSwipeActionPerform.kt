package com.example.jkmusicplayer;

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


class OnSwipeActionPerform(context: Context?, onswipePerformListner: OnswipePerformListner) :
    OnTouchListener {
    interface OnswipePerformListner {
        fun OnSwipePerformed(leftoright: Int)
    }

    private val gestureDetector: GestureDetector
    var onswipePerformListner: OnswipePerformListner
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListner : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val result = false
            try {
                val diffy = e2.y - e1.y
                val diffx = e2.x - e1.x
                if (Math.abs(diffx) > Math.abs(diffy)) {
                    if (Math.abs(diffx) > Companion.SWIPE_THRESHOLD && Math.abs(velocityX) > Companion.SWIPE_VELOCITY_THRESHOLD) {
                        if (diffx > 0) {
                            onswipePerformListner.OnSwipePerformed(1)
                        } else {
                            onswipePerformListner.OnSwipePerformed(0)
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }


    }
    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    init {
        gestureDetector = GestureDetector(context, GestureListner())
        this.onswipePerformListner = onswipePerformListner
    }
}
