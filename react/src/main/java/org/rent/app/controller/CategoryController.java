package org.rent.app.controller;

import org.rent.app.domain.Category;
import org.rent.app.reposiroty.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;
/**
 * CategoryController
 * <p>
 *     Category Controller
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@RestController
@RequestMapping("cat")
public class CategoryController {

    @Autowired
    private CategoryRepository repository;

    @GetMapping("children")
    Flux<Category> getChildren(@RequestParam(required = false) Long parentId){
        return Objects.nonNull(parentId)
                ?repository.findAllByParentId(parentId): repository.findAllByParentIdIsNull();
    }

}
