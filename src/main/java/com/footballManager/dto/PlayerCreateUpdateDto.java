package com.footballManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlayerCreateUpdateDto {
    @NotNull
    @Size(min = 3,max = 32, message = "Your full name should be between 3 and 32 characters")
    @Pattern(regexp = "[a-zA-Z ]*", message = "full name cannot contain any special characters and digits")
    private String fullName;
    @NotNull
    @Past
    private Date dateOfBirth;
    @NotNull
    @Past
    private Date startOfCareer;
    @NotNull
    private Long team;

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

    public Long getTeam() {
        return team;
    }

    public void setTeam(Long team) {
        this.team = team;
    }

    public String toJSON(){
        return  String.format("{\"fullName\":\"%s\",\"dateOfBirth\":\"%s\",\"startOfCareer\":\"%s\",\"team\":%s}",
                fullName,dateOfBirth,startOfCareer,team);
    }
}
