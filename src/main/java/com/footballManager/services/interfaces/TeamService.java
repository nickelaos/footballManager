package com.footballManager.services.interfaces;

import com.footballManager.dto.TeamCreateUpdateDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;

import java.util.Set;

public interface TeamService {

    public Set<Player> getPlayersByTeam(Long id) ;

    Iterable<Team> getAllTeams();

    Team createTeam(TeamCreateUpdateDto teamCreateUpdateDto);

    Team updateTeam(TeamCreateUpdateDto teamCreateUpdateDto, Long id);

    void deleteTeam(Long id);

    Team getTeam(Long id);
}
