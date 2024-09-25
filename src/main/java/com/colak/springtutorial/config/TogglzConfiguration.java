package com.colak.springtutorial.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.user.NoOpUserProvider;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;
import org.togglz.redis.RedisStateRepository;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class TogglzConfiguration implements TogglzConfig {

    @Value("${togglz.max-connection-pool}")
    private int maxConnectionPool;
    @Value("${togglz.max-idle-connection}")
    private int maxIdleConnection;
    @Value("${togglz.min-idle-connection}")
    private int minIdleConnection;
    @Value("${togglz.max-wait}")
    private int maxWait;
    @Value("${togglz.username}")
    private String username;
    @Value("${togglz.feature-admin}")
    private boolean featureAdmin;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public FeatureManager featureManager() {
        return new FeatureManagerBuilder()
                .togglzConfig(this)
                .build();
    }

    @Override
    public Class<? extends Feature> getFeatureClass() {
        return ProductCheckFeature.class;
    }

    @Override
    public StateRepository getStateRepository() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxConnectionPool);   // Maximum number of connections in the pool
        poolConfig.setMaxIdle(maxIdleConnection);   // Maximum number of idle connections in the pool
        poolConfig.setMinIdle(minIdleConnection);   // Minimum number of idle connections in the pool
        poolConfig.setMaxWait(Duration.ofMillis(maxWait));   // Maximum time to wait for a connection from the pool

        // Togglz uses its own JedisPool
        JedisPool jedisPool = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getUsername(), redisProperties.getPassword());
        return new RedisStateRepository.Builder()
                .jedisPool(jedisPool)
                .build();
    }

    @Override
    public UserProvider getUserProvider() {
        return () -> new SimpleFeatureUser(username, featureAdmin);
    }
}
