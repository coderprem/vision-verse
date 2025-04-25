package com.example.visionverse.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

class FaceDetectionHelper(
    context: Context,
    private val onResults: (FaceDetectorResult) -> Unit
) {
    private var faceDetector: FaceDetector? = null

    init {
        setupFaceDetector(context.applicationContext)
    }

    private fun setupFaceDetector(context: Context) {
        try {
            // 1. Verify model file exists
            val modelFileName = "face_detection_short_range.tflite"
            val assets = context.assets
            val files = assets.list("")
            if (files?.contains(modelFileName) != true) {
                throw IllegalStateException("Model file '$modelFileName' not found in assets")
            }

            // 2. Configure base options
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(modelFileName)
                .setDelegate(Delegate.GPU) // Keep GPU for now, but be ready to test without
                .build()

            val options = FaceDetector.FaceDetectorOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinDetectionConfidence(0.5f)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result, _ -> onResults(result) }
                .build()

            faceDetector = FaceDetector.createFromOptions(context, options)
            Log.d(TAG, "FaceDetector initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize FaceDetector: ${e.message}")
            throw RuntimeException("Failed to initialize FaceDetector. ${e.message}", e)
        }
    }

    fun detect(image: Bitmap, rotationDegrees: Int = 0, timestamp: Long = System.currentTimeMillis()) {
        try {
            val mpImage = BitmapImageBuilder(image).build()
            val imageProcessingOptions = ImageProcessingOptions.builder()
                .setRotationDegrees(rotationDegrees)
                .build()

            faceDetector?.detectAsync(mpImage, imageProcessingOptions, timestamp)
        } catch (e: Exception) {
            Log.e(TAG, "Detection error: ${e.message}")
        }
    }

    fun close() {
        try {
            faceDetector?.close()
            faceDetector = null
            Log.d(TAG, "FaceDetector closed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing FaceDetector: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "FaceDetectionHelper"
    }
}