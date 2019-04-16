package com.example.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import androidx.core.content.ContextCompat
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Irene Khramova on 13.04.2019.
 */
class CustomImageView: AppCompatImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mMaskPath: Path = Path()
    private var pathDst: Path = Path()
    private val mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCornerRadius = 0f
    var matrixPerspective: Matrix = Matrix()
    var src: FloatArray? = null
    var dst: FloatArray? = null

    init {


        this.setLayerType(LAYER_TYPE_SOFTWARE, null)
        mMaskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        mMaskPaint.color = ContextCompat.getColor(context, android.R.color.transparent)

        mCornerRadius = dpToPx(6f, context)


    }

    /**
     * Set the corner radius to use for the RoundedRectangle.
     */
    fun setCornerRadius(cornerRadius: Float) {
        mCornerRadius = cornerRadius
        generateMaskPath(width, height)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        if (w != oldW || h != oldH) {
            generateMaskPath(w, h)
        }
    }

    private fun generateMaskPath(w: Int, h: Int) {
        mMaskPath = Path()
        //mMaskPath.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), mCornerRadius, mCornerRadius, Path.Direction.CW)
        mMaskPath.addRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), Path.Direction.CW)
        mMaskPath.fillType = Path.FillType.INVERSE_WINDING

        src = floatArrayOf(0f, 0f, width.toFloat(), 0f, width.toFloat(), height.toFloat(), 0f, height.toFloat())
        dst = floatArrayOf(0f, dpToPx(16f, context), width.toFloat(), dpToPx(8f, context), width.toFloat(), height.toFloat(), dpToPx(8f, context), height.toFloat())
        //dst = floatArrayOf(dpToPx(8f, context), 0f, width.toFloat(), 0f, width.toFloat(), dpToPx(height.toFloat() - 8f, context), 0f, dpToPx(height.toFloat() - 16f, context))
        matrixPerspective.setPolyToPoly(src, 0, dst, 0, 4)
        mMaskPath.transform(matrixPerspective, pathDst)
    }

    override fun onDraw(canvas: Canvas) {
        if (canvas.isOpaque) { // If canvas is opaque, make it transparent
            canvas.saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), 255)
        }
        super.onDraw(canvas)
        canvas.drawPath(pathDst, mMaskPaint)


    }

    fun dpToPx(dp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}