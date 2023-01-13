package com.footballManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    @NotNull
    private Long player;
    @NotNull
    private Long team;

    public String toJSON() {
        return  String.format("{\"player\":%s,\"team\":%s}",
                player ,team);
    }
}
