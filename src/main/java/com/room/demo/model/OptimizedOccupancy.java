package com.room.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;


@JsonSerialize
public record OptimizedOccupancy(
        int occupiedPremium,
        double premiumRevenue,
        int occupiedEconomy,
        double economyRevenue,
        double totalRevenue,
        List<Guest> upgradedGuests) {
}
