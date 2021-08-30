package com.intellekta.serialize;

import java.io.Serializable;

public class ContactPerson implements Serializable {
    //Имя пользователя
    private String name;
    //Номер телефона
    private String phone;
    //Электронная почта
    private String email;

    //Геттеры и сеттеры для поля name
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    //Геттеры и сеттеры для поля email
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    //Геттеры и сеттеры для phone
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
    //Конструктор класса
    public ContactPerson(String pDataCsv){
        String[] data = pDataCsv.split(",", -1);
        this.email = data[1];
        this.name = data[0];
        this.phone = data[2];
    }

    public ContactPerson(String name, String email, String phone){
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
