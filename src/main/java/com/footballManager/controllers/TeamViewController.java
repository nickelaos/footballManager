package com.footballManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Regular non rest controler
@Controller
@RequestMapping("/view")
public class TeamViewController {

    // Method to display teams view
    @GetMapping("/teams")
    public String teams() {
        return "teams.html";
    }

}