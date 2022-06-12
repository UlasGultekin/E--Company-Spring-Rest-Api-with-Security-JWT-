package com.herocompany.services;

import com.herocompany.configs.Configs;
import com.herocompany.entities.Admin;
import com.herocompany.entities.AdminSettings;
import com.herocompany.repositories.AdminRepository;
import com.herocompany.utils.REnum;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    final AdminRepository adminRepository;
    final HttpSession httpSession;
    final  UserDetailService userDetailService;
    final CacheManager cacheManager;
    final Configs configs;

    public AdminService(AdminRepository adminRepository, HttpSession httpSession, UserDetailService userDetailService, CacheManager cacheManager, Configs configs) {
        this.adminRepository = adminRepository;
        this.httpSession = httpSession;
        this.userDetailService = userDetailService;
        this.cacheManager = cacheManager;
        this.configs = configs;
    }

    public ResponseEntity<Map<REnum,Object>> save(Admin admin){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        Admin adm= adminRepository.save(admin);
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,admin);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> update(Admin admin){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Optional<Admin> optionalAdmin= adminRepository.findById(admin.getId());
            if (optionalAdmin.isPresent()){
                adminRepository.saveAndFlush(admin);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, admin);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Admin is null! try again");
                return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            hashMap.put(REnum.status,false);
            hashMap.put(REnum.message,ex.getMessage());
            return new  ResponseEntity(hashMap, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> delete(Integer id){
        Map<REnum,Object> hashMap =new LinkedHashMap<>();
        try {
            adminRepository.deleteById(id);
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
        cacheManager.getCache("adminList").clear();
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,adminRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> settings(AdminSettings adminSettingsAttr){
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        try {
            Admin admin= (Admin) httpSession.getAttribute("admin");
            Optional<Admin> optionalAdmin= adminRepository.findById(admin.getId());
            if (optionalAdmin.isPresent()){
                admin.setAdminName(adminSettingsAttr.getAdminName());
                admin.setAdminSurname(adminSettingsAttr.getAdminSurname());
                admin.setCompanyName(adminSettingsAttr.getCompanyName());
                admin.setEmail(adminSettingsAttr.getEmail());

                adminRepository.saveAndFlush(admin);
                hashMap.put(REnum.status,true);
                hashMap.put(REnum.result, admin);
                return new  ResponseEntity(hashMap, HttpStatus.OK);
            }else {
                hashMap.put(REnum.status,false);
                hashMap.put(REnum.message,"Admin is null! try again");
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
            Admin admin= (Admin) httpSession.getAttribute("admin");
            boolean result = userDetailService.encoder().matches(oldPwd, admin.getPassword());
            if (result){
                if (pwd.equals(confirmPwd)){
                    System.out.println("/********************");
                    String newPassword=userDetailService.encoder().encode(pwd);
                    admin.setPassword(newPassword);
                    adminRepository.saveAndFlush(admin);
                    hashMap.put(REnum.status,true);
                    hashMap.put(REnum.result, admin);
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


}
