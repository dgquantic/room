package com.room.demo.api;


import com.room.demo.model.OccupancyRequest;
import com.room.demo.model.OptimizedOccupancy;
import com.room.demo.service.OccupancyOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/occupancy")
public class OccupancyController {
    private final OccupancyOptimizer occupancyOptimizer;

    @Autowired
    public OccupancyController (OccupancyOptimizer occupancyOptimizer) {
        this.occupancyOptimizer = occupancyOptimizer;
    }

    @Async
    @PostMapping("/optimize")
    public CompletableFuture<ResponseEntity<OptimizedOccupancy>> optimizeOccupancy (@RequestBody OccupancyRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            OptimizedOccupancy response = occupancyOptimizer.optimizeOccupancy(
                    request.premiumRooms(),
                    request.economyRooms(),
                    request.potentialGuests());
            return ResponseEntity.ok(response);
        });
    }

}