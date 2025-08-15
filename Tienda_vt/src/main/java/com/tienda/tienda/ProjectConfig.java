package com.tienda.tienda;

import com.tienda.domain.Ruta;
import com.tienda.service.RutaPermitService;
import com.tienda.service.RutaService;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    /* Los siguiente métodos son para implementar el tema de seguridad dentro del proyecto */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/ejemplo2").setViewName("ejemplo2");
        registry.addViewController("/multimedia").setViewName("multimedia");
        registry.addViewController("/iframes").setViewName("iframes");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registro").setViewName("/registro/nuevo");
    }

    /* El siguiente método se utilizar para publicar en la nube, independientemente  */
    @Bean
    public SpringResourceTemplateResolver templateResolver_0() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }


    @Autowired
    private RutaPermitService rutaPermitService;
    @Autowired
    private RutaService rutaService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] rutasPermit = rutaPermitService.getRutaPermitsString();
        List<Ruta> rutas = rutaService.getRutas();

        http.authorizeHttpRequests((request) -> {
            request.requestMatchers(rutasPermit).permitAll();

            for (Ruta ruta : rutas) {

                request.requestMatchers(ruta.getPatron()).hasRole(ruta.getRolName());
            }

        }
        )
                .formLogin((form) -> form
                .loginPage("/login").permitAll())
                .logout((logout) -> logout.permitAll());
        return http.build();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build)
            throws Exception {

        build.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

}
// a este tipo de datos se le dicen datos quemados
/* El siguiente método se utiliza para completar la clase no es 
    realmente funcional, la próxima semana se reemplaza con usuarios de BD 
    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("juan")
                .password("{noop}123")
                .roles("USER", "VENDEDOR", "ADMIN")
                .build();
        
        UserDetails admin2 = User.builder()
                .username("mateo")
                .password("{noop}369")
                .roles("USER", "VENDEDOR", "ADMIN")
                .build();
        
        UserDetails sales = User.builder()
                .username("rebeca")
                .password("{noop}456")
                .roles("USER", "VENDEDOR")
                .build();
        UserDetails user = User.builder()
                .username("pedro")
                .password("{noop}789")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user, sales, admin, admin2);
    }*/
 /* Los siguiente métodos son para implementar el tema de seguridad dentro del proyecto */
//@Override
//public void addViewControllers(ViewControllerRegistry registry) {
// registry.addViewController("/").setViewName("index");
//  registry.addViewController("/ejemplo2").setViewName("ejemplo2");
// }
/* El siguiente método se utilizar para publicar en la nube, independientemente  */
// @Bean
// public SpringResourceTemplateResolver templateResolver_0() {
//    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//    resolver.setPrefix("classpath:/templates");
//   resolver.setSuffix(".html");
//   resolver.setTemplateMode(TemplateMode.HTML);
//   resolver.setOrder(0);
//   resolver.setCheckExistence(true);
//  return resolver;
/* }*/