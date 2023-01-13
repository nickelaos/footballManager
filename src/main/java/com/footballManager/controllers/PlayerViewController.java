package com.footballManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/view")
public class PlayerViewController {
    @GetMapping("/players")
    public String players(){
        return "players.html";
    }
}
