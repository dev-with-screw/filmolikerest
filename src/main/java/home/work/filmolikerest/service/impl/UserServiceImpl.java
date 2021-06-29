package home.work.filmolikerest.service.impl;

import home.work.filmolikerest.enums.RegistrationStatus;
import home.work.filmolikerest.enums.SearchingStatus;
import home.work.filmolikerest.model.Role;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.RoleRepository;
import home.work.filmolikerest.repository.UserRepository;
import home.work.filmolikerest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static home.work.filmolikerest.model.User.NULL_USER;

/**
 * Implementation of {@link UserService} interface.
 * Wrapper for {@link UserRepository} + business logic.
 */

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        log.info("IN getAll - {} users found", users.size());
        return users;
    }

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
        User user = userRepository.findByUsername(username).orElse(NULL_USER);

        if (user.equals(NULL_USER)) {
            log.warn("IN findByUsername - no user found by username: {}", username);
        } else {
            log.info("IN findByUsername - user with id {} found by username: {}", user.getId(), username);
        }

        return user;
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

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

//        Optional<User> byUsername = userRepository.findByUsername(username);

//        if (byUsername.isPresent()) {
//            log.info("IN checkForUnique - found by username {}", username);
//            String emailOfFound = byUsername.get().getEmail();
//            if (emailOfFound.equals(email)){
//                return String.format("User with username= \"%s\" and email= \"%s\" already registered", username, email);
//            }
//            return String.format("User with username= \"%s\" already registered", username);
//        }
//
//        Optional<User> byEmail = userRepository.findByEmail(email);
//
//        if (byEmail.isPresent()) {
//            log.info("IN checkForUnique - found by email {}", email);
//            return String.format("User with email= \"%s\" already registered", email);
//        }
//
//        log.info("IN checkForUnique - no user found by username {} and email {}", username, email);
//        return "unique";
//    }

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

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

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

}
