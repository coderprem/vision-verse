package com.example.visionverse.presentation.screen.home

import android.app.Activity
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun FaceRecognitionScreen(modifier: Modifier = Modifier, activity: Activity, context: Context) {
    val controller = remember {
        LifecycleCameraController(activity.applicationContext).apply {
            // Set to use front camera
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            }
        )
    }
}

//import android.content.Context
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Rect
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import com.example.visionverse.utils.FaceDetectionHelper
//import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
//import java.util.concurrent.Executors
//
//@Composable
//fun FaceRecognitionScreen(modifier: Modifier = Modifier, context: Context, activity: Activity) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    // Track face detection results
//    var detectionResult by remember { mutableStateOf<FaceDetectorResult?>(null) }
//    var isFaceInPosition by remember { mutableStateOf(false) }
//
//    // Reference rectangle (centered, 60% of screen width)
//    val referenceRect = remember { Rect(0.2f, 0.3f, 0.8f, 0.7f) }
//
//    // Initialize face detector
//    val faceDetectorHelper = remember {
//        FaceDetectionHelper(context) { result ->
//            detectionResult = result
//            isFaceInPosition = result.detections().any { detection ->
//                detection.boundingBox()?.let { bbox ->
//                    val normalizedBbox = Rect(
//                        bbox.left / 1000f,
//                        bbox.top / 1000f,
//                        bbox.right / 1000f,
//                        bbox.bottom / 1000f
//                    )
//                    normalizedBbox.overlaps(referenceRect)
//                } ?: false
//            }
//        }
//    }
//
//    // Clean up on dispose
//    DisposableEffect(Unit) {
//        onDispose { faceDetectorHelper.close() }
//    }
//
//    Box(modifier = modifier.fillMaxSize()) {
//        // Camera Preview
//        AndroidView(
//            factory = { ctx ->
//                PreviewView(ctx).apply {
//                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
//                    cameraProviderFuture.addListener({
//                        val cameraProvider = cameraProviderFuture.get()
//
//                        // Camera configuration
//                        val preview = Preview.Builder().build()
//                        val imageAnalysis = ImageAnalysis.Builder()
//                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
//                            .build()
//                            .also { analysis ->
//                                analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
//                                    try {
//                                        val bitmap = imageProxy.toBitmap()
//                                        faceDetectorHelper.detect(bitmap)
//                                    } finally {
//                                        imageProxy.close()
//                                    }
//                                }
//                            }
//
//                        // Use front camera
//                        val cameraSelector = CameraSelector.Builder()
//                            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
//                            .build()
//
//                        cameraProvider.unbindAll()
//                        cameraProvider.bindToLifecycle(
//                            lifecycleOwner,
//                            cameraSelector,
//                            preview,
//                            imageAnalysis
//                        )
//                        preview.setSurfaceProvider(surfaceProvider)
//                    }, ContextCompat.getMainExecutor(ctx))
//                }
//            },
//            modifier = Modifier.fillMaxSize()
//        )
//
//        // Draw UI elements
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val canvasWidth = size.width
//            val canvasHeight = size.height
//
//            // Draw reference rectangle
//            val rectLeft = referenceRect.left * canvasWidth
//            val rectTop = referenceRect.top * canvasHeight
//            val rectRight = referenceRect.right * canvasWidth
//            val rectBottom = referenceRect.bottom * canvasHeight
//
//            drawRect(
//                color = if (isFaceInPosition) Color.Green else Color.Red,
//                topLeft = Offset(rectLeft, rectTop),
//                size = androidx.compose.ui.geometry.Size(
//                    rectRight - rectLeft,
//                    rectBottom - rectTop
//                ),
//                style = Stroke(width = 4.dp.toPx())
//            )
//
//            // Draw face bounding boxes
//            detectionResult?.detections()?.forEach { detection ->
//                detection.boundingBox()?.let { bbox ->
//                    val left = bbox.left / 1000f * canvasWidth
//                    val top = bbox.top / 1000f * canvasHeight
//                    val right = bbox.right / 1000f * canvasWidth
//                    val bottom = bbox.bottom / 1000f * canvasHeight
//
//                    drawRect(
//                        color = Color.Yellow,
//                        topLeft = Offset(left, top),
//                        size = androidx.compose.ui.geometry.Size(
//                            right - left,
//                            bottom - top
//                        ),
//                        style = Stroke(width = 2.dp.toPx())
//                    )
//                }
//            }
//        }
//    }
//}