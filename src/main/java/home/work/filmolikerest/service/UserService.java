package home.work.filmolikerest.service;

import home.work.filmolikerest.enums.RegistrationStatus;
import home.work.filmolikerest.model.User;

public interface UserService
{
    User findById(Long id);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    RegistrationStatus register(User userFromController);
}
