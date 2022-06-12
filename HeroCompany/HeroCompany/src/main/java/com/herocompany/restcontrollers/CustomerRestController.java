package com.herocompany.restcontrollers;

import com.herocompany.configs.JwtUtil;
import com.herocompany.entities.Customer;
import com.herocompany.entities.CustomerSettings;
import com.herocompany.services.CustomerService;
import com.herocompany.services.UserDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    final CustomerService customerService;
    final UserDetailService userDetailService;
    final JwtUtil jwtUtil;

    public CustomerRestController(CustomerService customerService, UserDetailService userDetailService, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }


    //nesneyi kabul eden bir json dosyası alamız biz bu nesneye dönüştürmemiz lazım.

    @PostMapping("/register") //save işlemi
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return userDetailService.registerCustomer(customer);
    }


    @GetMapping("/list")
    public ResponseEntity list(){
        return customerService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Customer customer){
        return customerService.update(customer);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Long id){
        return  customerService.delete(id);
    }

    @PutMapping("/settings")
    public ResponseEntity settings(@Valid @RequestBody CustomerSettings customerSettingsAttr){
        return customerService.settings(customerSettingsAttr);
    }
    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPwd,@RequestParam  String newPwd,
                                         @RequestParam String newPwdConf){
        return customerService.changePassword(oldPwd,newPwd,newPwdConf);
    }

}
