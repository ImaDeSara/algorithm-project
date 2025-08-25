# Overview
This project implements a pathfinding algorithm to solve sliding puzzles on frictionless ice surfaces, commonly seen in video games. 
The goal is to move from the start position (S) to the finish position (F) while navigating around rocks (0). 
Due to the frictionless surface, the player slides in the chosen direction until they hit a wall or an obstacle, making the reachability and shortest-path computation different from standard pathfinding problems.

# Features
## 1. Map Representation (Task 2)
- Uses an efficient 2D array (or vector-based) data structure to represent the puzzle grid.
- Supports:
- Empty ice squares (.)
- Rocks (0)
- Start (S) and Finish (F) positions
- Stores width, height, start, and finish coordinates for fast lookup.

## 2. Map Parsing (Task 3)
- Reads puzzle maps from an input file in the provided format.
- Automatically detects:
- Grid dimensions
-  Start (S) and Finish (F) locations
-  Rock positions (0)
- Handles variable map sizes and different obstacle layouts.

## 3. Pathfinding Algorithm (Task 4)
- Implements a shortest-path search algorithm designed for sliding puzzles.
- Candidate algorithms:
- Breadth-First Search (BFS) – Guarantees shortest path.
- Optimized with precomputed sliding moves to improve efficiency.
- Outputs the complete step-by-step solution:
- Direction of movement
- Coordinates reached after each slide
- Total number of moves
