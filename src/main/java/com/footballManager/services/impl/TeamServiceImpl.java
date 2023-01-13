package com.footballManager.services.impl;

import com.footballManager.dto.TeamCreateUpdateDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;
import com.footballManager.exceptions.EntityNotFoundException;
import com.footballManager.repositories.PlayerRepository;
import com.footballManager.repositories.TeamRepository;
import com.footballManager.services.interfaces.TeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team getTeam(Long id) throws EntityNotFoundException{
        return teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public Set<Player> getPlayersByTeam(Long id) {
        Team team = getTeam(id);
        return team.getPlayers();
    }

    @Override
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team createTeam(TeamCreateUpdateDto teamCreateUpdateDto) {
        Team team = Team.builder()
                .name(teamCreateUpdateDto.getName())
                .commissionForTransfer(teamCreateUpdateDto.getCommissionForTransfer())
                .balance(teamCreateUpdateDto.getBalance())
                .build();

        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(TeamCreateUpdateDto teamCreateUpdateDto, Long id) {
        Team team = getTeam(id);
        BeanUtils.copyProperties(teamCreateUpdateDto, team);

        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long id) {
        Team team = getTeam(id);
        teamRepository.delete(team);
    }
}
