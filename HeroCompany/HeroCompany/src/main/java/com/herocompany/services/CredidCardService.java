package com.herocompany.services;

import com.herocompany.configs.Configs;
import com.herocompany.entities.Category;
import com.herocompany.entities.CreditCard;
import com.herocompany.repositories.CreditCardRepository;
import com.herocompany.utils.REnum;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CredidCardService {

    final CreditCardRepository cardRepository;
    final CacheManager cacheManager;
    final Configs configs;


    public CredidCardService(CreditCardRepository cardRepository, CacheManager cacheManager, Configs configs) {
        this.cardRepository = cardRepository;
        this.cacheManager = cacheManager;
        this.configs = configs;
    }
    public ResponseEntity<Map<REnum,Object>> save(CreditCard creditCard){
//        HttpHeaders headers=new HttpHeaders();
//        headers.add("customData","ex");
        Map<REnum,Object> hashMap= new LinkedHashMap<>();
        CreditCard card= cardRepository.save(creditCard);
        cacheManager.getCache("CreditList").clear();
        hashMap.put(REnum.status,true);
        hashMap.put(REnum.result,creditCard);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }
    public ResponseEntity<Map<String ,Object>> update(CreditCard creditCard){
        Map<REnum,Object> hashMap = new LinkedHashMap<>();
        try{
            Optional<CreditCard> optionalCreditCard = cardRepository.findById(creditCard.getId());
            if(optionalCreditCard.isPresent()){
                cardRepository.saveAndFlush(creditCard);
                hashMap.put(REnum.status, true);
                hashMap.put(REnum.result, creditCard);
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
            cardRepository.deleteById(id);
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
        hashMap.put(REnum.result,cardRepository.findAll());
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }




}
