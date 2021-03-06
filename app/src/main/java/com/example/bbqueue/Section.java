package com.example.bbqueue;

import java.util.ArrayList;

public class Section {

    private String id;
    private ArrayList<Table> tables;

    public Section(){
        id = "Section";
        tables = new ArrayList<Table>();
        tables.add(new Table());
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

    /**
     * Removes table at given index, returns false if only 1 table left
     * 0 tables breaks the database interaction.
     * @param i
     * @return
     */
    public boolean removeTableAtIndex(int i) {
        if (tables.size() > 1){
            tables.remove(i);
            return true;
        }
        return false;
    }

    public String getOpenTableString(){

        String max = "/" + tables.size();
        int open = 0;
        for (int i = 0; i < tables.size(); ++i){
            if (tables.get(i).isOpen()){ ++open;}
        }
        return open + max + " Tables Open";
    }
}
