
package org.delivery.model;

import java.util.List;
import java.util.Map;

public record RouteDecision(
        String strategyName,
        Route selectedRoute,
        double totalCost,
        double estimatedTime,
        String explanation,
        Map<String, List<String>> rejectedRoutes
) {
}
