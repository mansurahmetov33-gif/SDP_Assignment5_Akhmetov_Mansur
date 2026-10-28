
package org.delivery.service;

import org.delivery.model.DeliveryPriority;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;

public class RouteCostCalculator {

    public static double calculate(
            Route route,
            DeliveryRequest request
    ) {
        double baseCost = route.cost();
        double distanceCost = route.distanceKm() * 20;

        double prioritySurcharge =
                request.priority() == DeliveryPriority.URGENT
                        ? 500 : 0;

        return baseCost + distanceCost + prioritySurcharge;
    }
}
