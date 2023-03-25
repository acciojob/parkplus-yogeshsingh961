package com.driver.model;

import javax.persistence.*;
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int noOfHours;
    @ManyToOne
    @JoinColumn
    User user;
    @ManyToOne
    @JoinColumn
    Spot spot;
    @OneToOne
    Payment payment;


}
