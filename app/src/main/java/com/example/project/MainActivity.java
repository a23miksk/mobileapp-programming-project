package com.example.project;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements JsonTask.JsonTaskListener, AdapterView.OnItemSelectedListener{
    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=a23miksk";
    ArrayList<County> items = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    String[] sorts = { "Län", "Folkmängd",
            "Area"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        sortSpinner.setOnItemSelectedListener(this);

        ArrayAdapter sortAdapter
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                sorts);
        sortAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        Button activityButton = findViewById(R.id.aboutButton);
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, About.class);

                startActivity(intent);
            }
        });


        adapter = new RecyclerViewAdapter(this, items, new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(County item) {
                Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView view = findViewById(R.id.recycler_view);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);


        new JsonTask(this).execute(JSON_URL);

    }

    @Override
    public void onPostExecute(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<County>>() {}.getType();
        ArrayList<County> listOfCounties = gson.fromJson(json, type);
        items.addAll(listOfCounties);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (sorts[i]) {
            case "Län":
                Collections.sort(items, new Comparator<County>() {
                    public int compare(County county1, County county2) {
                        return county1.getName().compareTo(county2.getName());
                    }
                });
                break;
            case "Folkmängd":
                Collections.sort(items, new Comparator<County>() {
                    public int compare(County county1, County county2) {
                        return Integer.compare(county1.getPopulationInt(), county2.getPopulationInt());
                    }
                });
                Collections.reverse(items);
                break;
            case "Area":
                Collections.sort(items, new Comparator<County>() {
                    public int compare(County county1, County county2) {
                        return Integer.compare(county1.getSizeInt(), county2.getSizeInt());
                    }
                });
                Collections.reverse(items);
                break;
        }
        Log.d("==>", "sorted: " + items);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
