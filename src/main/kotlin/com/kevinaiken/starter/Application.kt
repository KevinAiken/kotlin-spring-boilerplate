package com.kevinaiken.starter

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableCaching
@EnableScheduling
@SpringBootApplication
class Application : WebMvcConfigurer {

    // Disabled to smooth development. Should probably be enabled in production.
    override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600)
        super.addCorsMappings(registry)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
