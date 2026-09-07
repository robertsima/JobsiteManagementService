package com.jobsite_management_service.repository;

import com.jobsite_management_service.abstraction.enums.UserType;
import com.jobsite_management_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByBusinessId(Long businessId);

    //role based access control lookups
    List<User> findByBusinessIdAndUserType(Long businessId, UserType userType);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long getIdByEmail(@Param("email") String email);
}
