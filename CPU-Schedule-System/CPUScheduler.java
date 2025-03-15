import java.util.*;

class Process {
    String id;
    int arrivalTime, burstTime, remainingTime, waitingTime, turnaroundTime, completionTime;

    Process(String id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class CPUScheduler {
    
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nCPU Scheduling Algorithms:");
            System.out.println("1. First-Come, First-Served (FCFS)");
            System.out.println("2. Shortest-Job-First (SJF)");
            System.out.println("3. Shortest-Remaining-Time (SRT)");
            System.out.println("4. Round Robin (RR)");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            if (choice == 5) break;
            
            List<Process> processes = getProcessInput(); // Get user input for processes
            
            switch (choice) {
                case 1: fcfsScheduling(processes); break;
                case 2: sjfScheduling(processes); break;
                case 3: srtScheduling(processes); break;
                case 4: roundRobinScheduling(processes); break;
                default: System.out.println("Invalid option! Try again.");
            }
        }
    }

    // Function to get user input for process
    private static List<Process> getProcessInput() {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        List<Process> processes = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Process ID: ");
            String id = scanner.next();
            System.out.print("Enter Arrival Time: ");
            int at = scanner.nextInt();
            System.out.print("Enter Burst Time: ");
            int bt = scanner.nextInt();

            // Validate input
            if (at < 0 || bt <= 0) {
                System.out.println("Invalid input! Arrival Time must be >= 0 and Burst Time > 0.");
                i--;    //repeat input for this process
                continue;
            }
            processes.add(new Process(id, at, bt));
        }
        return processes;
    }

    // First come first serve  (FCFS) Scheduling Algorithm
    private static void fcfsScheduling(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));   //Sort by arrival time 
        int currentTime = 0;
        List<String> gantt = new ArrayList<>();
        List<Integer> times = new ArrayList<>();

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) { // If CPU is idle
                gantt.add("Idle");
                times.add(currentTime);
                currentTime = p.arrivalTime;
            }
            gantt.add(p.id);
            times.add(currentTime);
            p.completionTime = currentTime + p.burstTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            currentTime = p.completionTime;
        }
        times.add(currentTime);
        printResults("FCFS", processes, gantt, times);
    }


    //Shortest-job First (Non-preemptive SJF) Scheduling Algorithm
    private static void sjfScheduling(List<Process> processes) {
        int currentTime = 0, completed = 0;
        List<String> gantt = new ArrayList<>();
        List<Integer> times = new ArrayList<>();

        while (completed < processes.size()) {
            Process shortest = null;
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (shortest == null || p.burstTime < shortest.burstTime) {
                        shortest = p;
                    }
                }
            }
            if (shortest == null) {
                currentTime++;
                continue;
            }
            gantt.add(shortest.id);
            times.add(currentTime);

            shortest.completionTime = currentTime + shortest.burstTime;
            shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
            shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
            currentTime += shortest.burstTime;
            shortest.remainingTime = 0;
            completed++;
        }
        times.add(currentTime);
        printResults("SJF", processes, gantt, times);
    }

    //Shortest Remaining Time (Preemptive SJF) Scheduling Algorithm
    private static void srtScheduling(List<Process> processes) {
        int currentTime = 0, completed = 0;
        Process currentProcess = null;
        List<String> gantt = new ArrayList<>();
        List<Integer> times = new ArrayList<>();

        while (completed < processes.size()) {
            Process shortest = null;
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && (shortest == null || p.remainingTime < shortest.remainingTime)) {
                    shortest = p;
                }
            }
            if (shortest == null) {
                currentTime++;
                continue;
            }
            if (currentProcess != shortest) {
                gantt.add(shortest.id);
                times.add(currentTime);
                currentProcess = shortest;
            }
            shortest.remainingTime--;
            if (shortest.remainingTime == 0) {
                completed++;
                shortest.completionTime = currentTime + 1;
                shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
            }
            currentTime++;
        }
        times.add(currentTime);
        printResults("SRT", processes, gantt, times);
    }

    //Round Robin Scheduling Algorithm
    private static void roundRobinScheduling(List<Process> processes) {
        System.out.print("Enter Time Quantum: ");
        int timeQuantum = scanner.nextInt();

        Queue<Process> queue = new LinkedList<>();
        List<String> gantt = new ArrayList<>();
        List<Integer> times = new ArrayList<>();
        int currentTime = 0, completed = 0;

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        queue.addAll(processes);

        while (!queue.isEmpty()) {
            Process p = queue.poll();
            if (p.arrivalTime > currentTime) {
                gantt.add("Idle");
                times.add(currentTime);
                currentTime = p.arrivalTime;    //Handle Idle time
            }
            gantt.add(p.id);
            times.add(currentTime);

            int executionTime = Math.min(timeQuantum, p.remainingTime);
            currentTime += executionTime;
            p.remainingTime -= executionTime;

            if (p.remainingTime == 0) {
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
                completed++;
            } else {
                queue.add(p);   //Add back if process is not complete
            }
        }
        times.add(currentTime);
        printResults("Round Robin", processes, gantt, times);
    }

    // Function to print scheduling results
    private static void printResults(String algorithm, List<Process> processes, List<String> gantt, List<Integer> times) {
        System.out.println("\n" + algorithm + " Scheduling Results:");
        System.out.println("PID  AT  BT  CT  TAT  WT"); 

        double totalWT = 0, totalTAT = 0;

        for (Process p : processes) {
            System.out.printf("%s    %d   %d   %d   %d    %d\n", p.id, p.arrivalTime, p.burstTime, p.completionTime, p.turnaroundTime, p.waitingTime);
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        double avgWT = totalWT / processes.size();
        double avgTAT = totalTAT / processes.size();

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", avgWT));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", avgTAT));

        System.out.println("\nGantt Chart:");
        for (String process : gantt) {
            System.out.print("| " + process + " ");
        }
        System.out.println("|");
        for (int time : times) {
            System.out.print(time + "   ");
        }
        System.out.println();
    }
}
