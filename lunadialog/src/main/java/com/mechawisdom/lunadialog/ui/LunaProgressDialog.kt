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
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.mechawisdom.lunadialog.library.R
import com.mechawisdom.lunadialog.library.databinding.DialogLunaProgressDialogBinding
import com.mechawisdom.lunadialog.utils.AnimationType
import com.mechawisdom.lunadialog.utils.DialogAnimationStyle
import com.mechawisdom.lunadialog.utils.OrientationType
import com.mechawisdom.lunadialog.utils.ProgressDrawable
import com.mechawisdom.lunadialog.utils.dpToPx
import com.mechawisdom.lunadialog.utils.sdpToPx
import com.mechawisdom.lunadialog.utils.updateLayoutParams
import com.mechawisdom.lunadialog.utils.updateLayoutParamsSDP
import com.mechawisdom.lunadialog.utils.updatePadding
import com.mechawisdom.lunadialog.utils.updatePaddingSDP


class LunaProgressDialog(activity: Activity) : Dialog(activity) {
    private var animationType: AnimationType = AnimationType.IMAGEVIEW
    private var titleText: String = ""
    private var descriptionText: String = ""
    private val binding: DialogLunaProgressDialogBinding =
        DialogLunaProgressDialogBinding.inflate(activity.layoutInflater)

    private var frameTime = 1000 / 12
    private var updateViewRunnable: Runnable? = null
    private var needToUpdateView = true
    private var backgroundStrokeColor: Int = Color.TRANSPARENT
    private var backgroundStrokeWidth: Int = 0
    private var delayInMillis: Long = 0L
    private var startAnimation: Boolean = true
    private var backgroundDrawable: GradientDrawable = GradientDrawable()
    private var lottieRepeatCount: Int = -1

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
        private var instance: LunaProgressDialog? = null

