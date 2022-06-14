package com.epam.todo.auth.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
//        RememberMeAuthenticationFilter
//        TokenBasedRememberMeServices
//        UsernamePasswordAuthenticationFiler
//        AbstractAuthenticationProcessingFilter
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化 persistent_logins（ddl在db目录下） 表；若存在，请注释掉这条语句，否则会报错。
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/token").permitAll()
                .antMatchers("/refreshToken").permitAll()
                .antMatchers("/logout1").permitAll()
                .anyRequest().authenticated()
//                .and().rememberMe().rememberMeServices(rememberMeServices())
//                .tokenValiditySeconds(10000000)
//                .userDetailsService(customUserDetailsService)
                .and()
                .csrf().disable();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices("remember-me",
                customUserDetailsService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(100000);
        return rememberMeServices;
    }

    /**
     * 配置基于内存的用户
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 提供自定义UserDetailsService
     * @return 自定义UserDetailsService
     * @throws Exception
     */
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
