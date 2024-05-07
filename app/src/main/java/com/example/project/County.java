package com.example.project;

import com.google.gson.annotations.SerializedName;

public class County {
    private String ID;
    private String name;
    @SerializedName("company")
    private String cityOfResidence;
    private String location;
    @SerializedName("category")
    private String codeOfCounty;
    private int size;
    @SerializedName("cost")
    private int population;
    @SerializedName("auxdata")
    private String imgurl;

    public County(String ID, String name, String cityOfResidence, String location, String codeOfCounty, int size, int population, String imgurl) {
        this.ID = ID;
        this.name = name;
        this.cityOfResidence = cityOfResidence;
        this.location = location;
        this.codeOfCounty = codeOfCounty;
        this.size = size;
        this.population = population;
        this.imgurl = imgurl;
    }
}
