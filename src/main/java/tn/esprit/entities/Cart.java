package tn.esprit.entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int cartId;
    private int userId;
    private int artRef; // Added field
    private List<Integer> artRefs; // Changed to List of Integer
    private List<Art_Piece> artList;

    public Cart() {
        this.artRefs = new ArrayList<>(); // Initialize artRefs

        this.artList = new ArrayList<>();
    }

    public Cart(int cartId, int userId, int artRef) { // Modified constructor
        this.cartId = cartId;
        this.userId = userId;
        this.artRef = artRef;
        this.artRefs = new ArrayList<>(); // Initialize artRefs
        this.artList = new ArrayList<>();
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArtRef() { // Added getter
        return artRef;
    }

    public void setArtRef(int artRef) { // Added setter
        this.artRef = artRef;
    }

    public List<Art_Piece> getArtList() {
        return artList;
    }

    public void addArt(Art_Piece art) {
        artList.add(art);
    }

    public void removeArt(Art_Piece art) {
        artList.remove(art);
    }

    public void clearCart() {
        artList.clear();
    }

    public void setArtList(List<Art_Piece> artList) {
        this.artList = artList;
    }

    public float getTotalPrice() {
        float totalPrice = 0;
        for (Art_Piece art : artList) {
            totalPrice += art.getArt_price();
        }
        return totalPrice;
    }
}
