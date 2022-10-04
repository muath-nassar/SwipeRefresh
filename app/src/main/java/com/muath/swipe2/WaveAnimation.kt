package com.muath.swipe2

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import androidx.core.graphics.minus
import androidx.core.graphics.xor
import com.simform.refresh.SSAnimationView

class WaveAnimation(context: Context) : SSAnimationView(context) {
    private var amplitude = 3f.toDp() // scale
    private var speed = 0f
    private val pathWave = Path()
    private var pathBackground = Path()
    private var textPath = Path()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var cutBackgroundPath = Path()

    override fun onDraw(c: Canvas) {
        createBackground()
        //createRoundCutInBackground()
        //pathBackground = pathBackground.minus(cutBackgroundPath)
        pathBackground = pathBackground.minus(pathWave)
        c.drawPath(pathBackground, backgroundPaint)
        showTitle(c)

    }

    private fun createAnimator(): ValueAnimator {
        return ValueAnimator.ofFloat(0f, Float.MAX_VALUE).apply {
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                speed -= WAVE_SPEED
                createPath()
                invalidate()
            }
        }
    }

    private fun createBackground() {
        val x = height.toFloat()/3.2f
        pathBackground.reset()
        pathBackground.fillType = Path.FillType.EVEN_ODD
        backgroundPaint.color = Color.CYAN//parseColor("#203354")
        pathBackground.moveTo(0f, 0f)
        pathBackground.lineTo(width.toFloat(),0f)
        pathBackground.lineTo(width.toFloat(),height.toFloat()-x)
        pathBackground.lineTo(0f,height.toFloat()-x)
        //pathBackground.lineTo(0f,0f)//in case of rectangle
        pathBackground.arcTo(RectF(0f,height.toFloat()-x*2,width.toFloat(),height.toFloat()),0f,180f)
        pathBackground.close()

    }


    private fun createRoundCutInBackground(){
        if (animator==null || animator?.isRunning == false){
            val rectF = RectF(0f,0f,width.toFloat(),0f)
            cutBackgroundPath.reset()
            cutBackgroundPath.moveTo(0f, height.toFloat())
            cutBackgroundPath.lineTo(0f, 10f)
            cutBackgroundPath.arcTo(rectF,0f,360f,false)
            cutBackgroundPath.lineTo(width.toFloat(), 0f)
            cutBackgroundPath.lineTo(0f, height.toFloat())

            cutBackgroundPath.close()
        }
    }

    private fun showTitle(c: Canvas) {
        textPath.reset()
        textPath = Path()
        textPath.addRect(0f,0f,width.toFloat(),height.toFloat()/2,Path.Direction.CW)
        textPaint.textSize = 45f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = Color.WHITE
        if (animator?.isRunning == true){
            c.drawText("updating content", (width / 2).toFloat(), (height / 2).toFloat()-40, textPaint)

        }else{
            c.drawText("scroll down to update", (width / 2).toFloat(), (height / 2).toFloat(), textPaint)

        }
    }

    private fun createPath() {
        pathWave.reset()
        paint.color = Color.WHITE//parseColor("#203354")
        pathWave.moveTo(0f, height.toFloat())
        pathWave.lineTo(0f, amplitude)
        pathWave.lineTo(0f, amplitude - 10)
        var i = 0
        while (i < width + 10) {
            val wx = i.toFloat()
            val wy =
                amplitude * 2 + amplitude * kotlin.math.sin((i + 10) * Math.PI / WAVE_AMOUNT_ON_SCREEN + speed) + 200
            pathWave.lineTo(wx, wy.toFloat())
            i += 10
        }
        pathWave.lineTo(width.toFloat(), height.toFloat())
        pathWave.close()
        //pathBackground = pathBackground.minus(pathWave)


    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    companion object {
        const val WAVE_SPEED = 0.25f
        const val WAVE_AMOUNT_ON_SCREEN = 350
    }

    private fun Float.toDp() = this * context.resources.displayMetrics.density

    override fun reset() {
    }

    override fun refreshing() {
        animator = createAnimator().apply {
            start()
        }
    }

    override fun refreshComplete() {
        animator?.cancel()
        pathWave.reset()
        textPath.reset()
    }
    override fun pullToRefresh() {
        animator = createAnimator().apply {
            start()
        }
    }

    override fun releaseToRefresh() {

    }

    override fun pullProgress(pullDistance: Float, pullProgress: Float) {
    }

}