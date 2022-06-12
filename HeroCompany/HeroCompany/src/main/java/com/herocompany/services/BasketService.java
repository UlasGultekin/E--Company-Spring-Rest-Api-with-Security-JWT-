package com.herocompany.services;

import com.herocompany.entities.Basket;
import com.herocompany.entities.Customer;
import com.herocompany.entities.Product;
import com.herocompany.repositories.BasketRepository;
import com.herocompany.repositories.ProductRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BasketService {
    final BasketRepository basketRepository;
    final UserDetailService userDetailService;
    final HttpSession httpSession;
    final ProductRepository productRepository;

    public BasketService(BasketRepository basketRepository, UserDetailService userDetailService, HttpSession httpSession, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.userDetailService = userDetailService;
        this.httpSession = httpSession;
        this.productRepository = productRepository;
    }

    public ResponseEntity<Map<REnum,Object>> save(Basket basket){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Customer customer = (Customer) httpSession.getAttribute("customer");
        List<Basket> baskets=basketRepository.findByStatusIsTrueAndCustomer_EmailEqualsIgnoreCase(customer.getEmail());
        boolean isSameProduct=false;
        Long basketId= Long.valueOf(0);
        int oldQuantityBasket=0;

        Optional<Product> optionalProduct = productRepository.findById(basket.getProduct().getId());

        if (optionalProduct.isPresent()) {
            for(Basket basketItem:baskets) {
                if (basketItem.getProduct().getId() == basket.getProduct().getId()) {
                    isSameProduct=true;
                    basketId=basketItem.getId();
                    oldQuantityBasket=basketItem.getQuantity();
                    break;
                }
            }
            Product product=optionalProduct.get();
            Integer stockQuantity = product.getStock();
            Integer basketQuantity= basket.getQuantity();

            if (basketQuantity <= stockQuantity) {
                product.setStock(stockQuantity-basketQuantity);
                productRepository.save(product);
                basket.setProduct(product);
                if(isSameProduct){
                    basket.setId(basketId);
                    basket.setQuantity(basketQuantity+oldQuantityBasket);
                    basket.setCreatedBy(customer.getEmail());
                }
                basketRepository.save(basket);
                hashMap.put(REnum.status, true);
                hashMap.put(REnum.result, basket);
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            } else {
                hashMap.put(REnum.status, false);
                hashMap.put(REnum.message, "Not enough stock");
                return new ResponseEntity<>(hashMap, HttpStatus.OK);
            }


        } else {
            hashMap.put(REnum.status, false);
            hashMap.put(REnum.message, "There is not such a product");
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }

    }

    public ResponseEntity<Map<REnum,Object>> update(Basket basket){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Basket> optionalBasket= basketRepository.findById(basket.getId());
            if (optionalBasket.isPresent()){
                basketRepository.saveAndFlush(basket);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, basket);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Basket is null! try again");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Map<REnum,Object>> delete(Long id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        try {
            basketRepository.deleteById(id);
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
        hashMap.put(REnum.result,basketRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> customerBasket(String email){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        List<Basket> baskets=basketRepository.findByStatusIsTrueAndCustomer_EmailEqualsIgnoreCase(email);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,baskets);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

}
