package com.github.afanas10101111.jtb.dto;

import lombok.Data;

@Data
public class GroupStatTo {
    private final Integer id;
    private final String title;
    private final Long activeUserCount;
}
