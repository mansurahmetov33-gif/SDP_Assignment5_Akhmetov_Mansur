
package org.delivery.strategy;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;

import java.util.Comparator;
import java.util.List;

public class FastestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(
            List<Route> routes,
            DeliveryRequest request
    ) {
        if (routes == null || request == null) {
            throw new IllegalArgumentException(
                    "Routes and request cannot be null");
        }

        return routes.stream()
                .filter(route -> route.available())
                .filter(route -> route.cost() <= request.maxBudget())
                .filter(route ->
                        route.effectiveMinutes()
                                <= request.maxDeliveryMinutes())
                .min(Comparator.comparingDouble(
                        Route::effectiveMinutes))
                .orElseThrow(() ->
                        new NoSuitableRouteException(
                                "No suitable route found"));
    }

    @Override
    public String getStrategyName() {
        return "Fastest Route Strategy";
    }
}
