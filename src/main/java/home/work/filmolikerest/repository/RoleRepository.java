package home.work.filmolikerest.repository;


import home.work.filmolikerest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByName(String name);
}
