package com.example.wh40kapp.data;

import java.util.ArrayList;
import java.util.HashMap;

public class CompressedProfile {
    private int id, line, range, ap, attacks_chosen;
    private String name, s, d;
    private ArrayList<String> type;

    public CompressedProfile(int id, int line, int range, int ap, int attacks_chosen, String name, String s, String d, String[] type) {
        this.id = id;
        this.line = line;
        this.range = range;
        this.ap = ap;
        this.attacks_chosen = attacks_chosen;
        this.name = name;
        this.s = s;
        this.d = d;
        this.type = new ArrayList<String>();
        for (String t : type) {
            this.type.add(t);
        }
    }

    public WargearProfile uncompressProfile() {
        String[] type = new String[this.type.size()];
        for (int i = 0; i < this.type.size(); i++) {
            type[i] = this.type.get(i);
        }
        return new WargearProfile(id, line, range, ap, attacks_chosen, name, s, d, type);
    }

    public static CompressedProfile compressedProfileFromHash(HashMap values) {
        int id = Integer.parseInt(values.get("id").toString());
        int line = Integer.parseInt(values.get("line").toString());
        int range = Integer.parseInt(values.get("range").toString());
        int ap = Integer.parseInt(values.get("ap").toString());
        int attacks_chosen = Integer.parseInt(values.get("attacks_chosen").toString());
        String name = (String) values.get("name");
        String s = (String) values.get("s");
        String d = (String) values.get("d");
        String[] type = type = new String[]{
            ((ArrayList) values.get("type")).get(0).toString(), ((ArrayList) values.get("type")).get(1).toString()};
        return new CompressedProfile(id, line, range, ap, attacks_chosen, name, s, d, type);
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

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }
}
