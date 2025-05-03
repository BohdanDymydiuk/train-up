package com.example.trainup.model.user;

import com.example.trainup.model.Gym;
import com.example.trainup.model.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@SQLDelete(sql = "UPDATE gym_owners SET is_deleted=true WHERE id=?")
@Table(name = "gym_owners")
public class GymOwner extends BaseUser implements UserDetails {
    @OneToMany(mappedBy = "gymOwner")
    @NotEmpty(message = "Gyms can not be blank.")
    private Set<Gym> ownedGyms = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "gym_owner_phone_numbers",
            joinColumns = @JoinColumn(name = "gym_owner_id")
    )
    //    TODO: move validation to DTO
    @Pattern(regexp = "^\\+38 \\(\\d{3}\\) - \\d{3}-\\d{4}$",
            message = "Contact phone number should be in the format +38 (XXX) - XXX-XXXX")
    @NotEmpty(message = "Contact phone number can not be empty.")
    private Set<String> phoneNumbers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gym_owner_roles",
            joinColumns = @JoinColumn(name = "gym_owner_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
