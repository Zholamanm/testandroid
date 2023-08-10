package com.example.starwarsapp1.adapters;

public class DbModel {
    private long id;
    private String name;
    private String description;
    private String additionalInfo;
    private String isFav;

    public DbModel(long id, String name, String description, String additionalInfo, String isFav) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.additionalInfo = additionalInfo;
        this.isFav = isFav;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getIsFav() {
        return isFav;
    }
}

