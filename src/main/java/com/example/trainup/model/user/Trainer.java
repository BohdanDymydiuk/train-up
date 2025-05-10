package com.example.trainup.model.user;

import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Review;
import com.example.trainup.model.Sport;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @ManyToMany(mappedBy = "trainers")
    @NotEmpty(message = "A trainer must be associated with at least one sport.")
    private Set<Sport> sports = new HashSet<>();

    @ManyToMany(mappedBy = "trainers")
    private Set<Gym> gyms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address location;

    private boolean onlineTraining = false;

    @ElementCollection
    @CollectionTable(name = "trainer_certificates", joinColumns = @JoinColumn(name = "trainer_id"))
    private Set<String> certificates;

    private String description;

    private String socialMediaLinks;

    private Float overallRating;

    @OneToMany(mappedBy = "trainer")
    private List<Review> reviews;
}
