package com.example.trainup.model;

import com.example.trainup.model.user.Admin;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role name can not be blank")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Admin> admins = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Athlete> athletes = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<GymOwner> gymOwners = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Trainer> trainers = new HashSet<>();
}
