package pl.lepi.springcloudpluralsight.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix="some")
@Getter
@Setter
public class ConfigClientConfiguration {
    private String property;
}
