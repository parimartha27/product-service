package com.training.product.controller;

import com.training.product.dto.*;
import com.training.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> editProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {
        return productService.editProduct(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/name")
    public ResponseEntity<ApiResponse> findProductByName(@RequestBody FindByNameRequest request) {
        return productService.getProductByName(request);
    }

    @PostMapping("/update/stock")
    public ResponseEntity<ApiResponse> updateProductStock(@RequestBody UpdateStockRequest request) {
        return productService.updateProductStock(request);
    }
}
