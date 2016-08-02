package com.example.chirp.app;

import com.example.chirp.app.resources.ResourceTestSupport;
import com.example.chirp.app.stores.InMemoryUserStore;
import com.example.chirp.app.stores.UserStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile("test")    // This is only for the TEST profile
@Configuration      // Equivalent of the xml config.
public class TestSpringConfig {

    /**
     * AutoConfigs the fileExtensionMap that is used in the file FileExtensionRequestFilter
     * Maps to the {@code @Value(#{fileExtensionMap})} annotation in that class.
     * The value in {@code @Value} maps to the below {@code @Bean(name = "fileExtensionMap")}
     */
    @Bean(name = "fileExtensionMap")
    public Map<String, String> createFileExtensionMap() {
        Map<String, String> map = new HashMap<>();
        map.put(".txt", "text/plain");
        map.put(".xml", "application/xml");
        map.put(".json", "application/json");
        map.put(".avi", "application/avi"); // avi only used for testing
        return map;
    }

    @Bean
    public UserStore whatever(){
        UserStore userStore = new InMemoryUserStore(true);
        // Hack below to set the static reference to the user store.
        ResourceTestSupport.userStore = userStore;
        return userStore;
    }
}
