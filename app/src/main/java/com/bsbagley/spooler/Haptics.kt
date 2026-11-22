package com.bsbagley.spooler

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build

/**
 * Haptic feedback for tag interactions. The app suppresses the platform NFC
 * sound (FLAG_READER_NO_PLATFORM_SOUNDS), so these are the user's only cue
 * that something happened — distinct effects tell success from failure
 * without looking at the screen.
 */
class Haptics(context: Context) {

    private val vibrator: Vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(VibratorManager::class.java)
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Vibrator::class.java)
                ?: @Suppress("DEPRECATION") context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

    /** Subtle tick: a tag entered the field and the app is acting on it. */
    fun tagDetected() = play(VibrationEffect.EFFECT_TICK)

    /** Single click: read or write completed successfully. */
    fun success() = play(VibrationEffect.EFFECT_CLICK)

    /** Double click: read or write failed — re-present the tag. */
    fun error() = play(VibrationEffect.EFFECT_DOUBLE_CLICK)

    private fun play(effect: Int) {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createPredefined(effect))
        }
    }
}
