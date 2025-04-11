package edu.northeastern;
/**
 * Represents the priority level of a task.
 */
public enum Priority {
    HIGH("High"), 
    MEDIUM("Medium"), 
    LOW("Low");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
