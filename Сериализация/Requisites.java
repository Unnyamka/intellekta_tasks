package com.intellekta.serialize;

import java.io.Serializable;

public class Requisites implements Serializable {
    //Название реквизита
    private String name;
    //Значение реквизита
    private String value;

    //Геттеры и сеттеры для поля name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //Геттеры и сеттеры для поля value
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    //Конструкторы
    public Requisites(String pDataCsv){
        String[] data = pDataCsv.split(":", -1);
        this.name = data[0];
        this.value = data[1];
    }
    public Requisites(String name, String value){
        this.name = name;
        this.value = value;
    }
}
