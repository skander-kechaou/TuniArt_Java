package tn.esprit.entities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Comparator;

public class Order {
    private int orderId;
    private int userId;
    private List<Art_Piece> artList;
    private float totalPrice;
    private LocalDate orderDate;
    private int status;
    public static Comparator<Order> uidComparator = Comparator.comparingInt(Order::getUserId);
    public static Comparator<Order> dateComparator = Comparator.comparing(Order::getOrderDate);

    public Order() {
        this.artList = new ArrayList<>();
    }

    public Order( int userId, List<Art_Piece> artList, float totalPrice, LocalDate orderDate, int status) {
        this.userId = userId;
        this.artList = artList;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Order(int userId, float totalPrice, LocalDate orderDate, int status) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Art_Piece> getArtList() {
        return artList;
    }

    public void setArtList(List<Art_Piece> artList) {
        this.artList = artList;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




    public Order(int orderId, int userId, List<Art_Piece> artList, float totalPrice, LocalDate orderDate, int status) {
        this.orderId = orderId;
        this.userId = userId;
        this.artList = artList;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Order(int orderId, int userId, float totalPrice, LocalDate orderDate, int status) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
        this.artList = new ArrayList<>(); // Initialize the artList
    }
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", artList=" + artList +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
   }
