# Enigma

A Java implementation of the World War II German Enigma encryption machine simulator. This project provides a detailed simulation of the mechanical-electrical mechanisms of the original Enigma machine, including rotors, reflectors, and the stepping mechanism.

## Overview

The Enigma machine was a sophisticated encryption device used by German forces during World War II. This simulator recreates the functionality of the historical Enigma machine, implementing its key features:

- Substitution cipher mechanism
- Progressive substitution for each letter
- Configurable rotor system
- Reflector mechanism
- Double-stepping feature

## Features

- Configurable number of rotors (N)
- Support for multiple rotor types
- Reflector implementation
- Moving and Fixed rotor support
- Comprehensive permutation system
- Full alphabet support
- Detailed testing suite

## Technical Details

### Technologies Used

- Java
- JUnit (for testing)
- Make (build system)

### Project Structure

```
enigma/
├── Machine.java       # Main Enigma machine implementation
├── Rotor.java        # Base rotor class
├── MovingRotor.java  # Implementation of moving rotors
├── Permutation.java  # Permutation logic
├── TestUtils.java    # Testing utilities
└── Makefile         # Build configuration
```

### Key Components

1. **Machine**: The main class that simulates the Enigma machine
2. **Rotor**: Implements the rotor mechanism with permutations
3. **MovingRotor**: Handles the rotating mechanism of the rot ors
4. **Permutation**: Manages the letter substitution logic
5. **Reflector**: Implements the reflection mechanism

## Building and Testing

### Prerequisites

- Java Development Kit (JDK)
- Make

### Build Instructions

```bash
make
```

### Running Tests

```bash
make check
```

## Usage

The simulator can be configured with different initial settings and can both encode and decode messages. The encryption is reciprocal, meaning the same settings will both encrypt and decrypt messages.

## License

This project is licensed under the included LICENSE file.

## Acknowledgments

This implementation is based on the historical Enigma machine used during World War II. The project structure and testing framework were designed to ensure historical accuracy while maintaining modern software engineering practices. Project from UC Berkeley's CS 61B Course
