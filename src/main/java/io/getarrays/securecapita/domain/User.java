package io.getarrays.securecapita.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.getarrays.securecapita.ProjectManagement.Issue;

import io.getarrays.securecapita.roles.UserRole;
import io.getarrays.securecapita.stationsassignment.UserStation;
import io.getarrays.securecapita.user.UserDepartment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 8/22/2022
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;

    private int projectSize;

    @JsonIgnore
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Issue> assignedIssues = new ArrayList<>();

    @Column(name = "non_locked")
    private boolean isNotLocked = true;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    
    @Column(name = "password_last_changed")
    private LocalDateTime passwordLastChanged;

    @JsonIgnore
    private Timestamp verificationTokenExpiry;
    @JsonIgnore
    private String verificationToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<UserStation> stations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<UserDepartment> userDepartments = new ArrayList<>();

    //    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserRole> roles = new HashSet<>();

    public void addRole(UserRole role) {
        roles.add(role);
    }

    public void removeRole(UserRole role) {
        roles.remove(role);
    }

    public void expireAllRoles() {
        roles.forEach((role) -> role.setActive(false));
    }

    public void removeAllRole() {
        roles.clear();
    }

    public boolean isStationAssigned(Long stationId) {
        Hibernate.initialize(getStations());
        return stations.stream()
                .anyMatch(userStation -> userStation.getStation().getStation_id().equals(stationId));
    }

    //is stations empty
    public boolean isStationAssigned() {
        Hibernate.initialize(getStations());
        return !stations.isEmpty();
    }

    /**
     * Check if password has expired (90 days)
     */
    public boolean isPasswordExpired() {
        if (passwordLastChanged == null) {
            return true; // If never set, consider expired
        }
        return passwordLastChanged.plusDays(90).isBefore(LocalDateTime.now());
    }

    /**
     * Get days until password expires
     */
    public long getDaysUntilPasswordExpires() {
        if (passwordLastChanged == null) {
            return 0; // Already expired
        }
        LocalDateTime expiryDate = passwordLastChanged.plusDays(90);
        return java.time.Duration.between(LocalDateTime.now(), expiryDate).toDays();
    }

    /**
     * Check if password expires within specified days
     */
    public boolean isPasswordExpiringWithin(int days) {
        if (passwordLastChanged == null) {
            return true; // Already expired
        }
        return getDaysUntilPasswordExpires() <= days;
    }

    //laptops

}




