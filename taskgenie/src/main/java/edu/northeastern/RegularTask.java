package edu.northeastern;
import java.time.LocalDate;
/**
 * Implementation of Task representing a regular task.
 */
public class RegularTask extends Task {
    
    /**
     * Constructs a regular task with the specified properties.
     *
     * @param name Task name
     * @param description Task description
     * @param priority Task priority
     * @param deadline Task deadline
     * @param category Task category
     */
    public RegularTask(String name, String description, Priority priority, LocalDate deadline, Category category) {
        super(name, description, priority, deadline, category);
    }

    @Override
    public String getTaskType() {
        return "Regular Task";
    }
}
