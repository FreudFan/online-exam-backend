package edu.sandau.config;

import edu.sandau.security.RequestFilter;
import edu.sandau.security.ResponseFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.AcceptHeaderApiListingResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JerseyConfig extends ResourceConfig {

    @Value("${spring.jersey.application-path}")
    private String apiPath;

    public JerseyConfig() {
        this.packages("edu.sandau.rest");
        this.register(MultiPartFeature.class);

        this.register(RequestFilter.class);
        this.register(ResponseFilter.class);
    }

    @PostConstruct
    public void init() {
        // Register components where DI is needed
        this.configureSwagger();
    }

    private void configureSwagger() {
        // Available at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(AcceptHeaderApiListingResource.class);
        this.register(SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setTitle("智云宝盒API文档");
        config.setVersion("v1.0");
        config.setContact("fmy");
        config.setBasePath(this.apiPath);
        config.setResourcePackage("edu.sandau");
        config.setPrettyPrint(true);
        config.setScan(true);
    }

}
