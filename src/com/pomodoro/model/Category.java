package com.pomodoro.model;

import java.util.UUID;

public class Category {
    private String id;
    private String name;
    private String colorCode;

    public Category(String name, String colorCode) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.colorCode = colorCode;
    }

    // ดึงข้อมูลหมวดหมู่เก่าจากฐานข้อมูลมาสร้างใหม่
    public Category(String id, String name, String colorCode) {
        this.id = id;
        this.name = name;
        this.colorCode = colorCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return name;
    }
}