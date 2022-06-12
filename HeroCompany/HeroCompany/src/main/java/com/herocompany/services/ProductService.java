package com.herocompany.services;

import com.herocompany.configs.Configs;
import com.herocompany.entities.Category;
import com.herocompany.entities.Product;
import com.herocompany.repositories.CategoryRepository;
import com.herocompany.repositories.ProductRepository;
import com.herocompany.utils.REnum;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;
    final CacheManager cacheManager;
    final Configs configs;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CacheManager cacheManager, Configs configs) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cacheManager = cacheManager;
        this.configs = configs;
    }

    public ResponseEntity<Map<REnum,Object>> save(Product product){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
//        System.out.println("send    "+product.getCategory().getId());
        try {
            Optional<Category> optionalCategory= Optional.of(categoryRepository.getReferenceById(product.getCategory().getId()));
            if (optionalCategory.isPresent()){
                productRepository.save(product);
                cacheManager.getCache("productList").clear();
                cacheManager.getCache("proByCat").clear();
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result,product);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }else{
                hashMap.put(REnum.status, false);
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status, false);
            hashMap.put(REnum.message, ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }


    }

    public ResponseEntity<Map<String ,Object>> update(Product product){
        Map<REnum,Object> hashMap = new LinkedHashMap<>();
        try{
            Optional<Product> optionalProduct = productRepository.findById(product.getId());
            if(optionalProduct.isPresent()){
                productRepository.saveAndFlush(product);
                hashMap.put(REnum.result, product);
                hashMap.put(REnum.status, true);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else{
                hashMap.put(REnum.status, false);
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            hashMap.put(REnum.status, false);
            hashMap.put(REnum.message, ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Map<REnum,Object>> delete(Long id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        try {
            productRepository.deleteById(id);
            hashMap.put(REnum.status,true);
            return new ResponseEntity<>(hashMap, HttpStatus.OK);

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> list(){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,productRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> search(String q){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        List<Product> productList=productRepository.findByProductNameContainsIgnoreCaseOrDetailContainsIgnoreCase(q,q);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,productList);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> productByCategory(Long id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        List<Product> productList=productRepository.findByCategory_IdEqualsOrderByProductNameAsc(id);
        if (productList.size()!=0){
            hashMap.put(REnum.status,true);
            hashMap.put(REnum.result,productList);
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }
        hashMap.put(REnum.status,false);
        hashMap.put(REnum.message,"There are no products in this category");
        return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);

    }


}
