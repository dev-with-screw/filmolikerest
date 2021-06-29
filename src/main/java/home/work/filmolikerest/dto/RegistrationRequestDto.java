package home.work.filmolikerest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import home.work.filmolikerest.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestDto {
    @NotBlank
    @Length(min=1, max=100)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    public User toUser(){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstname);
        user.setLastName(lastname);

        return user;
    }

    public static RegistrationRequestDto fromUser(User user) {
        RegistrationRequestDto regDto = new RegistrationRequestDto();

        regDto.setUsername(user.getUsername());
        regDto.setEmail(user.getEmail());
        regDto.setFirstname(user.getFirstName());
        regDto.setLastname(user.getLastName());

        return regDto;
    }

}
