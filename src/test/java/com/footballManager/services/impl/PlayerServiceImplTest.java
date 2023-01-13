package com.footballManager.services.impl;

import com.footballManager.dto.PlayerCreateUpdateDto;
import com.footballManager.dto.TransferDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;
import com.footballManager.exceptions.ApiValidationException;
import com.footballManager.exceptions.EntityNotFoundException;
import com.footballManager.repositories.PlayerRepository;
import com.footballManager.services.interfaces.TeamService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    private PlayerServiceImpl playerServiceImpl;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamServiceImpl teamService;

    private Player mockPlayer;
    private Team mockTeam;

    @BeforeEach
    void setUp(){
        playerServiceImpl = new PlayerServiceImpl(playerRepository, teamService);

        mockTeam = Team.builder()
                .id(Long.valueOf(1))
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
    void isAbleToFindAll() {
        given(playerRepository.findAll()).willReturn(List.of(mockPlayer));

        List<Player> players = new ArrayList<>();
        playerServiceImpl.findAll().iterator().forEachRemaining(players::add);

        verify(playerRepository, times(1)).findAll();
        assertThat(players.contains(mockPlayer)).isTrue();
    }

    @Test
    void findAllByPage() {
        Pageable pageable = PageRequest.of(1,10);
        playerServiceImpl.findAllByPage(pageable);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(playerRepository,times(1)).findAll(pageableArgumentCaptor.capture());
        Pageable result = pageableArgumentCaptor.getValue();

        assertThat(result).isEqualTo(pageable);
    }

    @Test
    void createPlayer() {
        //Mock data
        given(teamService.getTeam(anyLong())).willReturn(mockTeam);


        PlayerCreateUpdateDto playerDto = PlayerCreateUpdateDto.builder().fullName(mockPlayer.getFullName())
                .dateOfBirth(mockPlayer.getDateOfBirth())
                .startOfCareer(mockPlayer.getStartOfCareer())
                .team(1L).build();

        //Then
        playerServiceImpl.createPlayer(playerDto);

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository,times(1)).save(playerArgumentCaptor.capture());
        Player resultPlayer = playerArgumentCaptor.getValue();

        assertThat(resultPlayer).isEqualTo(mockPlayer);
    }

    @Test
    void getOnePlayer() {
        given(playerRepository.findById(anyLong())).willReturn(Optional.of(mockPlayer));
        Long id = 1L;
        playerServiceImpl.getPlayer(id);

        ArgumentCaptor<Long> resultId = ArgumentCaptor.forClass(Long.class);
        verify(playerRepository,times(1)).findById(resultId.capture());

        assertThat(resultId.getValue()).isEqualTo(id);
    }

    @Test
    void findNonExistingPlayer(){
        assertThatThrownBy(() -> playerServiceImpl.getPlayer(anyLong()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updatePlayer() {
        given(playerRepository.findById(anyLong())).willReturn(Optional.of(mockPlayer));
        given(teamService.getTeam(Long.valueOf(1))).willReturn(mockTeam);

        PlayerCreateUpdateDto playerCreateUpdateDto = PlayerCreateUpdateDto.builder()
                .fullName("Anatoliy")
                .dateOfBirth(Date.valueOf("1918-04-04"))
                .startOfCareer(Date.valueOf("2020-02-02"))
                .team(Long.valueOf(1))
                .build();

        Player expectedPlayer = Player.builder()
                .fullName("Anatoliy")
                .dateOfBirth(Date.valueOf("1918-04-04"))
                .startOfCareer(Date.valueOf("2020-02-02"))
                .team(mockTeam)
                .build();

        playerServiceImpl.updatePlayer(Long.valueOf(1), playerCreateUpdateDto);
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository,times(1)).save(playerArgumentCaptor.capture());

        assertThat(playerArgumentCaptor.getValue()).isEqualTo(expectedPlayer);
    }

    @Test
    void deletePlayer() {
        given(playerRepository.findById(1L)).willReturn(Optional.of(mockPlayer));

        playerServiceImpl.deletePlayer(1L);
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository,times(1)).delete(playerArgumentCaptor.capture());

        assertThat(playerArgumentCaptor.getValue()).isEqualTo(mockPlayer);
    }

    @Test
    void transferPlayer() {
        Team transferToTeam = Team.builder()
                .id(2L)
                .commissionForTransfer((float) 0.05)
                .balance(BigDecimal.valueOf(1000000))
                .name("Toporivtsi")
                .build();
        final BigDecimal balanceTransferToTeamAfter =BigDecimal.valueOf(839999);
        final BigDecimal balanceMockTeamAfter = BigDecimal.valueOf(2160001);
        given(teamService.getTeam(2L)).willReturn(transferToTeam);

        TransferDto transferDto = TransferDto.builder()
                .player(1L)
                .team(2L).build();

        given(playerRepository.findById(anyLong())).willReturn(Optional.of(mockPlayer));

        //Then
        playerServiceImpl.transferPlayer(transferDto);

        assertThat(transferToTeam.getBalance().setScale(0, RoundingMode.HALF_UP))
                .isEqualTo(balanceTransferToTeamAfter);

        assertThat(mockTeam.getBalance().setScale(0, RoundingMode.HALF_UP)).
                isEqualTo(balanceMockTeamAfter);

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository,times(1)).save(playerArgumentCaptor.capture());

        assertThat(playerArgumentCaptor.getValue().getTeam()).isEqualTo(transferToTeam);
    }

    @Test
    void transferToTheSameTeam(){
        given(teamService.getTeam(anyLong())).willReturn(mockTeam);
        given(playerRepository.findById(anyLong())).willReturn(Optional.of(mockPlayer));

        TransferDto transferDto = TransferDto.builder()
                .player(1L)
                .team(2L).build();

        verify(playerRepository,never()).save(any());
        assertThatThrownBy(() -> playerServiceImpl.transferPlayer(transferDto))
                .isInstanceOf(ApiValidationException.class).hasMessageContaining("Player is already in that team");
    }

    @Test
    void notEnoughToTransfer(){
        Team transferToTeam = Team.builder()
                .id(Long.valueOf(2))
                .commissionForTransfer((float) 0.05)
                .balance(BigDecimal.valueOf(1000))
                .name("Toporivtsi")
                .build();

        given(teamService.getTeam(2L)).willReturn(transferToTeam);
        given(playerRepository.findById(anyLong())).willReturn(Optional.of(mockPlayer));

        TransferDto transferDto = TransferDto.builder()
                .player(1L)
                .team(2L).build();

        verify(playerRepository,never()).save(any());

        assertThatThrownBy(() -> playerServiceImpl.transferPlayer(transferDto))
                .isInstanceOf(ArithmeticException.class).hasMessageContaining("Not enough money");
    }
}