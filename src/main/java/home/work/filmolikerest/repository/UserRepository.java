package home.work.filmolikerest.repository;

import home.work.filmolikerest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 */

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByEmail(String email);

    List<User> findByUsernameOrEmail(String username, String email);
}
