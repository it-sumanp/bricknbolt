package com.bricknbolt.repository;

import com.bricknbolt.domain.BoltUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the BoltUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoltUserRepository extends JpaRepository<BoltUser, Long> {
    Optional<BoltUser> findByPhone(String phone);
}
