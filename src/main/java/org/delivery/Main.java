
package org.delivery;

import org.delivery.model.*;

public class Main {
    public static void main(String[] args) {

        Location astana = new Location(
                "Astana", 51.1694, 71.4491);

        Location airport = new Location(
                "Astana Airport", 51.0222, 71.4669);

        Route route = new Route(
                "R1",
                astana,
                airport,
                18.5,
                25,
                3500,
                2.4,
                1.2,
                true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT,
                5000,
                40,
                false
        );

        System.out.println("Route: " + route.id());
        System.out.println("Distance: " + route.distanceKm());
        System.out.println("Time: " + route.effectiveMinutes());
        System.out.println("Cost: " + route.cost());
        System.out.println("Priority: " + request.priority());
    }
}
