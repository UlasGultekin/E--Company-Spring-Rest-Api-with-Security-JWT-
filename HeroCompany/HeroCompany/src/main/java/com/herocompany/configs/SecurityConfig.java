package com.herocompany.configs;

import com.herocompany.entities.Customer;
import com.herocompany.services.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final JwtFilter jwtFilter;
    final UserDetailService userDetailService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailService userDetailService) {
        this.jwtFilter = jwtFilter;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(userDetailService.encoder());

    }


    //rol ve yönetim
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests() //giriş rolleri ile çalış
                //.antMatchers("/customer/**").permitAll()
                //.antMatchers("/admin/**").permitAll()  //bunlar açık oldu. rol ve giriş şartı aramıyorz.
           /*     .antMatchers("/orders/**").hasRole("user")  //hangi servis hangi rolle çalışcak emrini veriyoruz.
               .antMatchers("/basket/**").hasRole("admin")   //giriş olac
                .antMatchers("/category/**").hasRole("customer")*/
                .antMatchers("/orders/**").permitAll()
                .antMatchers("/basket/**").permitAll()
                .antMatchers("/category/**").hasRole("admin")
                .antMatchers("/product/save").hasRole("admin")
                .antMatchers("/product/delete").hasRole("admin")
                .antMatchers("/product/update").hasRole("admin")
                .antMatchers("/product/list").permitAll()
                .antMatchers("/customer/register").permitAll()
                .antMatchers("/customer/delete").hasRole("admin")
                .antMatchers("/customer/update").permitAll()
                .antMatchers("/customer/list").hasRole("admin")
                .antMatchers("/admin/**").hasRole("admin")

                .and()       //tanımlar dışında config var onları da koy
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //jwt nin üreteceği sessionun oluşturulmasına izin veriyor

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Bean  //spring
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }






}
