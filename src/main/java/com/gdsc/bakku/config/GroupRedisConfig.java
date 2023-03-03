package com.gdsc.bakku.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class GroupRedisConfig extends RedisConfig {

    @Bean
    @Primary
    public RedisConnectionFactory groupRedisConnectionFactory() {
        return createLettuceConnectionFactory(0);
    }

    @Bean
    @Qualifier("groupRedisTemplate")
    public RedisTemplate<String, Object> groupRedisTemplate() {
        RedisTemplate<String, Object>  template = new RedisTemplate<>();
        template.setConnectionFactory(groupRedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "groupStringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(groupRedisConnectionFactory());
        return stringRedisTemplate;
    }
}
