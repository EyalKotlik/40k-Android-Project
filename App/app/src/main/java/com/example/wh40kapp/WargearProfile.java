package com.example.wh40kapp;

import android.content.Context;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Objects;

public class WargearProfile implements Serializable {
    private int id, line, range, ap, attacks_chosen;
    private String name, s, d;
    private String[] type;

    public WargearProfile(int id, int line, int range, int ap, int attacks_chosen, String name, String s, String d, String[] type){
        this.id = id;
        this.line = line;
        this.range = range;
        this.ap = ap;
        this.attacks_chosen = attacks_chosen;
        this.name = name;
        this.s = s;
        this.d = d;
        this.type = type;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public int getAttacks_chosen() {
        return attacks_chosen;
    }

    public void setAttacks_chosen(int attacks_chosen) {
        this.attacks_chosen = attacks_chosen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public WargearProfile(Context context, int id, int line) throws IOException, CsvValidationException {
        this.id = id;
        this.line = line;
        this.attacks_chosen = 0; //if it is a ranged weapon any value above a 0 will be counted as maximum attacks.
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("Wargear_list.csv"));
        CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        String[] nextLine, profile = new String[9];
        nextLine = reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            if (Integer.parseInt(nextLine[0]) == id && Integer.parseInt(nextLine[1]) == line){
                profile = nextLine;
                break;
            }
        }
        if (profile == new String[9])
            throw new RuntimeException("Profile does not exist");
        reader.close();

        this.name = profile[2];
        if (!profile[3].equals("melee"))
            this.range = Integer.parseInt(profile[3]);
        else
            this.range = -1;
        this.type = profile[4].split(" ");
        if (Objects.equals(this.type[0], "rapid"))
            this.type = new String[]{"rapid fire", this.type[2]};
        if (this.type.length == 1)
            this.type = new String[]{this.type[0], ""};
        this.s = profile[5];
        this.ap = Integer.parseInt(profile[6]);
        this.d = profile[7];
    }
}
