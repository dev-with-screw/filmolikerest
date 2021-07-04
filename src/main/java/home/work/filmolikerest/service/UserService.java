package home.work.filmolikerest.service;

import home.work.filmolikerest.enums.RegistrationStatus;
import home.work.filmolikerest.model.User;

import java.util.List;

public interface UserService
{
    List<User> getAll();

    User findById(Long id);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    void delete(Long id);

    RegistrationStatus register(User userFromController);
}
