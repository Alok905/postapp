package com.alok.postapp.entity;

import com.alok.postapp.data.ConstraintMessageMapper;
import com.alok.postapp.data.DbConstraints;
import com.alok.postapp.enums.Permission;
import com.alok.postapp.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter @Setter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = DbConstraints.USER_EMAIL,
                        columnNames = {"email"}
                )
        }
)
@ToString
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Post> posts;

    @Enumerated(EnumType.STRING)
    Role role;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Permission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "user")
    List<Session> sessions;

    @CreationTimestamp
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;

    Instant deletedAt;

    Instant deactivatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Permission> effectivePermissions = new HashSet<>(this.role.getPermissions());
        effectivePermissions.addAll(this.permissions);

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));

        authorities.addAll(effectivePermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    ///  it'll be used to update the database if some extra permissions are given by admin
    public Set<Permission> getStoredPermissions() {
        return this.permissions;
    }

    public Set<Permission> getPermissions() {
        Set<Permission> effectivePermissions = new HashSet<>(this.permissions);
        effectivePermissions.addAll(this.role.getPermissions());

        return effectivePermissions;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
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
        return this.deactivatedAt == null;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", email='" + email + '\'' +
//                ", role=" + role +
//                ", createdAt=" + createdAt +
//                ", deletedAt=" + deletedAt +
//                ", deactivatedAt=" + deactivatedAt +
//                '}';
//    }

}

