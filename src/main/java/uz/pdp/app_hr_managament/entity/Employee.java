package uz.pdp.app_hr_managament.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data@AllArgsConstructor@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Employee implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @ManyToOne
    private Company company;

    @ManyToMany
    private Set<Role> role;

    private boolean enabled;

    private Integer emailCode;

    private Double salary;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;

    private boolean AccountNonExpired = true; // Account amal qilish muddati

    private boolean AccountNonLocked = true; // Account bloklanganligi

    private boolean CredentialsNonExpired = true; // Royhatdan otgandagi holati

    ///EMPLOYEEE METHODLARI

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.AccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.AccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.CredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}