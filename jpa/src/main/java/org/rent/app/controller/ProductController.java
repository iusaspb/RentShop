package org.rent.app.controller;

import org.rent.app.dto.ProductDto;
import org.rent.app.mapper.ProductMapper;
import org.rent.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;
/**
 * ProductController
 * <p>
 *     Product Controller
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@RestController
@RequestMapping("product/")
public class ProductController {

    private static final ProductMapper MAPPER = ProductMapper.INSTANCE;

    @Autowired
    private ProductRepository repository;

    @GetMapping
    Collection<ProductDto> getAllProducts(){
        return MAPPER.toDtos(repository.findAll());
    }
    @GetMapping("owner/{ownerId}")
    Collection<ProductDto> getByOwner(@PathVariable Long ownerId){
        return MAPPER.toDtos(repository.findAllByOwnerId(ownerId));
    }
    @GetMapping("category/{categoryId}")
    Collection<ProductDto> getByCategory(@PathVariable Long categoryId){
        return MAPPER.toDtos(repository.findAllByCategoryId(categoryId));
    }

    @PostMapping
    Collection<ProductDto> search(@RequestBody(required = false) String searchText) {
        // Call findAll if searchText is blank
        if (Objects.nonNull(searchText) && !searchText.isBlank()) {
            searchText = searchText.trim();
            if (!searchText.endsWith("%")) {
                searchText += "%";
            }
            return MAPPER.toDtos(repository.recursiveSearch(searchText));
        } else {
            return MAPPER.toDtos(repository.findAll());
        }
    }

}
