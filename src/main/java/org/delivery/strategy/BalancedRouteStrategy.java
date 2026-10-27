
package org.delivery.strategy;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;

import java.util.Comparator;
import java.util.List;

public class BalancedRouteStrategy implements RouteStrategy {

    private final double timeWeight;
    private final double costWeight;
    private final double distanceWeight;
    private final double emissionWeight;

    public BalancedRouteStrategy(
            double timeWeight,
            double costWeight,
            double distanceWeight,
            double emissionWeight
    ) {
        double sum = timeWeight + costWeight
                + distanceWeight + emissionWeight;

        if (!Double.isFinite(sum)
                || Math.abs(sum - 1.0) > 0.000001
                || !validWeight(timeWeight)
                || !validWeight(costWeight)
                || !validWeight(distanceWeight)
                || !validWeight(emissionWeight)) {
            throw new IllegalArgumentException(
                    "Weights must be between 0 and 1 and sum to 1.0");
        }

        this.timeWeight = timeWeight;
        this.costWeight = costWeight;
        this.distanceWeight = distanceWeight;
        this.emissionWeight = emissionWeight;
    }

    private boolean validWeight(double weight) {
        return Double.isFinite(weight)
                && weight >= 0
                && weight <= 1;
    }

    @Override
    public Route selectRoute(
            List<Route> routes,
            DeliveryRequest request
    ) {
        if (routes == null || request == null) {
            throw new IllegalArgumentException(
                    "Routes and request cannot be null");
        }

        List<Route> validRoutes = routes.stream()
                .filter(Route::available)
                .filter(route ->
                        route.cost() <= request.maxBudget())
                .filter(route ->
                        route.effectiveMinutes()
                                <= request.maxDeliveryMinutes())
                .toList();

        if (validRoutes.isEmpty()) {
            throw new NoSuitableRouteException(
                    "No suitable balanced route found");
        }

        double maxTime = validRoutes.stream()
                .mapToDouble(Route::effectiveMinutes)
                .max().orElse(1);

        double maxCost = validRoutes.stream()
                .mapToDouble(Route::cost)
                .max().orElse(1);

        double maxDistance = validRoutes.stream()
                .mapToDouble(Route::distanceKm)
                .max().orElse(1);

        double maxEmission = validRoutes.stream()
                .mapToDouble(Route::carbonEmission)
                .max().orElse(1);

        return validRoutes.stream()
                .min(Comparator.comparingDouble(route -> {
                    double time = maxTime == 0 ? 0
                            : route.effectiveMinutes() / maxTime;
                    double cost = maxCost == 0 ? 0
                            : route.cost() / maxCost;
                    double distance = maxDistance == 0 ? 0
                            : route.distanceKm() / maxDistance;
                    double emission = maxEmission == 0 ? 0
                            : route.carbonEmission() / maxEmission;

                    return time * timeWeight
                            + cost * costWeight
                            + distance * distanceWeight
                            + emission * emissionWeight;
                }))
                .orElseThrow();
    }

    @Override
    public String getStrategyName() {
        return "Balanced Route Strategy";
    }
}
