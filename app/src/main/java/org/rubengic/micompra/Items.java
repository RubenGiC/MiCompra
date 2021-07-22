package org.rubengic.micompra;

import java.io.Serializable;

public class Items implements Serializable {

    private Integer id;
    private String name;
    private Double price;
    private String market;

    public Items(Integer id, String name, Double price, String market){
        this.id = id;
        this.name = name;
        this.price = price;
        this.market = market;
    }

    public Items(){}

    //gets and sets
    public Integer getId(){ return id; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public Double getPrice(){ return price; }
    public void setPrice(double price){ this.price = price;}
    public String getMarket(){ return market; }
    public void setMarket(String market) { this.market = market; }



}
