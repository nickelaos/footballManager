package com.footballManager.services.impl;

import com.footballManager.dto.TeamCreateUpdateDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;
import com.footballManager.exceptions.EntityNotFoundException;
import com.footballManager.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    private TeamServiceImpl teamServiceImpl;
    @Mock
    private TeamRepository teamRepository;

    private Player mockPlayer;
    private Team mockTeam;

    @BeforeEach
    void setUp(){
        teamServiceImpl = new TeamServiceImpl(teamRepository);

        mockTeam = Team.builder()
                .name("Bukovina")
                .commissionForTransfer((float) 0.1)
                .balance(BigDecimal.valueOf(2000000))
                .build();

        mockPlayer = Player.builder()
                .fullName("Xsavie")
                .dateOfBirth(Date.valueOf("1999-12-12"))
                .startOfCareer(Date.valueOf("2019-12-02"))
                .team(mockTeam).build();
    }

    @Test
    void getTeam() {
        given(teamRepository.findById(anyLong())).willReturn(Optional.of(mockTeam));
        Long id = 1L;
        teamServiceImpl.getTeam(id);

        ArgumentCaptor<Long> resultId = ArgumentCaptor.forClass(Long.class);
        verify(teamRepository,times(1)).findById(resultId.capture());

        assertThat(resultId.getValue()).isEqualTo(id);
    }

    @Test
    void findNonExistingTeam(){
        assertThatThrownBy(() -> teamServiceImpl.getTeam(anyLong()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getAllTeams() {
        given(teamRepository.findAll()).willReturn(List.of(mockTeam));

        List<Team> teams = new ArrayList<>();
        teamServiceImpl.getAllTeams().iterator().forEachRemaining(teams::add);

        verify(teamRepository, times(1)).findAll();
        assertThat(teams.contains(mockTeam)).isTrue();
    }

    @Test
    void createTeam() {
        TeamCreateUpdateDto teamCreateUpdateDto = TeamCreateUpdateDto.builder()
                .name(mockTeam.getName())
                .balance(mockTeam.getBalance())
                .commissionForTransfer(mockTeam.getCommissionForTransfer())
                .build();

        teamServiceImpl.createTeam(teamCreateUpdateDto);

        ArgumentCaptor<Team> teamArgumentCaptor = ArgumentCaptor.forClass(Team.class);
        verify(teamRepository, times(1)).save(teamArgumentCaptor.capture());
        assertThat(teamArgumentCaptor.getValue()).isEqualTo(mockTeam);
    }

    @Test
    void updateTeam() {
       given(teamRepository.findById(anyLong())).willReturn(Optional.of(mockTeam));
       Team expectedTeam = Team.builder().name("Liverpool")
               .commissionForTransfer((float) 0.02)
               .balance(BigDecimal.valueOf(1200000)).build();

       TeamCreateUpdateDto teamCreateUpdateDto = TeamCreateUpdateDto.builder()
                 .name("Liverpool")
               .commissionForTransfer((float) 0.02)
               .balance(BigDecimal.valueOf(1200000)).build();

       teamServiceImpl.updateTeam(teamCreateUpdateDto,1L);
       ArgumentCaptor<Team> teamArgumentCaptor = ArgumentCaptor.forClass(Team.class);
       verify(teamRepository, times(1)).save(teamArgumentCaptor.capture());

       assertThat(teamArgumentCaptor.getValue()).isEqualTo(expectedTeam);
    }

    @Test
    void deleteTeam() {
        given(teamRepository.findById(anyLong())).willReturn(Optional.of(mockTeam));

        teamServiceImpl.deleteTeam(1L);
        ArgumentCaptor<Team> teamArgumentCaptor = ArgumentCaptor.forClass(Team.class);
        verify(teamRepository,times(1)).delete(teamArgumentCaptor.capture());

        assertThat(teamArgumentCaptor.getValue()).isEqualTo(mockTeam);
    }
}