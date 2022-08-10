package org.rent.app.controller;

import org.rent.app.domain.Product;
import org.rent.app.reposiroty.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
    @Autowired
    private ProductRepository repository;

    @GetMapping
    Flux<Product> getAllProducts(){
        return repository.findAll();
    }

    @GetMapping("owner/{ownerId}")
    Flux<Product> getByOwner(@PathVariable Long ownerId){
        return repository.findAllByOwnerId(ownerId);
    }

    @GetMapping("category/{categoryId}")
    Flux<Product> getByCategory(@PathVariable Long categoryId){
        return repository.findAllByCategoryId(categoryId);
    }

    @PostMapping
    Flux<Product> search(@RequestBody(required = false) String searchText) {
        // Call findAll if searchText is blank
        if (Objects.nonNull(searchText) && !searchText.isBlank()) {
            searchText = searchText.trim();
            if (!searchText.endsWith("%")) {
                searchText += "%";
            }
            return repository.recursiveSearch(searchText);
        } else {
            return repository.findAll();
        }
    }
}
