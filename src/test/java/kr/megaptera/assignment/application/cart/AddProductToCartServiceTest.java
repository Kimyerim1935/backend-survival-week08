package kr.megaptera.assignment.application.cart;


import java.util.Optional;

import kr.megaptera.assignment.application.cart.AddProductToCartService;
import kr.megaptera.assignment.models.Cart;
import kr.megaptera.assignment.models.Product;
import kr.megaptera.assignment.models.ProductId;
import kr.megaptera.assignment.repositories.CartRepository;
import kr.megaptera.assignment.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AddProductToCartServiceTest {
    private CartRepository cartRepository;
    private ProductRepository productRepository;

    private AddProductToCartService addProductToCartService;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);

        addProductToCartService = new AddProductToCartService(cartRepository, productRepository);
    }

    @Test
    @DisplayName("addProduct - when cart doesn't exist")
    void cartNotExists() {
        Product product = Fixtures.product();
        ProductId productId = product.id();

        given(cartRepository.findById(any())).willReturn(Optional.empty());
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        Cart cart = addProductToCartService.addProduct(productId, 1);

        assertThat(cart.lineItemsSize()).isEqualTo(1);

        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("addProduct - when cart exists")
    void cartExists() {
        Cart cart = Fixtures.cart();

        Product product = Fixtures.product();
        ProductId productId = product.id();

        given(cartRepository.findById(any())).willReturn(Optional.of(cart));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        addProductToCartService.addProduct(productId, 1);

        assertThat(cart.lineItemsSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("addProduct - when product doesn't exist")
    void productNotExists() {
        ProductId productId = new ProductId("test-product-id");

        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            addProductToCartService.addProduct(productId, 1);
        });
    }
}