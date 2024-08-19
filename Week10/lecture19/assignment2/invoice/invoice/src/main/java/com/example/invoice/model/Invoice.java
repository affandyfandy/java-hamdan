package com.example.invoice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products;
}
