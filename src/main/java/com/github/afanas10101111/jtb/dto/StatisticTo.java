package com.github.afanas10101111.jtb.dto;

import java.util.List;

public record StatisticTo(
        int activeUserCount, int inactiveUserCount, List<GroupStatTo> groupStatTos, double averageGroupCountByUser
) {
}
