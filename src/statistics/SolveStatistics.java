package statistics;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolveStatistics {
    private String puzzleName;
    private long solveTimeMs;
    private boolean solved;
    private int backtrackCount;
    private int totalMoveAttempts;
    private int invalidMoveAttempts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public  SolveStatistics(String puzzleName) {
        this.puzzleName = puzzleName;
        solveTimeMs = System.currentTimeMillis();
        solved = false;
        backtrackCount = 0;
        totalMoveAttempts = 0;
        invalidMoveAttempts = 0;
        startTime = LocalDateTime.now();
    }
    public void recordStart() {
        startTime=LocalDateTime.now();
    }
    public void recordEnd(boolean solved) {
        endTime=LocalDateTime.now();
        this.solved = solved;
        solveTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
    }
    public void incrementBacktrack() {
        backtrackCount++;
    }
    public void incrementTotalMoveAttempts() {
        totalMoveAttempts++;
    }
    public void incrementInvalidMoveAttempts() {
        invalidMoveAttempts++;
    }
    public double getSolveTimeSeconds() {
        return solveTimeMs / 1000.0;
    }
    public int getValidMoveAttempts() {
        return totalMoveAttempts - invalidMoveAttempts;
    }
    public double EfficiencyRate() {
        if (totalMoveAttempts == 0) return 0.0;
        return (double) getValidMoveAttempts() / totalMoveAttempts * 100;
    }

    @Override   //override the default to String
    public String toString() {      //Polymorphism
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder obj = new StringBuilder();  //empty string to use append() method to design string the way u want
        obj.append("═══════════════════════════════════════════════\n");
        obj.append("             SOLUTION STATISTICS\n");
        obj.append("═══════════════════════════════════════════════\n");
        obj.append(String.format("Puzzle Name: " + puzzleName + "\n"));
        obj.append(String.format("Status:            %s\n", solved ? "✓ SOLVED" : "✗ UNSOLVED"));  // %s: placeholder for the result
        obj.append(String.format("Solve Time:        %.3f seconds \n", getSolveTimeSeconds()));
        obj.append(String.format("Backtrack Count:    %d\n", backtrackCount));
        obj.append(String.format("Total Move Attempts: %d\n", totalMoveAttempts));
        obj.append(String.format("Invalid Move Attempts: %d\n", invalidMoveAttempts));
        obj.append(String.format("Valid Move Attempts: %d\n", getValidMoveAttempts()));
        obj.append(String.format("Efficiency Rate: %.2f\n", EfficiencyRate()));
        obj.append(String.format("Start Time: %s\n", startTime.format(formatter)));
        if (endTime != null) {
            obj.append(String.format("End Time: %s\n", endTime.format(formatter)));
        }
        obj.append("═══════════════════════════════════════════════");
        return obj.toString();
    }
}


