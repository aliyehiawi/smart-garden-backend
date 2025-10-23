package org.smartgarden.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a user in the Smart Garden system.
 * 
 * <p>Users have authentication credentials (username and password)
 * and are assigned a role (ADMIN or USER) that determines their
 * access permissions. Users can be assigned to multiple gardens.
 * 
 * <p>Passwords are stored encrypted using BCrypt and should never
 * be exposed in API responses.
 * 
 * @author Smart Garden Team
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username used for authentication.
     * Must be unique across all users.
     */
    @Column(nullable = false, unique = true, length = 64)
    private String username;

    /**
     * Encrypted password using BCrypt.
     * Never expose this field in API responses.
     */
    @Column(nullable = false)
    private String password;

    /**
     * User's role determining access permissions.
     * Can be ADMIN or USER.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private UserRole role;

    /**
     * Set of gardens this user is assigned to.
     * Users can access data only from their assigned gardens.
     */
    @ManyToMany
    @JoinTable(name = "user_garden",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "garden_id"))
    @Builder.Default
    private Set<Garden> assignedGardens = new HashSet<>();
}


