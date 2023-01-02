package com.example.wh40kapp;

import android.content.Context;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Model {
    private String name;
    private int id, line, ws, bs, s, t, w, a;
    private Dictionary<String, int[]> saves;
    private ArrayList<String> keywords;
    private ArrayList<Wargear> wargear;

    public Model(Context context, String name) throws IOException, CsvValidationException {
        this.name = name;
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(context.getApplicationInfo().dataDir + File.separatorChar + "Datasheets_models.csv")).withCSVParser(parser).build();
        String[] nextLine, model= new String[14];
        boolean initializedModel = false;

        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[2] == name){
                model = nextLine;
                reader.close();
                initializedModel=true;
                break;
            }
        }
        if (!initializedModel)
            throw   new RuntimeException("the model named '"+name+"' does not exist");

        this.id = Integer.parseInt(model[0]);
        this.line = Integer.parseInt(model[1]);
        this.ws = Integer.parseInt(model[2]);
        this.bs = Integer.parseInt(model[3]);
        this.s = Integer.parseInt(model[4]);
        this.t = Integer.parseInt(model[5]);
        this.w = Integer.parseInt(model[6]);
        this.a = Integer.parseInt(model[7]);

        String sv = model[8];
        this.saves = new Hashtable<String, int[]>();
        if (sv.indexOf('/') == -1)
            this.saves.put("armour", new int[]{Integer.parseInt(sv), Integer.parseInt(sv)});
        else
            this.saves.put("armour", new int[]{Integer.parseInt(String.valueOf(sv.charAt(0))), Integer.parseInt(String.valueOf(sv.charAt(2)))});

        reader = new CSVReaderBuilder(new FileReader(context.getApplicationInfo().dataDir + File.separatorChar + "Datasheets_keywords.csv")).withCSVParser(parser).build();
        this.keywords = new ArrayList<>();
        while((nextLine = reader.readNext()) != null){
            if ((Integer.parseInt(nextLine[0]) == this.id) && nextLine[2] == this.name)
                keywords.add(nextLine[1]);
            else if (Integer.parseInt(nextLine[0]) > this.id)
                break;
        }

        reader = new CSVReaderBuilder(new FileReader(context.getApplicationInfo().dataDir + File.separatorChar + "Datasheets_wargear.csv")).withCSVParser(parser).build();
        this.wargear = new ArrayList<Wargear>();
        while((nextLine = reader.readNext()) != null){
            if (Integer.parseInt(nextLine[0])==this.id)
                this.wargear.add(new Wargear(context, Integer.parseInt(nextLine[2])));
        }
    }
}
