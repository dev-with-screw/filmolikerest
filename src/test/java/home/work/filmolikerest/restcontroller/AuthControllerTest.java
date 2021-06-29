package home.work.filmolikerest.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.work.filmolikerest.dto.AuthRequestDto;
import home.work.filmolikerest.dto.RegistrationRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRegister_returnSuccess() throws Exception {
        //create request with username and email that have not registered earlier
        RegistrationRequestDto request = createRegistrationRequest("u2", "b@b.ru");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User has been registered successful")));
    }

    @Test
    void postRegisterWithExistedUsername_returnNotAcceptable() throws Exception {
        //username already exists in db
        RegistrationRequestDto request = createRegistrationRequest("u", "c@c.ru");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
        )
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$", is("User with username= \"u\" already registered")));
    }

    @Test
    void postRegisterWithExistedEmail_returnNotAcceptable() throws Exception {
        //email already exists in db
        RegistrationRequestDto request = createRegistrationRequest("u3", "a@a.ru");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
        )
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$", is("User with email= \"a@a.ru\" already registered")));
    }

    @Test
    void postRegisterWithExistedUsernameAndEmail_returnNotAcceptable() throws Exception {
        //username and email already existed in db
        RegistrationRequestDto request = createRegistrationRequest("u", "a@a.ru");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
        )
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$", is("User with username= \"u\" and email= \"a@a.ru\" already registered")));
    }

    @Test
    void postLogin_returnToken() throws Exception {
        AuthRequestDto request = new AuthRequestDto("u", "p");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("u")))
                .andExpect(jsonPath("$.token", notNullValue()));
        //TODO возможно, стоит переписать для корректной проверки токена
    }

    @Test
    void postLoginWithBadCredentials_returnException() throws Exception {
        AuthRequestDto request = new AuthRequestDto("u","incorrect password");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
        )
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.timestamp", lessThan(LocalDateTime.now().toString())))
                .andExpect(jsonPath("$.status", is("NOT_ACCEPTABLE")))
                .andExpect(jsonPath("$.message", is("Invalid username or password")))
                .andExpect(jsonPath("$.errors.[0]", is("Invalid username or password")));
    }


    private RegistrationRequestDto createRegistrationRequest(String username, String email) {
        RegistrationRequestDto request = new RegistrationRequestDto();

        request.setUsername(username);
        request.setFirstname("firstname");
        request.setLastname("lastname");
        request.setEmail(email);
        request.setPassword("p");

        return request;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}