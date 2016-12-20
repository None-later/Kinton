package com.kinton.test.azure.com.kinton.test.azure.model;

import com.microsoft.azure.storage.table.TableServiceEntity;

import java.util.Date;

/**
 * Created by mgkj1 on 11/24/2016.
 */
public class TableStoragePOCModel extends TableStoragePOCModelBase{
    String orderID;
    float price;
    Date orderDate;
    String comment;
    String customerID;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    String storeCode;

    public TableStoragePOCModel(String orderID, float price, Date orderDate, String comment, String customerID,String storeCode) {
        this.orderID = orderID;
        this.price = price;
        this.orderDate = orderDate;
        this.comment = comment;
        this.customerID = customerID;
        this.storeCode = storeCode;
        this.partitionKey = storeCode;
        this.rowKey = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @Override
    public String toString() {
        return "TableStoragePOCModel{" +
                "orderID='" + orderID + '\'' +
                ", price=" + price +
                ", orderDate=" + orderDate +
                ", comment='" + comment + '\'' +
                ", customerID='" + customerID + '\'' +
                '}';
    }
}
