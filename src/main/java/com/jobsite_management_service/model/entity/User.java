package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "business")
@Table(name = "users")
public class User implements com.jobsite_management_service.abstraction.User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    Business business;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "phone")
    String phone;

    //drives role based access control
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    UserType userType;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Override
    public String getFullName() {
        return String.join(" ", firstName == null ? "" : firstName, lastName == null ? "" : lastName).trim();
    }

    @Override
    public String getUserId() {
        return id == null ? null : String.valueOf(id);
    }
}
