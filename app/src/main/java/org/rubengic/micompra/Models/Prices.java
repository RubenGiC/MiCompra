package org.rubengic.micompra.Models;

public class Prices implements Comparable {

    private Integer id;
    private Integer id_item;
    private Double price;
    private String market;

    public Prices(Integer id, Integer id_item, Double price, String market){
        this.id = id;
        this.id_item = id_item;
        this.price = price;
        this.market = market;
    }

    public Prices(){}

    //gets and sets
    public Integer getId(){ return id; }
    public Integer getIdItem(){ return id_item; }
    public Double getPrice(){ return price; }
    public void setPrice(double price){ this.price = price;}
    public String getMarket(){ return market; }
    public void setMarket(String market) { this.market = market; }

    //compare objects
    @Override
    public int compareTo(Object o) {

        Prices price = (Prices) o;

        if(price.getId() == id && price.getIdItem()== id_item && price.getPrice() == this.price && price.getMarket().equals(market))
            return 0;
        return 1;
    }
}
