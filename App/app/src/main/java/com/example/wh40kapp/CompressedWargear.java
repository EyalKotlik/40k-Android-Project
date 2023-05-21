package com.example.wh40kapp;

import java.util.ArrayList;
import java.util.HashMap;

public class CompressedWargear {
    private int id, cost;
    private String name, profileChoice;
    private ArrayList<CompressedProfile> profiles;

    public CompressedWargear(int id, int cost, String name, String profileChoice, ArrayList<CompressedProfile> profiles) {
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.profileChoice = profileChoice;
        this.profiles = profiles;
    }
    public Wargear uncompressWargear(){
        ArrayList<WargearProfile> profiles = new ArrayList<>();
        for (CompressedProfile profile : this.profiles){
            profiles.add(profile.uncompressProfile());
        }
        return new Wargear(id, cost, name, profileChoice, profiles);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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

    public ArrayList<CompressedProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<CompressedProfile> profiles) {
        this.profiles = profiles;
    }

    public static CompressedWargear compressedWargearFromHash(HashMap values){
        int id = Integer.parseInt(values.get("id").toString());
        int cost = Integer.parseInt(values.get("cost").toString());
        String name = (String) values.get("name");
        String profileChoice = (String) values.get("profileChoice");
        ArrayList<CompressedProfile> profiles = new ArrayList<>();
        for (HashMap profile : (ArrayList<HashMap>) values.get("profiles")) {
            profiles.add(CompressedProfile.compressedProfileFromHash((profile)));
        }
        return new CompressedWargear(id, cost, name, profileChoice, profiles);
    }
}
