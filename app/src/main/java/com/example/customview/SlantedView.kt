package com.example.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_image_container.view.*
import kotlinx.android.synthetic.main.view_slanted.view.*

/**
 * Created by Irene Khramova on 15.04.2019.
 */
class SlantedView : ConstraintLayout {

    private var textWidth = 0
    private var caption: String? = null

    private var firstLineBounds = Rect()
    private var secondLineBounds = Rect()
    private var firstLineWidth = 0
    private var secondLineWidth = 0

    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun setImages(imageTopLeft: String, imageTopRight: String, imageBottomLeft: String, imageBottomRight: String) {
        viewImageContainer.setImages(imageTopLeft, imageTopRight, imageBottomLeft, imageBottomRight)
    }

    fun setText(text: String) {
        caption = text
        textViewCaption.text = caption
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.view_slanted, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        caption?.let { caption ->
            val layout = textViewCaption.layout
            val textPaint = textViewCaption.paint

            var start = 0
            var end = layout.getLineEnd(0)
            var line = caption.substring(start, end)
            textPaint.getTextBounds(line, 0, line.length, firstLineBounds)
            firstLineWidth = firstLineBounds.width()

            start = end
            end = layout.getLineEnd(1)
            line = caption.substring(start, end)
            textPaint.getTextBounds(line, 0, line.length, secondLineBounds)
            secondLineWidth = secondLineBounds.width()

            textWidth = if (firstLineWidth > secondLineWidth) firstLineWidth else secondLineWidth

            viewImageContainer.textViewWidth = textWidth + textViewCaption.marginStart
            viewImageContainer.textViewHeight = textViewCaption.height - viewImageContainer.marginBottom
        }
    }
}

class ImageContainer : ConstraintLayout {

    companion object {
        private const val SLANT_VALUE = 8
        private const val ADDITIONAL_WIDTH = 20
    }

    var textViewWidth = 0
    var textViewHeight = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pdMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private val path = Path()
    private var slantValuePx = 0
    private var additionalWidth = 0

    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun setImages(imageTopLeft: String, imageTopRight: String, imageBottomLeft: String, imageBottomRight: String) {

        Glide.with(context)
            .load(imageTopLeft)
            .transform(SlantTransformation(SlantTransformation.Position.TOP_LEFT))
            .into(imageViewTopLeft)

        Glide.with(this)
            .load(imageTopRight)
            .transform(SlantTransformation(SlantTransformation.Position.TOP_RIGHT))
            .into(imageViewTopRight)

        Glide.with(this)
            .load(imageBottomLeft)
            .transform(SlantTransformation(SlantTransformation.Position.BOTTOM_LEFT))
            .into(imageViewBottomLeft)

        Glide.with(this)
            .load(imageBottomRight)
            .transform(SlantTransformation(SlantTransformation.Position.BOTTOM_RIGHT))
            .into(imageViewBottomRight)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.view_image_container, this)

        additionalWidth = ADDITIONAL_WIDTH.dpToPx()
        slantValuePx = SLANT_VALUE.dpToPx()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        super.dispatchDraw(canvas)

        textViewWidth += additionalWidth

        paint.color = Color.BLACK
        paint.xfermode = pdMode

        path.reset()
        path.moveTo(0f, height.toFloat())

        path.lineTo(textViewWidth.toFloat(), height.toFloat())
        path.lineTo(
            textViewWidth.toFloat() + slantValuePx,
            (height - textViewHeight - slantValuePx).toFloat()
        )

        path.lineTo(0f, (height - textViewHeight).toFloat())
        path.close()

        canvas.drawPath(path, paint)
        canvas.restoreToCount(saveCount)
    }
}