package com.example.dairy;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cows")
public class Cow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cow_id")
    private int cowId;

    @Column(nullable = false)
    private String name;

    private String breed;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public Cow() {
    }

    public Cow(String name, String breed, LocalDate dateOfBirth) {
        this.name = name;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
    }

    public Cow(int cowId, String name, String breed, LocalDate dateOfBirth) {
        this.cowId = cowId;
        this.name = name;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getCowId() {
        return cowId;
    }

    public void setCowId(int cowId) {
        this.cowId = cowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Breed: %s",
                cowId, name, breed);
    }
}
