package org.rubengic.micompra.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Items implements Comparable {

    private Integer id;
    private String name;
    private Double price;
    private String market;
    private String path_image;
    private Integer id_item;

    public Items(Integer id, String name, Double price, String market, String path_image, Integer id_item){
        this.id = id;
        this.name = name;
        this.price = price;
        this.market = market;
        this.path_image = path_image;
        this.id_item = id_item;
    }

    public Items(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Items(){}

    //gets and sets
    public Integer getId(){ return id; }
    public Integer getIdItem(){ return id_item; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public Double getPrice(){ return price; }
    public void setPrice(double price){ this.price = price;}
    public String getMarket(){ return market; }
    public void setMarket(String market) { this.market = market; }
    public String getPathImage(){ return path_image; }

    //compare objects
    @Override
    public int compareTo(Object o) {

        Items items = (Items) o;

        if(items.getId() == id && items.getName().equals(name) && items.getPrice() == price && items.getMarket().equals(market))
            return 0;
        return 1;
    }
}
