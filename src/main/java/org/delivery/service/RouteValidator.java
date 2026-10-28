
package org.delivery.service;

import org.delivery.model.DeliveryRequest;
import org.delivery.model.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteValidator {

    public static List<String> getRejectionReasons(
            Route route,
            DeliveryRequest request
    ) {
        List<String> reasons = new ArrayList<>();

        if (!route.available()) {
            reasons.add("Route is unavailable");
        }

        if (RouteCostCalculator.calculate(route, request)
                > request.maxBudget()) {
            reasons.add("Budget exceeded");
        }

        if (route.effectiveMinutes()
                > request.maxDeliveryMinutes()) {
            reasons.add("Delivery deadline exceeded");
        }

        return reasons;
    }

    public static boolean isValid(
            Route route,
            DeliveryRequest request
    ) {
        return getRejectionReasons(route, request).isEmpty();
    }

    public static List<Route> filterValidRoutes(
            List<Route> routes,
            DeliveryRequest request
    ) {
        if (routes == null || request == null) {
            throw new IllegalArgumentException(
                    "Routes and request cannot be null");
        }

        if (routes.stream().anyMatch(r -> r == null)) {
            throw new IllegalArgumentException(
                    "Routes cannot contain null");
        }

        return routes.stream()
                .filter(route -> isValid(route, request))
                .toList();
    }
}
