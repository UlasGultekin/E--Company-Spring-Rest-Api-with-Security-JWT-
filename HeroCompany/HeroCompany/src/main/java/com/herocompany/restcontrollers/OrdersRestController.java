package com.herocompany.restcontrollers;

import com.herocompany.entities.Orders;
import com.herocompany.services.OrdersService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrdersRestController {
    final OrdersService ordersService;

    public OrdersRestController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Orders orders){
        return ordersService.save(orders);
    }

    @Cacheable("orderList")
    @GetMapping("/list")
    public ResponseEntity list(){
        return ordersService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Orders orders){
        return ordersService.update(orders);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Long id){
        return  ordersService.delete(id);
    }
}
