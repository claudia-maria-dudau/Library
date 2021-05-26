package library.people;

import java.util.Date;

public abstract class Person {
    static int noPersons;
    protected int id;
    protected String name;
    protected Date birthDate;
    protected String mail;

    Person(String name, Date birthDate, String mail) {
        this.id = ++noPersons;
        this.name = name;
        this.birthDate = birthDate;
        this.mail = mail;
    }

    Person(int id, String name, Date birthDate, String mail) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getMail() {
        return mail;
    }

    public static void setNoPersons(int noPersons) {
        Person.noPersons = noPersons;
    }
}
