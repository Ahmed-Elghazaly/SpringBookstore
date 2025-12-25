package com.example.bookstore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    @Id
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "threshold_quantity", nullable = false)
    private int thresholdQuantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_name", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "book")
    private List<AuthorBook> authorBooks;

    protected Book() {
    }

    public Book(String isbn, String title, Integer publicationYear, BigDecimal sellingPrice, 
                int stockQuantity, int thresholdQuantity, Publisher publisher, Category category) {
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.thresholdQuantity = thresholdQuantity;
        this.publisher = publisher;
        this.category = category;
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public Integer getPublicationYear() { return publicationYear; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public int getThresholdQuantity() { return thresholdQuantity; }
    public Publisher getPublisher() { return publisher; }
    public Category getCategory() { return category; }
    public List<AuthorBook> getAuthorBooks() { return authorBooks; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setThresholdQuantity(int thresholdQuantity) { this.thresholdQuantity = thresholdQuantity; }
}
