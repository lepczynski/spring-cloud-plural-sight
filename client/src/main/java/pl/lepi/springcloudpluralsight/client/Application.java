package pl.lepi.springcloudpluralsight.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class Application {

	// Netflix Eureka disco client
	@Inject
	EurekaClient eurekaClient;

	// for config from Spring Cloud Config Server
	@Autowired
	private ConfigClientConfiguration properties;
	@Value("${some.other.property}")
	private String someOtherProperty;

//	// Spring discovery client instead of Eureka
//	@Inject
//	DiscoveryClient springClient;
//	List<ServiceInstance> springInstances = springClient.getInstances("service2b");
//	String baseUrl2 = springInstances.get(0).getUri().toString();

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/config")
	public String printConfig() {
		StringBuilder sb = new StringBuilder();
		sb.append(properties.getProperty());
		sb.append(" || ");
		sb.append(someOtherProperty);

		return sb.toString();
	}

	@RequestMapping("/")
	public String callService() {
		RestTemplate restTemplate = restTemplateBuilder.build();
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("data.service", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		ResponseEntity<String> response =
				restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class);
		return response.getBody();
	}
}
