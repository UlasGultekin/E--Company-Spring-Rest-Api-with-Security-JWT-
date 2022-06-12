package com.herocompany.restcontrollers;

import com.herocompany.entities.Category;
import com.herocompany.services.CategoryService;
import com.herocompany.utils.REnum;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/category")
public class CategoryRestController {

    final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Category category){
        return categoryService.save(category);
    }
    @Cacheable("categoryList")
    @GetMapping("/list")
    public ResponseEntity list(){
        return categoryService.list();
    }
    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Category category){
        return categoryService.update(category);
    }
    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return categoryService.delete(id);
    }




}
