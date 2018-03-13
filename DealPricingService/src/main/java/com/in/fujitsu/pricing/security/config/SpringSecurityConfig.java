package com.in.fujitsu.pricing.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.in.fujitsu.pricing.security.JwtAuthenticationEntryPoint;
import com.in.fujitsu.pricing.security.JwtAuthenticationProvider;
import com.in.fujitsu.pricing.security.JwtAuthenticationSuccessHandler;
import com.in.fujitsu.pricing.security.JwtAuthenticationTokenFilter;
import com.in.fujitsu.pricing.security.WebSecurityCorsFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private JwtAuthenticationProvider authenticationProvider;

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {

		return new ProviderManager(Arrays.asList(authenticationProvider));
	}

	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManager());
		authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
		return authenticationTokenFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// we don't need CSRF because our token is invulnerable
		http.csrf().disable();

		//login and registration will not have any security
		http.authorizeRequests().antMatchers("/login", "/register").permitAll()
		.antMatchers("/resources/admin/**").access("hasAuthority('Admin')");

		// All other urls must be authenticated (filter for token always fires (/**)
		http.authorizeRequests().anyRequest().authenticated();

		// Call our errorHandler if authentication/authorisation fails
		http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);

		// don't create session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Custom JWT based security filter
		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(new WebSecurityCorsFilter(), ChannelProcessingFilter.class);

		// disable page caching
		http.headers().cacheControl();
	}
}
