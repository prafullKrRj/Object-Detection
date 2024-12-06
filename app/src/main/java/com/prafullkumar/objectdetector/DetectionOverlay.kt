package com.prafullkumar.objectdetector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.objects.DetectedObject
import kotlin.random.Random

class DetectionOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val detections = mutableListOf<DetectedObject>()
    private var imageWidth = 0
    private var imageHeight = 0

    // Color generation for unique object identification
    private val colorCache = mutableMapOf<String, Int>()

    private val boxPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
        textSize = 40f
    }

    private val textBackgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        alpha = 200
    }

    fun updateDetections(objects: List<DetectedObject>, width: Int, height: Int) {
        detections.clear()
        detections.addAll(objects)
        imageWidth = width
        imageHeight = height
        invalidate()
    }

    private fun generateUniqueColor(label: String): Int {
        return colorCache.getOrPut(label) {
            Color.rgb(
                Random.nextInt(100, 220),
                Random.nextInt(100, 220),
                Random.nextInt(100, 220)
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight

        detections.forEach { detection ->
            val boundingBox = detection.boundingBox
            val scaledRect = RectF(
                boundingBox.left * scaleX,
                boundingBox.top * scaleY,
                boundingBox.right * scaleX,
                boundingBox.bottom * scaleY
            )

            // Get primary label or use generic label
            val primaryLabel = detection.labels
                .maxByOrNull { it.confidence }
                ?.text
                ?: "Object"

            // Generate consistent color for this object type
            val objectColor = generateUniqueColor(primaryLabel)

            // Configure box paint with unique color
            boxPaint.color = objectColor
            textBackgroundPaint.color = objectColor

            // Draw bounding box
            canvas.drawRect(scaledRect, boxPaint)

            // Draw label background
            val labelText = "$primaryLabel (${
                String.format(
                    "%.1f%%",
                    detection.labels.firstOrNull()?.confidence?.times(100) ?: 0f
                )
            })"
            val textBounds = Rect()
            textPaint.getTextBounds(labelText, 0, labelText.length, textBounds)

            val labelBackgroundRect = RectF(
                scaledRect.left,
                scaledRect.top - textBounds.height() - 10f,
                scaledRect.left + textBounds.width() + 20f,
                scaledRect.top
            )

            canvas.drawRect(labelBackgroundRect, textBackgroundPaint)

            // Draw label text
            canvas.drawText(
                labelText,
                labelBackgroundRect.left + 10f,
                labelBackgroundRect.bottom - 10f,
                textPaint
            )
        }
    }

    // Additional method to clear detections
    fun clearDetections() {
        detections.clear()
        invalidate()
    }
}