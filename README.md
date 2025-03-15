# CPU Scheduling Simulator

## Overview
This project is a CPU Scheduling Simulator that implements four scheduling algorithms:
- **First-Come, First-Served (FCFS)**
- **Shortest Job First (SJF)**
- **Shortest Remaining Time (SRT)**
- **Round Robin (RR)**

The simulator takes user input for processes, including arrival time and burst time, and schedules them based on the selected algorithm. It then outputs the Gantt Chart, waiting times, turnaround times, and average times for evaluation.

## Features
- Supports multiple CPU scheduling algorithms.
- Displays a **Gantt Chart** to visualize scheduling order.
- Computes **waiting times, turnaround times**, and their averages.
- Handles idle time when no processes are available for execution.
- Supports **time quantum input** for Round Robin scheduling.

## Prerequisites
- Java Development Kit (JDK) 8 or later
- A Java IDE (Eclipse, IntelliJ, or VS Code) or a command-line terminal

## How to Run
1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-username/CPU-Scheduling-Simulator.git
   cd CPU-Scheduling-Simulator
   ```
2. **Compile the Java program:**
   ```sh
   javac CPUScheduler.java
   ```
3. **Run the program:**
   ```sh
   java CPUScheduler
   ```
4. **Follow the on-screen instructions** to enter process details and select a scheduling algorithm.

## Example Output
### Input:
```
Number of processes: 4
Process ID: P1, Arrival Time: 0, Burst Time: 6
Process ID: P2, Arrival Time: 2, Burst Time: 8
Process ID: P3, Arrival Time: 4, Burst Time: 7
Process ID: P4, Arrival Time: 6, Burst Time: 3
Scheduling Algorithm: FCFS
```

### Output:
```
FCFS Scheduling Results:
PID   AT  BT  CT  TAT  WT
P1    0   6   6   6    0
P2    2   8   14  12   4
P3    4   7   21  17   10
P4    6   3   24  18   15

Average Waiting Time: 7.25
Average Turnaround Time: 13.25

Gantt Chart:
| P1 | P2 | P3 | P4 |
0    6    14   21   24
```
