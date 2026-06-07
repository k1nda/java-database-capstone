package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "name cannot be null")
    @Size(min = 3, max = 100, message = "name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "specialty cannot be null")
    @Size(min = 3, max = 50, message = "specialty must be between 3 and 50 characters")
    @Column(nullable = false)
    private String specialty;

    @NotNull(message = "email cannot be null")
    @Email(message = "email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "password cannot be null")
    @Size(min = 6, message = "password must be at least 6 characters")
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "phone cannot be null")
    @Pattern(regexp = "^(?:\\d{10}|\\d{3}-\\d{3}-\\d{4})$", message = "phone number must be 10 digits or in XXX-XXX-XXXX format")
    @Column(nullable = false)
    private String phone;

    @ElementCollection
    @CollectionTable(name = "doctor_available_times", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "available_times")
    @JsonAlias("available_times")
    private List<String> availableTimes;

    // Constructors
    public Doctor() {
    }

    public Doctor(String name, String specialty, String email, String password, String phone) {
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public Doctor(String name, String specialty, String email, String password, String phone, List<String> availableTimes) {
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.availableTimes = availableTimes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    @JsonProperty("available_times")
    public List<String> getAvailableTimesAlias() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}

