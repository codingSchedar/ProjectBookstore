package com.app.projectbookstore.Models;

public class Orders {
    private String orderUserID, orderDate, orderName, orderPhone, orderState, orderTime, orderTotalAmount;

    public Orders() {
    }

    public Orders(String orderUserID, String orderDate, String orderName, String orderPhone, String orderState, String orderTime, String orderTotalAmount) {
        this.orderUserID = orderUserID;
        this.orderDate = orderDate;
        this.orderName = orderName;
        this.orderPhone = orderPhone;
        this.orderState = orderState;
        this.orderTime = orderTime;
        this.orderTotalAmount = orderTotalAmount;
    }

    public String getOrderUserID() {
        return orderUserID;
    }

    public void setOrderUserID(String orderUserID) {
        this.orderUserID = orderUserID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(String orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }
}
