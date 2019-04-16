package com.example.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.custom_constraint.view.*

/**
 * Created by Irene Khramova on 15.04.2019.
 */
class Customconstraint : ConstraintLayout {
    constructor(context: Context) : super(context) {
        init(context, null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.custom_constraint, this)
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.Customconstraint,
                    0,
                    0)
            try {
                //TODO: default values
                Glide.with(this)
                        .load(a.getResourceId(R.styleable.Customconstraint_srcTopLeft, 0))
                        .transform(MyTransformation(context, MyTransformation.Position.TOP_LEFT))
                        .into(imageViewTopLeft)
                Glide.with(this)
                        .load(a.getResourceId(R.styleable.Customconstraint_srcTopRight, 0))
                        .transform(MyTransformation(context, MyTransformation.Position.TOP_RIGHT))
                        .into(imageViewTopRight)
                Glide.with(this)
                        .load(a.getResourceId(R.styleable.Customconstraint_srcBottomLeft, 0))
                        .transform(MyTransformation(context, MyTransformation.Position.BOTTOM_LEFT))
                        .into(imageViewBottomLeft)
                Glide.with(this)
                        .load(a.getResourceId(R.styleable.Customconstraint_srcBottomRight, 0))
                        .transform(MyTransformation(context, MyTransformation.Position.BOTTOM_RIGHT))
                        .into(imageViewBottomRight)
            } finally {
                a.recycle()
            }
        }
    }
}