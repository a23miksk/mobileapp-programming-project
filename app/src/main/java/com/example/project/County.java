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
    private String size;
    @SerializedName("cost")
    private String population;
    @SerializedName("auxdata")
    private String imgurl;

    public County(String ID, String name, String cityOfResidence, String location, String codeOfCounty, String size, String population, String imgurl) {
        this.ID = ID;
        this.name = name;
        this.cityOfResidence = cityOfResidence;
        this.location = location;
        this.codeOfCounty = codeOfCounty;
        this.size = size;
        this.population = population;
        this.imgurl = imgurl;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCityOfResidence() {
        return cityOfResidence;
    }

    public String getLocation() {
        return location;
    }

    public String getCodeOfCounty() {
        return codeOfCounty;
    }

    public String getSize() {
        return size;
    }

    public String getPopulation() {
        return population;
    }
    public int getSizeInt() {
        return Integer.parseInt(size);
    }

    public int getPopulationInt() {
        return Integer.parseInt(population);
    }

    public String getImgurl() {
        return imgurl;
    }

    @Override
    public String toString() {
        return "{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", cityOfResidence='" + cityOfResidence + '\'' +
                ", location='" + location + '\'' +
                ", codeOfCounty='" + codeOfCounty + '\'' +
                ", size='" + size + '\'' +
                ", population='" + population + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
