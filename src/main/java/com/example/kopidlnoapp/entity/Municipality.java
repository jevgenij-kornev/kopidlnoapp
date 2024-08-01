package com.example.kopidlnoapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "municipality")
@Data
public class Municipality {

    @Id
    private String code;
    private String name;
}