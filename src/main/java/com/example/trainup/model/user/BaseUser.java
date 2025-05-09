package com.example.trainup.model.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "is_deleted=false")
public abstract class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private Character maleOrFemale;

    private LocalDate dateOfBirth;

    private String profileImageUrl;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_credentials_id", nullable = false)
    private UserCredentials userCredentials;

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private boolean isDeleted = false;
}
