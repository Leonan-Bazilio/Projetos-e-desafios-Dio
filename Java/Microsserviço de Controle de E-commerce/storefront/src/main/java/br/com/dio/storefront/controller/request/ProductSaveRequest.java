package br.com.dio.storefront.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSaveRequest(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name) {
}
