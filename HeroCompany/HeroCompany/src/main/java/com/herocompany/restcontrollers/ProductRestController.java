package com.herocompany.restcontrollers;

import com.herocompany.entities.Category;
import com.herocompany.entities.Product;
import com.herocompany.services.ProductService;
import com.herocompany.utils.REnum;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductRestController {
    final ProductService productService;


    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Product product){
        return productService.save(product);
    }

    @Cacheable("productList")
    @GetMapping("/list")
    public ResponseEntity list(){
        return productService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Product product){
        return productService.update(product);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return productService.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String q){
        return productService.search(q);
    }

    @Cacheable("proByCat")
    @GetMapping("/productByCategory")
    public ResponseEntity productByCategory(@RequestParam Long id){
        return productService.productByCategory(id);
    }


}
