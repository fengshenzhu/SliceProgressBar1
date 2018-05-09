package com.fengshenzhu.libspb

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SliceProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_GAP_COLOR = Color.GREEN
        private const val DEFAULT_GAP_WIDTH = 20
        private const val DEFAULT_BG_COLOR = Color.BLACK
        private const val DEFAULT_PROGRESS_COLOR = Color.YELLOW
        private const val DEFAULT_MIN = 0
        private const val DEFAULT_MAX = 100
    }

    private var mMax = DEFAULT_MIN
    private var mMin = DEFAULT_MAX
    var progress = 0
        set(value) {
            field = value
            invalidate()
        }
    private val mSlices = arrayListOf<Slice>()
    private var mPaint: Paint? = null

    private var mGapWidth = DEFAULT_GAP_WIDTH
    private var mGapColor = DEFAULT_GAP_COLOR
    private var mBackgroundColor = DEFAULT_BG_COLOR
    private var mProgressColor = DEFAULT_PROGRESS_COLOR

    var max: Int
        get() = mMax
        set(max) {
            if (mMax == max) return
            mMax = max
            invalidate()
        }

    var min: Int
        get() = mMin
        set(min) {
            if (mMin == min) return
            mMin = min
            invalidate()
        }

    val sliceSize: Int
        get() = mSlices.size

    val lastPieceProgress: Int
        get() = if (mSlices.size == 0) {
            0
        } else {
            mSlices[mSlices.size - 1].endProgress
        }

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SliceProgressBar)
        try {
            mGapWidth = a.getDimensionPixelSize(R.styleable.SliceProgressBar_gap_width, DEFAULT_GAP_WIDTH)
            mGapColor = a.getColor(R.styleable.SliceProgressBar_gap_color, DEFAULT_GAP_COLOR)
            mBackgroundColor = a.getColor(R.styleable.SliceProgressBar_bg_color, DEFAULT_BG_COLOR)
            mProgressColor = a.getColor(R.styleable.SliceProgressBar_progress_color, DEFAULT_PROGRESS_COLOR)
            mMin = a.getInt(R.styleable.SliceProgressBar_progress_min, DEFAULT_MIN)
            mMax = a.getInt(R.styleable.SliceProgressBar_progress_max, DEFAULT_MAX)
        } finally {
            a.recycle()
        }
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    fun setRange(min: Int, max: Int) {
        if (mMin == min && mMax == max) return
        mMin = min
        mMax = max
        invalidate()
    }

    fun addSlice(color: Int): Boolean {
        if (progress <= lastPieceProgress) return false
        mSlices.add(Slice(lastPieceProgress, progress, color))
        invalidate()
        return true
    }

    fun removeLastSlice() {
        if (mSlices.size == 0) {
            return
        }
        mSlices.removeAt(mSlices.size - 1)
        progress = lastPieceProgress
    }

    fun updateLastSliceColor(color: Int) {
        if (mSlices.size > 0) {
            mSlices[mSlices.size - 1].color = color
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // draw background
        canvas.drawColor(mBackgroundColor)
        // draw min progress
        val factor = canvas.width * 1.0f / mMax
        val height = height
        mPaint!!.color = mProgressColor
        val minLeft = (factor * mMin).toInt()
        canvas.drawRect(minLeft.toFloat(), 0f, (minLeft + height).toFloat(), height.toFloat(), mPaint!!)
        // draw recorded slices
        for (i in mSlices.indices) {
            val piece = mSlices[i]
            val left = (piece.startProgress * factor).toInt()
            val right = (piece.endProgress * factor).toInt()
            if (right - left > mGapWidth) {
                // draw slice
                mPaint!!.color = piece.color
                canvas.drawRect((if (left == 0) left else left + mGapWidth).toFloat(), 0f, right.toFloat(), height.toFloat(), mPaint!!)
            }
        }
        // draw recording progress
        val left = lastPieceProgress * factor
        val right = progress * factor
        mPaint!!.color = mProgressColor
        if (left == 0f) { // 第一段直接绘制进度
            canvas.drawRect(left, 0f, right, height.toFloat(), mPaint!!)
        } else if (right - left > mGapWidth) { // 非第一段进度超过gap再绘制
            canvas.drawRect(left + mGapWidth, 0f, right, height.toFloat(), mPaint!!)
        }
    }

}
