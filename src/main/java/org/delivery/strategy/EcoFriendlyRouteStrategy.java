
package org.delivery.strategy;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;
import org.delivery.service.RouteValidator;
import java.util.Comparator;
import java.util.List;

public class EcoFriendlyRouteStrategy implements RouteStrategy {

    private double calculateEcoScore(Route route) {
        double emissionWeight = 0.8;
        double distanceWeight = 0.2;

        return route.carbonEmission() * emissionWeight
                + route.distanceKm() * distanceWeight;
    }


    @Override
    public Route selectRoute(
            List<Route> routes,
            DeliveryRequest request
    ) {
        return RouteValidator.filterValidRoutes(routes, request)
                .stream()
                .min(Comparator.comparingDouble(
                        this::calculateEcoScore))
                .orElseThrow(() ->
                        new NoSuitableRouteException(
                                "No suitable eco route found"));
    }


    @Override
    public String getStrategyName() {
        return "Eco Friendly Route Strategy";
    }
}
