package com.alok.postapp.entity;

import jakarta.persistence.*;

@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
