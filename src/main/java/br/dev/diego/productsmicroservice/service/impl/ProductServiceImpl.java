package br.dev.diego.productsmicroservice.service.impl;

import br.dev.diego.productsmicroservice.controller.request.CreateProductRequest;
import br.dev.diego.productsmicroservice.service.ProductCreatedEvent;
import br.dev.diego.productsmicroservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createdProduct(CreateProductRequest request) {
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                request.title(),
                request.price(),
                request.quantity()
        );

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message {}...", ex.getMessage());
            } else {
                log.info("Product created event sent: {}", result.getProducerRecord().value());
            }
        });
        log.info("Returning productId {}", productId);
        return productId;
    }
}
