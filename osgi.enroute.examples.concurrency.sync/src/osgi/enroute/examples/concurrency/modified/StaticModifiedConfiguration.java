package osgi.enroute.examples.concurrency.modified;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(service=StaticModifiedConfiguration.class, configurationPolicy=ConfigurationPolicy.REQUIRE)
public class StaticModifiedConfiguration {
	
	int		config;
	
	@interface Config {
		int config() default 1;
	}
	
	
	@Activate
	void activate(Config config) {
		this.config = config.config();
	}

}
