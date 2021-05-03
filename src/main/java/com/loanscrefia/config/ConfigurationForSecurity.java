package com.loanscrefia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.loanscrefia.common.login.service.LoginService;
import com.loanscrefia.config.login.LoginFailurHandler;
import com.loanscrefia.config.login.LoginSuccessHandler;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ConfigurationForSecurity extends WebSecurityConfigurerAdapter {
    
	@Autowired private LoginService loginService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/static/plugin/**", "/static/css/**", "/static/js/**", "/static/img/**", "/static/lib/**", "/static/images/**","/favicon.ico", "/resources/**", "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	
    	// 권한체크/
		http.authorizeRequests()
			.antMatchers("/admin/**").hasAnyRole("ADMIN","SYSTEM")
			.antMatchers("/member/**").hasAnyRole("MEMBER","SYSTEM")
			.antMatchers("/system/**").hasAnyRole("SYSTEM")
			// .antMatchers("/bo/mem/boardList").permitAll()
			.antMatchers("/signup").permitAll()
			.antMatchers("/idcheck").permitAll()
			.antMatchers("/terms").permitAll()
			.antMatchers("/common/**").permitAll()
			.antMatchers("/signupTest").permitAll() 
			.antMatchers("/prevLogin").hasAnyRole("TEMP_MEMBER")
			.antMatchers("/**").hasAnyRole("ADMIN","SYSTEM","MEMBER")
			.anyRequest().authenticated();

		http.formLogin()
			.loginPage("/login").permitAll()
			.loginProcessingUrl("/j_spring_security")
			.usernameParameter("memberId")
			.passwordParameter("password")
			//.failureHandler(failurHandler())
			.successHandler(successHandler()).permitAll()
			.failureHandler(failurHandler()).permitAll();

		http.logout().permitAll()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true);
		
		http.exceptionHandling().accessDeniedPage("/denied");
		http.csrf().disable();
    }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService);
	}
	
	public AuthenticationSuccessHandler successHandler() {
		return new LoginSuccessHandler("/main");
	}
	
	@Bean
	public AuthenticationFailureHandler failurHandler() {
		return new LoginFailurHandler();
	}
}
