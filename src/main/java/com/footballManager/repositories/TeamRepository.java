package com.footballManager.repositories;

import com.footballManager.entities.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team,Long> {
 //   Team findByName(String team);
}
