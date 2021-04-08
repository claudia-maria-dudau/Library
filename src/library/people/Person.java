package library.people;

import java.util.Date;

public abstract class Person {
    protected String name;
    protected Date birthDate;
    protected String mail;

    Person(String name, Date birthDate, String mail) {
        this.name = name;
        this.birthDate = birthDate;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }
}
