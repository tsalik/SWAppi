# Project Name

## Overview

This project is a solution for building a Star Wars app using Jetpack Compose and Apollo GraphQL. The purpose of this document is to explain my thought process, architecture choices, and decisions made during implementation.

---

## Project Breakdown

### 1. **Initial Steps**
- **Understanding Requirements**: I carefully reviewed the problem statement and listed out the key features to implement.
- **Defining Scope**: Focused on delivering an MVP (Minimum Viable Product) that includes list main features, e.g., fetching characters and their associated planets.
- **Planning**: Divided the work into smaller tasks for better focus and organization.
---

## Architecture Decisions

### 1. **Architecture Pattern**
I used the **MVVM (Model-View-ViewModel)** pattern because:
- It aligns well with Android's recommended architecture.
- Provides a clear separation of concerns, improving testability and maintainability.
- Did not add any `UseCases` or `Interactors` per Clean Architecture as for the purpose of the challenge it would be an overkill. 

### 2. **Key Components**
- **Jetpack Compose**: Used for building the UI declaratively.
- **Apollo GraphQL Client**: For network communication with the SWAPI GraphQL API.
- **Coroutines + Flows**: For handling asynchronous operations and reactive streams.

### 3. **Navigation**
- **Jetpack Compose Navigation**: For managing screen transitions and passing arguments between composables.

### 4. **Fetching data**
- **Do not paginate for MVP**: As I had not worked with GraphQL in the past, the most important was to focus on implementing all the requirements of the challenge. It was simpler to fetch all data and integrate with Apollo/familiarize myself with GraphQL. 

---

## Third-Party Libraries

Most of the Third-Party libraries are Google dependencies around Compose, and the Apollo Kotlin dependency for the GraphQL queries.

One exception is the usage of [Arrow's](https://github.com/arrow-kt/arrow) `Either`. I could have used the built-in Kotlin `Result`, but the drawback is that the failure needs to be always an exception.
I could also roll down my own `Either`-like implementation. I decided to use a well-known library to avoid any confusion. In a production app I would go over with my colleagues and decide what's best for our use case. 

---

## Improvements

Given more time, I would consider implementing the following:
- **Testing**:
    - Add more comprehensive unit tests 
    - Add Espresso UI tests.
    - Add screenshot tests.
- **Caching**:
    - Use a local database (e.g., Room) to cache data for offline use.
- **Pagination**:
    - Implement pagination for large datasets to optimize performance.
- **Theming**:
    - Provide support for dynamic theming and a dark mode.
- **Error Differentiation**:
    - Enhance error handling with more granular feedback for API and network failures.

---

## Thought Process

1. **Problem Understanding**: I broke down the challenge into modular tasks.
2. **Research**: Looked for the best tools and libraries suited to the problem.
3. **Iterative Development**: Followed an iterative approach, focusing on one feature at a time.

---
