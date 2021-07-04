package home.work.filmolikerest.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.work.filmolikerest.dto.TestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1() throws Exception {
        mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ok")));

    }

    @Test
    void test3() throws Exception {
        mockMvc.perform(post("/test3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"smb\":\"random_string\"}")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("random_string")));
    }

    @Test
    void test3withDto() throws Exception {
        TestRequest req = new TestRequest();
        req.setSmb("random_string");
        
        mockMvc.perform(post("/test3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(req))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("random_string")));
    }
    
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}