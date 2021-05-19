package com.gabrielduarte.customconfigpoc.repository;

import com.gabrielduarte.customconfigpoc.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
