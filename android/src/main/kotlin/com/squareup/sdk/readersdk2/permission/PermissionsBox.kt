package com.squareup.sdk.readersdk2.permission

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.sdk.readersdk2.R

class PermissionsBox(
  context: Context,
  attrs: AttributeSet?,
) : ConstraintLayout(context, attrs) {

    private val titleView: TextView
    private val subtitleView: TextView
    private val checkBox: ImageView

    init {
        inflate(context, R.layout.permissions_box, this)
        titleView = findViewById(R.id.permission_name)
        subtitleView = findViewById(R.id.permission_subtitle)
        checkBox = findViewById(R.id.check_box)
    }

    fun setTitle(title: String) {
        titleView.text = title
    }

    fun setSubtitle(subtitle: String) {
        subtitleView.text = subtitle
    }

    fun setChecked() {
        checkBox.setImageDrawable(
            resources.getDrawable(
                R.drawable.checkbox_filled_green_circle
            )
        )
    }
}
