package com.footballManager.controllers;

import com.footballManager.configurations.PlayerTestConfiguration;
import com.footballManager.configurations.TeamTestConfiguration;
import com.footballManager.dto.TeamCreateUpdateDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;
import com.footballManager.services.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {TeamController.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {TeamTestConfiguration.class})
class TeamControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamServiceImpl teamService;
    private Team mockTeam;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockTeam = Team.builder()
                .id(1L)
                .name("Bukovina")
                .commissionForTransfer((float) 0.09)
                .balance(BigDecimal.valueOf(2000000))
                .build();

        mockPlayer = Player.builder().fullName("Xsavie")
                .dateOfBirth(Date.valueOf("1999-12-12"))
                .startOfCareer(Date.valueOf("2019-12-02"))
                .team(mockTeam).build();
    }

    @Test
    void getPlayersByTeam() throws Exception {
        Set<Player> players =new HashSet<>();
        players.add(mockPlayer);
        given(teamService.getPlayersByTeam(anyLong())).willReturn(players);

        mockMvc.perform(get("/teams/1/players"))
                .andExpect(status().isOk())
                .andExpect(content().string("["+mockPlayer.toJSON()+"]"));
    }

    @Test
    void getTeam() throws Exception {
        given(teamService.getTeam(anyLong())).willReturn(mockTeam);

        mockMvc.perform(get("/teams/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(mockTeam.toJSON()));
    }

    @Test
    void createTeam() throws Exception {
        given(teamService.createTeam(any())).willReturn(mockTeam);

        TeamCreateUpdateDto teamCreateUpdateDto = TeamCreateUpdateDto.builder()
                .name(mockTeam.getName())
                .balance(mockTeam.getBalance())
                .commissionForTransfer(mockTeam.getCommissionForTransfer())
                .build();

        mockMvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(teamCreateUpdateDto.toJSON()))
                .andExpect(status().isCreated())
                .andExpect(content().string(mockTeam.toJSON()));
    }

    @Test
    void updateTeam() throws Exception {
        Team editedTeam = Team.builder().id(1L)
                .name("Liverpool")
                .commissionForTransfer((float) 0.02)
                .balance(BigDecimal.valueOf(1200000)).build();

        given(teamService.updateTeam(any(),anyLong())).willReturn(editedTeam);

        TeamCreateUpdateDto teamCreateUpdateDto = TeamCreateUpdateDto.builder()
                .name("Liverpool")
                .commissionForTransfer((float) 0.02)
                .balance(BigDecimal.valueOf(1200000)).build();

        mockMvc.perform(put("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(teamCreateUpdateDto.toJSON()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(editedTeam.toJSON()));
    }

    @Test
    void deleteTeam() throws Exception {
        mockMvc.perform(delete("/teams/1").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    void teamCheckValidation() throws Exception{
        given(teamService.updateTeam(any(),anyLong())).willReturn(mockTeam);
        TeamCreateUpdateDto incorrectDto = TeamCreateUpdateDto.builder()
                .name(mockTeam.getName())
                .balance(mockTeam.getBalance())
                .commissionForTransfer((float) 0.5) // max 0.1
                .build();

        mockMvc.perform(put("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(incorrectDto.toJSON()))
                .andExpect(status().is4xxClientError());
    }
}