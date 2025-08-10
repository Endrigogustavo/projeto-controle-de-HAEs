package br.com.fateczl.apihae.driver.config.cloudinary;

import org.springframework.beans.factory.annotation.Value;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${CLOUDINARY_NAME}")
    private String cloudinaryName;

    @Value("${CLOUDINARY_API_KEY}")
    private String cloudinaryApiKey;

    @Value("${CLOUDINARY_API_SECRET}")
    private String cloudinaryApiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudinaryName,
            "api_key", cloudinaryApiKey,
            "api_secret", cloudinaryApiSecret,
            "secure", true));
    }
}
