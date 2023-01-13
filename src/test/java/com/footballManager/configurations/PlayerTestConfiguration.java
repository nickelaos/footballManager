package com.footballManager.configurations;

import com.footballManager.repositories.PlayerRepository;
import com.footballManager.services.impl.PlayerServiceImpl;
import com.footballManager.services.interfaces.PlayerService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PlayerTestConfiguration {
    @Bean
    public PlayerRepository playerRepository(){
        return Mockito.mock(PlayerRepository.class);
    }
    @Bean
    public PlayerServiceImpl playerService(){
        return Mockito.mock(PlayerServiceImpl.class);
    }

}
