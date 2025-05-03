package com.example.trainup.model.user;

import com.example.trainup.model.Role;
import com.example.trainup.model.Sport;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@SQLDelete(sql = "UPDATE athletes SET is_deleted=true WHERE id=?")
@Table(name = "athletes")
public class Athlete extends BaseUser implements UserDetails {
    @ManyToMany(mappedBy = "athletes")
    private Set<Sport> sports = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "athlete_phone_numbers", joinColumns = @JoinColumn(name = "athlete_id"))
    //    TODO: move validation to DTO
    @Pattern(regexp = "^\\+38 \\(\\d{3}\\) - \\d{3}-\\d{4}$",
            message = "Contact phone number should be in the format +38 (XXX) - XXX-XXXX")
    @NotEmpty(message = "Contact phone number can not be empty.")
    private Set<String> phoneNumbers = new HashSet<>();

    @NotNull(message = "Would you like to receive email updates about upcoming events, "
            + "open workouts, competitions, and promotional offers?")
    private boolean emailPermission;

    @NotNull(message = "Would you like to receive SMS updates about upcoming events, "
            + "open workouts, competitions, and promotional offers?")
    private boolean phonePermission;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "athlete_roles",
            joinColumns = @JoinColumn(name = "athlete_id"),
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
