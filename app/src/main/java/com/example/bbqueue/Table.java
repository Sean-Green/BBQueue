package com.example.bbqueue;

public class Table {
    private String tableID;
    private boolean open;
    private Customer customer;

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
}
