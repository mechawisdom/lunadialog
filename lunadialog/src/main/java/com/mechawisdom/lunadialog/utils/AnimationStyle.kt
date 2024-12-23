package com.mechawisdom.lunadialog.utils

import com.mechawisdom.lunadialog.library.R

enum class AnimationStyle(val styleRes: Int) {
    ZOOM_NORMAL(R.style.DialogZoomAnimation_Normal),
    ZOOM_SLOW(R.style.DialogZoomAnimation_Slow),
    ZOOM_FAST(R.style.DialogZoomAnimation_Fast),
    SLIDE_UP(R.style.DialogSlideUpAnimation),
    SLIDE_DOWN(R.style.DialogSlideDownAnimation),
    SLIDE_LEFT(R.style.DialogSlideLeftAnimation),
    SLIDE_RIGHT(R.style.DialogSlideRightAnimation),
    OVERSHOOT(R.style.DialogOvershootAnimation),
    BOUNCE(R.style.DialogBounceAnimation),
    SHAKE(R.style.DialogShakeAnimation),
    ALPHA(R.style.DialogAlphaAnimation),
}