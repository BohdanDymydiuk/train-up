package com.example.trainup.model;

import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.Trainer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE sports SET is_deleted=true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "sports")
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sportName;

    @ManyToMany(mappedBy = "sports")
    private Set<Gym> gyms = new HashSet<>();

    @ManyToMany(mappedBy = "sports")
    private Set<Trainer> trainers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "sports_athletes",
            joinColumns = @JoinColumn(name = "sport_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private Set<Athlete> athletes = new HashSet<>();

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private boolean isDeleted = false;
}
