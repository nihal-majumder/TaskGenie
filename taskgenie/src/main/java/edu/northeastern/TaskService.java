package edu.northeastern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Service class that manages tasks and categories.
 * Contains business logic for task operations.
 */
public class TaskService {
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final FilteredList<Task> filteredTasks = new FilteredList<>(tasks);
    private final Map<String, Category> categories = new HashMap<>();
    
    /**
     * Initializes the task service with default categories.
     */
    public TaskService() {
        initializeDefaultCategories();
    }
    
    /**
     * Creates the default set of task categories.
     */
    private void initializeDefaultCategories() {
        categories.put("Work", new Category("Work"));
        categories.put("Personal", new Category("Personal"));
        categories.put("Study", new Category("Study"));
        categories.put("Health", new Category("Health"));
        categories.put("Finance", new Category("Finance"));
    }
    
    /**
     * Gets the observable list of all tasks.
     *
     * @return ObservableList of all tasks
     */
    public ObservableList<Task> getTasks() {
        return tasks;
    }
    
    /**
     * Gets the filtered list of tasks based on current filter predicates.
     *
     * @return FilteredList of tasks
     */
    public FilteredList<Task> getFilteredTasks() {
        return filteredTasks;
    }
    
    /**
     * Gets the map of all categories.
     *
     * @return Map of category names to Category objects
     */
    public Map<String, Category> getCategories() {
        return categories;
    }
    
    /**
     * Adds a new task to the task list.
     *
     * @param name Task name
     * @param description Task description
     * @param priority Task priority
     * @param deadline Task deadline
     * @param categoryName Category name
     * @return The created task
     */
    public Task addTask(String name, String description, Priority priority, 
                         LocalDate deadline, String categoryName) {
        Category category = categories.get(categoryName);
        Task task = new RegularTask(name, description, priority, deadline, category);
        tasks.add(task);
        return task;
    }
    
    /**
     * Deletes a task from the task list.
     *
     * @param task The task to delete
     * @return true if the task was deleted, false otherwise
     */
    public boolean deleteTask(Task task) {
        return tasks.remove(task);
    }
    
    /**
     * Sets the filter predicate for the filtered task list.
     *
     * @param predicate The predicate to use for filtering
     */
    public void setFilterPredicate(Predicate<Task> predicate) {
        filteredTasks.setPredicate(predicate);
    }
    
    /**
     * Gets the count of tasks matching the given predicate.
     *
     * @param predicate The predicate to match tasks against
     * @return The count of matching tasks
     */
    public long getTaskCount(Predicate<Task> predicate) {
        return tasks.stream().filter(predicate).count();
    }
    
    /**
     * Gets the total number of tasks.
     *
     * @return The total task count
     */
    public int getTotalTaskCount() {
        return tasks.size();
    }
    
    /**
     * Gets the count of completed tasks.
     *
     * @return The completed task count
     */
    public long getCompletedTaskCount() {
        return getTaskCount(Task::isCompleted);
    }
    
    /**
     * Gets the count of overdue tasks.
     *
     * @return The overdue task count
     */
    public long getOverdueTaskCount() {
        return getTaskCount(task -> !task.isCompleted() && task.getDeadline().isBefore(LocalDate.now()));
    }
    
    /**
     * Gets the count of high priority tasks.
     *
     * @return The high priority task count
     */
    public long getHighPriorityTaskCount() {
        return getTaskCount(task -> task.getPriority() == Priority.HIGH);
    }
}
