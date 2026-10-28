
package org.delivery.strategy;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;
import org.delivery.service.RouteValidator;

import java.util.Comparator;
import java.util.List;

public class DistanceFirstRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(
            List<Route> routes,
            DeliveryRequest request
    ) {
        return RouteValidator.filterValidRoutes(routes, request)
                .stream()
                .min(Comparator
                        .comparingDouble(Route::distanceKm)
                        .thenComparingDouble(Route::effectiveMinutes))
                .orElseThrow(() ->
                        new NoSuitableRouteException(
                                "No suitable distance-first route found"));
    }

    @Override
    public String getStrategyName() {
        return "Distance First Route Strategy";
    }
}
