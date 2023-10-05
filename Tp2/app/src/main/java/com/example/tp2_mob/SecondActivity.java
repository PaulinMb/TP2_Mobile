package com.example.tp2_mob;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Récupérez l'identifiant de la ressource audio transmise depuis MainActivity
        int audioResourceId = getIntent().getIntExtra("audioResource", -1);

        if (audioResourceId != -1) {
            mediaPlayer = MediaPlayer.create(this, audioResourceId);

            if (mediaPlayer != null) {
                try {
                    mediaPlayer.start();
                } catch (IllegalStateException e) {
                    // Gestion d'exception en cas d'erreur de démarrage de la lecture
                    e.printStackTrace();
                    Toast.makeText(this, "Erreur lors de la lecture de la musique.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Gestion d'exception en cas d'échec d'initialisation du lecteur audio
                Toast.makeText(this, "Impossible d'initialiser le lecteur audio.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Gestion d'exception si aucune ressource audio n'a été transmise
            Toast.makeText(this, "Aucune ressource audio n'a été spécifiée.", Toast.LENGTH_SHORT).show();
        }
    }

    // ...

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
