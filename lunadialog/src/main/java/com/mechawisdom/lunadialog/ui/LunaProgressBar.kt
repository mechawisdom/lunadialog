package com.mechawisdom.lunadialog.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.mechawisdom.lunadialog.library.R
import com.mechawisdom.lunadialog.library.databinding.DialogLunaProgressBarBinding
import com.mechawisdom.lunadialog.utils.DialogAnimationStyle
import com.mechawisdom.lunadialog.utils.OrientationType
import com.mechawisdom.lunadialog.utils.ProgressDrawable


class LunaProgressBar(activity: Activity) : Dialog(activity) {
    private val binding: DialogLunaProgressBarBinding =
        DialogLunaProgressBarBinding.inflate(activity.layoutInflater)

    private var frameTime = 1000 / 12
    private var updateViewRunnable: Runnable? = null
    private var needToUpdateView = true
    private var backgroundStrokeColor: Int = Color.TRANSPARENT
    private var backgroundStrokeWidth: Int = 0
    private var delayInMillis: Long = 0L
    private var backgroundDrawable: GradientDrawable = GradientDrawable()

    init {
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes?.dimAmount = 0.5f
            setWindowAnimations(R.style.DialogZoomAnimation_Normal)

            setContainerPositionGravity(Gravity.CENTER, 0, 0)
        }

