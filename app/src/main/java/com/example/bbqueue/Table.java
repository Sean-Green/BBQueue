package com.example.bbqueue;

public class Table {
    private String tableID;
    private int size;
    private boolean open;
    private Customer customer;

    public Table() {
        open = true;
        size = 2;
        tableID = "table";
        customer = null;
    }

    public Table(String tableID, int size){
        this.tableID = tableID;
        this.size = size;
        this.open = true;
    }

    public boolean seat(Customer cust) {
        if (open) {
            customer = cust;
            open = false;
            return true;
        } else {
            return false;
        }
    }

    public void open(){
        this.open = true;
        customer = null;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getTableID(){
        return tableID;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getSize() {
        return size;
    }

     public void setSize(int size){
        this.size = size;
     }
}
