package com.example.trainup.model;

import com.example.trainup.model.user.Trainer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"country", "city", "street", "house"})
@SQLDelete(sql = "UPDATE addresses SET is_deleted=true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    private String cityDistrict;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String house;

    @OneToOne(mappedBy = "location")
    private Gym gym;

    @OneToMany(mappedBy = "location")
    private Set<Trainer> trainers = new HashSet<>();

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private boolean isDeleted = false;
}
