# Smart Delivery & Route Optimization Engine

## Project Overview

This project is a Java application that selects the most suitable delivery route using the Strategy Design Pattern.

The system supports different route selection algorithms based on delivery time, cost, distance, carbon emissions, and delivery constraints.

## Design Pattern: Strategy

The Strategy Pattern allows the application to switch between different route selection algorithms without changing the main planning logic.

Main components:

- `RouteStrategy` — interface for all route selection algorithms.
- `RoutePlanner` — context that uses the selected strategy.
- `StrategySelector` — automatically selects a strategy based on delivery priority.
- `RouteValidator` — checks route availability, budget, and deadline.
- `RouteCostCalculator` — calculates total delivery cost.

## Implemented Strategies

| Strategy | Description |
|---|---|
| FastestRouteStrategy | Selects the route with the shortest effective delivery time |
| CheapestRouteStrategy | Selects the route with the lowest total cost |
| EcoFriendlyRouteStrategy | Selects the route with the lowest environmental score |
| BalancedRouteStrategy | Uses configurable weights for time, cost, distance, and emissions |
| DistanceFirstRouteStrategy | Selects the shortest valid route |

## Features

- Five route selection strategies
- Runtime strategy switching
- Automatic strategy selection
- Budget and deadline constraints
- Route availability validation
- Traffic multiplier support
- Strategy comparison mode
- Route decision explanations
- Custom exception for unavailable routes
- JUnit 5 automated tests

## Automatic Strategy Selection

The system selects a strategy according to delivery priority:

- URGENT → FastestRouteStrategy
- ECONOMY → CheapestRouteStrategy
- ECO → EcoFriendlyRouteStrategy
- STANDARD → BalancedRouteStrategy

## Balanced Strategy

The BalancedRouteStrategy uses four configurable weights:

- Time: 0.4
- Cost: 0.3
- Distance: 0.1
- Carbon emissions: 0.2

All weights must sum to 1.0.

Each criterion is normalized before calculating the final weighted score. The route with the lowest score is selected.

## Route Constraints

Before selecting a route, the system checks:

1. The route must be available.
2. The calculated total cost must not exceed the delivery budget.
3. The effective delivery time must not exceed the deadline.

If no suitable route exists, the system throws `NoSuitableRouteException`.


## Technologies

- Java 17+
- Maven
- JUnit 5
- PlantUML
- IntelliJ IDEA

## How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Load the Maven dependencies.
4. Run `Main.java` from `src/main/java/org/delivery`.

To run automated tests, use the Maven `test` lifecycle or run `RouteStrategyTest.java` directly in IntelliJ IDEA.

## Testing

The project includes 17 JUnit 5 tests covering:

- Strategy selection correctness
- Runtime switching
- Automatic strategy selection
- Budget and deadline validation
- Unavailable routes
- Invalid configuration
- Exception handling
- Boundary conditions

## SOLID Principles

**Single Responsibility Principle:** Each strategy handles one route selection algorithm.

**Open/Closed Principle:** New strategies can be added without modifying the RoutePlanner.

**Liskov Substitution Principle:** All concrete strategies can be used through the RouteStrategy interface.

**Interface Segregation Principle:** RouteStrategy contains only the methods required for route selection.

**Dependency Inversion Principle:** RoutePlanner depends on the RouteStrategy abstraction instead of concrete implementations.

## Strategy vs Bridge

Strategy is used to switch between different algorithms at runtime.

Bridge separates two independent dimensions of an application so they can evolve separately.

In this project, Strategy is appropriate because the main goal is to change how a delivery route is selected.

## Author

Mansur Akhmetov

Astana IT University

Software Design Patterns — Assignment 5