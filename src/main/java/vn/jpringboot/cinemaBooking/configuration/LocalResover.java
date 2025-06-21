package vn.jpringboot.cinemaBooking.configuration;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class LocalResover extends AcceptHeaderLocaleResolver implements
        WebMvcConfigurer {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String languageHeader = request.getHeader("Accept-Language");
        return StringUtils.hasLength(languageHeader)
                ? Locale.lookup(Locale.LanguageRange.parse(languageHeader),
                        List.of(new Locale("en", "EN"), new Locale("fr", "FR")))
                : Locale.getDefault();
    }

    @Bean
    public ResourceBundleMessageSource bundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }
}