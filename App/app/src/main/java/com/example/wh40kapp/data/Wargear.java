package com.example.wh40kapp.data;

import android.content.Context;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class Wargear implements Serializable {
    private int id, cost;
    private String name, profileChoice;
    private ArrayList<WargearProfile> profiles;

    public Wargear(int id, int cost, String name, String profileChoice, ArrayList<WargearProfile> profiles) {
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.profileChoice = profileChoice;
        this.profiles = profiles;
    }

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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Wargear(Context context, int id, int cost) throws IOException, CsvValidationException {
        this.id = id;
        this.cost = cost;
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
                    else if (wargear[3].contains("before selecting targets, select one of the profiles below to make attacks with"))
                        this.profileChoice = "exclusive";
                    else
                        this.profileChoice = "none";
                }
                break;
            }
        }
        if (!initializedWargear)
            throw   new RuntimeException("the wargear named '"+name+"' does not exist");
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

    public static String[] canCreateWargear(Context context, String name) throws IOException, CsvValidationException {
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("Wargear_list.csv"));
        CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        String[] nextLine, wargear = new String[14];
        boolean initializedModel = false;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[2].equals(name)) {
                wargear = nextLine;
                reader.close();
                initializedModel = true;
                break;
            }
        }
        if (!initializedModel) {
            reader.close();
            inputStreamReader.close();
            return null;
        }
        return wargear;
    }
}
