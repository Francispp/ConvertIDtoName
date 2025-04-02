package org.example.convertidtoname.model;


public class Trade {
    private String date;
    private String productName;
    private String currency;
    private String price;

    public Trade(String date, String productName, String currency, String price) {
        this.date = date;
        this.productName = productName;
        this.currency = currency;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public String getProductName() {
        return productName;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        return price;
    }
}

