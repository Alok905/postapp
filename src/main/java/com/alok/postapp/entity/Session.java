package com.alok.postapp.entity;

import jakarta.persistence.*;

@Entity
public class Session {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
