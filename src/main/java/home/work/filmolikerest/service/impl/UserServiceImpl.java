package home.work.filmolikerest.service.impl;

import home.work.filmolikerest.enums.RegistrationStatus;
import home.work.filmolikerest.enums.SearchingStatus;
import home.work.filmolikerest.model.Role;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.RoleRepository;
import home.work.filmolikerest.repository.UserRepository;
import home.work.filmolikerest.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static home.work.filmolikerest.model.User.NULL_USER;

@Service
@Transactional
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(NULL_USER);

        if (user.equals(NULL_USER)) {
            log.warn("IN findById - no user found by id: {}", id);
        } else {
            log.info("IN findById - user found by id: {}", id);
        }

        return user;
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            log.info("IN findByUsername - user with id {} found by username: {}", user.get().getId(), username);
            return user.get();
        } else {
            log.warn("IN findByUsername - no user found by username: {}", username);
            return NULL_USER;
        }
    }

    public User findByUsernameAndPassword(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.info("IN findByUsernameAndPassword - user with id {} found by username {}", user.getId(), username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return NULL_USER;
    }

    public RegistrationStatus register(User user) {

        final SearchingStatus status = checkUniqueUsernameAndEmail(user.getUsername(), user.getEmail());

        switch (status) {
            case USERNAME_AND_EMAIL_FOUND: {
                return RegistrationStatus.USERNAME_AND_EMAIL_EXIST;
            }
            case USERNAME_FOUND: {
                return RegistrationStatus.USERNAME_EXIST;
            }
            case EMAIL_FOUND: {
                return RegistrationStatus.EMAIL_EXIST;
            }
        }

        user.setRoles(addUserRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User registeredUser = userRepository.save(user);

        log.info("IN register - user successfully registered with id {}", registeredUser.getId());

        return RegistrationStatus.SUCCESS;
    }

    private SearchingStatus checkUniqueUsernameAndEmail(String username, String email) {
        List<User> foundUsers = userRepository.findByUsernameOrEmail(username, email);

        boolean usernameFound = false;
        boolean emailFound = false;

        if (!foundUsers.isEmpty()) {
            for (User u : foundUsers) {
                if (u.getUsername().equals(username)) {
                    usernameFound = true;
                }
                if (u.getEmail().equals(email)) {
                    emailFound = true;
                }
            }
        }

        if (usernameFound) {
            if (emailFound) {
                return SearchingStatus.USERNAME_AND_EMAIL_FOUND;
            }
            return SearchingStatus.USERNAME_FOUND;
        }

        if (emailFound) {
            return SearchingStatus.EMAIL_FOUND;
        }

        return SearchingStatus.NOT_FOUND;
    }

    private List<Role> addUserRole() {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        return userRoles;
    }

}
