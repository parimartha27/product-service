package com.training.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.product.constant.Constant;
import com.training.product.dto.*;
import com.training.product.entity.ProductEntity;
import com.training.product.repository.ProductRepository;
import com.training.product.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper mapper;
    private final ResponseHelper responseHelper;

    public void saveProduct(ProductEntity product){
        productRepository.save(product);
    }

    public ResponseEntity<ApiResponse> getProductById(Long id){
        Optional<ProductEntity> productFromDb = productRepository.findById(id);

        if(productFromDb.isEmpty()) {
           return responseHelper.setResponse(
                   HttpStatus.NOT_FOUND,
                   Constant.NOT_FOUND,
                   "Product with ID: " + id + " not found",
                   null
                   );
        }
        ProductResponse response = mapper.convertValue(productFromDb, ProductResponse.class);
        return responseHelper.setResponse(
                HttpStatus.OK,
                Constant.SUCCESS,
                "Success for Product with ID: " + id,
                response
        );
    }

    public ResponseEntity<ApiResponse> getAllProducts(){

       List<ProductEntity> products = productRepository.findAll();

       List<ProductResponse> response = products.stream().map(
               product -> ProductResponse.builder()
                       .name(product.getName())
                       .description(product.getDescription())
                       .price(product.getPrice())
                       .stock(product.getStock())
                       .build())
               .collect(Collectors.toList());

       return responseHelper.setResponse(
               HttpStatus.OK,
               Constant.SUCCESS,
               "Success",
               response);
    }

    public ResponseEntity<ApiResponse> addProduct(ProductRequest request){

        ProductEntity productEntity = mapper.convertValue(request, ProductEntity.class);
        this.saveProduct(productEntity);

        return responseHelper.setResponse(
                HttpStatus.CREATED,
                Constant.CREATED,
                "Success added new product",
                null);
    }

    public ResponseEntity<ApiResponse> editProduct(Long id, ProductRequest request){

        Optional<ProductEntity> productFromDb = productRepository.findById(id);

        if(productFromDb.isEmpty()) {
            return responseHelper.setResponse(
                    HttpStatus.NOT_FOUND,
                    Constant.NOT_FOUND,
                    "Product with ID: " + id + " not found",
                    null
            );
        }

        ProductEntity editedProduct = productFromDb.get();
        editedProduct.setName(request.getName());
        editedProduct.setDescription(request.getDescription());
        editedProduct.setPrice(request.getPrice());
        editedProduct.setStock(request.getStock());
        editedProduct.setUpdatedDate(LocalDateTime.now());
        this.saveProduct(editedProduct);

        return responseHelper.setResponse(
                HttpStatus.CREATED,
                Constant.SUCCESS,
                "Success updated data product",
                null);
    }

    public ResponseEntity<ApiResponse> deleteProduct (Long id) {

        boolean isExist = productRepository.existsById(id);

        if (!isExist) {
            return responseHelper.setResponse(
                    HttpStatus.NOT_FOUND,
                    Constant.NOT_FOUND,
                    "Product with ID: " + id + " not found",
                    null);
        }
        productRepository.deleteById(id);
        return responseHelper.setResponse(
                HttpStatus.OK,
                Constant.SUCCESS,
                "Success deleted data product",
                null);
    }

    public ResponseEntity<ApiResponse> getProductByName(FindByNameRequest request) {
        Optional<ProductEntity> data = productRepository.findByName(request.getName());

        if(data.isPresent()){
            ProductEntity entity = data.get();
            ProductResponse response = ProductResponse.builder()
                    .name(entity.getName())
                    .stock(entity.getStock())
                    .price(entity.getPrice())
                    .description(entity.getDescription()).build();

            return responseHelper.setResponse(HttpStatus.OK, "P-200", "Success", response);
        }

        return responseHelper.setResponse(HttpStatus.NOT_FOUND, "P-404", "Product not found", null);
    }

    public ResponseEntity<ApiResponse> updateProductStock(@RequestBody UpdateStockRequest request){
        Optional<ProductEntity> productFromDb = productRepository.findByName(request.getProductName());

        if(productFromDb.isPresent()){
            ProductEntity product = productFromDb.get();

            product.setStock(product.getStock() - request.getQuantity());
            product.setUpdatedDate(LocalDateTime.now());
            this.saveProduct(product);

            return responseHelper.setResponse(
                    HttpStatus.OK,
                    Constant.SUCCESS,
                    "Success updated stock",
                    null
            );
        }

        return responseHelper.setResponse(
                HttpStatus.NOT_FOUND,
                Constant.NOT_FOUND,
                "Product with ID: " + request.getProductName() + " not found",
                null);
    }

}
