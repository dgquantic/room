package com.room.demo.service;

import com.room.demo.model.Guest;
import com.room.demo.model.OptimizedOccupancy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class OccupancyOptimizer {
    private static final Logger log = LoggerFactory.getLogger(OccupancyOptimizer.class);
    private static final BigDecimal LIMIT = BigDecimal.valueOf(100);
    private final Predicate<BigDecimal> roomPredicate = (t) -> t.compareTo(LIMIT) >= 0;


    /**
     * Optimizes the occupancy of rooms based on bids from potential guests.
     * The method assigns premium and economy rooms to maximize total revenue.
     *
     * <p>Guests offering an amount of €100 and above are considered as premium guests.
     * Premium guests are assigned premium rooms preferentially.
     * If premium rooms are exhausted, remaining guests are considered for economy rooms.
     * Economy guests may be upgraded to premium rooms if premium rooms are under-occupied and if
     * more economy guests are present than there are economy rooms.</p>
     *
     * <p>The method calculates and logs the usage and revenues associated with each room type and total revenue post-optimization.</p>
     *
     * @param premiumRooms    the number of premium rooms available for occupation
     * @param economyRooms    the number of economy rooms available for occupation
     * @param potentialGuests a list of bids from potential guests
     * @return an instance of OptimizedOccupancy encapsulating usage and revenue information for each room type and total revenue post-optimization
     */
    public OptimizedOccupancy optimizeOccupancy (int premiumRooms, int economyRooms, List<Double> potentialGuests) {
        List<BigDecimal> guests = new ArrayList<>();
        List<Guest> upgradedGuests = new ArrayList<>();
        AtomicInteger availablePremiumRooms = new AtomicInteger(premiumRooms);
        AtomicInteger availableEconomyRooms = new AtomicInteger(economyRooms);
        BigDecimal premiumRoomRevenue = BigDecimal.ZERO, economyRoomRevenue = BigDecimal.ZERO;

        for (Double bid : potentialGuests) {
            guests.add(BigDecimal.valueOf(bid).setScale(2, RoundingMode.HALF_UP));
        }

        guests.sort(Comparator.reverseOrder());

        Map<Boolean, List<BigDecimal>> premiumAndEconomyGuestsMap = guests.stream()
                .collect(Collectors.partitioningBy(roomPredicate));

        List<BigDecimal> premiumGuests = premiumAndEconomyGuestsMap.get(true);
        List<BigDecimal> economyGuests = premiumAndEconomyGuestsMap.get(false);

        for (BigDecimal guest : premiumGuests) {
            if (availablePremiumRooms.get() > 0) {
                premiumRoomRevenue = premiumRoomRevenue.add(guest);
                availablePremiumRooms.decrementAndGet();
            } else {
                break;
            }
        }

        if (availablePremiumRooms.get() > 0 && economyGuests.size() > economyRooms) {
            Iterator<BigDecimal> iterator = economyGuests.iterator();
            while (iterator.hasNext()) {
                if (availablePremiumRooms.get() > 0 && economyGuests.size() > economyRooms) {
                    BigDecimal guest = iterator.next();
                    premiumRoomRevenue = premiumRoomRevenue.add(guest);
                    upgradedGuests.add(new Guest(guest.doubleValue()));
                    availablePremiumRooms.decrementAndGet();
                    iterator.remove();
                } else {
                    break;
                }
            }
        }

        for (BigDecimal guest : economyGuests) {
            if (availableEconomyRooms.get() > 0) {
                economyRoomRevenue = economyRoomRevenue.add(guest);
                availableEconomyRooms.decrementAndGet();
            } else {
                break;
            }
        }

        int premiumUsage = premiumRooms - availablePremiumRooms.get();
        int economyUsage = economyRooms - availableEconomyRooms.get();
        BigDecimal totalRevenue = premiumRoomRevenue.add(economyRoomRevenue);

        log.info("Optimized occupancy: premium room usage = {}, premium room revenue = {}, " +
                        "economy room usage = {}, economy room revenue = {}, total revenue = {}",
                premiumUsage, premiumRoomRevenue.doubleValue(),
                economyUsage, economyRoomRevenue.doubleValue(), totalRevenue.doubleValue());

        return new OptimizedOccupancy(premiumUsage, premiumRoomRevenue.doubleValue(),
                economyUsage, economyRoomRevenue.doubleValue(), totalRevenue.doubleValue(), upgradedGuests);
    }
}