package com.example.trainup.model.user;

import com.example.trainup.model.Sport;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE athletes SET is_deleted=true WHERE id=?")
@Table(name = "athletes")
public class Athlete extends BaseUser {
    @ElementCollection
    @CollectionTable(name = "athlete_phone_numbers", joinColumns = @JoinColumn(name = "athlete_id"))
    @NotEmpty(message = "Contact phone number can not be empty.")
    private Set<String> phoneNumbers = new HashSet<>();

    @ManyToMany(mappedBy = "athletes")
    private Set<Sport> sports = new HashSet<>();

    @NotNull(message = "Would you like to receive email updates about upcoming events, "
            + "open workouts, competitions, and promotional offers?")
    private Boolean emailPermission;

    @NotNull(message = "Would you like to receive SMS updates about upcoming events, "
            + "open workouts, competitions, and promotional offers?")
    private Boolean phonePermission;
}
