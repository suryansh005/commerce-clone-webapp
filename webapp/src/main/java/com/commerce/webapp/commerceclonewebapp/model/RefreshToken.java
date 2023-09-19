package com.commerce.webapp.commerceclonewebapp.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false , unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "customer_id" ,  referencedColumnName = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
