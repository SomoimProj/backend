package com.oinzo.somoim.config;

import com.oinzo.somoim.config.security.JwtExceptionHandlerFilter;
import com.oinzo.somoim.config.security.JwtAuthenticationEntryPoint;
import com.oinzo.somoim.config.security.JwtAuthenticationFilter;
import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.config.security.JwtAccessDeniedHandler;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.management.MXBean;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final JwtProvider jwtProvider;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.cors().configurationSource(corsConfigurationSource())

			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.authorizeRequests()
			.antMatchers("/users/oauth/**", "/users/auth/**", "/upload-url").permitAll()
			.anyRequest().hasRole("USER")

			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
			.accessDeniedHandler(new JwtAccessDeniedHandler())

			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		// preflighted request를 보낼 때 OPTIONS로 보내므로 API에서 안쓰더라도 꼭 추가해주어야 함
		configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE","PATCH"));
		configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CACHE_CONTROL, HttpHeaders.CONTENT_TYPE, HttpHeaders.SET_COOKIE));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);	// 3600초 동안 preflighted request 결과를 캐시에 저장
		configuration.setExposedHeaders(List.of(HttpHeaders.SET_COOKIE));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