        fun Builder(activity: Activity): LunaProgressDialog {
            return instance ?: synchronized(this) {
                instance ?: LunaProgressDialog(activity).also { instance = it }
            }
        }
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
    }

    override fun show() {
        if (!isShowing) {
            super.show()
            when (animationType) {
                AnimationType.IMAGEVIEW -> {
                    if (startAnimation) {
                        startImageViewAnimation()
                    }
                }

                AnimationType.LOTTIE -> {
                    if (startAnimation) {
                        startLottieAnimation()
                    }
                }
            }

        }
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
            instance = null
            when (animationType) {
                AnimationType.IMAGEVIEW -> {
                    if (startAnimation) {
                        stopRotatingAnimation()
                    }
                }

                AnimationType.LOTTIE -> {
                    if (startAnimation) {
                        stopLottieAnimation()
                    }
                }
            }
        }
    }

    fun setContainerPositionGravity(
        gravity: Int,
        yOffsetDP: Int,
        xOffsetDP: Int
    ): LunaProgressDialog {
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
    ): LunaProgressDialog {
        window?.attributes?.let {
            it.gravity = gravity
            it.x = xOffsetSDP.sdpToPx(context)
            it.y = yOffsetSDP.sdpToPx(context)
            window?.attributes = it
        }
        return this
    }

    fun setAnimationType(type: AnimationType): LunaProgressDialog {
        animationType = type
        when (animationType) {
            AnimationType.IMAGEVIEW -> {
                binding.progressImage.visibility = View.VISIBLE
                binding.lottieAnimationView.visibility = View.GONE
            }

            AnimationType.LOTTIE -> {
                binding.lottieAnimationView.visibility = View.VISIBLE
                binding.progressImage.visibility = View.GONE
            }
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


    fun setAnimationStyle(animationStyle: DialogAnimationStyle): LunaProgressDialog {
        window?.setWindowAnimations(animationStyle.styleRes)
        return this
    }

    fun setContainerDimAmount(dimAmount: Float): LunaProgressDialog {
        val validDimAmount = when {
            dimAmount < 0f -> 0f
            dimAmount > 1f -> 1f
            else -> dimAmount
        }
        window?.attributes?.dimAmount = validDimAmount
        window?.attributes = window?.attributes
        return this
    }


    fun setTitleText(title: String): LunaProgressDialog {
        titleText = title
        return this
    }

    fun setAnimationDelay(seconds: Int): LunaProgressDialog {
        delayInMillis = seconds * 1000L
        return this
    }

    fun setAnimationDelay(millis: Long): LunaProgressDialog {
        delayInMillis = millis
        return this
    }

    fun setAnimationStart(animation: Boolean): LunaProgressDialog {
        startAnimation = animation
        return this
    }

    fun setDescriptionText(description: String): LunaProgressDialog {
        descriptionText = description
        return this
    }

    fun setCancelableOption(isCancelable: Boolean): LunaProgressDialog {
        setCancelable(isCancelable)
        return this
    }

    fun setProgressDrawable(drawable: ProgressDrawable): LunaProgressDialog {
        binding.progressImage.setImageResource(drawable.drawableRes)
        return this
    }

    fun setCustomDrawable(drawable: Int): LunaProgressDialog {
        binding.progressImage.setImageResource(drawable)
        return this
    }

    fun setProgressImageSize(widthDp: Int, heightDp: Int): LunaProgressDialog {
        val widthPx = widthDp.dpToPx(context)
        val heightPx = heightDp.dpToPx(context)
        when (animationType) {
            AnimationType.IMAGEVIEW -> {
                val layoutParams = binding.progressImage.layoutParams
                layoutParams.width = widthPx
                layoutParams.height = heightPx
                binding.progressImage.layoutParams = layoutParams
            }

            AnimationType.LOTTIE -> {
                val layoutParams = binding.lottieAnimationView.layoutParams
                layoutParams.width = widthPx
                layoutParams.height = heightPx
                binding.lottieAnimationView.layoutParams = layoutParams
            }
        }

        return this
    }

    fun setProgressImageSizeSDP(widthSDP: Int, heightSDP: Int): LunaProgressDialog {
        val widthPx = widthSDP.sdpToPx(context)
        val heightPx = heightSDP.sdpToPx(context)

        when (animationType) {
            AnimationType.IMAGEVIEW -> {
                val layoutParams = binding.progressImage.layoutParams
                layoutParams.width = widthPx
                layoutParams.height = heightPx
                binding.progressImage.layoutParams = layoutParams
            }

            AnimationType.LOTTIE -> {
                val layoutParams = binding.lottieAnimationView.layoutParams
                layoutParams.width = widthPx
                layoutParams.height = heightPx
                binding.lottieAnimationView.layoutParams = layoutParams
            }
        }
        return this
    }

    fun setProgressScaleType(scaleType: ImageView.ScaleType): LunaProgressDialog {
        when (animationType) {
            AnimationType.IMAGEVIEW -> {
                binding.progressImage.scaleType = scaleType
            }

            AnimationType.LOTTIE -> {
                binding.lottieAnimationView.scaleType = scaleType
            }
        }
        return this
    }

    fun setContainerOrientation(orientation: OrientationType): LunaProgressDialog {
        binding.progressBackground.orientation = orientation.value
        return this
    }

    fun setContainerBackgroundShape(shape: Int): LunaProgressDialog {
        backgroundDrawable.shape = shape
        return this
    }

    fun setContainerCornerRadius(cornerRadius: Int): LunaProgressDialog {
        backgroundDrawable.cornerRadius = getPxFromDp(cornerRadius).toFloat()
        return this
    }

    fun setContainerCornerRadiusSDP(cornerRadius: Int): LunaProgressDialog {
        backgroundDrawable.cornerRadius = context.resources.getDimension(cornerRadius)
        return this
    }

    fun setContainerPadding(padding: Int): LunaProgressDialog {
        updatePadding(context, binding.progressBackground, padding = padding)
        return this
    }

    fun setContainerPadding(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
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


    fun setContainerPaddingSDP(padding: Int): LunaProgressDialog {
        updatePaddingSDP(context, binding.progressBackground, padding = padding)
        return this
    }

    fun setContainerPaddingSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
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

    fun setContainerStrokeWidth(strokeWidth: Int): LunaProgressDialog {
        backgroundStrokeWidth = strokeWidth.dpToPx(context)
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerStrokeWidthSDP(strokeWidth: Int): LunaProgressDialog {
        backgroundStrokeWidth = strokeWidth.sdpToPx(context)
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerStrokeColor(strokeColor: Int): LunaProgressDialog {
        backgroundStrokeColor = strokeColor
        backgroundDrawable.setStroke(backgroundStrokeWidth, backgroundStrokeColor)
        return this
    }

    fun setContainerBackgroundColor(backgroundColor: Int): LunaProgressDialog {
        backgroundDrawable.setColor(backgroundColor)
        return this
    }


    // TEXT container

    fun setTextContainerGravity(gravity: Int): LunaProgressDialog {
        binding.textViewContainer.gravity = gravity
        return this
    }

    fun setTextContainerMargin(margin: Int): LunaProgressDialog {
        updateLayoutParams(context, binding.textViewContainer, margin = margin)
        return this
    }

    fun setTextContainerMargin(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updateLayoutParams(
            context,
            binding.textViewContainer,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setTextContainerMarginSDP(margin: Int): LunaProgressDialog {
        updateLayoutParamsSDP(context, binding.textViewContainer, margin = margin)
        return this
    }

    fun setTextContainerMarginSDP(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): LunaProgressDialog {
        updateLayoutParamsSDP(
            context,
            binding.textViewContainer,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }
    //

    fun setTextContainerPadding(padding: Int): LunaProgressDialog {
        updatePadding(context, binding.textViewContainer, padding = padding)
        return this
    }

    fun setTextContainerPadding(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updatePadding(
            context,
            binding.textViewContainer,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setTextContainerPaddingSDP(padding: Int): LunaProgressDialog {
        updatePaddingSDP(context, binding.textViewContainer, padding = padding)
        return this
    }

    fun setTextContainerPaddingSDP(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): LunaProgressDialog {
        updatePaddingSDP(
            context,
            binding.textViewContainer,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    //Title
    fun setTitleTextColor(textColor: Int): LunaProgressDialog {
        binding.titleTextView.setTextColor(textColor)
        return this
    }

    fun setTitleTextSize(textSize: Int): LunaProgressDialog {
        binding.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        return this
    }

    fun setTitleTextSizeSSP(textSize: Int): LunaProgressDialog {
        binding.titleTextView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimensionPixelSize(textSize).toFloat()
        )
        return this
    }

    fun setTitleTextStyle(textStyle: Int): LunaProgressDialog {
        binding.titleTextView.setTypeface(binding.titleTextView.typeface, textStyle)
        return this
    }

    fun setTitleFontFamily(fontResId: Int): LunaProgressDialog {
        val typeface = ResourcesCompat.getFont(context, fontResId)
        binding.titleTextView.typeface = typeface
        return this
    }

    fun setTitleMargin(margin: Int): LunaProgressDialog {
        updateLayoutParams(context, binding.titleTextView, margin = margin)
        return this
    }

    fun setTitleMargin(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updateLayoutParams(
            context,
            binding.titleTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setTitleMarginSDP(margin: Int): LunaProgressDialog {
        updateLayoutParamsSDP(context, binding.titleTextView, margin = margin)
        return this
    }

    fun setTitleMarginSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updateLayoutParamsSDP(
            context,
            binding.titleTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }


    fun setTitlePadding(padding: Int): LunaProgressDialog {
        updatePadding(context, binding.titleTextView, padding = padding)
        return this
    }

    fun setTitlePadding(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updatePadding(
            context,
            binding.titleTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setTitlePaddingSDP(padding: Int): LunaProgressDialog {
        updatePaddingSDP(context, binding.titleTextView, padding = padding)
        return this
    }

    fun setTitlePaddingSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updatePaddingSDP(
            context,
            binding.titleTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }


    // DESC

    fun setDescriptionTextColor(textColor: Int): LunaProgressDialog {
        binding.descriptionTextView.setTextColor(textColor)
        return this
    }

    fun setDescriptionTextSize(textSize: Int): LunaProgressDialog {
        binding.descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        return this
    }

    fun setDescriptionTextSizeSSP(textSize: Int): LunaProgressDialog {
        binding.descriptionTextView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimensionPixelSize(textSize).toFloat()
        )
        return this
    }

    fun setDescriptionTextStyle(textStyle: Int): LunaProgressDialog {
        binding.descriptionTextView.setTypeface(binding.descriptionTextView.typeface, textStyle)
        return this
    }

    fun setDescriptionFontFamily(fontResId: Int): LunaProgressDialog {
        val typeface = ResourcesCompat.getFont(context, fontResId)
        binding.descriptionTextView.typeface = typeface
        return this
    }

    fun setDescriptionMargin(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updateLayoutParams(
            context,
            binding.descriptionTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setDescriptionMargin(margin: Int): LunaProgressDialog {
        updateLayoutParams(context, binding.descriptionTextView, margin = margin)
        return this
    }

    fun setDescriptionMarginSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updateLayoutParamsSDP(
            context,
            binding.descriptionTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setDescriptionMarginSDP(margin: Int): LunaProgressDialog {
        updateLayoutParamsSDP(context, binding.descriptionTextView, margin = margin)
        return this
    }


    fun setDescriptionPadding(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updatePadding(
            context,
            binding.descriptionTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setDescriptionPadding(padding: Int): LunaProgressDialog {
        updatePadding(context, binding.descriptionTextView, padding = padding)
        return this
    }


    fun setDescriptionPaddingSDP(left: Int, top: Int, right: Int, bottom: Int): LunaProgressDialog {
        updatePaddingSDP(
            context,
            binding.descriptionTextView,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
        return this
    }

    fun setDescriptionPaddingSDP(padding: Int): LunaProgressDialog {
        updatePaddingSDP(context, binding.descriptionTextView, padding = padding)
        return this
    }


    private fun getPxFromDp(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun setAnimationFPS(fps: Int): LunaProgressDialog {
        frameTime = if (fps in 1..90) {
            1000 / fps
        } else {
            1000 / 12
        }
        return this
    }

    private fun startRotatingAnimation() {
        /*
        val rotationAnimation = ObjectAnimator.ofFloat(binding.progressImage, "rotation", 0f, 360f).apply {
            duration = 1000 // 1 saniyede bir tur
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator() // Düzgün hızda döndürme
        }
        rotationAnimation.start()
*/


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


    private fun setLottieAnimationRepeatCount(count:Int) {
        lottieRepeatCount=count
    }

    private fun startLottieAnimation() {
        binding.lottieAnimationView.repeatCount = lottieRepeatCount
        binding.lottieAnimationView.playAnimation()
    }

    private fun pauseLottieAnimation() {
        binding.lottieAnimationView.pauseAnimation()
    }

    private fun stopLottieAnimation() {
        binding.lottieAnimationView.cancelAnimation()
    }


    private fun setTextVisibility(textView: TextView, text: String) {
        textView.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
        textView.text = text
    }

    fun build(): LunaProgressDialog {
        setTextVisibility(binding.titleTextView, titleText)
        setTextVisibility(binding.descriptionTextView, descriptionText)
        return this
    }
}