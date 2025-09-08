package com.example.trainup.model;

import com.example.trainup.model.user.Trainer;
import com.example.trainup.validation.EventTarget;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EventTarget
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name can not be blank")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    @NotNull(message = "The sport can not be null")
    private Sport sport;

    private String description;

    @NotNull(message = "The date and time can not be blank")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(nullable = false)
    private Boolean onlineTraining;

    @Column(nullable = false)
    private Integer intensity;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(max = 3, message = "The maximum numbers of event photos is 3.")
    private Set<EventPhoto> photos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address location;
}
