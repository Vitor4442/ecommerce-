package com.ecommerce.product.service;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        UpdateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
      return   productRepository.findById(id).map(existingProduct -> {
            UpdateProductFromRequest(existingProduct, productRequest);
            Product savedProduct = productRepository.save(existingProduct);
            return mapToProductResponse(savedProduct);
        });
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setActive(savedProduct.getActive());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());

        return productResponse;
    }

    private void UpdateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setActive(productRequest.getActive());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
       return productRepository.findById(id)
               .map(product -> {
                   product.setActive(false);
                   productRepository.save(product);
                   return true;
               }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }
}
