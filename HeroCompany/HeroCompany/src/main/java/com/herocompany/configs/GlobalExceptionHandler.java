package com.herocompany.configs;

import com.herocompany.utils.REnum;
import jdk.internal.org.objectweb.asm.commons.Method;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.bind.ValidationException;
import java.util.*;

@ControllerAdvice //bu olmalı globalexcp için
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {//progrmaın geneline indirgenen hataları yakalıcaz

//MethodArgumentNotValidException.class, bu hata ResponseEntityExceptionHandler içinde yok

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, TransactionSystemException.class, ValidationException.class} )
    protected ResponseEntity<Object> myFnc( Exception ex ) {
        //     super.handleBindException(ex, headers, status, request)   return super.handleBindException(ex, headers, status, request);  //ResponseEntityExceptionHandler bunun default hatasını ezdik.
        //extension jdknın framework içinde olmayan bir özellik varsa biz varmış gibi eklemek istersek eklerse extension deniyor buna

        if ( ex instanceof IllegalStateException ) {
            IllegalStateException il = (IllegalStateException) ex;
            System.out.println( il.getMessage() );
        }

        return null;
    }

//    @ExceptionHandler({MethodArgumentNotValidException.class}) //bad request hatalarında hep bu exception
//    public Map handler(MethodArgumentNotValidException ex){
//        Map<REnum,Object> hm = new LinkedHashMap<>();
//        List<FieldError> errors=ex.getFieldErrors(); //aynı anda birden fazla hata olabilir.
//        List< Map<String,String>> lss= new ArrayList<>();
//        for (FieldError item:errors){
//            Map<String,String> hmx=new HashMap<>();
//            String fieldName=item.getField();  //adını verir fieldın
//            String message= item.getDefaultMessage(); //mesajı veriri
////            System.out.println(fieldName+" "+message);
//
//            hmx.put(fieldName,message);
//            lss.add(hmx);
//
//        }
//        hm.put(REnum.status,false);
//        hm.put(REnum.error,lss);
//
//        return hm;
//    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleMethodArgumentNotValid(ex, headers, status, request);
        Map<REnum,Object> hm = new LinkedHashMap<>();
        List<FieldError> errors=ex.getFieldErrors(); //aynı anda birden fazla hata olabilir.
        List<Map<String,String>> lss= new ArrayList<>();
        for (FieldError item:errors){
            Map<String,String> hmx=new HashMap<>();
            String fieldName=item.getField();  //adını verir fieldın
            String message= item.getDefaultMessage(); //mesajı veriri
//            System.out.println(fieldName+" "+message);

            hmx.put(fieldName,message);
            lss.add(hmx);

        }
        hm.put(REnum.status,false);
        hm.put(REnum.error,lss);
        return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
    }
}
