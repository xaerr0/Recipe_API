package codingnomads.co.Recipe.API.repositories;

import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<CustomUserDetails, Long> {

    CustomUserDetails findByUsername(String username);
}