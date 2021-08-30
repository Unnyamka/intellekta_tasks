package com.intellekta.serialize;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ImportClients {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    }
    //Метод для сериализации клиентов
    public static void clientsToCards(LinkedList<Clients> clientsLinkedList) throws IOException {
        Integer i = 0;
        for(Clients client: clientsLinkedList){
            //Сериализация
            ObjectOutputStream clientStream = new ObjectOutputStream(new FileOutputStream("client" + i.toString() + ".out"));
            clientStream.writeObject(client);
            i = i + 1;
        }
        
    }
    //Метод для десериализации клиентов
    public static LinkedList<Clients> cardsToClients(String path) throws IOException, ClassNotFoundException {
        LinkedList<Clients>clients = new LinkedList<>();
        for(int i =0;i<4;i++) {
            try {
                //Десериализация
                ObjectInputStream clientInputStream = new ObjectInputStream(new FileInputStream(path + "\\client" + i + ".out"));
                Clients client = (Clients) clientInputStream.readObject();
                if (client != null)
                    clients.add(client);
                else
                    return new LinkedList<Clients>();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return new LinkedList<Clients>();
            }
        }
        return clients;
    }
    //Метод для создания клиентов
    public static LinkedList<Clients> createClientsObjectCsv() throws FileNotFoundException {
        LinkedList<Clients> clientList = new LinkedList<Clients>();
        //Открываем файл со строками
        File clientsFile = new File("Clients.txt");
        Scanner sc = new Scanner(clientsFile);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            //Создаем нового клиента
            Clients client = new Clients(line);
            clientList.add(client);
        }
        return clientList;
    }
    //Метод для вывода клиента с самым коротким названием
    public static void checkImport(List<Clients> clients)
    {
        //Проверка на пустой список
        if(clients.isEmpty())
            System.out.println("import failed");
        else
        {
            int min_size=clients.get(0).getName().length(), min_index=0;
            for(int i=1;i<clients.size();i++)
            {
                //Поиск id клиента с самым коротким названием
                if (clients.get(i).getName().length()<min_size)
                    min_index = i;
            }
            //Вывод на консоль
            System.out.print(clients.get(min_index).getName()+","+clients.get(min_index).getContactPerson().getName()+","
                    +clients.get(min_index).getRequisites().get(0).getValue()+","
                    +clients.get(min_index).getRequisites().get(1).getValue());
        }
    }
    //Метод для сериализации
    public static void Serial(String path) throws IOException, ClassNotFoundException {
        LinkedList<Clients> clients = createClientsObjectCsv();
        try{
            ObjectOutputStream oos;
            for(int i=0;i<4;i++) {
                oos = new ObjectOutputStream(new FileOutputStream("client"+i+".out"));
                oos.writeObject(clients.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Counter{
    private int c = 0;

    public synchronized void increment() { c++; }

    public synchronized void decrement() { c--; }

    public synchronized int value()  { return c; }
}

class AtomicCounter {

    private AtomicInteger c = new AtomicInteger(0);

    public void increment() { c.incrementAndGet(); }

    public void decrement() { c.decrementAndGet(); }

    public int value() {return c.get();}



}


