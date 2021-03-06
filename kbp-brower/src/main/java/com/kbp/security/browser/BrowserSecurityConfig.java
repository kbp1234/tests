package com.kbp.security.browser;

import com.kbp.core.mobile.SmsCodeSecurityConfig;
import com.kbp.core.properties.SecurityProperties;
import com.kbp.core.validate.code.ValidateCodeSecurityConfig;
import com.kbp.security.browser.authentication.FailHandler;
import com.kbp.security.browser.authentication.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Administrator on 2018/9/28.
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private FailHandler failHandler;

    @Autowired
    private SuccessHandler successHandler;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeSecurityConfig smsCodeSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

               http.apply(validateCodeSecurityConfig)
                      .and()
                    .apply(smsCodeSecurityConfig) ;

               http.formLogin()
                       .loginPage("/auth/require")
                        .loginProcessingUrl("/auth/form")
                       .successHandler(successHandler)
                       .failureHandler(failHandler)
                .and()
                   .authorizeRequests()
                   .antMatchers("/auth/require","/code/*",
                          securityProperties.getBrowser().getLoginPage()).permitAll()
                   .anyRequest()
                   .authenticated()
                .and()
                   .csrf().disable();

    }


}
