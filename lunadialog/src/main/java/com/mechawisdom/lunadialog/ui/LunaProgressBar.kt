package com.mechawisdom.lunadialog.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import com.mechawisdom.lunadialog.library.R
import com.mechawisdom.lunadialog.library.databinding.DialogLunaProgressBarBinding
import com.mechawisdom.lunadialog.utils.AnimationStyle
import com.mechawisdom.lunadialog.utils.OrientationType
import com.mechawisdom.lunadialog.utils.ProgressDrawable
import com.mechawisdom.lunadialog.utils.dpToPx
import com.mechawisdom.lunadialog.utils.sdpToPx
import com.mechawisdom.lunadialog.utils.updatePadding
import com.mechawisdom.lunadialog.utils.updatePaddingSDP


class LunaProgressBar(activity: Activity) : Dialog(activity) {
    private val binding: DialogLunaProgressBarBinding =
        DialogLunaProgressBarBinding.inflate(activity.layoutInflater)

    private var frameTime = 1000 / 12
    private var updateViewRunnable: Runnable? = null
    private var needToUpdateView = true
    private var backgroundStrokeColor: Int = Color.TRANSPARENT
    private var backgroundStrokeWidth: Int = 0
    private var delayInMillis: Long = 0L
    private var startAnimation: Boolean = true
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
            if (startAnimation) {
                startImageViewAnimation()
            }
        }
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
            instance = null
            if (startAnimation) {
                stopRotatingAnimation()
            }
        }
    }

    fun setContainerPositionGravity(
        gravity: Int,
        yOffsetDP: Int,
        xOffsetDP: Int
    ): LunaProgressBar {
        window?.attributes?.let {
            it.gravity = gravity
            it.x = xOffsetDP.dpToPx(context)
            it.y = yOffsetDP.dpToPx(context)
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
            it.x = xOffsetSDP.sdpToPx(context)
            it.y = yOffsetSDP.sdpToPx(context)
            window?.attributes = it
        }
        return this
    }


    private fun startImageViewAnimation() {
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


    fun setAnimationStyle(animationStyle: AnimationStyle): LunaProgressBar {
        window?.setWindowAnimations(animationStyle.styleRes)
        return this
    }

    fun setCustomAnimationStyle(animationStyle: Int): LunaProgressBar {
        window?.setWindowAnimations(animationStyle)
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

    fun setAnimationStart(animation: Boolean): LunaProgressBar {
        startAnimation = animation
        return this
    }


    fun setCancelableOption(isCancelable: Boolean): LunaProgressBar {
        setCancelable(isCancelable)
        return this
    }

    fun setProgressDrawable(drawable: ProgressDrawable): LunaProgressBar {
        binding.progressImage.setImageResource(drawable.drawableRes)
        return this
    }

    fun setCustomDrawable(drawable: Int): LunaProgressBar {
        binding.progressImage.setImageResource(drawable)
        return this
    }

    fun setProgressImageSize(widthDp: Int, heightDp: Int): LunaProgressBar {
        val widthPx = widthDp.dpToPx(context)
        val heightPx = heightDp.dpToPx(context)
        val layoutParams = binding.progressImage.layoutParams
        layoutParams.width = widthPx
        layoutParams.height = heightPx
        binding.progressImage.layoutParams = layoutParams
        return this
    }

    fun setProgressImageSizeSDP(widthSDP: Int, heightSDP: Int): LunaProgressBar {
        val widthPx = widthSDP.sdpToPx(context)
        val heightPx = heightSDP.sdpToPx(context)
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
        backgroundDrawable.cornerRadius = cornerRadius.dpToPx(context).toFloat()
        return this
    }

    fun setContainerCornerRadiusSDP(cornerRadius: Int): LunaProgressBar {
        backgroundDrawable.cornerRadius = cornerRadius.sdpToPx(context).toFloat()
        return this
    }

    fun setContainerPadding(padding: Int): LunaProgressBar {
        updatePadding(context, binding.progressBackground, padding = padding)
        return this
    }

    fun setContainerPadding(left: Int, top: Int, right: Int, bottom: Int): LunaProgressBar {
        updatePadding(
            context,
            binding.progressBackground,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }


    fun setContainerPaddingSDP(padding: Int): LunaProgressBar {
        updatePaddingSDP(context, binding.progressBackground, padding = padding)
        return this
    }

    fun setContainerPaddingSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressBar {
        updatePaddingSDP(
            context,
            binding.progressBackground,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setContainerStrokeWidth(strokeWidth: Int): LunaProgressBar {
        backgroundStrokeWidth = strokeWidth.dpToPx(context)
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerStrokeWidthSDP(strokeWidth: Int): LunaProgressBar {
        backgroundStrokeWidth = strokeWidth.sdpToPx(context)
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