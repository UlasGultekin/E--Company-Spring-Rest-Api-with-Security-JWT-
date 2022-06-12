package com.herocompany.services;

import com.herocompany.repositories.AdminRepository;
import com.herocompany.configs.JwtUtil;

import com.herocompany.entities.Login;
import com.herocompany.entities.Role;

import com.herocompany.repositories.CustomerRepository;
import com.herocompany.entities.Admin;
import com.herocompany.entities.Customer;
import com.herocompany.utils.REnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Transactional
public class UserDetailService  implements UserDetailsService {

    final CustomerRepository customerRepository;
    final AdminRepository adminRepository;
    final AuthenticationManager authenticationManager; //spring securitye haber vermek için ara sınıf kullanolacak
    final JwtUtil jwtUtil;
    final HttpSession httpSession;

    //@Lazy yorgun yükleme manasındadır. Bu ifadeye göre içiçe çağrılmış injecte nesnelerinin circle a girmesini engeller.Sonsuz döngüye girmesini engeller.
    public UserDetailService(CustomerRepository customerRepository, AdminRepository adminRepository, @Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil, HttpSession httpSession) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.httpSession = httpSession;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(username);
        Optional<Customer> optionalCustomer=customerRepository.findByEmailEqualsIgnoreCase(username);
        if (optionalAdmin.isPresent() && !optionalCustomer.isPresent()){
            Admin admin= optionalAdmin.get();
            UserDetails userDetails=new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.isEnabled(),
                    admin.isTokenExpired(),
                    true,
                    true,
                    roles(admin.getRole())
            );
            httpSession.setAttribute("admin",admin);
            return userDetails;
        }else if(optionalCustomer.isPresent()&& !optionalAdmin.isPresent()){
            Customer customer= optionalCustomer.get();
            UserDetails userDetails=new org.springframework.security.core.userdetails.User(
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.isEnabled(),
                    customer.isTokenExpired(),
                    true,
                    true,
                    roles(customer.getRole())
            );
            httpSession.setAttribute("customer",customer);
            return userDetails;
        }
        else {
            throw new UsernameNotFoundException("User not found"); //403 gibi bir hata
        }
    }


    public Collection roles(Role role ) {
        List<GrantedAuthority> ls = new ArrayList<>();
        ls.add( new SimpleGrantedAuthority( role.getName()));
        return ls;
    }

    public ResponseEntity registerAdmin(Admin admin){
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(admin.getEmail());
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        if (!optionalAdmin.isPresent()){
            admin.setPassword(encoder().encode(admin.getPassword()));
            Admin adm=adminRepository.save(admin);
            hashMap.put(REnum.status,true);
            hashMap.put(REnum.result,adm);
            return new ResponseEntity(hashMap, HttpStatus.OK);
        }else{
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,"This email "+admin.getEmail() +" has been received");
            hashMap.put(REnum.result,admin);
            return new ResponseEntity(hashMap, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity registerCustomer(Customer customer){
        Optional<Customer> optionalCustomer=customerRepository.findByEmailEqualsIgnoreCase(customer.getEmail());
        Map<REnum,Object> hm= new LinkedHashMap<>();
        if (!optionalCustomer.isPresent()){
            customer.setPassword(encoder().encode(customer.getPassword()));
            Customer cus=customerRepository.save(customer);
            hm.put(REnum.status,true);
            hm.put(REnum.result,cus);
            return new ResponseEntity(hm, HttpStatus.OK);
        }else{
            hm.put(REnum.status,false);
            hm.put(REnum.message,"This email "+customer.getEmail() +" has been received");
            hm.put(REnum.result,customer);
            return new ResponseEntity(hm, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    //auth
    //jwt almak için login işlemi yaparak bu fonk tetiklenmelidir.
    public  ResponseEntity login (Login login){
        Map<REnum,Object> hashMap = new LinkedHashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getUsername(),login.getPassword()));
            UserDetails userDetails=loadUserByUsername(login.getUsername());
            String jwt= jwtUtil.generateToken(userDetails);
            hashMap.put(REnum.status,true);
            hashMap.put(REnum.jwt,jwt);
            return new ResponseEntity(hashMap,HttpStatus.OK);
        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.error,ex.getMessage());
            return new ResponseEntity(hashMap,HttpStatus.NOT_ACCEPTABLE);
        }


    }

    //get admin in ContextHolder.
    public Admin infoAdmin(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        System.out.println(username);
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(username);
        if (optionalAdmin.isPresent()){
            return optionalAdmin.get();
        }
        return null;
    }

    public Customer infoCustomer(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        System.out.println(username);
        Optional<Customer> optionalCustomer=customerRepository.findByEmailEqualsIgnoreCase(username);
        if (optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }
        return null;
    }
}
