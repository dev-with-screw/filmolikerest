package home.work.filmolikerest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequestDto
{
    private String username;
    private String password;
}
