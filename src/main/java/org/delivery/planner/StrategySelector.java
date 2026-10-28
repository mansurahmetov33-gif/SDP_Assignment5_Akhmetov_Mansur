
package org.delivery.planner;

import org.delivery.model.DeliveryPriority;
import org.delivery.model.DeliveryRequest;
import org.delivery.strategy.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class StrategySelector {

    private final Map<DeliveryPriority, RouteStrategy> strategies;

    public StrategySelector() {
        strategies = new EnumMap<>(DeliveryPriority.class);

        strategies.put(
                DeliveryPriority.URGENT,
                new FastestRouteStrategy()
        );

        strategies.put(
                DeliveryPriority.ECONOMY,
                new CheapestRouteStrategy()
        );

        strategies.put(
                DeliveryPriority.ECO,
                new EcoFriendlyRouteStrategy()
        );

        strategies.put(
                DeliveryPriority.STANDARD,
                new BalancedRouteStrategy(
                        0.4, 0.3, 0.1, 0.2
                )
        );
    }

    public RouteStrategy selectStrategy(DeliveryRequest request) {
        Objects.requireNonNull(request, "Request cannot be null");

        RouteStrategy strategy = strategies.get(request.priority());

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "No strategy for priority: " + request.priority()
            );
        }

        return strategy;
    }

    public void registerStrategy(
            DeliveryPriority priority,
            RouteStrategy strategy
    ) {
        strategies.put(
                Objects.requireNonNull(priority),
                Objects.requireNonNull(strategy)
        );
    }
}
