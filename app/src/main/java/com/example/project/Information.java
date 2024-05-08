package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_information);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String text = extras.getString("carriedCounty");
            County county = new Gson().fromJson(text, County.class);

            getSupportActionBar().setTitle(county.getName());


            TextView textViewName = findViewById(R.id.countyName);
            textViewName.setText(county.getName());
            ImageView ImageViewBanner = findViewById(R.id.countyBanner);
            Picasso.get().load(county.getImgurl()).into(ImageViewBanner);

            TextView textViewCode = findViewById(R.id.countyCode);
            textViewCode.setText("Länskod: " + county.getCodeOfCounty());

            TextView textViewPop = findViewById(R.id.countyPop);
            textViewPop.setText("Folkmängd: " + county.getPopulation());

            TextView textViewArea = findViewById(R.id.countyArea);
            textViewArea.setText("Area: " + county.getSize() + "km²");

            TextView textViewCity = findViewById(R.id.countyCity);
            textViewCity.setText("Residensstad: " + county.getCityOfResidence());

            TextView textViewCoord = findViewById(R.id.countyCoord);
            textViewCoord.setText("Läge: " + county.getLocation());

        }

    }
}