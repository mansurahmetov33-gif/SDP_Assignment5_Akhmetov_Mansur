package org.delivery.model;

public record DeliveryRequest(
        DeliveryPriority priority,
        double maxBudget,
        int maxDeliveryMinutes,
        boolean ecoPreferred
) {
    public DeliveryRequest {
        if (priority == null) {
            throw new IllegalArgumentException(
                    "Priority cannot be null");
        }

        if (!Double.isFinite(maxBudget) || maxBudget < 0) {
            throw new IllegalArgumentException(
                    "Invalid budget");
        }

        if (maxDeliveryMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Delivery time must be positive");
        }
    }
}
