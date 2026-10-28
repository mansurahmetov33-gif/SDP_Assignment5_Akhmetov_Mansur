
package org.delivery;

import org.delivery.model.*;
import org.delivery.planner.RoutePlanner;
import org.delivery.planner.StrategySelector;
import org.delivery.strategy.*;
import org.delivery.comparison.StrategyComparison;
import org.delivery.service.RouteDecisionService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Location astana = new Location(
                "Astana", 51.1694, 71.4491);

        Location airport = new Location(
                "Astana Airport", 51.0222, 71.4669);

        // 8 candidate routes
        List<Route> routes = List.of(
                new Route("R1", astana, airport,
                        18.5, 25, 3500, 2.4, 1.2, true),

                new Route("R2", astana, airport,
                        15.0, 20, 6000, 3.0, 1.0, true),

                new Route("R3", astana, airport,
                        16.0, 25, 3000, 2.0, 1.0, false),

                new Route("R4", astana, airport,
                        22.0, 35, 2500, 2.8, 1.0, true),

                new Route("R5", astana, airport,
                        19.0, 28, 2900, 1.5, 1.1, true),

                new Route("R6", astana, airport,
                        14.0, 32, 4200, 1.8, 1.3, true),

                new Route("R7", astana, airport,
                        24.0, 38, 2100, 3.5, 1.0, true),

                new Route("R8", astana, airport,
                        17.0, 27, 3200, 1.2, 1.0, true)
        );

        // main delivery request
        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT,
                5000,
                40,
                false
        );

        // runtime strategy switching
        System.out.println("=== RUNTIME STRATEGY SWITCHING ===");

        RoutePlanner planner = new RoutePlanner(
                new FastestRouteStrategy()
        );

        Route fastest = planner.plan(routes, request);
        System.out.println("Fastest: " + fastest.id());

        planner.setStrategy(new CheapestRouteStrategy());
        Route cheapest = planner.plan(routes, request);
        System.out.println("Cheapest: " + cheapest.id());

        planner.setStrategy(new EcoFriendlyRouteStrategy());
        Route eco = planner.plan(routes, request);
        System.out.println("Eco: " + eco.id());

        planner.setStrategy(new BalancedRouteStrategy(
                0.4, 0.3, 0.1, 0.2
        ));
        Route balanced = planner.plan(routes, request);
        System.out.println("Balanced: " + balanced.id());

        // automatic strategy selection
        System.out.println("\n=== AUTOMATIC STRATEGY SELECTION ===");

        StrategySelector selector = new StrategySelector();

        DeliveryRequest urgentRequest = new DeliveryRequest(
                DeliveryPriority.URGENT, 7000, 60, false
        );

        DeliveryRequest economyRequest = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 5000, 60, false
        );

        DeliveryRequest ecoRequest = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 60, true
        );

        DeliveryRequest standardRequest = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        List<DeliveryRequest> requests = List.of(
                urgentRequest,
                economyRequest,
                ecoRequest,
                standardRequest
        );

        for (DeliveryRequest deliveryRequest : requests) {

            RouteStrategy selectedStrategy =
                    selector.selectStrategy(deliveryRequest);

            planner.setStrategy(selectedStrategy);

            Route selectedRoute = planner.plan(
                    routes, deliveryRequest
            );

            System.out.println(
                    deliveryRequest.priority()
                            + " -> "
                            + selectedStrategy.getStrategyName()
                            + " -> "
                            + selectedRoute.id()
            );
        }

        // strategy comparison mode
        StrategyComparison comparison = new StrategyComparison(
                List.of(
                        new FastestRouteStrategy(),
                        new CheapestRouteStrategy(),
                        new EcoFriendlyRouteStrategy(),
                        new BalancedRouteStrategy(
                                0.4, 0.3, 0.1, 0.2
                        ),
                        new DistanceFirstRouteStrategy()
                )
        );

        comparison.compare(routes, standardRequest);

        // route decision explanation
        RouteDecisionService decisionService =
                new RouteDecisionService();

        RouteDecision decision = decisionService.explain(
                new FastestRouteStrategy(),
                routes,
                request
        );

        System.out.println("\n=== ROUTE DECISION ===");
        System.out.println(decision.explanation());

        System.out.println("\nRejected routes:");

        decision.rejectedRoutes().forEach((id, reasons) -> {
            System.out.println(
                    id + ": " + String.join(", ", reasons)
            );
        });

        // Fifth Strategy
        System.out.println("\n=== FIFTH STRATEGY ===");

        planner.setStrategy(new DistanceFirstRouteStrategy());

        Route shortest = planner.plan(routes, request);

        System.out.println(
                "Strategy: " + planner.getCurrentStrategyName());

        System.out.println(
                "Selected Route: " + shortest.id());

        System.out.println(
                "Distance: " + shortest.distanceKm() + " km");

        System.out.println(
                "Time: " + shortest.effectiveMinutes() + " min");
    }
}
