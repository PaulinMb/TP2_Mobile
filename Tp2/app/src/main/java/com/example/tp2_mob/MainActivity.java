package com.example.tp2_mob;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil; //Probleme ici

import com.example.tp2_mob.modele.DominosPizza;
import com.example.tp2_mob.modele.DominosPizzaDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DominosPizzaDataSource dataSource;
    private HashMap<String, Double> pizzas;
    private DominosPizza dominosPizzaPlusProche;

import android.os.Bundle;


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


        dataSource = new DominosPizzaDataSource(this);
        dataSource.open();

        //Ajout des pizzerias dans la base de données
        dataSource.deleteAllDominosPiza();
        dataSource.createDominosPizza("3400 1re Av., Québec, QC G1L 3R5",
                "(418) 622-3336");
        dataSource.createDominosPizza(" 3505 Rue Clemenceau, Québec, QC G1C 0L9",
                "(418) 663-3336");
        dataSource.createDominosPizza("680 Bd Wilfrid-Hamel, Québec, QC G1M 2P9",
                "(418) 681-3363");
        dataSource.createDominosPizza("9065 Bd de l'Ormière, Québec, QC G2B 3K2",
                "(418) 843-1444");
        dataSource.createDominosPizza(" 2673 Ch Ste-Foy, Québec, QC G1V 1V3",
                "(418) 650-5555");

        //Ajouter l'Adapteur des pizzas
        pizzas = new HashMap<>();
        pizzas.put("Fromage", 10.0);
        pizzas.put("Peppéroni", 11.0);
        pizzas.put("Bacon", 12.0);
        pizzas.put("Garnie", 13.0);
        pizzas.put("Végé", 14.0);

        String[] sortes = new String[pizzas.size()];
        int i = 0;
        for (String sorte: pizzas.keySet()) {
            sortes[i++] = sorte;
        }
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sortes);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Modifier le champ prix lors de la selection d'une pizza
        Spinner spinner =  findViewById(R.id.spinner);
        TextView prix = findViewById(R.id.champPrix);

        prix.setText("" + pizzas.get(spinner.getSelectedItem().toString()) + "$");
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * Lorsque les champs sont remplies, envoie une Alert indiquant la succursale la plus proche.
     * Sinon, envoie une Alert d'erreur.
     */
    public void showCommande(View view) {
        if(checkEmptyFields()) {
            new AlertDialog.Builder(this)
                    .setTitle("Erreur")
                    .setMessage("Tous les champs doivent être complétés")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            findClosestDominosPizza();

            new AlertDialog.Builder(this)
                    .setTitle("Commande Enregstrer!")
                    .setMessage("Votre commande va être livrée par la succursale " + dominosPizzaPlusProche.getAdresse() +
                            "\nNuméro de téléphone de cette sucursale " + dominosPizzaPlusProche.getNumeroTelephone())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    /**
     * Lorsque les champs sont remplies, lance l'activité Google Map
     * Sinon, envoie une Alert d'erreur.
     */
    public void showMap(View view) {
        if(checkEmptyFields()) {
            new AlertDialog.Builder(this)
                    .setTitle("Erreur")
                    .setMessage("Tous les champs doivent être complétés")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            findClosestDominosPizza();

            //Obtenir l'adresse afin d'envoyer la latitude et longitude à l'intent
            Geocoder localisateur = new Geocoder(this, Locale.CANADA);
            Address adresse = null;
            try {
                adresse= localisateur.getFromLocationName(dominosPizzaPlusProche.getAdresse(), 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("latitude", adresse.getLatitude());
            intent.putExtra("longitude", adresse.getLongitude());
            startActivity(intent);
        }
    }

    /**
     * Trouve la succursale la plus proche de l'adresse écrite dans les champs du formulaire.
     */
    private void findClosestDominosPizza() {
        EditText etAdresse = findViewById(R.id.champAdresse);
        EditText etCodePostal = findViewById(R.id.champCodePostal);
        String adresse = etAdresse.getText().toString() + " " + etCodePostal.getText().toString();

        Geocoder localisateur = new Geocoder(this, Locale.CANADA);

        Address adressePersonelle = null;
        try {
            adressePersonelle= localisateur.getFromLocationName(adresse, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng latLngPersonelle = new LatLng(adressePersonelle.getLatitude(), adressePersonelle.getLongitude());

        //Boucle dans toutes les pizzerias en bd et garde la succursale ayant la moin grande distance
        for (DominosPizza dominosPizza: dataSource.getAllDomniosPizza()) {
            if(dominosPizzaPlusProche == null) {
                dominosPizzaPlusProche = dominosPizza;
            }

            Address plusProche = null;
            Address nouvelle = null;
            try {
                plusProche = localisateur.getFromLocationName(dominosPizzaPlusProche.getAdresse(), 1).get(0);
                nouvelle = localisateur.getFromLocationName(dominosPizza.getAdresse(), 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            LatLng latLngPlusProche = new LatLng(plusProche.getLatitude(), plusProche.getLongitude());
            Double distancePlusProche = SphericalUtil.computeDistanceBetween(latLngPersonelle, latLngPlusProche);

            LatLng latLngNouvelle = new LatLng(nouvelle.getLatitude(), nouvelle.getLongitude());
            Double distanceNouvelle = SphericalUtil.computeDistanceBetween(latLngPersonelle, latLngNouvelle);

            if(distanceNouvelle < distancePlusProche) {
                dominosPizzaPlusProche = dominosPizza;
            }
        }
    }

    /**
     * Vérifie si tous les champs du formulaire sont remplis
     */
    private boolean checkEmptyFields() {
        EditText nom = findViewById(R.id.champNom);
        EditText prenom = findViewById(R.id.champPrenom);
        EditText adresse = findViewById(R.id.champAdresse);
        EditText codePostal = findViewById(R.id.champCodePostal);
        EditText telephone = findViewById(R.id.champTelephone);

        return (nom.getText().toString().equals("") || prenom.getText().toString().equals("")
                || adresse.getText().toString().equals("") || codePostal.getText().toString().equals("")
                || telephone.getText().toString().equals(""));
    }

        imgs = getResources().obtainTypedArray(R.array.images);

        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), images, this);
        gridView = (GridView) findViewById(R.id.simplegridview);
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
        if(hasRolled){
            selectedImage = imageView;
            for (int i = 0; i < gridView.getChildCount(); i++) {
                if(i == imageView) {
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
        if (selectedImage != -1) { //Gestion dexecption : oblige de selectionner au moins une image
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("imageSelection", selectedImage + 1);
            intent.putExtra("imageId", gridView.getAdapter().getItemId(selectedImage));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Veuillez sélectionner une image avant de continuer.", Toast.LENGTH_SHORT).show();
        }
    }



}