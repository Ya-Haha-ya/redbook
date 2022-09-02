package com.xiazhao.redbook.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonConfig {
    @Bean
    fun modelMapper() = ModelMapper()
}
