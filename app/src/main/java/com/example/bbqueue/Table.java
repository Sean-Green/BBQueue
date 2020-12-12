package com.example.bbqueue;

public class Table {
    private String tableID;
    private int size;
    private boolean open;
    private String customerID;

    public Table() {
        open = true;
        size = 2;
        tableID = "table";
        customerID = null;
    }

    public Table(String tableID, int size){
        this.tableID = tableID;
        this.size = size;
        this.open = true;
    }

    public boolean seat(String cust) {
        if (open) {
            customerID = cust;
            open = false;
            return true;
        } else {
            return false;
        }
    }

    public void open(){
        this.open = true;
        customerID = null;
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

    public void setCustomer(String customer) {
        this.customerID = customer;
    }

    public String getCustomer() {
        return customerID;
    }

    public int getSize() {
        return size;
    }

    public String getSizeString() { return size + " Seats";}
     public void setSize(int size){
        this.size = size;
     }
}
