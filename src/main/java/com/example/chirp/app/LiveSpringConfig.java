package com.example.chirp.app;

import com.example.chirp.app.springdata.SpringDataUserStore;
import com.example.chirp.app.springdata.UserRepository;
import com.example.chirp.app.stores.InMemoryUserStore;
import com.example.chirp.app.stores.UserStore;
import com.example.chirp.app.stores.UserStoreUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile("live")    // This is only for the LIVE profile
@Configuration      // Equivalent of the xml config.
public class LiveSpringConfig {

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
        return map;
    }

    /**
     * Injects to all UserStore attributes that has been autowired.
     * The type of the attribute is matched to the return type here.
     * When there are more than one @Beans in this file with the same return type, give the mean a "name" value.
        @Bean
        public UserStore whatever(){
            return new InMemoryUserStore(true);
        }
     */

    /**
     * Spring creates the UserRepository object and injects it into this method.
     */
    @Bean
    public UserStore whatever(UserRepository userRepository){
        UserStore userStore = new SpringDataUserStore(userRepository);
        UserStoreUtils.resetAndSeedRepository(userStore);
        return userStore;
    }
}
