//package de.sonnmatt.muutos;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//	
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("smu").password("smu").roles("ADMIN");
//	}
//	
//	
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//		.antMatchers("/muutos.jsp", "/Muutos-Server/**", "/user").authenticated()
//		//.and().formLogin().loginPage(loginPage)
//		.and().formLogin().defaultSuccessUrl("/muutos.jsp")
//		.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
//		.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//	}
//
//}