        setContentView(binding.root)
        binding.progressBackground.background = backgroundDrawable
    }


    companion object {
        @Volatile
        private var instance: LunaProgressBar? = null

        fun Builder(activity: Activity): LunaProgressBar {
            return instance ?: synchronized(this) {
                instance ?: LunaProgressBar(activity).also { instance = it }
            }
        }
    }

    override fun show() {
        if (!isShowing) {
            super.show()
            if (delayInMillis == 0L) {
                startRotatingAnimation()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isShowing) {
                        startRotatingAnimation()
                    }
                }, delayInMillis)
            }
        }
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
            instance = null
        }
    }

    fun setContainerPositionGravity(
        gravity: Int,
        yOffsetDP: Int,
        xOffsetDP: Int
    ): LunaProgressBar {
        window?.attributes?.let {
            it.gravity = gravity
            it.x = xOffsetDP.dpToPx()
            it.y = yOffsetDP.dpToPx()
            window?.attributes = it
        }
        return this
    }

    fun setContainerPositionGravitySDP(
        gravity: Int,
        yOffsetSDP: Int,
        xOffsetSDP: Int
    ): LunaProgressBar {
        window?.attributes?.let {
            it.gravity = gravity
            it.x = xOffsetSDP.sdpToPx()
            it.y = yOffsetSDP.sdpToPx()
            window?.attributes = it
        }
        return this
    }

    private fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private fun Int.sdpToPx(): Int {
        return context.resources.getDimension(this).toInt()
    }

    private fun updateLayoutParams(
        view: View,
        margin: Int? = null,
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return

        margin?.let {
            val marginPx = it.dpToPx()
            params.setMargins(marginPx, marginPx, marginPx, marginPx)
        }

        if (left != null || top != null || right != null || bottom != null) {
            params.setMargins(
                left?.dpToPx() ?: params.leftMargin,
                top?.dpToPx() ?: params.topMargin,
                right?.dpToPx() ?: params.rightMargin,
                bottom?.dpToPx() ?: params.bottomMargin
            )
        }

        view.layoutParams = params
    }

    private fun updateLayoutParamsSDP(
        view: View,
        margin: Int? = null,
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return

        margin?.let {
            val marginPx = it.sdpToPx()
            params.setMargins(marginPx, marginPx, marginPx, marginPx)
        }

        if (left != null || top != null || right != null || bottom != null) {
            params.setMargins(
                left?.sdpToPx() ?: params.leftMargin,
                top?.sdpToPx() ?: params.topMargin,
                right?.sdpToPx() ?: params.rightMargin,
                bottom?.sdpToPx() ?: params.bottomMargin
            )
        }

        view.layoutParams = params
    }

    private fun updatePadding(
        view: View,
        padding: Int? = null,
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        if (padding != null) {
            val paddingPx = padding.dpToPx()
            view.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
        } else {
            view.setPadding(
                left?.dpToPx() ?: view.paddingLeft,
                top?.dpToPx() ?: view.paddingTop,
                right?.dpToPx() ?: view.paddingRight,
                bottom?.dpToPx() ?: view.paddingBottom
            )
        }
    }

    private fun updatePaddingSDP(
        view: View,
        padding: Int? = null,
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        if (padding != null) {
            val paddingPx = padding.sdpToPx()
            view.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
        } else {
            view.setPadding(
                left?.sdpToPx() ?: view.paddingLeft,
                top?.sdpToPx() ?: view.paddingTop,
                right?.sdpToPx() ?: view.paddingRight,
                bottom?.sdpToPx() ?: view.paddingBottom
            )
        }
    }

    fun setAnimationStyle(animationStyle: DialogAnimationStyle): LunaProgressBar {
        window?.setWindowAnimations(animationStyle.styleRes)
        return this
    }

    fun setContainerDimAmount(dimAmount: Float): LunaProgressBar {
        val validDimAmount = when {
            dimAmount < 0f -> 0f
            dimAmount > 1f -> 1f
            else -> dimAmount
        }
        window?.attributes?.dimAmount = validDimAmount
        window?.attributes = window?.attributes
        return this
    }

    fun setAnimationDelay(seconds: Int): LunaProgressBar {
        delayInMillis = seconds * 1000L
        return this
    }

    fun setAnimationDelay(millis: Long): LunaProgressBar {
        delayInMillis = millis
        return this
    }

    fun setCancelableOption(isCancelable: Boolean): LunaProgressBar {
        setCancelable(isCancelable)
        return this
    }

    fun setProgressDrawable(drawable: Any): LunaProgressBar {
        when (drawable) {
            is ProgressDrawable -> {
                binding.progressImage.setImageResource(drawable.drawableRes)
            }

            is Int -> {
                binding.progressImage.setImageResource(drawable)
            }
        }
        return this
    }

    fun setProgressImageSize(widthDp: Int, heightDp: Int): LunaProgressBar {
        val widthPx = widthDp.dpToPx()
        val heightPx = heightDp.dpToPx()
        val layoutParams = binding.progressImage.layoutParams
        layoutParams.width = widthPx
        layoutParams.height = heightPx
        binding.progressImage.layoutParams = layoutParams

        return this
    }

    fun setProgressImageSizeSDP(widthSDP: Int, heightSDP: Int): LunaProgressBar {
        val widthPx = widthSDP.sdpToPx()
        val heightPx = heightSDP.sdpToPx()
        val layoutParams = binding.progressImage.layoutParams
        layoutParams.width = widthPx
        layoutParams.height = heightPx
        binding.progressImage.layoutParams = layoutParams
        return this
    }

    fun setProgressScaleType(scaleType: ImageView.ScaleType): LunaProgressBar {
        binding.progressImage.scaleType = scaleType
        return this
    }

    fun setContainerOrientation(orientation: OrientationType): LunaProgressBar {
        binding.progressBackground.orientation = orientation.value
        return this
    }

    fun setContainerBackgroundShape(shape: Int): LunaProgressBar {
        backgroundDrawable.shape = shape
        return this
    }

    fun setContainerCornerRadius(cornerRadius: Int): LunaProgressBar {
        backgroundDrawable.cornerRadius = getPxFromDp(cornerRadius).toFloat()
        return this
    }

    fun setContainerCornerRadiusSDP(cornerRadius: Int): LunaProgressBar {
        backgroundDrawable.cornerRadius = context.resources.getDimension(cornerRadius)
        return this
    }

    fun setContainerPadding(padding: Int): LunaProgressBar {
        updatePadding(binding.progressBackground, padding = padding)
        return this
    }

    fun setContainerPadding(left: Int, top: Int, right: Int, bottom: Int): LunaProgressBar {
        updatePadding(
            binding.progressBackground,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }


    fun setContainerPaddingSDP(padding: Int): LunaProgressBar {
        updatePaddingSDP(binding.progressBackground, padding = padding)
        return this
    }

    fun setContainerPaddingSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressBar {
        updatePaddingSDP(
            binding.progressBackground,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setContainerStrokeWidth(strokeWidth: Int): LunaProgressBar {
        backgroundStrokeWidth = strokeWidth.dpToPx()
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerStrokeWidthSDP(strokeWidth: Int): LunaProgressBar {
        backgroundStrokeWidth = strokeWidth.sdpToPx()
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerStrokeColor(strokeColor: Int): LunaProgressBar {
        backgroundStrokeColor = strokeColor
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerBackgroundColor(backgroundColor: Int): LunaProgressBar {
        backgroundDrawable.setColor(backgroundColor)
        return this
    }


    private fun getPxFromDp(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun setAnimationFPS(fps: Int): LunaProgressBar {
        frameTime = if (fps in 1..90) {
            1000 / fps
        } else {
            1000 / 12
        }
        return this
    }

    private fun startRotatingAnimation() {
        var rotateDegrees = 0f
        needToUpdateView = true

        updateViewRunnable = object : Runnable {
            override fun run() {
                rotateDegrees += 30
                rotateDegrees = if (rotateDegrees < 360) rotateDegrees else rotateDegrees - 360
                binding.progressImage.rotation = rotateDegrees
                if (needToUpdateView) {
                    binding.progressImage.postDelayed(this, frameTime.toLong())
                }
            }
        }
        binding.progressImage.post(updateViewRunnable)
    }

    private fun stopRotatingAnimation() {
        needToUpdateView = false
        updateViewRunnable?.let {
            Handler(Looper.getMainLooper()).removeCallbacks(it)
            updateViewRunnable = null
        }
    }

    fun build(): LunaProgressBar {
        return this
    }
}