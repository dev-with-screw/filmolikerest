package home.work.filmolikerest.restcontroller;

import home.work.filmolikerest.dto.TestRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@Validated
public class TestController
{

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @GetMapping("/test2/{number}")
    public String test2(@PathVariable("number") @Min(3) Integer number) {

        return String.valueOf(number);
    }

    @PostMapping("/test3")
    public String test3(@RequestBody TestRequest t) {
        return t.getSmb();
    }



}
