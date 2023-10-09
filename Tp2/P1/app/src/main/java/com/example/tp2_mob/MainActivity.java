package com.example.tp2_mob;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private TypedArray imgs;
    private int selectedImage = -1;
    private boolean hasRolled = false;
    private int[] images = {R.drawable.default_image, R.drawable.default_image,
            R.drawable.default_image, R.drawable.default_image};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgs = getResources().obtainTypedArray(R.array.images);

        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), images, this);
        gridView = findViewById(R.id.simplegridview);
        gridView.setAdapter(adapter);
    }

    public void rollImages(View view) {
        Random random = new Random();
        int image1 = imgs.getResourceId(random.nextInt(imgs.length()), 0);
        int image2 = imgs.getResourceId(random.nextInt(imgs.length()), 0);
        int image3 = imgs.getResourceId(random.nextInt(imgs.length()), 0);
        int image4 = imgs.getResourceId(random.nextInt(imgs.length()), 0);
        images = new int[]{image1, image2, image3, image4};
        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), images, this);
        gridView.setAdapter(adapter);

        hasRolled = true;
    }

    public void selectImage(View view, int imageView) {
        if (hasRolled) {
            selectedImage = imageView;
            for (int i = 0; i < gridView.getChildCount(); i++) {
                if (i == imageView) {
                    gridView.getChildAt(i).setAlpha(0.5f);
                } else {
                    gridView.getChildAt(i).setAlpha(1);
                }
            }
        } else {
            rollImages(view);
        }
    }

    public void startSecondaryActivity(View view) {
        if (selectedImage != -1) {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("imageResource", images[selectedImage]);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Veuillez sélectionner une image avant de continuer.", Toast.LENGTH_SHORT).show();
        }
    }
}
