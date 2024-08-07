package spring.songify_app.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import spring.songify_app.domain.usercrud.UserRepository;

@Configuration
class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsService(UserRepository userRepository){
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable()); // CSRF zostaje wyłączone
        http.formLogin(Customizer.withDefaults()); // Formularz logowania zostaje włączony
        http.httpBasic(Customizer.withDefaults()); // HTTP Basic zostaje włączony
        return http.build();
    }
}