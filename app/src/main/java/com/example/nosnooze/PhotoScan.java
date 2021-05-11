package com.example.nosnooze;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class PhotoScan extends AppCompatActivity {

    private int maxDifference = 1000;
    private int[] capturedColors, generatedColors;
    ImageView snappedPhoto;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_scan);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        snappedPhoto = findViewById(R.id.picture);
        Button snapButton = findViewById(R.id.snap_button);
        View startColor = findViewById(R.id.imageView);
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        int randomColor = Color.rgb(r, g, b);
        this.generatedColors = getAverageColors(loadBitmapFromView(startColor));
        startColor.setBackgroundColor(randomColor);

        if (ContextCompat.checkSelfPermission(PhotoScan.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PhotoScan.this,
                    new String[] {
                            Manifest.permission.CAMERA
                    }, 100);
        }
        snapButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            this.capturedColors = getAverageColors(capturedImage);
            snappedPhoto.setImageBitmap(capturedImage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int[] getAverageColors(Bitmap bitmap) {
        int R = 0;
        int G = 0;
        int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int color : pixels) {
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return new int[]{R/n, G/n, B/n};
    }

    private int getDifference() {
        int difference = 0;
        for (int i = 0; i < 3; i++) {
            difference += Math.abs(generatedColors[i] - capturedColors[i]);
        }
        return difference;
    }

    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }


}