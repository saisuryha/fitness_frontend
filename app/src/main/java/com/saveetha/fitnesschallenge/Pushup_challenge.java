package com.saveetha.fitnesschallenge;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pushup_challenge extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1001;
    private static final long INACTIVITY_TIMEOUT_MS = 5000;

    private PreviewView previewView;
    private ImageView startButton;
    private ImageView restartButton;
    private TextView pushupCountView;
    private ExecutorService cameraExecutor;

    private boolean isDown = false;
    private int pushUpCount = 0;

    private long lastPushUpTime = 0;
    private boolean challengeCompleted = false;
    private final Handler inactivityHandler = new Handler(Looper.getMainLooper());
    private final Runnable inactivityRunnable = () -> {
        if (!challengeCompleted && (System.currentTimeMillis() - lastPushUpTime) >= INACTIVITY_TIMEOUT_MS) {
            challengeCompleted = true;
            showCompletionDialog();
        }
    };

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
        restartButton = findViewById(R.id.restart_button);
        pushupCountView = findViewById(R.id.pushup_count);
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

        restartButton.setOnClickListener(v -> {
            pushUpCount = 0;
            isDown = false;
            challengeCompleted = false;
            runOnUiThread(() -> pushupCountView.setText("0"));
            Toast.makeText(this, "Push-up count reset", Toast.LENGTH_SHORT).show();
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
                .addOnSuccessListener(this::detectPushUp)
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(task -> imageProxy.close());
    }

    private void detectPushUp(Pose pose) {
        if (challengeCompleted) return;

        PoseLandmark shoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark elbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);

        if (shoulder != null && elbow != null) {
            float shoulderY = shoulder.getPosition().y;
            float elbowY = elbow.getPosition().y;
            float diff = elbowY - shoulderY;

            if (!isDown && diff > 70) {
                isDown = true;
            } else if (isDown && diff < 40) {
                isDown = false;
                pushUpCount++;
                lastPushUpTime = System.currentTimeMillis();
                runOnUiThread(() -> pushupCountView.setText(String.valueOf(pushUpCount)));

                inactivityHandler.removeCallbacks(inactivityRunnable);
                inactivityHandler.postDelayed(inactivityRunnable, INACTIVITY_TIMEOUT_MS);
            }
        }
    }

    private void showCompletionDialog() {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Push-up Challenge Completed")
                .setMessage("Your push-up count is " + pushUpCount)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show());
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
        inactivityHandler.removeCallbacks(inactivityRunnable);
    }
}
