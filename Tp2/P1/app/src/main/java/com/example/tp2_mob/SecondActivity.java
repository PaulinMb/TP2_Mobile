package com.example.tp2_mob;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {
    private ImageView selectedImageView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        selectedImageView = findViewById(R.id.selectedImage);

        int imageResource = getIntent().getIntExtra("imageResource", -1);

        if (imageResource != -1) {
            selectedImageView.setImageResource(imageResource);
        } else {
            Toast.makeText(this, "Aucune ressource image n'a été spécifiée.", Toast.LENGTH_SHORT).show();
        }

        // Lire musique aléatoire à partir de res/raw
        playRandomMusic();
    }

    private void playRandomMusic() {
        // Tableau des MP3 dans res/raw
        int[] musicResources = {R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4};

        // Sélectionne une musique aléatoire
        int randomIndex = new Random().nextInt(musicResources.length);
        int selectedMusicResource = musicResources[randomIndex];

        try {
            mediaPlayer = MediaPlayer.create(this, selectedMusicResource);

            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "Impossible de lire la musique.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la lecture de la musique.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
