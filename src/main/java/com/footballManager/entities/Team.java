package com.footballManager.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float commissionForTransfer;
    private BigDecimal balance;

    @JsonBackReference
    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Player> players;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name1) {
        name = name1;
    }

    public Float getCommissionForTransfer() {
        return commissionForTransfer;
    }

    public void setCommissionForTransfer(Float commissionForTransfer) {
        this.commissionForTransfer = commissionForTransfer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", commissionForTransfer=" + commissionForTransfer +
                ", balance=" + balance +
                '}';
    }
    public String toJSON(){
        return String.format("{\"id\":%s,\"name\":\"%s\",\"commissionForTransfer\":%s,\"balance\":%s}",
                id,name,commissionForTransfer,balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (id != null ? !id.equals(team.id) : team.id != null) return false;
        if (!name.equals(team.name)) return false;
        if (!commissionForTransfer.equals(team.commissionForTransfer)) return false;
        return balance.equals(team.balance);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + commissionForTransfer.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }
}
