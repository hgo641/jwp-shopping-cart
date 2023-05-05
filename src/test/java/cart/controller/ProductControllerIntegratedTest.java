package cart.controller;

import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegratedTest {
    @LocalServerPort
    private int port;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        namedParameterJdbcTemplate.getJdbcTemplate().execute("ALTER TABLE PRODUCT ALTER COLUMN id RESTART WITH 1");
        RestAssured.port = port;
    }

    @Test
    void 모든_상품_목록을_관리자_페이지로_가져온다() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("관리자 페이지"));
    }

    @Test
    void 상품_목록을_조회한다() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    @Test
    void 상품을_생성한다() {
        final ProductRequest productRequest = new ProductRequest("아벨", "aaaa", 10000);
        RestAssured.given().log().all()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/products/1");
    }

    @Test
    void 상품을_수정한다() {
        final ProductRequest productRequest = new ProductRequest("아벨", "aaaa", 10000);
        RestAssured.given().log().all()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().patch("/products/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 상품을_삭제한다() {
        RestAssured.given().log().all()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/products/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 상품_이름이_null_또는_empty일_시_예외_발생(final String name) {
        final ProductRequest productRequest = new ProductRequest(name, "asd", 10);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 상품 이름을 입력해주세요."));
    }

    @Test
    void 상품_이름_길이가_255초과일때_예외_발생() {
        final ProductRequest productRequest = new ProductRequest("a".repeat(256), "asd", 10);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 상품 이름은 255자까지 입력가능합니다."));
    }

    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 이미지_URL이_null_또는_empty일_시_예외_발생(final String imageUrl) {
        final ProductRequest productRequest = new ProductRequest("홍고", imageUrl, 10);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 이미지 URL을 입력해주세요."));
    }

    @Test
    void 이미지_URL_길이가_255초과일때_예외_발생() {
        final ProductRequest productRequest = new ProductRequest("아벨", "a".repeat(256), 10);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 이미지 URL은 255자까지 입력가능합니다."));
    }

    @Test
    void 가격이_null일_시_예외_발생() {
        final ProductRequest productRequest = new ProductRequest("홍고", "홍고", null);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 가격을 입력해주세요."));
    }

    @Test
    void 가격이_1원_미만일때_예외_발생() {
        final ProductRequest productRequest = new ProductRequest("아벨", "a", 0);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 가격의 최소 금액은 1원입니다."));
    }

    @Test
    void 가격이_천만원_초과일때_예외_발생() {
        final ProductRequest productRequest = new ProductRequest("아벨", "a", 10_000_001);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 가격의 최대 금액은 1000만원입니다."));
    }
}
