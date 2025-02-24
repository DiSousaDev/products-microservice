package br.dev.diego.productsmicroservice.service;

import br.dev.diego.productsmicroservice.controller.request.CreateProductRequest;

public interface ProductService {

    String createdProduct(CreateProductRequest request);

}
