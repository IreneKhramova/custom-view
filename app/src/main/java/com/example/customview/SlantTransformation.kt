package com.example.customview

import android.graphics.Bitmap
import android.graphics.Matrix
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Created by Irene Khramova on 15.04.2019.
 */
class SlantTransformation(
    private val position: Position
) : BitmapTransformation() {

    companion object {
        private const val ID = "com.bumptech.glide.transformations.FillSpace"
        private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))
        private const val INT_SIZE_IN_BYTES = 4

        private const val SLANT_VALUE = 16
        private const val CORNER_RADIUS = 6

        private const val POINT_COUNT = 4
    }

    var src: FloatArray? = null
    var dst: FloatArray? = null

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)

        val positionData = ByteBuffer.allocate(INT_SIZE_IN_BYTES).putInt(position.ordinal).array()
        messageDigest.update(positionData)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val slantValuePx = SLANT_VALUE.dpToPx()
        val slantHalfValuePx = SLANT_VALUE.dpToPx() / 2
        val width = outWidth.toFloat()
        val height = outHeight.toFloat()
        val matrix = Matrix()

        src = floatArrayOf(0f, 0f, width, 0f, width, height, 0f, height)

        dst = when (position) {
            Position.TOP_LEFT -> {
                floatArrayOf(
                    0f, slantValuePx.toFloat(), width, slantHalfValuePx.toFloat(),
                    width, height, slantHalfValuePx.toFloat(), height
                )
            }
            Position.TOP_RIGHT -> {
                floatArrayOf(0f, slantHalfValuePx.toFloat(), width, 0f, width - slantHalfValuePx, height, 0f, height)
            }
            Position.BOTTOM_LEFT -> {
                floatArrayOf(slantHalfValuePx.toFloat(), 0f, width, 0f, width, height, slantValuePx.toFloat(), height)
            }
            Position.BOTTOM_RIGHT -> {
                floatArrayOf(0f, 0f, width - slantHalfValuePx, 0f, width - slantValuePx, height, 0f, height)
            }
        }
        matrix.setPolyToPoly(src, 0, dst, 0, POINT_COUNT)

        val centerBitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        val roundedCornersBitmap = TransformationUtils.roundedCorners(pool, centerBitmap, CORNER_RADIUS.dpToPx())
        return Bitmap.createBitmap(roundedCornersBitmap, 0, 0, outWidth, outHeight, matrix, true)
    }

    override fun equals(other: Any?): Boolean {
        if (other is SlantTransformation) {
            return position == other.position
        }
        return false
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), position.hashCode())
    }

    enum class Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}