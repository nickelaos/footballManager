package com.footballManager.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="players")
public class Player {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private Date dateOfBirth;
    private Date startOfCareer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getStartOfCareer() {
        return startOfCareer;
    }

    public void setStartOfCareer(Date startOfCareer) {
        this.startOfCareer = startOfCareer;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String toJSON(){
        return  String.format("{\"id\":%s,\"fullName\":\"%s\",\"dateOfBirth\":\"%s\",\"startOfCareer\":\"%s\",\"team\":%s}",
                id,fullName,dateOfBirth,startOfCareer,team.toJSON());
    }

    @Override
    public String toString() {
        return "Player{" +
                "fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", startOfCareer=" + startOfCareer +
                ", team=" + team +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != null ? !id.equals(player.id) : player.id != null) return false;
        if (!fullName.equals(player.fullName)) return false;
        if (!dateOfBirth.equals(player.dateOfBirth)) return false;
        if (!startOfCareer.equals(player.startOfCareer)) return false;
        return team.equals(player.team);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        result = 31 * result + startOfCareer.hashCode();
        result = 31 * result + team.hashCode();
        return result;
    }
}
