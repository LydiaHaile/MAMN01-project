package com.example.nosnooze;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class PhotoScan extends AppCompatActivity {

    ImageView startColor;
    int colorMatch;
    int limit = 180;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_scan);
        TextView time = findViewById(R.id.time);
        time.setText(getIntent().getStringExtra("time"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        startColor = findViewById(R.id.imageView);
        generateColor();
        ImageButton snapButton = findViewById(R.id.snap_button);
        Button genNewColor = findViewById(R.id.gen_color);
        genNewColor.setOnClickListener(v -> {
            generateColor();
        });
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
        try {
            if (requestCode == 100 ) {
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                int compositeColor = getAverageColor(capturedImage);
                ImageView compositeColorView = findViewById(R.id.composite_color);
                compositeColorView.setBackgroundColor(compositeColor);

                if (compareColors(compositeColor, colorMatch) < limit) {
                    Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
                    serviceIntent.putExtra("extra", "alarm_off");
                    this.startService(serviceIntent);
                    Intent returnIntent = new Intent(this, MainActivity.class);
                    this.startActivity(returnIntent);
                } else {
                    TextView retryPrompt = findViewById(R.id.retry_prompt);
                    retryPrompt.setText("Try again!");
                }
            }
        }catch (NullPointerException e){
            Log.d("message","hej" +e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getAverageColor(Bitmap bitmap) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int pixelCount = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int count = 0;
        for (int color: pixels) {
            count++;
            int countWidth = count % width;
            if (countWidth > width * 0.30 && countWidth <  width * 0.70) {
                pixelCount++;
                red+= Color.red(color);
                green += Color.green(color);
                blue += Color.blue(color);
            }
        }
        return Color.rgb(red / pixelCount, green / pixelCount, blue / pixelCount);
    }

    private String getRGB(int color) {
        return "R: " + Color.red(color) + "G: " + Color.green(color) + "B: " + Color.blue(color);
    }

    private void generateColor() {
        Random rand = new Random();
        int color = Color.rgb(rand.nextInt(), rand.nextInt(), rand.nextInt());
        startColor.setBackgroundColor(color);
        colorMatch = color;
    }

    private int compareColors(int color1, int color2) {
        int redDiff = Math.abs(Color.red(color1) - Color.red(color2));
        int greenDiff = Math.abs(Color.green(color1) - Color.green(color2));
        int blueDiff = Math.abs(Color.blue(color1) - Color.blue(color2));
        return redDiff + greenDiff + blueDiff;
    }
}