package home.work.filmolikerest.repository;


import home.work.filmolikerest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Role}.
 */

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
