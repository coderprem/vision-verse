package com.example.visionverse.utils

import android.graphics.*
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalGetImage::class)
fun ImageProxy.toBitmap(): Bitmap {
    val image = this.image ?: throw IllegalStateException("Image not available")
    val width = this.width
    val height = this.height
    
    return when (image.format) {
        ImageFormat.YUV_420_888 -> {
            // Convert YUV to RGB
            val yBuffer = image.planes[0].buffer
            val uBuffer = image.planes[1].buffer
            val vBuffer = image.planes[2].buffer
            
            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()
            
            val nv21 = ByteArray(ySize + uSize + vSize)
            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)
            
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, width, height), 90, out)
            val yuv = out.toByteArray()
            BitmapFactory.decodeByteArray(yuv, 0, yuv.size)
        }
        ImageFormat.FLEX_RGBA_8888 -> {
            val buffer = image.planes[0].buffer
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            bitmap
        }
        else -> throw IllegalArgumentException("Unsupported image format")
    }.also { bitmap ->
        // Apply rotation
        val matrix = Matrix().apply {
            postRotate(this@toBitmap.imageInfo.rotationDegrees.toFloat())
            // Flip horizontally for front camera
            postScale(-1f, 1f, width / 2f, height / 2f)
        }
        Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }
}