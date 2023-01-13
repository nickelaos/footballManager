package com.footballManager.controllers;

import com.footballManager.dto.PlayerCreateUpdateDto;
import com.footballManager.dto.TransferDto;
import com.footballManager.entities.Player;
import com.footballManager.services.interfaces.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("api/players")
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/all")
    public Iterable<Player> getAllPlayers() {
        return playerService.findAll();
    }

    @GetMapping
    public Page<Player> findAllByPage(@PageableDefault(size = 5) Pageable pageable){
        return playerService.findAllByPage(pageable);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Player createPlayer(@RequestBody @Valid PlayerCreateUpdateDto playerCreateUpdateDto){
        return playerService.createPlayer(playerCreateUpdateDto);
    }


    @GetMapping("/{id}")
    public Player getPlayer (@PathVariable("id") Long id){
        return playerService.getPlayer(id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable("id") Long id,
                               @RequestBody @Valid  PlayerCreateUpdateDto playerCreateUpdateDto){
        return playerService.updatePlayer(id, playerCreateUpdateDto);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable("id") Long id){
        playerService.deletePlayer(id);
    }


    @PostMapping("/transfer")
    public Player transferPlayer(@RequestBody @Valid  TransferDto transferDto){
        return playerService.transferPlayer(transferDto);
    }



}
