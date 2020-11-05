package com.example.bbqueue;

import java.util.ArrayList;

public class Section {

    private String id;
    private ArrayList<Table> tables;

    public Section(){
        id = "Section";
        tables = new ArrayList<Table>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public void addTable(Table table){
        tables.add(table);
    }
}
