package com.footballManager.controllers;

import com.footballManager.dto.TeamCreateUpdateDto;
import com.footballManager.entities.Player;
import com.footballManager.entities.Team;
import com.footballManager.services.interfaces.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("api/teams")
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping("/{id}/players")
    public Set<Player> getPlayersByTeam(@PathVariable Long id){
      return teamService.getPlayersByTeam(id);
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id){
        return teamService.getTeam(id);
    }

    @GetMapping
    public Iterable<Team> getAllTeams(){
        return teamService.getAllTeams();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Team createTeam(@RequestBody @Valid  TeamCreateUpdateDto teamCreateUpdateDto){
        return teamService.createTeam(teamCreateUpdateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public Team updateTeam(@RequestBody @Valid   TeamCreateUpdateDto teamCreateUpdateDto, @PathVariable Long id){
        return teamService.updateTeam(teamCreateUpdateDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id){
        teamService.deleteTeam(id);
    }
}
