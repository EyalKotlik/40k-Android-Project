package com.example.wh40kapp.data;

import java.util.ArrayList;
import java.util.HashMap;

public class CompressedModel {
    private String name, unitComposition, wargearOptions;
    private int id, line, ws, bs, s, t, w, a, model_num, cost, save;
    private ArrayList<String> keywords;
    private ArrayList<CompressedWargear> wargear;

    public CompressedModel(String name, int cost, int ws, int bs, int s, int t, int w, int a, int save, ArrayList<CompressedWargear> wargear) {
        this.name = name;
        this.cost = cost;
        this.ws = ws;
        this.bs = bs;
        this.s = s;
        this.t = t;
        this.w = w;
        this.a = a;
        this.save = save;
        this.wargear = wargear;
    }
    public Model uncompressModel(){
        ArrayList<Wargear> wargear = new ArrayList<>();
        for (CompressedWargear w : this.wargear){
            wargear.add(w.uncompressWargear());
        }
        return new Model(this.name, this.cost, this.ws, this.bs, this.s, this.t, this.w, this.a, this.save, wargear);
    }
    public static CompressedModel compressedModelFromHash(HashMap values){
        String name = (String)values.get("name");
        int cost = Integer.parseInt(values.get("cost").toString());
        int ws = Integer.parseInt(values.get("ws").toString());
        int bs = Integer.parseInt(values.get("bs").toString());
        int s = Integer.parseInt(values.get("s").toString());
        int t = Integer.parseInt(values.get("t").toString());
        int w = Integer.parseInt(values.get("w").toString());
        int a = Integer.parseInt(values.get("a").toString());
        int save = Integer.parseInt(values.get("save").toString());
        ArrayList<CompressedWargear> wargear = new ArrayList<>();
        for (HashMap gear : (ArrayList<HashMap>)values.get("wargear")){
            wargear.add(CompressedWargear.compressedWargearFromHash(gear));
        }
        return new CompressedModel(name, cost, ws, bs, s, t, w, a, save, wargear);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<CompressedWargear> getWargear() {
        return wargear;
    }

    public void setWargear(ArrayList<CompressedWargear> wargear) {
        this.wargear = wargear;
    }
}
