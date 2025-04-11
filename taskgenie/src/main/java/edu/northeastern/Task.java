package edu.northeastern;
import java.time.LocalDate;
import javafx.beans.property.*;
/**
 * Abstract base class for tasks in the task management system.
 */
public abstract class Task {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<Priority> priority = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> deadline = new SimpleObjectProperty<>();
    private final BooleanProperty completed = new SimpleBooleanProperty(false);
    private final ObjectProperty<Category> category = new SimpleObjectProperty<>();

    /**
     * Constructs a task with the specified properties.
     *
     * @param name Task name
     * @param description Task description
     * @param priority Task priority
     * @param deadline Task deadline
     * @param category Task category
     */
    public Task(String name, String description, Priority priority, LocalDate deadline, Category category) {
        this.name.set(name);
        this.description.set(description);
        this.priority.set(priority);
        this.deadline.set(deadline);
        this.category.set(category);
    }

    /**
     * Returns the type of task.
     *
     * @return A string representing the task type
     */
    public abstract String getTaskType();

    // Name property
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    // Description property
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    // Priority property
    public Priority getPriority() { return priority.get(); }
    public void setPriority(Priority priority) { this.priority.set(priority); }
    public ObjectProperty<Priority> priorityProperty() { return priority; }

    // Deadline property
    public LocalDate getDeadline() { return deadline.get(); }
    public void setDeadline(LocalDate deadline) { this.deadline.set(deadline); }
    public ObjectProperty<LocalDate> deadlineProperty() { return deadline; }

    // Completed property
    public boolean isCompleted() { return completed.get(); }
    public void setCompleted(boolean completed) { this.completed.set(completed); }
    public BooleanProperty completedProperty() { return completed; }

    // Category property
    public Category getCategory() { return category.get(); }
    public void setCategory(Category category) { this.category.set(category); }
    public ObjectProperty<Category> categoryProperty() { return category; }

    /**
     * Checks if the task is overdue.
     *
     * @return true if the task is overdue and not completed
     */
    public boolean isOverdue() {
        return !isCompleted() && getDeadline().isBefore(LocalDate.now());
    }
}
