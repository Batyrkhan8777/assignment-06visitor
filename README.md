# Advanced File System - Visitor Pattern

This repository contains a Java 17 implementation of an in-memory hierarchical file system using the **Visitor** behavioral design pattern. 

This project was developed for Assignment 06 (Software Design Patterns).

## Features
- **Object Model:** `FileNode` and `DirectoryNode` elements supporting arbitrary nesting.
- **Double Dispatch:** Proper implementation of `accept(Visitor)` delegating to overloaded `visit()` methods without using `instanceof` or runtime type discrimination.
- **Independent Operations (Visitors):**
  - `TotalSizeVisitor`: Computes the sum of file sizes for an entire subtree.
  - `SearchVisitor`: Finds files based on a name criterion and minimum-size threshold, returning lexicographically sorted root-relative paths.
  - `StatisticsVisitor`: Collects total file count, directory count, and maximum tree depth.

## Prerequisites
- **Java 17** (or newer)
- **Apache Maven**

## How to Run Tests
The project includes a comprehensive JUnit 5 test suite verifying all required acceptance criteria, including edge cases, cyclic dependencies protection, and deterministic outputs.

To run the automated tests from the terminal, navigate to the project root directory (where `pom.xml` is located) and execute:

```bash
mvn test