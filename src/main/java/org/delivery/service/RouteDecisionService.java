
package org.delivery.service;

import org.delivery.model.*;
import org.delivery.strategy.RouteStrategy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RouteDecisionService {

    public RouteDecision explain(
            RouteStrategy strategy,
            List<Route> routes,
            DeliveryRequest request
    ) {
        Objects.requireNonNull(strategy);
        Objects.requireNonNull(request);

        List<Route> validRoutes =
                RouteValidator.filterValidRoutes(routes, request);

        Route selected = strategy.selectRoute(routes, request);

        Map<String, List<String>> rejected =
                new LinkedHashMap<>();

        for (Route route : routes) {
            List<String> reasons =
                    RouteValidator.getRejectionReasons(route, request);

            if (!reasons.isEmpty()) {
                rejected.put(route.id(), reasons);
            }
        }

        double totalCost =
                RouteCostCalculator.calculate(selected, request);

        String explanation =
                "Selected " + selected.id()
                        + " using " + strategy.getStrategyName()
                        + " from " + validRoutes.size()
                        + " valid routes. "
                        + "Effective time: "
                        + selected.effectiveMinutes()
                        + " min, total cost: "
                        + totalCost
                        + ", distance: "
                        + selected.distanceKm()
                        + " km, CO2: "
                        + selected.carbonEmission()
                        + " kg.";

        return new RouteDecision(
                strategy.getStrategyName(),
                selected,
                totalCost,
                selected.effectiveMinutes(),
                explanation,
                rejected
        );
    }
}
