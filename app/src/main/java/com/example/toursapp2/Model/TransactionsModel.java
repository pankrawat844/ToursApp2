package com.example.toursapp2.Model;

public class TransactionsModel {
    String date;
    String prodname;
    String amount;

    public TransactionsModel(String date, String prodname, String amount) {
        this.date = date;
        this.prodname = prodname;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getProdname() {
        return prodname;
    }

    public String getAmount() {
        return amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
