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
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

public class Model implements Serializable {
    private String name, unitComposition, wargearOptions;
    private int id, line, ws, bs, s, t, w, a, model_num, cost;
    private Dictionary<String, int[]> saves;
    private ArrayList<String> keywords;
    private ArrayList<Wargear> wargear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getWs() {
        return ws;
    }

    public void setWs(int ws) {
        this.ws = ws;
    }

    public int getBs() {
        return bs;
    }

    public void setBs(int bs) {
        this.bs = bs;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getModel_num() {
        return model_num;
    }

    public void setModel_num(int model_num) {
        this.model_num = model_num;
    }

    public Dictionary<String, int[]> getSaves() {
        return saves;
    }

    public void setSaves(Dictionary<String, int[]> saves) {
        this.saves = saves;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<Wargear> getWargear() {
        return wargear;
    }

    public void setWargear(ArrayList<Wargear> wargear) {
        this.wargear = wargear;
    }

    public String getUnitComposition() {
        return unitComposition;
    }

    public void setUnitComposition(String unitComposition) {
        this.unitComposition = unitComposition;
    }

    public String getWargearOptions() {
        return wargearOptions;
    }

    public void setWargearOptions(String wargearOptions) {
        this.wargearOptions = wargearOptions;
    }

    public Model(Context context, String[] model) throws IOException, CsvValidationException {
        this.model_num = 1;
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("Datasheets_models.csv"));
        CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        String[] nextLine;

        this.id = Integer.parseInt(model[0]);
        this.line = Integer.parseInt(model[1]);
        this.name = model[2];
        this.ws = Integer.parseInt(model[3]);
        this.bs = Integer.parseInt(model[4]);
        this.s = Integer.parseInt(model[5]);
        this.t = Integer.parseInt(model[6]);
        this.w = Integer.parseInt(model[7]);
        this.a = Integer.parseInt(model[8]);
        this.cost = (int) Double.parseDouble(model[10]);

        String sv = model[9];
        this.saves = new Hashtable<String, int[]>();
        if (sv.indexOf('/') == -1)
            this.saves.put("armour", new int[]{Integer.parseInt(sv), Integer.parseInt(sv)});
        else
            this.saves.put("daemonic", new int[]{Integer.parseInt(String.valueOf(sv.charAt(0))), Integer.parseInt(String.valueOf(sv.charAt(2)))});

        inputStreamReader = new InputStreamReader(context.getAssets().open("Datasheets_keywords.csv"));
        reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        this.keywords = new ArrayList<>();
        nextLine = reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            if ((Integer.parseInt(nextLine[0]) == this.id) && Objects.equals(nextLine[2], this.name))
                keywords.add(nextLine[1]);
            else if (Integer.parseInt(nextLine[0]) > this.id)
                break;
        }
        reader.close();

        inputStreamReader = new InputStreamReader(context.getAssets().open("Datasheets_wargear.csv"));
        reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        this.wargear = new ArrayList<Wargear>();
        nextLine = reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            if (Integer.parseInt(nextLine[0]) == this.id)
                this.wargear.add(new Wargear(context, Integer.parseInt(nextLine[2]), Integer.parseInt(nextLine[3])));
        }
        reader.close();

        inputStreamReader = new InputStreamReader(context.getAssets().open("Datasheets.csv"));
        reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        nextLine = reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            if (Integer.parseInt(nextLine[0]) == this.id)
                this.unitComposition = nextLine[5];
        }
        reader.close();

        inputStreamReader = new InputStreamReader(context.getAssets().open("Datasheets_options.csv"));
        reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        nextLine = reader.readNext();
        wargearOptions = "";
        while ((nextLine = reader.readNext()) != null) {
            if (Integer.parseInt(nextLine[0]) == this.id)
                this.wargearOptions = this.wargearOptions + nextLine[2].toString() + nextLine[3].toString() + "\n";
        }
        reader.close();
        inputStreamReader.close();
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public static String[] canCreateModel(Context context, String name) throws IOException, CsvValidationException {
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("Datasheets_models.csv"));
        CSVReader reader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
        String[] nextLine, model = new String[14];
        boolean initializedModel = false;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[2].equals(name)) {
                model = nextLine;
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
        return model;
    }
}
