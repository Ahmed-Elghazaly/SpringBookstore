package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phoneNumber;

    @OneToMany(mappedBy = "publisher")
    private List<Book> books;

    protected Publisher() {
    }

    public Publisher(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Book> getBooks() {
        return books;
    }
}