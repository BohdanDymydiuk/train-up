package com.example.trainup.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE admins SET is_deleted=true WHERE id=?")
@SQLRestriction(value = "is_deleted = false")
@Table(name = "admins")
public class Admin extends BaseUser {
}
