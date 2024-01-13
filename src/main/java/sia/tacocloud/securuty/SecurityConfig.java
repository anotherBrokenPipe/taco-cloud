package sia.tacocloud.securuty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import sia.tacocloud.TacoCloudUser;
import sia.tacocloud.data.UserRepository;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepo;

    public SecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/design", "/orders").access(
                                        new WebExpressionAuthorizationManager("hasRole('USER')")
                                )
                                .requestMatchers("/", "/**").access(
                                        new WebExpressionAuthorizationManager("permitAll")
                                )
        )
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/login")
                                .defaultSuccessUrl("/design", true)
                                .loginProcessingUrl("/authenticate")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return username -> {
            TacoCloudUser tacoCloudUser = userRepo.findByUsername(username);
            if (tacoCloudUser != null) return tacoCloudUser;

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

}
