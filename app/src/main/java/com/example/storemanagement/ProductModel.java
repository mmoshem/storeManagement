package com.example.storemanagement;

public class ProductModel {
    private int m_image;
    private String m_name;
    private String m_price;
    private int m_countItem;
    private int m_id;

    public ProductModel(){}
    public int getM_image() {
        return m_image;
    }

    public void setM_image(int m_image) {
        this.m_image = m_image;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_price() {
        return m_price;
    }

    public void setM_price(String m_price) {
        this.m_price = m_price;
    }

    public int getM_countItem() {
        return m_countItem;
    }

    public void setM_countItem(int m_countItem) {
        this.m_countItem = m_countItem;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public ProductModel(int m_image, String m_name, String m_price, int m_countItem, int m_id) {
        this.m_image = m_image;
        this.m_name = m_name;
        this.m_price = m_price;
        this.m_countItem = m_countItem;
        this.m_id = m_id;
    }
}
