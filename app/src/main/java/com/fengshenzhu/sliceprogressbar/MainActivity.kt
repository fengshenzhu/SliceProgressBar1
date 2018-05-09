package com.fengshenzhu.sliceprogressbar

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sliceProgressBar.apply {
//            setRange(10, 100)
//            max = 200
//            min = 100
        }

        subProgress.setOnClickListener({
            val newProgress = sliceProgressBar.progress - 1
            progress.text = newProgress.toString()
            sliceProgressBar.progress = newProgress
        })

        addProgress.setOnClickListener({
            val newProgress = sliceProgressBar.progress + 1
            progress.text = newProgress.toString()
            sliceProgressBar.progress = newProgress
        })

        subSlice.setOnClickListener {
            sliceProgressBar.removeLastSlice()
            progress.text = sliceProgressBar.progress.toString()
            Log.d(TAG, sliceProgressBar.lastPieceProgress.toString())
            Log.d(TAG, sliceProgressBar.sliceSize.toString())
        }

        addSlice.setOnClickListener {
            sliceProgressBar.addSlice(Color.BLUE)
            Log.d(TAG, sliceProgressBar.lastPieceProgress.toString())
            Log.d(TAG, sliceProgressBar.sliceSize.toString())
        }

        updateLastSColor.setOnClickListener {
            sliceProgressBar.updateLastSliceColor(Color.RED)
        }

    }
}
