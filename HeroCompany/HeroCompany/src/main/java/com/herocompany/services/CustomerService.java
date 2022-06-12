package com.herocompany.services;

import com.herocompany.entities.Customer;


import com.herocompany.entities.CustomerSettings;
import com.herocompany.repositories.CustomerRepository;
import com.herocompany.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CustomerService {

    final CustomerRepository customerRepository;
    final UserDetailService userDetailService;
    final HttpSession httpSession;

    public CustomerService(CustomerRepository customerRepository, UserDetailService userDetailService,
                           HttpSession httpSession) {
        this.customerRepository = customerRepository;
        this.userDetailService = userDetailService;
        this.httpSession = httpSession;
    }


    public ResponseEntity<Map<REnum,Object>> save(Customer customer){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Customer cus= customerRepository.save(customer);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,customer);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);

    }

    public ResponseEntity<Map<REnum,Object>> update(Customer customer){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Customer> optionalCustomer= customerRepository.findById(customer.getId());
            if (optionalCustomer.isPresent()){
                customerRepository.saveAndFlush(customer);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, customer);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Customer is null! try again");
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
            customerRepository.deleteById(id);
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
        hashMap.put(REnum.result,customerRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> settings(CustomerSettings customerSettingsAttr){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Customer customer= (Customer) httpSession.getAttribute("customer");
            Optional<Customer> optionalCustomer= customerRepository.findById(customer.getId());
            if (optionalCustomer.isPresent()){
                customer.setFirstName(customerSettingsAttr.getFirstName());
                customer.setLastName(customerSettingsAttr.getLastName());
                customer.setPhone(customerSettingsAttr.getPhone());
                customer.setEmail(customerSettingsAttr.getEmail());
                customerRepository.saveAndFlush(customer);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, customer);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Customer is null! try again");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> changePassword(String oldPwd,String pwd, String confirmPwd){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Customer customer= (Customer) httpSession.getAttribute("customer");
            boolean result = userDetailService.encoder().matches(oldPwd, customer.getPassword());
            if (result){
                if (pwd.equals(confirmPwd)){
                    System.out.println("/********************");
                    String newPassword=userDetailService.encoder().encode(pwd);
                    customer.setPassword(newPassword);
                    customerRepository.saveAndFlush(customer);
                    hashMap.put(REnum.status,true);
                    hashMap.put(REnum.result, customer);
                    return new  ResponseEntity(hashMap, HttpStatus.OK);
                }
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"new password is not equals new password confirm");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Old password is false");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }

    }

    public void updateResetPasswordToken(String token, String email) throws Exception {
        //kullanıcının emailni bulduk customerapassword tokenı kaydettik
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
           optionalCustomer.get().setPassword(token);
            customerRepository.save(optionalCustomer.get());
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }

    public Customer getByResetPasswordToken(String token) {
        //kaydettiğimiz tokenı çağpırdık
       Optional<Customer> optionalCustomer= customerRepository.findByResetPasswordToken(token);
        if (optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }
        return null;
    }

    public void updatePassword(Customer customer, String newPassword) {
        //yeni passwordu şifreledik.
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);

        customer.setResetPasswordToken(null);
        customerRepository.save(customer);
    }

}
