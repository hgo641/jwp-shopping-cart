package cart.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Product {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Integer price;

    public Product(String name, String imageUrl, Integer price) {
        this(null, name, imageUrl, price);
    }
}
