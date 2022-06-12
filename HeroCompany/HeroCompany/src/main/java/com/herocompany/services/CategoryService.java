package com.herocompany.services;

import com.herocompany.configs.Configs;
import com.herocompany.entities.Category;
import com.herocompany.repositories.CategoryRepository;
import com.herocompany.utils.REnum;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;
    final CacheManager cacheManager;
    final Configs configs;

    public CategoryService(CategoryRepository categoryRepository, CacheManager cacheManager, Configs configs) {
        this.categoryRepository = categoryRepository;
        this.cacheManager = cacheManager;
        this.configs = configs;
    }

    public ResponseEntity<Map<REnum,Object>> save(Category category){
//        HttpHeaders headers=new HttpHeaders();
//        headers.add("customData","ex");
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Category cat= categoryRepository.save(category);
        cacheManager.getCache("categoryList").clear();
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,category);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<String ,Object>> update(Category category){
        Map<REnum,Object> hashMap = new LinkedHashMap<>();
        try{
            Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
            if(optionalCategory.isPresent()){
                categoryRepository.saveAndFlush(category);
                hashMap.put(REnum.status, true);
                hashMap.put(REnum.result, category);
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
            categoryRepository.deleteById(id);
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
        hashMap.put(REnum.result,categoryRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }
}
