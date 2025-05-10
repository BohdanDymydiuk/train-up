package com.example.trainup.model.user;

import com.example.trainup.model.Gym;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
@SQLDelete(sql = "UPDATE gym_owners SET is_deleted=true WHERE id=?")
@Table(name = "gym_owners")
public class GymOwner extends BaseUser {
    @ElementCollection
    @CollectionTable(
            name = "gym_owner_phone_numbers",
            joinColumns = @JoinColumn(name = "gym_owner_id")
    )
    @NotEmpty(message = "Contact phone number can not be empty.")
    private Set<String> phoneNumbers = new HashSet<>();

    @OneToMany(mappedBy = "gymOwner")
    @NotEmpty(message = "Gyms can not be blank.")
    private Set<Gym> ownedGyms = new HashSet<>();
}
