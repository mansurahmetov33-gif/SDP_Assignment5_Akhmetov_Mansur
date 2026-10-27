
package org.delivery.planner;

import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;
import org.delivery.strategy.RouteStrategy;

import java.util.List;
import java.util.Objects;

public class RoutePlanner {

    private RouteStrategy strategy;

    public RoutePlanner(RouteStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public void setStrategy(RouteStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public Route plan(
            List<Route> routes,
            DeliveryRequest request
    ) {
        return strategy.selectRoute(routes, request);
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}
