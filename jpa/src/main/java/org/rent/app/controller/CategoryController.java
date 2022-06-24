package org.rent.app.controller;

import org.rent.app.dto.CategoryDto;
import org.rent.app.mapper.CategoryMapper;
import org.rent.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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
    private static final CategoryMapper MAPPER = CategoryMapper.INSTANCE;

    @Autowired
    private CategoryRepository repository;

    @GetMapping("children")
    Collection<CategoryDto> getChildren(@RequestParam(required = false) Long parentId){
        return MAPPER.toDtos(Objects.nonNull(parentId)
                ?repository.findAllByParentId(parentId): repository.findAllByParentIsNull());
    }

}
