package com.example.toursapp2.Model;

public class HorizontalProductScrollModel {

    private String catprodimage;
    private String catprodname;
    private long catID;

    public long getCatID() {
        return catID;
    }

    public HorizontalProductScrollModel(String catprodimage, String catprodname,long catID) {
        this.catprodimage = catprodimage;
        this.catprodname = catprodname;
        this.catID=catID;
    }


    public String getCatprodimage() {
        return catprodimage;
    }

    public String getCatprodname() {
        return catprodname;
    }

    public void setCatprodimage(String catprodimage) {
        this.catprodimage = catprodimage;
    }

    public void setCatprodname(String catprodname) {
        this.catprodname = catprodname;
    }
}
