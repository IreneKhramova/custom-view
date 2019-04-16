package com.example.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.util.TypedValue
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.util.Util
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Created by Irene Khramova on 15.04.2019.
 */
class MyTransformation(private val context: Context, private val position: Position) : BitmapTransformation() {

    companion object {
        private const val ID = "com.bumptech.glide.transformations.FillSpace"
        //TODO: посмотреть в доке
        private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))

        private const val SLANT_VALUE = 16f
    }

    var src: FloatArray? = null
    var dst: FloatArray? = null

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        if (toTransform.width == outWidth && toTransform.height == outHeight) {
            return toTransform
        }
        val slantValuePx = dpToPx(SLANT_VALUE, context)
        val slantHalfValuePx = dpToPx(SLANT_VALUE / 2, context)
        val width = outWidth.toFloat()
        val height = outHeight.toFloat()
        val matrix = Matrix()
        src = floatArrayOf(0f, 0f, width, 0f, width, height, 0f, height)

        dst = when (position) {
            Position.TOP_LEFT -> {
                floatArrayOf(0f, slantValuePx, width, slantHalfValuePx, width, height, slantHalfValuePx, height)
            }
            Position.TOP_RIGHT -> {
                floatArrayOf(0f, slantHalfValuePx, width, 0f, width - slantHalfValuePx, height, 0f, height)
            }
            Position.BOTTOM_LEFT -> {
                floatArrayOf(slantHalfValuePx, 0f, width, 0f, width, height, slantValuePx, height)
            }
            Position.BOTTOM_RIGHT -> {
                floatArrayOf(0f, 0f, width - slantHalfValuePx, 0f, width - slantValuePx, height, 0f, height)
            }
        }
        matrix.setPolyToPoly(src, 0, dst, 0, 4)

        val centerBitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        val roundedCornersBitmap = TransformationUtils.roundedCorners(pool, centerBitmap, dpToPx(6f, context).toInt())
        return Bitmap.createBitmap(roundedCornersBitmap, 0, 0, outWidth, outHeight, matrix, true)
    }

    override fun equals(other: Any?): Boolean {
        //TODO: context?
        if (other is MyTransformation) {
            return position == other.position
        }
        return false
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), position.hashCode())
    }

    private fun dpToPx(dp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    enum class Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}