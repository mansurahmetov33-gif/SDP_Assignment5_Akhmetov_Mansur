
package org.delivery.strategy;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;
import org.delivery.service.RouteValidator;
import org.delivery.service.RouteCostCalculator;
import java.util.Comparator;
import java.util.List;

public class CheapestRouteStrategy implements RouteStrategy {

    private double calculateTotalCost(
            Route route,
            DeliveryRequest request
    ) {
        double baseCost = route.cost();
        double distanceCost = route.distanceKm() * 20;

        double prioritySurcharge =
                request.priority() ==
                        org.delivery.model.DeliveryPriority.URGENT
                        ? 500 : 0;

        return baseCost + distanceCost + prioritySurcharge;
    }


    @Override
    public Route selectRoute(
            List<Route> routes,
            DeliveryRequest request
    ) {
        return RouteValidator.filterValidRoutes(routes, request)
                .stream()
                .min(Comparator.comparingDouble(
                        route -> RouteCostCalculator.calculate(
                                route, request)))
                .orElseThrow(() ->
                        new NoSuitableRouteException(
                                "No suitable cheapest route found"));
    }


    @Override
    public String getStrategyName() {
        return "Cheapest Route Strategy";
    }
}
