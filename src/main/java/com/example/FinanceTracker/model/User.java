package com.example.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String password; // Nullable for OAuth users

    private String provider; // LOCAL / GOOGLE
}
