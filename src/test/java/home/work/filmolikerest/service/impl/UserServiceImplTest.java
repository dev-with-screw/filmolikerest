package home.work.filmolikerest.service.impl;

import home.work.filmolikerest.enums.RegistrationStatus;
import home.work.filmolikerest.model.Role;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.RoleRepository;
import home.work.filmolikerest.repository.UserRepository;
import home.work.filmolikerest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    void register_return_RegistrationStatus_Success() {
        //given
        User savingUser = createUser("username", "email");

        when(userRepository.save(savingUser))
                .thenReturn(createUser("username", "email"));

        //when
        RegistrationStatus expectedStatus = userService.register(savingUser);

        //then
        assertEquals(expectedStatus, RegistrationStatus.SUCCESS, "RegistrationStatus should be SUCCESS");

        verify(userRepository, times(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_setRoleForSavingUser() {
        //given
        User savingUser = createUser("username", "email");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(savingUser))
                .thenReturn(createUser("username", "email"));

        //when
        userService.register(savingUser);

        //then
        assertEquals(savingUser.getRoles().get(0), role,"Saving user should have role ROLE_USER before saving to DB");

        verify(roleRepository,times(1)).findByName(any(String.class));
    }

    @Test
    void register_setEncodedPasswordForSavingUser() {
        //given
        User savingUser = createUser("username", "email");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        when(userRepository.findByUsernameOrEmail(any(String.class), any(String.class)))
                .thenReturn(Collections.emptyList());
        when(userRepository.save(any(User.class)))
                .thenReturn(createUser("username", "email"));

        //when
        userService.register(savingUser);

        //then
        assertEquals(savingUser.getPassword(), "encodedPassword","Saving user should have role encoded password before saving to DB");

        verify(passwordEncoder,times(1)).encode(any(String.class));
    }

    @Test
    void register_return_RegistrationStatus_USERNAME_AND_EMAIL_EXIST() {
        //given
        User savingUser = createUser("username", "email");

        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(createUser("username", "email"));
        when(userRepository.findByUsernameOrEmail(savingUser.getUsername(), savingUser.getEmail()))
                .thenReturn(foundUsers);

        //when
        RegistrationStatus expectedStatus = userService.register(savingUser);

        //then
        assertEquals(expectedStatus, RegistrationStatus.USERNAME_AND_EMAIL_EXIST, "RegistrationStatus should be USERNAME_AND_EMAIL_EXIST");

        verify(userRepository, times(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void register_return_RegistrationStatus_USERNAME_EXIST() {
        //given
        User savingUser = createUser("username", "email");

        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(createUser("username", "email_another"));
        when(userRepository.findByUsernameOrEmail(savingUser.getUsername(), savingUser.getEmail()))
                .thenReturn(foundUsers);

        //when
        RegistrationStatus expectedStatus = userService.register(savingUser);

        //then
        assertEquals(expectedStatus, RegistrationStatus.USERNAME_EXIST, "RegistrationStatus should be USERNAME_EXIST");

        verify(userRepository, times(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void register_return_RegistrationStatus_EMAIL_EXIST() {
        //given
        User savingUser = createUser("username", "email");

        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(createUser("username_another", "email"));
        when(userRepository.findByUsernameOrEmail(savingUser.getUsername(), savingUser.getEmail()))
                .thenReturn(foundUsers);

        //when
        RegistrationStatus expectedStatus = userService.register(savingUser);

        //then
        assertEquals(expectedStatus, RegistrationStatus.EMAIL_EXIST, "RegistrationStatus should be EMAIL_EXIST");

        verify(userRepository, times(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void register_return_RegistrationStatus_USERNAME_AND_EMAIL_EXIST_() {
        //given
        User savingUser = createUser("username", "email");

        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(createUser("username_another", "email"));
        foundUsers.add(createUser("username", "email_another"));
        when(userRepository.findByUsernameOrEmail(savingUser.getUsername(), savingUser.getEmail()))
                .thenReturn(foundUsers);

        //when
        RegistrationStatus expectedStatus = userService.register(savingUser);

        //then
        assertEquals(expectedStatus, RegistrationStatus.USERNAME_AND_EMAIL_EXIST, "RegistrationStatus should be USRNAME_AND_EMAIL_EXIST");

        verify(userRepository, times(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }


    private User createUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");

        return user;
    }


    @Test
    void findByUsername_success() {
        //given
        User user = new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        // when
        final User foundUser = userService.findByUsername("username");

        //then
        assertEquals(foundUser, user);
    }

    @Test
    void findByUsername_returnNullUser() {
        //given
        User user = new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        // when
        final User foundUser = userService.findByUsername("username");

        //then
        assertEquals(foundUser, User.NULL_USER);
    }
}