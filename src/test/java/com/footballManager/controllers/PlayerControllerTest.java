package com.footballManager.controllers;

import com.footballManager.configurations.PlayerTestConfiguration;
import com.footballManager.dto.PlayerCreateUpdateDto;
import com.footballManager.dto.TransferDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;
import com.footballManager.services.impl.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {PlayerController.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {PlayerTestConfiguration.class})
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerServiceImpl playerService;

    private Player mockPlayer;
    private Team mockTeam;


    @BeforeEach
    void setUp(){
        mockTeam = Team.builder()
                .id(1L)
                .name("Bukovina")
                .commissionForTransfer((float) 0.1)
                .balance(BigDecimal.valueOf(2000000))
                .build();

        mockPlayer = Player.builder().fullName("Xsavie")
                .dateOfBirth(Date.valueOf("1999-12-12"))
                .startOfCareer(Date.valueOf("2019-12-02"))
                .team(mockTeam).build();
    }

    @Test
    void getPlayer() throws Exception {
        given(playerService.getPlayer(anyLong())).willReturn(mockPlayer);

        mockMvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockPlayer.toJSON()));
    }

    @Test
    void findByPage() throws Exception {
        Page<Player> page = Page.empty();
        given(playerService.findAllByPage(any())).willReturn(page);
        mockMvc.perform(get("/players?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    void findAll() throws Exception {
        given(playerService.findAll()).willReturn(List.of(mockPlayer));

        mockMvc.perform(get("/players/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("["+mockPlayer.toJSON()+"]"));
    }

    @Test
    void createPlayer() throws Exception {
        given(playerService.createPlayer(any())).willReturn(mockPlayer);

        PlayerCreateUpdateDto playerCreateUpdateDto = PlayerCreateUpdateDto.builder()
                .fullName(mockPlayer.getFullName())
                .startOfCareer(mockPlayer.getStartOfCareer())
                .dateOfBirth(mockPlayer.getDateOfBirth())
                .team(mockPlayer.getTeam().getId()).build();

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(playerCreateUpdateDto.toJSON()))
                .andExpect(status().isCreated())
                .andExpect(content().string(mockPlayer.toJSON()));
    }

    @Test
    void  updatePlayer() throws Exception {
        Player editedPlayer = Player.builder()
                .fullName("Anatoliy")
                .dateOfBirth(Date.valueOf("1918-04-04"))
                .startOfCareer(Date.valueOf("2020-02-02"))
                .team(mockTeam)
                .build();

        given(playerService.updatePlayer(any(),any())).willReturn(editedPlayer);

        PlayerCreateUpdateDto playerCreateUpdateDto = PlayerCreateUpdateDto.builder()
                .fullName("Anatoliy")
                .dateOfBirth(Date.valueOf("1918-04-04"))
                .startOfCareer(Date.valueOf("2020-02-02"))
                .team(Long.valueOf(1))
                .build();

        mockMvc.perform(put("/players/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(playerCreateUpdateDto.toJSON()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(editedPlayer.toJSON()));
    }

    @Test
    void deletePlayer() throws Exception {
        mockMvc.perform(delete("/players/1").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    void transfer() throws Exception {
        TransferDto transferDto = TransferDto.builder()
                .team(2L)
                .player(1L)
                .build();

        mockMvc.perform(post("/players/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(transferDto.toJSON()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void checkValidation() throws Exception{
        PlayerCreateUpdateDto incorrectDto = PlayerCreateUpdateDto.builder()
                .fullName(mockPlayer.getFullName())
                .team(mockPlayer.getTeam().getId())
                .dateOfBirth(mockPlayer.getDateOfBirth())
                .startOfCareer(Date.valueOf("2023-12-12")) // Date in future
                .build();

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(incorrectDto.toJSON()))
                .andExpect(status().is4xxClientError());
    }

}