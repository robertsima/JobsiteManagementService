# Jobsite Management Service
The main purpose behind this project is to practice implementations for common design patterns and connect object relationship principles, object-oriented principles, and system architecture.

This project will serve as an open-source construction resource management ordering system. Business to business, the user of the application would be a construction business and they would be using 'providers' such as home depot to source jobsite construction materials. This application would be helpful for creating quotes and final orders/payments. 

**Object Oriented Principles (I-APE)**
Inheritance - _Is-A_
Abstraction
Polymorphism
Encapsulation

**Object Relationship Principles (ACA)**
Association - _Can-use_
Composition - _Part-of_
Aggregation - _Has-a_

**Design Principles (SOLID)**
Single Responsibility
Open/Closed
Liskov substitution - _Is-a_
Interface segregation - _Can-do_
Dependency injection

## Design Patterns to Implement
### Creational
Having to do with instantiation of a class and setting state.

Strongly related OOP: Inheritance, Abstraction, Polymorphism, Encapsulation
SOLID: Liskov substitution, Dependency injection

- [] Singleton - Each provider only has ONE DiscountHierarchy
Single instance exists at all times
- [] Factory/Abstract Factory
Abstract means multiple factories; Factory creates many objects given params.
- [] Builder - Creating a quote/order
Chains functions to build objects Person.color("blue).hair("none)... streams
- [] Prototype - Provider prototype
Initial class/object that can be built alone or inherited to create different variations

### Structural 
Having to do creation of classes, interfaces, overall global project 'blueprints'.

Strongly related OOP: Inheritance, Abstraction, Polymorphism, Encapsulation
SOLID: Single Responsibility, Interface Segregation, Liskov, Open/Closed
All Object Relationship Principles

- [] Composite - Business/Client 
Class made of other objects from different classes
- [] Facade - Orchestrator
A way to hide implementation logic using encapsulation and abstraction
- [] Bridge - User -> Order/Quote relationship bridged by interface
connection of some sort between two classes
- [] Decorator - Discount decorator
annotations that serve as interface definitions or something for a class

### Behavioral
Has to do with describing ideal solutions for object relationships involving association principle

Strongly related OOP: Inheritance, Abstraction, Polymorphism, Encapsulation
SOLID: Single Responsibility, Open/Closed
ACA: Association

- [] Template - Provider
- [] Mediator - Chain of responsibility orchestrator
- [] Chain-of-responsibility - Request workflow (Select Jobsite -> Providers -> Quote -> Order)
- [] Observer - Async Notifications for quotes, orders, jobsite updates
- [] Strategy - Reports
- [] Command - Quote
- [] Interpreter - Search inventory
- [] Iterator - Iterate through items in cart -> Calculate total

### Miscellaneous
- [] MVC
- [] DAO
- [] Dependency Injection


For the sake of learning, will probably be over-engineering this project.

## Testing 
