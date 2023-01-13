package com.footballManager.services.interfaces;

import com.footballManager.dto.PlayerCreateUpdateDto;
import com.footballManager.dto.TransferDto;
import com.footballManager.entities.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Iterator;
import java.util.List;

public interface PlayerService {
    Player createPlayer(PlayerCreateUpdateDto playerCreateUpdateDto);

    Player getPlayer(Long id);

    Player updatePlayer(Long id, PlayerCreateUpdateDto playerCreateUpdateDto);

    void deletePlayer(Long id);

    Player transferPlayer(TransferDto transferDto);

    Page<Player> findAllByPage(Pageable pageable);

    Iterable<Player> findAll();
}
