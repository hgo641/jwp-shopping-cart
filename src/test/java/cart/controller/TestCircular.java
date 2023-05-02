package cart.controller;

import cart.dto.ProductResponse;
import cart.service.ProductService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(AdminController.class)
//@Import({ViewConfig2.class})
public class TestCircular {

    @MockBean
    ProductService productService;

//    @Autowired
//    WebApplicationContext webApplicationContext;

//    @Autowired
//    MockMvc mockMvc;

//    @Autowired
//    ContentNegotiatingViewResolver viewResolver;

    @BeforeEach
    void setUp() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");

        //RestAssuredMockMvc.standaloneSetup(new AdminController(productService));


        //RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        //RestAssuredMockMvc.standaloneSetup(new AdminController(productService));
        //RestAssuredMockMvc.mockMvc(mockMvc);

        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new AdminController(productService))
                        .setViewResolvers(viewResolver)

        );
    }

    @Test
    void test() throws Exception {
//        Object object = applicationContext.getBean("viewResolver");
//        object.getClass();
//
//        Map<String, ViewResolver> matchingBeans =
//                BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ViewResolver.class, true, false);

//        webApplicationContext.getServletContext();
        System.out.println("test1 !");
        final ProductResponse firstProductResponse = new ProductResponse(1L, "홍고", "https://ca.slack-edge.com/TFELTJB7V-U04M4NFB5TN-e18b78fabe81-512", 1_000_000_000);
        final ProductResponse secondProductResponse = new ProductResponse(2L, "아벨", "https://ca.slack-edge.com/TFELTJB7V-U04LMNLQ78X-a7ef923d5391-512", 1_000_000_000);
        given(productService.findAll()).willReturn(List.of(firstProductResponse, secondProductResponse));

        RestAssuredMockMvc.config();


        RestAssuredMockMvc.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .status(HttpStatus.OK);
    }

    @Test
    void test2() throws Exception {
//        Object object = applicationContext.getBean("viewResolver");
//        object.getClass();
//
//        Map<String, ViewResolver> matchingBeans =
//                BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ViewResolver.class, true, false);

//        webApplicationContext.getServletContext();

        System.out.println("test2 !");
        final ProductResponse firstProductResponse = new ProductResponse(1L, "홍고", "https://ca.slack-edge.com/TFELTJB7V-U04M4NFB5TN-e18b78fabe81-512", 1_000_000_000);
        final ProductResponse secondProductResponse = new ProductResponse(2L, "아벨", "https://ca.slack-edge.com/TFELTJB7V-U04LMNLQ78X-a7ef923d5391-512", 1_000_000_000);
        given(productService.findAll()).willReturn(List.of(firstProductResponse, secondProductResponse));

        RestAssuredMockMvc.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .status(HttpStatus.OK);
    }
}
