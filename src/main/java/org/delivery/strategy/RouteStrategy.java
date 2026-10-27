
package org.delivery.strategy;

import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;

import java.util.List;

public interface RouteStrategy {

    Route selectRoute(
            List<Route> routes,
            DeliveryRequest request
    );

    String getStrategyName();
}
