package com.kevin.dinofly.config;


import com.kevin.dinofly.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //由于使用的是JWT，这里不需要csrf防护
        httpSecurity.csrf(CsrfConfigurer::disable)
                //基于token，所以不需要session
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationRegistry -> authorizationRegistry
                        // 允许对于网站静态资源的无授权访问
                        .requestMatchers(HttpMethod.GET, "/", "/*.html").permitAll()
                        // 对登录注册允许匿名访问
                        .requestMatchers("/login", "/register","/chat/**","/code", "/test/**").permitAll()
                        // 对于 admin 相关的路径，只有角色为 Admin 的用户能够访问
                        .requestMatchers("/admin/**").hasRole("Admin")
                        // 对于删除广告的请求，要求角色为 Landlord 或 Admin 的用户能够访问
                        .requestMatchers(HttpMethod.DELETE, "/ads/**").hasAnyRole("Landlord", "Admin")
                        // 对于添加和修改广告的请求，要求角色为 Landlord 的用户能够访问
                        .requestMatchers(HttpMethod.POST, "/ads/**").hasRole("Landlord")
                        .requestMatchers(HttpMethod.PUT, "/ads/**").hasRole("Landlord")
                        // 除上面外的所有请求全部需要鉴权认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
