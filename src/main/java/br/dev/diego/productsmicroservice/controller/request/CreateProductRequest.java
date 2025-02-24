package br.dev.diego.productsmicroservice.controller.request;

import java.math.BigDecimal;

public record CreateProductRequest(
        String title,
        BigDecimal price,
        Integer quantity
) {
}
