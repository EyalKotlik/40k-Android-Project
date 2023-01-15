package com.example.wh40kapp;

import android.content.Context;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Wargear {
    private int id;
    private String name, profileChoice;
    private ArrayList<WargearProfile> profiles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileChoice() {
        return profileChoice;
    }

    public void setProfileChoice(String profileChoice) {
        this.profileChoice = profileChoice;
    }

    public ArrayList<WargearProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<WargearProfile> profiles) {
        this.profiles = profiles;
    }

    public Wargear(Context context, int id) throws IOException, CsvValidationException {
        this.id = id;
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("Wargear.csv"));
        CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();

        String[] nextLine, wargear= new String[7];
        boolean initializedWargear = false;
        nextLine = reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[0].length()!=0 && Integer.parseInt(nextLine[0]) == this.id ){
                wargear = nextLine;
                reader.close();
                initializedWargear=true;
                if (wargear[3] != ""){
                    if (wargear[3].contains("before selecting targets, select one or both of the profiles below to make attacks with"))
                        this.profileChoice = "inclusive";
                    else
                        this.profileChoice = "exclusive";
                }
                break;
            }
        }
        if (!initializedWargear)
            throw   new RuntimeException("the model named '"+name+"' does not exist");
        this.name = nextLine[1];
        reader.close();

        inputStreamReader = new InputStreamReader(context.getAssets().open("Wargear_list.csv"));
        reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        nextLine = reader.readNext();
        profiles = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null){
            if(Integer.parseInt(nextLine[0])== id){
                profiles.add(new WargearProfile(context,Integer.parseInt(nextLine[0]),Integer.parseInt(nextLine[1])));
            }
        }
        reader.close();
    }
}
