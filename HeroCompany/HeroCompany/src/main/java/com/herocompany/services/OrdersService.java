package com.herocompany.services;

import com.herocompany.configs.Configs;
import com.herocompany.entities.Basket;
import com.herocompany.entities.Customer;
import com.herocompany.entities.Orders;
import com.herocompany.repositories.BasketRepository;
import com.herocompany.repositories.OrdersRepository;
import com.herocompany.utils.REnum;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrdersService {

    final OrdersRepository ordersRepository;
    final BasketRepository basketRepository;
    final HttpSession httpSession;
    final CacheManager cacheManager;
    final Configs configs;

    public OrdersService(OrdersRepository ordersRepository, BasketRepository basketRepository, HttpSession httpSession, CacheManager cacheManager, Configs configs) {
        this.ordersRepository = ordersRepository;
        this.basketRepository = basketRepository;
        this.httpSession = httpSession;
        this.cacheManager = cacheManager;
        this.configs = configs;
    }

    public ResponseEntity<Map<REnum,Object>> save(Orders orders){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();

        int sum=0;
        Customer customer = (Customer) httpSession.getAttribute("customer");
        List<Basket> baskets = basketRepository.findByCustomer_EmailEqualsIgnoreCaseAndStatusFalse(customer.getEmail()); //orders.getCustomer().getEmail()
        if(baskets.size()>0){  //basketi varsa
//            orders.setCustomer(baskets.get(0).getCustomer()); //order customerına eşitle
            orders.setBaskets(baskets); //basketini set et ordersa
            for (Basket item : baskets) {
                sum = sum+item.getProduct().getPrice()*item.getQuantity();
                Optional<Basket> optionalBasket =basketRepository.findById(item.getId());
                optionalBasket.get().setStatus(true);
                basketRepository.saveAndFlush(optionalBasket.get());
            }
            orders.setTotal(sum);
            ordersRepository.save(orders);
            cacheManager.getCache("orderList").clear();
            hashMap.put(REnum.status,true);
            hashMap.put(REnum.result,orders);
            return new ResponseEntity<>(hashMap,HttpStatus.OK);}

        else {
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,"Basket is empty");
            return new ResponseEntity<>(hashMap,HttpStatus.NOT_ACCEPTABLE);
        }


    }

    public ResponseEntity<Map<REnum,Object>> update(Orders orders){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Orders> optionalOrders= ordersRepository.findById(orders.getId());
            if (optionalOrders.isPresent()){
                ordersRepository.saveAndFlush(orders);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, orders);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"orders is null! try again");
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
            ordersRepository.deleteById(id);
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
        hashMap.put(REnum.result,ordersRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }
}
