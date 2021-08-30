package com.intellekta.serialize;

import java.io.Serializable;
import java.util.LinkedList;

public class Clients implements Serializable {
    //Имя
    private String name;
    //Контактное лицо
    private ContactPerson contactPerson;
    //Реквизиты
    private LinkedList<Requisites> requisites;

    //Конструктор класса
    public Clients() {
    }
    //Геттеры и сттеры для поля name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //Геттеры и сетттеры для поля contactPerson
    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }
    //Геттеры и сетттеры для поля requisites
    public LinkedList<Requisites> getRequisites() {
        return requisites;
    }

    public void setRequisites(LinkedList<Requisites> requisites) {
        this.requisites = requisites;
    }
    //Конструктор класса
    public Clients(String pDataCsv){
        String line = pDataCsv.trim();
        String[] params = line.split(";");
        this.name = params[0];
        this.contactPerson = new ContactPerson(params[1]);
        this.requisites = new LinkedList<Requisites>();
        for (String item : params[2].split(",",-1))
        {
            Requisites req = new Requisites(item);
            this.requisites.add(req);
        }
    }
}
