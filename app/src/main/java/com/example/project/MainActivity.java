package com.example.project;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    ArrayList<County> filteredItems = new ArrayList<>();
    private int filterMinInt = 0;
    private int filterMaxInt = 1767016;
    private RecyclerViewAdapter adapter;

    String[] sorts = { "Län", "Folkmängd",
            "Area"};
    private SharedPreferences myPreferenceRef;
    private SharedPreferences.Editor myPreferenceEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText filterMin = findViewById(R.id.filterMin);
        final EditText filterMax = findViewById(R.id.filterMax);
        myPreferenceRef = getSharedPreferences("MyPreferencesName", MODE_PRIVATE);
        myPreferenceEditor = myPreferenceRef.edit();

        filterMin.setText(myPreferenceRef.getString("MyAppPreferenceFilterMin", "0"));
        filterMax.setText(myPreferenceRef.getString("MyAppPreferenceFilterMax", "1767016"));

        filterMin.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.isEmpty() && text.startsWith("0") && text.length() > 1) {
                    text = text.substring(1);
                    filterMin.setText(text);
                    filterMin.setSelection(text.length());
                } else if (text.isEmpty()) {
                    text = "0";
                }

                int minValue = Integer.parseInt(text);
                int maxValue = Integer.parseInt(myPreferenceRef.getString("MyAppPreferenceFilterMax", "1767016"));
                if (minValue > maxValue) {
                    text = String.valueOf(maxValue);
                    filterMin.setText(text);
                    filterMin.setSelection(text.length());
                }

                myPreferenceEditor.putString("MyAppPreferenceFilterMin", text);
                myPreferenceEditor.apply();
                filterItems();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        filterMax.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.isEmpty() && text.startsWith("0") && text.length() > 1) {
                    text = text.substring(1);
                    filterMax.setText(text);
                    filterMax.setSelection(text.length());
                } else if (text.isEmpty()) {
                    text = "0";
                }

                int maxValue = Integer.parseInt(text);
                int minValue = Integer.parseInt(myPreferenceRef.getString("MyAppPreferenceFilterMin", "0"));
                if (maxValue < minValue) {
                    text = String.valueOf(minValue);
                    filterMax.setText(text);
                    filterMax.setSelection(text.length());
                }

                myPreferenceEditor.putString("MyAppPreferenceFilterMax", text);
                myPreferenceEditor.apply();
                filterItems();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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


        adapter = new RecyclerViewAdapter(this, filteredItems, new RecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(County item) {
                Intent intent = new Intent(MainActivity.this, Information.class);

                intent.putExtra("carriedCounty", new Gson().toJson(item)); // Optional

                startActivity(intent);
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
        filterItems();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (sorts[i]) {
            case "Län":
                Collections.sort(filteredItems, new Comparator<County>() {
                    public int compare(County county1, County county2) {
                        return county1.getName().compareTo(county2.getName());
                    }
                });
                break;
            case "Folkmängd":
                Collections.sort(filteredItems, new Comparator<County>() {
                    public int compare(County county1, County county2) {
                        return Integer.compare(county1.getPopulationInt(), county2.getPopulationInt());
                    }
                });
                Collections.reverse(filteredItems);
                break;
            case "Area":
                Collections.sort(filteredItems, new Comparator<County>() {
                    public int compare(County county1, County county2) {
                        return Integer.compare(county1.getSizeInt(), county2.getSizeInt());
                    }
                });
                Collections.reverse(filteredItems);
                break;
        }
        Log.d("==>", "sorted: " + filteredItems);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void filterItems(){
        int minPop = Integer.parseInt(myPreferenceRef.getString("MyAppPreferenceFilterMin", "0"));
        int maxPop = Integer.parseInt(myPreferenceRef.getString("MyAppPreferenceFilterMax", "1767016"));

        filteredItems.clear();
        for (County county : items) {
            if (minPop <= county.getPopulationInt() && maxPop >= county.getPopulationInt()) {
                filteredItems.add(county);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
