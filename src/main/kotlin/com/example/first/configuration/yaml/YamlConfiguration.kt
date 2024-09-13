import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
class YamlConfiguration {
    val name: String? = null
    val url: String? = null
    val driverClassName: String? = null
    val username: String? = null
    val password: String? = null
}