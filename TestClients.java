package com.intellekta.serialize;

public class TestClients extends Clients{

    public TestClients(String data) {
            super();
    }

    public boolean equals(Clients client1, Clients client2)
    {
        boolean clients = true, contactPerson = true,requisites=true;
        clients = client1.getName().equals(client2.getName());
            contactPerson = client1.getContactPerson().getEmail().equals(client2.getContactPerson().getEmail())
                    && client1.getContactPerson().getName().equals(client2.getContactPerson().getName())
                    && client1.getContactPerson().getPhone().equals(client2.getContactPerson().getPhone());
        for(int i=0;i<client1.getRequisites().size();i++)
            if(!client1.getRequisites().get(i).getName().equals(client2.getRequisites().get(i).getName())
                    || !client1.getRequisites().get(i).getValue().equals(client2.getRequisites().get(i).getValue()))
                requisites=false;
        return clients&&contactPerson&&requisites;
    }
}
