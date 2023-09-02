package com.rishu.springauthentication.repository;

// Role model needs a repository to perform CRUD operations

import com.rishu.springauthentication.models.ERole;
import com.rishu.springauthentication.models.Role;
import com.rishu.springauthentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

}
