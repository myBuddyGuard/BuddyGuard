package buddyguard.mybuddyguard.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"buddyguard.mybuddyguard.hospital",
        "buddyguard.mybuddyguard.login", "buddyguard.mybuddyguard.pet",
        "buddyguard.mybuddyguard.weight", "buddyguard.mybuddyguard.jwt"})
@EnableRedisRepositories(basePackages = "buddyguard.mybuddyguard.invitation.repository")
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(3))  // 3초동안 Redis 응답 없으면 timeout
                .shutdownTimeout(Duration.ZERO)         // 종료 시 기다림 없이 바로 종료
                .build();
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort),
                clientConfig);
    }
}
