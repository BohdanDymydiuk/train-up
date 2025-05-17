package com.example.trainup.model;

import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
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
@SQLDelete(sql = "UPDATE gyms SET is_deleted=true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "gyms")
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private Address location;

    @ManyToMany
    @JoinTable(
            name = "sports_gyms",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    @NotEmpty(message = "A gym must be associated with at least one sport.")
    private Set<Sport> sports = new HashSet<>();

    private String description;

    private String website;

    @ElementCollection
    @CollectionTable(name = "gym_phone_numbers", joinColumns = @JoinColumn(name = "gym_id"))
    @Column(name = "phone_numbers", columnDefinition = "VARCHAR(255)")
    @NotEmpty(message = "Phone number can not be empty.")
    private Set<String> phoneNumbers;

    @ElementCollection
    @CollectionTable(name = "gym_working_hours", joinColumns = @JoinColumn(name = "gym_id"))
        private Set<WorkingHoursEntry> workingHours = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "gyms_trainers",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers = new HashSet<>();

    private Float overallRating = 0.0f;

    private Integer numberOfReviews = 0;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_owner_id")
    @NotNull(message = "Gym owner can not be blank.")
    private GymOwner gymOwner;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(max = 5, message = "The maximum number of gym photos is 5.")
    private Set<GymPhoto> photos = new HashSet<>();

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private boolean isDeleted = false;

    public void addPhoto(GymPhoto photo) {
        photos.add(photo);
        photo.setGym(this);
    }

    public void removePhoto(GymPhoto photo) {
        photos.remove(photo);
        photo.setGym(null);
    }

    public void setPhotos(Set<GymPhoto> newPhotos) {
        this.photos.clear();
        if (newPhotos != null) {
            newPhotos.forEach(this::addPhoto);
        }
    }
}
