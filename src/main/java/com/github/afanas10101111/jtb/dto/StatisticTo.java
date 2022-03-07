package com.github.afanas10101111.jtb.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatisticTo {
    private final int activeUserCount;
    private final int inactiveUserCount;
    private final List<GroupStatTo> groupStatTos;
    private final double averageGroupCountByUser;
}
