package com.forkexec.hub.domain;

import java.util.Objects;

public class CartItem {
    private String restaurantId;
    private String menuId;
    private int price;
    private int quantity;

    public CartItem(String restaurantId, String menuId, int price, int quantity) {
        this.restaurantId = restaurantId;
        this.menuId = menuId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public int getPrice() {
        return price * getQuantity();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return getPrice() == cartItem.getPrice() &&
            getQuantity() == cartItem.getQuantity() &&
            getRestaurantId().equals(cartItem.getRestaurantId()) &&
            getMenuId().equals(cartItem.getMenuId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRestaurantId(), getMenuId(), getPrice(), getQuantity());
    }
}
