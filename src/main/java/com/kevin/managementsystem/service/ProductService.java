package com.kevin.managementsystem.service;

import com.kevin.managementsystem.domain.Product;
import com.kevin.managementsystem.dto.ProductRequestDTO;
import com.kevin.managementsystem.dto.ProductResponseDTO;
import com.kevin.managementsystem.exception.BusinessException;
import com.kevin.managementsystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .category(dto.category())
                .active(true)
                .build();

        return ProductResponseDTO.fromEntity(productRepository.save(product));
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado: " + id));
        return ProductResponseDTO.fromEntity(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException("Produto não encontrado: " + id);
        }
        productRepository.deleteById(id);
    }
}