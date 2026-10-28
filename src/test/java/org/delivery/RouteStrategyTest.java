
package org.delivery;

import org.delivery.exception.NoSuitableRouteException;
import org.delivery.model.*;
import org.delivery.planner.*;
import org.delivery.service.*;
import org.delivery.strategy.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteStrategyTest {

    private List<Route> routes;
    private DeliveryRequest request;

    @BeforeEach
    void setUp() {
        Location astana = new Location(
                "Astana", 51.1694, 71.4491);

        Location airport = new Location(
                "Airport", 51.0222, 71.4669);

        routes = List.of(
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

        request = new DeliveryRequest(
                DeliveryPriority.URGENT,
                5000,
                40,
                false
        );
    }

    @Test
    void fastestStrategySelectsFastestValidRoute() {
        Route result = new FastestRouteStrategy()
                .selectRoute(routes, request);

        assertEquals("R8", result.id());
    }

    @Test
    void cheapestStrategySelectsLowestTotalCost() {
        Route result = new CheapestRouteStrategy()
                .selectRoute(routes, request);

        assertEquals("R7", result.id());
    }

    @Test
    void ecoStrategySelectsLowestEcoScore() {
        Route result = new EcoFriendlyRouteStrategy()
                .selectRoute(routes, request);

        assertEquals("R8", result.id());
    }

    @Test
    void balancedStrategySelectsBestWeightedRoute() {
        Route result = new BalancedRouteStrategy(
                0.4, 0.3, 0.1, 0.2
        ).selectRoute(routes, request);

        assertEquals("R8", result.id());
    }

    @Test
    void distanceStrategySelectsShortestValidRoute() {
        Route result = new DistanceFirstRouteStrategy()
                .selectRoute(routes, request);

        assertEquals("R8", result.id());
    }

    @Test
    void runtimeSwitchingChangesAlgorithm() {
        RoutePlanner planner = new RoutePlanner(
                new FastestRouteStrategy()
        );

        assertEquals("R8", planner.plan(routes, request).id());

        planner.setStrategy(new CheapestRouteStrategy());

        assertEquals("R7", planner.plan(routes, request).id());
    }

    @Test
    void automaticSelectorChoosesFastestForUrgent() {
        StrategySelector selector = new StrategySelector();

        RouteStrategy strategy = selector.selectStrategy(request);

        assertInstanceOf(FastestRouteStrategy.class, strategy);
    }

    @Test
    void automaticSelectorChoosesEcoForEcoPriority() {
        StrategySelector selector = new StrategySelector();

        DeliveryRequest ecoRequest = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 60, true
        );

        assertInstanceOf(
                EcoFriendlyRouteStrategy.class,
                selector.selectStrategy(ecoRequest)
        );
    }

    @Test
    void unavailableRouteIsRejected() {
        assertFalse(RouteValidator.isValid(
                routes.get(2), request
        ));
    }

    @Test
    void budgetRestrictionIsApplied() {
        assertFalse(RouteValidator.isValid(
                routes.get(1), request
        ));
    }

    @Test
    void deadlineRestrictionIsApplied() {
        DeliveryRequest strictRequest = new DeliveryRequest(
                DeliveryPriority.URGENT, 5000, 20, false
        );

        assertThrows(
                NoSuitableRouteException.class,
                () -> new FastestRouteStrategy()
                        .selectRoute(routes, strictRequest)
        );
    }

    @Test
    void noValidRouteThrowsException() {
        DeliveryRequest lowBudget = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 100, 60, false
        );

        assertThrows(
                NoSuitableRouteException.class,
                () -> new CheapestRouteStrategy()
                        .selectRoute(routes, lowBudget)
        );
    }

    @Test
    void invalidBalancedWeightsAreRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BalancedRouteStrategy(
                        0.5, 0.5, 0.5, 0.5
                )
        );
    }

    @Test
    void negativeDistanceIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Route(
                        "INVALID",
                        routes.get(0).origin(),
                        routes.get(0).destination(),
                        -10,
                        20,
                        1000,
                        1.0,
                        1.0,
                        true
                )
        );
    }

    @Test
    void emptyRouteListThrowsException() {
        assertThrows(
                NoSuitableRouteException.class,
                () -> new FastestRouteStrategy()
                        .selectRoute(List.of(), request)
        );
    }

    @Test
    void comparisonOfTwoStrategiesProducesDifferentRoutes() {
        Route fastest = new FastestRouteStrategy()
                .selectRoute(routes, request);

        Route cheapest = new CheapestRouteStrategy()
                .selectRoute(routes, request);

        assertNotEquals(fastest.id(), cheapest.id());
    }

    @Test
    void routeAtExactBudgetIsAccepted() {
        Route route = routes.get(0);

        double exactCost =
                RouteCostCalculator.calculate(route, request);

        DeliveryRequest exactBudgetRequest =
                new DeliveryRequest(
                        DeliveryPriority.URGENT,
                        exactCost,
                        40,
                        false
                );

        assertTrue(RouteValidator.isValid(
                route, exactBudgetRequest
        ));
    }
}
