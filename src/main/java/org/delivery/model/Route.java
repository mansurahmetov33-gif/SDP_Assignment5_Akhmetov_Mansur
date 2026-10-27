package org.delivery.model;

public record Route(
        String id,
        Location origin,
        Location destination,
        double distanceKm,
        int estimatedMinutes,
        double cost,
        double carbonEmission,
        double trafficMultiplier,
        boolean available
) {
    public Route {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Route ID cannot be empty");
        }

        if (origin == null || destination == null) {
            throw new IllegalArgumentException(
                    "Locations cannot be null");
        }

        if (!Double.isFinite(distanceKm) || distanceKm < 0) {
            throw new IllegalArgumentException(
                    "Invalid distance");
        }

        if (estimatedMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Invalid delivery time");
        }

        if (!Double.isFinite(cost) || cost < 0) {
            throw new IllegalArgumentException(
                    "Invalid cost");
        }

        if (!Double.isFinite(carbonEmission) || carbonEmission < 0) {
            throw new IllegalArgumentException(
                    "Invalid carbon emission");
        }

        if (!Double.isFinite(trafficMultiplier)
                || trafficMultiplier < 1.0) {
            throw new IllegalArgumentException(
                    "Traffic multiplier must be >= 1.0");
        }
    }

    public double effectiveMinutes() {
        return estimatedMinutes * trafficMultiplier;
    }
}
