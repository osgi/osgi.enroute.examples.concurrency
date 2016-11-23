package osgi.enroute.examples.concurrency.modified;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

@Component(service=DynamicModifiedConfiguration.class, configurationPolicy=ConfigurationPolicy.REQUIRE)
public class DynamicModifiedConfiguration {
	
	int		config;
	
	@interface Config {
		int config() default 1;
	}
	
	
	@Activate
	void activate(Config config) {
		this.config = config.config();
	}
	
	@Modified
	void modified(Config config) {
		this.config = config.config();
	}

}
