package com.sapam.inventario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("12345"))
            .roles("ADMIN")
            .build();

        UserDetails usuario = User.builder()
            .username("usuario")
            .password(passwordEncoder().encode("12345"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, usuario);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }

    // 🟢 NUEVO: Verificar usuarios al iniciar
    @Bean
    public CommandLineRunner testUsers(UserDetailsService userDetailsService) {
        return args -> {
            System.out.println("=== VERIFICANDO USUARIOS CARGADOS ===");
            try {
                System.out.println("Admin: " + userDetailsService.loadUserByUsername("admin"));
                System.out.println("Usuario: " + userDetailsService.loadUserByUsername("usuario"));
                System.out.println("✅ Usuarios cargados correctamente");
            } catch (Exception e) {
                System.out.println("❌ Error cargando usuarios: " + e.getMessage());
            }
        };
    }
}