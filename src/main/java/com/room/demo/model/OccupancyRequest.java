package com.room.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;


@JsonSerialize
public record OccupancyRequest(int premiumRooms,
                               int economyRooms,
                               List<Double> potentialGuests) {
}