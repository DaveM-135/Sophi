package com.sophi.app;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sophi.app.models.service.JpaUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurityConfig {
		
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/css/**","/js/**","/img/**","/scss/**","/vendor/**","/sendingEmail","/resetPassword","/newPassword**","/registroWebinar**","/fotoRecursoPerfil/**").permitAll()
//		.antMatchers("/**").hasAnyRole("USER")
//		.antMatchers("/listaClientes/**").hasAnyRole("ROLE_ADMIN","ROLE_LIDER")
//		.antMatchers("/agenda").hasAnyRole("ROLE_ADMIN","ROLE_LIDER")
//		.antMatchers("/listaProyectosTodo").hasAnyRole("ROLE_ADMIN")
//		.antMatchers("/listaMisProyectos/**").hasAnyRole("ROLE_LIDER")
//		.antMatchers("/misActividades/**").hasAnyRole("ROLE_LIDER","ROLE_USER")
//		.antMatchers("/aprobacionhoras/**").hasAnyRole("ROLE_LIDER","ROLE_ADMIN")
//		.antMatchers("/aprobaciongastos/**").hasAnyRole("ROLE_LIDER","ROLE_ADMIN")
		.anyRequest().authenticated().and().httpBasic()
		.and()
		.sessionManagement()
		.and()
			.formLogin()
				.loginPage("/login")
			.permitAll()
		.and()
			.logout().permitAll()
		.and()
			.exceptionHandling().accessDeniedPage("/accessDenied");

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder, JpaUserDetailsService userDetailsService) 
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	      .userDetailsService(userDetailsService)
	      .passwordEncoder(passwordEncoder)
	      .and()
	      .build();
	}
	
}
