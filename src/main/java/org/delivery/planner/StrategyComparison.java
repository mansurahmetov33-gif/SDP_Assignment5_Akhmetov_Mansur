
package org.delivery.comparison;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;
import org.delivery.planner.RoutePlanner;
import org.delivery.strategy.RouteStrategy;

import java.util.List;
import java.util.Objects;

public class StrategyComparison {

    private final List<RouteStrategy> strategies;

    public StrategyComparison(List<RouteStrategy> strategies) {
        this.strategies = List.copyOf(
                Objects.requireNonNull(strategies)
        );
    }

    public void compare(
            List<Route> routes,
            DeliveryRequest request
    ) {
        System.out.println("\n=== STRATEGY COMPARISON ===");

        for (RouteStrategy strategy : strategies) {
            RoutePlanner planner = new RoutePlanner(strategy);

            System.out.println(
                    "\nStrategy: " + strategy.getStrategyName()
            );

            try {
                Route selected = planner.plan(routes, request);

                System.out.println("Route: " + selected.id());
                System.out.println(
                        "Time: " + selected.effectiveMinutes() + " min"
                );
                System.out.println(
                        "Base cost: " + selected.cost()
                );
                System.out.println(
                        "Distance: " + selected.distanceKm() + " km"
                );
                System.out.println(
                        "CO2: " + selected.carbonEmission() + " kg"
                );

            } catch (NoSuitableRouteException e) {
                System.out.println("No suitable route: " + e.getMessage());
            }
        }
    }
}
