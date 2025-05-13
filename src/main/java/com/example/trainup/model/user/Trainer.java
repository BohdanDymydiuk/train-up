package com.example.trainup.model.user;

import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Review;
import com.example.trainup.model.Sport;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE trainers SET is_deleted=true WHERE id=?")
@Table(name = "trainers")
public class Trainer extends BaseUser {
    @ElementCollection
    @CollectionTable(name = "trainer_phone_numbers", joinColumns = @JoinColumn(name = "trainer_id"))
    @NotEmpty(message = "Contact phone number can not be empty.")
    private Set<String> phoneNumbers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "sports_trainers",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    private Set<Sport> sports = new HashSet<>();

    @ManyToMany(mappedBy = "trainers")
    private Set<Gym> gyms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address location;

    @Column(nullable = false)
    private boolean onlineTraining = false;

    @ElementCollection
    @CollectionTable(name = "trainer_certificates", joinColumns = @JoinColumn(name = "trainer_id"))
    private Set<String> certificates;

    private String description;

    private String socialMediaLinks;

    private Float overallRating = 0.0f;

    private Integer numberOfReviews = 0;

    @OneToMany(mappedBy = "trainer")
    private List<Review> reviews;
}
