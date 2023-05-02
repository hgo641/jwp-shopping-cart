package cart.controller;

import cart.dto.ProductResponse;
import cart.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
//@Import({ViewConfig.class})
public class TestCircular2 {

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        //RestAssuredMockMvc.standaloneSetup(new AdminController(productService));
        //RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void test() throws Exception {
        final ProductResponse firstProductResponse = new ProductResponse(1L, "홍고", "https://ca.slack-edge.com/TFELTJB7V-U04M4NFB5TN-e18b78fabe81-512", 1_000_000_000);
        final ProductResponse secondProductResponse = new ProductResponse(2L, "아벨", "https://ca.slack-edge.com/TFELTJB7V-U04LMNLQ78X-a7ef923d5391-512", 1_000_000_000);
        given(productService.findAll()).willReturn(List.of(firstProductResponse, secondProductResponse));

        mockMvc.perform(get("/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
