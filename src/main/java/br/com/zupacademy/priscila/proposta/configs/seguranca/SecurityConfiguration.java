package br.com.zupacademy.priscila.proposta.configs.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .antMatchers(HttpMethod.GET, "/propostas/**").hasAuthority("SCOPE_propostas")
                        .antMatchers(HttpMethod.GET, "/biometrias/**").hasAuthority("SCOPE_propostas")
                        .antMatchers(HttpMethod.POST, "/propostas/**").hasAuthority("SCOPE_propostas")
                        .antMatchers(HttpMethod.POST, "/biometrias/**").hasAuthority("SCOPE_propostas")
                        .antMatchers(HttpMethod.POST, "/bloqueios/**").hasAuthority("SCOPE_propostas")
                        .antMatchers(HttpMethod.POST, "/carteiras/**").hasAuthority("SCOPE_propostas")
                        .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                        .anyRequest().authenticated()
        )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    // Para usar ROLES ao inves de SCOPE:

//    @Override
//    protected void configure2(HttpSecurity http) throws Exception {
//        http.cors()
//            .and()
//                .authorizeRequests()
//                        .antMatchers(HttpMethod.GET, "/propostas/**")
//                        .hasAuthority("ROLE_USER")
//                        .anyRequest().authenticated()
//                        .and().oauth2ResourceServer()
//                        .jwt().jwtAuthenticationConverter(getJwtAuthenticationConverter());
//
//    }
//
//    JwtAuthenticationConverter getJwtAuthenticationConverter(){
//        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//        converter.setAuthoritiesClaimName("authorities");
//        converter.setAuthorityPrefix("");
//        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
//        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
//        return authenticationConverter;
//    }

}





