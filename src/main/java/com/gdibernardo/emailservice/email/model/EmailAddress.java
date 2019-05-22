package com.gdibernardo.emailservice.email.model;

public class EmailAddress {

    private String address;
    private String name;

    EmailAddress() {}

    public EmailAddress(String address) {
        this.address = address;
    }

    public EmailAddress(String address, String name) {
        this(address);

        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return (address == null) ? new String() : address;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return (name == null) ? new String() : name;
    }

    public Boolean hasName() {
        return (name != null && !name.trim().isEmpty());
    }
}