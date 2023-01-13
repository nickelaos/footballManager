package com.footballManager.configurations;

import com.footballManager.services.impl.TeamServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TeamTestConfiguration {
    @Bean
    public TeamServiceImpl teamService(){
        return Mockito.mock(TeamServiceImpl.class);
    }
}
