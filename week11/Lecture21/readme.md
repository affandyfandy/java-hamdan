# Week 11: Angular

## What is Angular?

<p align="center" width="100%">
    <img width="25%" src="image.png">
</p>

Angular is a platform and framework for building single-page client applications using HTML, CSS, and TypeScript. Developed and maintained by Google, Angular is a powerful and popular framework for building modern web applications. It provides a comprehensive solution for building rich, scalable, and maintainable web applications.

### Key Features of Angular:
- **Component-Based Architecture:** Angular applications are built using components, which are the building blocks of the user interface.
- **Two-Way Data Binding:** Angular provides two-way data binding, allowing for automatic synchronization of data between the model and the view.
- **Dependency Injection:** Angular's dependency injection system allows for efficient management of services and components, promoting modularity and testability.
- **Routing:** Angular includes a powerful router that enables navigation among views and allows developers to implement complex routing configurations.
- **RxJS Integration:** Angular leverages RxJS (Reactive Extensions for JavaScript) for reactive programming, enabling powerful asynchronous data streams and event handling.
- **Comprehensive CLI:** The Angular CLI (Command Line Interface) is a powerful tool that simplifies development tasks such as project creation, scaffolding components, and running tests.

## Investigating Angular Component Lifecycle

### Overview

In Angular, every component has a lifecycle, which refers to the sequence of events from the creation of the component to its destruction. Understanding the component lifecycle is crucial for implementing features like data fetching, event handling, and cleanup in a way that aligns with Angular's internal processes.

### Angular Component Lifecycle Hooks

Angular provides a series of lifecycle hooks that allow you to tap into key moments of a component's lifecycle. Below is a summary of the most commonly used lifecycle hooks:

| Lifecycle Hook       | Description                                                                                              | When to Use It                                                                                 |
|----------------------|----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| **`ngOnChanges`**    | Invoked when the input properties of a component change.                                                  | Use this hook to act upon changes to input properties, such as resetting state or triggering side effects. |
| **`ngOnInit`**       | Called once, after the first `ngOnChanges` and before the component is rendered for the first time.       | Use this hook to initialize the component, fetch data, or set up state that relies on input properties.    |
| **`ngDoCheck`**      | Invoked during every change detection run, allowing you to implement custom change detection logic.       | Use this hook to detect and respond to changes that Angular's default change detection might miss.          |
| **`ngAfterContentInit`** | Called once after Angular projects external content into the component's view.                           | Use this hook to perform actions after content projection is completed, such as interacting with projected content. |
| **`ngAfterContentChecked`** | Called after Angular checks the projected content for changes.                                      | Use this hook to respond after every check of projected content.                                          |
| **`ngAfterViewInit`** | Invoked once after the component's view and child views have been initialized.                            | Use this hook to interact with the view or child components, such as initializing third-party libraries that depend on the DOM. |
| **`ngAfterViewChecked`** | Called after every check of the component's view and child views.                                      | Use this hook to act after Angular has fully checked the component's view for changes.                     |
| **`ngOnDestroy`**    | Invoked just before Angular destroys the component.                                                       | Use this hook to clean up resources, unsubscribe from observables, and avoid memory leaks.                 |

### Example of Lifecycle Hook Usage

Here's a simple example of how some of these lifecycle hooks might be used in a component:

```typescript
import { Component, OnInit, OnChanges, SimpleChanges, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-example',
  templateUrl: './example.component.html',
  styleUrls: ['./example.component.css']
})
export class ExampleComponent implements OnInit, OnChanges, OnDestroy {
  
  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    console.log('ngOnChanges called', changes);
  }

  ngOnInit() {
    console.log('ngOnInit called');
    // Initialization logic, such as fetching data
  }

  ngOnDestroy() {
    console.log('ngOnDestroy called');
    // Cleanup logic, such as unsubscribing from observables
  }
}
```

### Lifecycle Hook Execution Order

The typical order of lifecycle hook execution for a component is as follows:

1. `ngOnChanges` (only if there are input properties)
2. `ngOnInit`
3. `ngDoCheck`
4. `ngAfterContentInit`
5. `ngAfterContentChecked`
6. `ngAfterViewInit`
7. `ngAfterViewChecked`
8. `ngOnDestroy`

### Importance of Understanding the Lifecycle

Understanding the lifecycle of a component is vital for writing efficient, maintainable, and bug-free Angular applications. It allows developers to:

- **Perform Initialization:** Properly initialize components with data or set up initial states.
- **Handle Changes:** Respond to changes in input properties or content projection.
- **Optimize Performance:** Use hooks like `ngOnChanges` and `ngDoCheck` to optimize change detection.

## Standalone Vs non-Standalone

In an Angular project, you can choose between the standalone and non-standalone approaches to develop components, services, and modules. Below is an explanation of the key differences between these two approaches and why the standalone approach might be preferred in some cases.

## Comparison Table

| Feature                            | Standalone                              | Non-Standalone                          |
|------------------------------------|-----------------------------------------|-----------------------------------------|
| **Modularization**                 | Does not require `NgModule`, components can stand alone and be used directly. | Requires `NgModule` to organize and manage components and services. |
| **Component Declaration**          | Components are declared within the component itself, using the `standalone: true` property. | Components must be declared in a `NgModule`. |
| **Imports**                        | Other modules and components are imported directly into the standalone component. | Imports are managed through `NgModule`, which provides the context for dependency injection. |
| **Complexity**                     | Simpler for small to medium-sized applications or micro-frontends. | More complex, suitable for large-scale applications with multiple modules. |
| **Reusability**                    | Easier to reuse standalone components across different projects. | Reusability often requires importing the entire `NgModule`. |
| **Migration**                      | Ideal for migrating individual components to a more modular approach. | Better for traditional Angular projects or large applications with shared modules. |
| **Bootstrapping**                  | Standalone components can be bootstrapped directly. | Bootstrapping is done via `NgModule`. |
| **Learning Curve**                 | Easier to learn and understand for beginners due to less boilerplate code. | Steeper learning curve due to the need to understand `NgModule` structure and dependency management. |

## Why Should We Use the Standalone Type?

The standalone approach in Angular offers several benefits, especially for specific use cases:

- **Simplicity and Efficiency:** Standalone components are more straightforward to develop and manage, as they don't require the overhead of creating and maintaining `NgModule`. This makes them ideal for small to medium-sized projects or micro-frontends.

- **Modularity:** Standalone components are self-contained, which promotes better modularity. This makes it easier to reuse components across different projects or in different parts of an application.

- **Reduced Complexity:** Without the need to manage `NgModule`, standalone components reduce the complexity of your application. This is particularly useful for teams that are focused on rapid development and prefer a more modern approach.

- **Better for Micro-Frontends:** Standalone components are well-suited for micro-frontend architectures, where individual components or small pieces of functionality are developed and deployed independently.

- **Ease of Migration:** For projects that are gradually moving towards a more modular architecture, adopting standalone components allows for an easier transition without needing to refactor large parts of the codebase.

The standalone type is a modern and streamlined approach in Angular development that simplifies component management, promotes modularity, and enhances the reusability of components, making it a recommended choice for many development scenarios.