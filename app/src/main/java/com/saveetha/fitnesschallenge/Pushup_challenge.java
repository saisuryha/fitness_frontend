package com.saveetha.fitnesschallenge;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pushup_challenge extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1001;
    private PreviewView previewView;
    private ImageView startButton;
    private TextView pushupCountView;
    private ExecutorService cameraExecutor;

    private boolean isDown = false;
    private int pushUpCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushup_challenge);

        previewView = new PreviewView(this);
        previewView.setLayoutParams(
                new PreviewView.LayoutParams(
                        PreviewView.LayoutParams.MATCH_PARENT,
                        PreviewView.LayoutParams.MATCH_PARENT
                )
        );

        View frame = findViewById(R.id.camera_frame);
        if (frame instanceof ViewGroup) {
            ((ViewGroup) frame).addView(previewView, 0);
        }

        startButton = findViewById(R.id.start_button);
        pushupCountView = findViewById(R.id.pushup_count);  // TextView for showing count
        cameraExecutor = Executors.newSingleThreadExecutor();

        startButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                AccuratePoseDetectorOptions options =
                        new AccuratePoseDetectorOptions.Builder()
                                .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                                .build();
                PoseDetector poseDetector = PoseDetection.getClient(options);

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    processImageProxy(poseDetector, imageProxy);
                });

                cameraProvider.unbindAll();
                Camera camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void processImageProxy(PoseDetector detector, ImageProxy imageProxy) {
        if (imageProxy == null || imageProxy.getImage() == null) {
            return;
        }

        InputImage image = InputImage.fromMediaImage(
                imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        detector.process(image)
                .addOnSuccessListener(pose -> detectPushUp(pose))
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(task -> imageProxy.close());
    }

    private void detectPushUp(Pose pose) {
        PoseLandmark shoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark wrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);

        if (shoulder != null && wrist != null) {
            float shoulderY = shoulder.getPosition().y;
            float wristY = wrist.getPosition().y;

            if (!isDown && wristY - shoulderY > 100) {
                isDown = true;
            } else if (isDown && wristY - shoulderY < 40) {
                isDown = false;
                pushUpCount++;

                runOnUiThread(() -> pushupCountView.setText(String.valueOf(pushUpCount)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}
