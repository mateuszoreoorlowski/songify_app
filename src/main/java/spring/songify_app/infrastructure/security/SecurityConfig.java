package spring.songify_app.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI zostaje udostępnione publicznie
                .requestMatchers("/swagger-resources/**").permitAll() // Swagger Resources zostaje udostępnione publicznie
                .requestMatchers("/v3/api-docs/**").permitAll() // Swagger API Docs zostaje udostępnione publicznie
                .requestMatchers("/users/register/**").permitAll() // Rejestracja użytkownika zostaje udostępniona publicznie
                .requestMatchers(HttpMethod.GET, "/songs/**").permitAll() // Pobieranie piosenek zostaje udostępnione publicznie
                .requestMatchers(HttpMethod.GET, "/artists/**").permitAll() // Pobieranie artystów zostaje udostępnione publicznie
                .requestMatchers(HttpMethod.GET, "/albums/**").permitAll() // Pobieranie albumów zostaje udostępnione publicznie
                .requestMatchers(HttpMethod.GET, "/genres/**").permitAll() // Pobieranie gatunków zostaje udostępnione publicznie
                .requestMatchers(HttpMethod.POST, "/songs/**").hasRole("ADMIN") // Poniższe żądania wymagają roli ADMIN
                .requestMatchers(HttpMethod.PATCH, "/songs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/songs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/songs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/artists/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/artists/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/artists/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/artists/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/albums/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/albums/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/genres/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                .anyRequest().authenticated() // Wymaga autoryzacji dla wszystkich żądań
        );
        return http.build();
    }
}