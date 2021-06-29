package home.work.filmolikerest.restcontroller;

import home.work.filmolikerest.dto.TestReq;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class TestContr {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        final List<User> all = userRepository.findAll();

        return "ok";
    }

    @GetMapping("/test2/{number}")
    public String test2(@PathVariable("number") @Min(3) Integer number) {

        return String.valueOf(number);
    }

    @PostMapping("/test3")
    public String test3(@RequestBody TestReq t) {
        return t.getSmb();
    }
}
