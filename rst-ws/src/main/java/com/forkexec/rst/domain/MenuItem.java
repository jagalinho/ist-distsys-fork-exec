package com.forkexec.rst.domain;

import com.forkexec.rst.domain.exceptions.InsufficientQuantityException;

import java.util.Objects;

public class MenuItem {
    private String id;
    private String entree;
    private String plate;
    private String dessert;
    private int price;
    private int preparationTime;
    private volatile int stock;

    public MenuItem(String id, String entree, String plate, String dessert, int price, int preparationTime, int stock) {
        this.id = id;
        this.entree = entree;
        this.plate = plate;
        this.dessert = dessert;
        this.price = price;
        this.preparationTime = preparationTime;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getEntree() {
        return entree;
    }

    public String getPlate() {
        return plate;
    }

    public String getDessert() {
        return dessert;
    }

    public int getPrice() {
        return price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public synchronized int getStock() {
        return stock;
    }

    public synchronized void subtractStock(int value) throws InsufficientQuantityException {
        if (getStock() - value < 0)
            throw new InsufficientQuantityException();

        stock -= value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return getId().equals(menuItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
