package com.jobsite_management_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository {
    Long getIdByEmail(String email);
}
