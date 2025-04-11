package edu.northeastern;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DefaultStringConverter;

/**
 * Controller for the main Task Genie UI.
 */
public class TaskManagerController implements Initializable {

    @FXML private ImageView logoImageView;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, Boolean> completedCol;
    @FXML private TableColumn<Task, String> nameCol;
    @FXML private TableColumn<Task, String> descriptionCol;
    @FXML private TableColumn<Task, Priority> priorityCol;
    @FXML private TableColumn<Task, LocalDate> deadlineCol;
    @FXML private TableColumn<Task, String> categoryCol;
    
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    
    @FXML private ComboBox<Priority> filterPriorityCombo;
    @FXML private ComboBox<String> filterCategoryCombo;
    @FXML private CheckBox showCompletedCheck;
    @FXML private CheckBox showOverdueCheck;
    
    @FXML private Label totalTasksLabel;
    @FXML private Label completedTasksLabel;
    @FXML private Label overdueTasksLabel;
    @FXML private Label highPriorityLabel;
    @FXML private ProgressBar completionProgressBar;
    
    private TaskService taskService;
    
    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskService = new TaskService();
        
        setupComboBoxes();
        setupTableColumns();
        setupFilters();
        
        // Connect table to task service
        taskTable.setItems(taskService.getFilteredTasks());
        
        // Make table editable
        taskTable.setEditable(true);
        
        // Set column resize policy
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Initial statistics update
        updateStatistics();
    }
    
    /**
     * Sets up the combo boxes with appropriate values.
     */
    private void setupComboBoxes() {
        // Priority combo box
        filterPriorityCombo.getItems().add(null); // null represents "All Priorities"
        filterPriorityCombo.getItems().addAll(Priority.values());
        filterPriorityCombo.setValue(null); // Default to "All Priorities"
        
        // Custom cell factory to display "All Priorities" for null
        filterPriorityCombo.setCellFactory(lv -> new ListCell<Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "All Priorities" : item.toString());
            }
        });
        
        filterPriorityCombo.setButtonCell(new ListCell<Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "All Priorities" : item.toString());
            }
        });
        
        // Category combo box
        filterCategoryCombo.getItems().add("All Categories");
        filterCategoryCombo.getItems().addAll(taskService.getCategories().keySet());
        filterCategoryCombo.setValue("All Categories");
    }
    
    /**
     * Sets up the table columns with appropriate cell factories and edit commit handlers.
     */
    private void setupTableColumns() {
        // Completed column with checkbox
        completedCol.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            SimpleBooleanProperty prop = new SimpleBooleanProperty(task.isCompleted());
            
            // Update the task when the checkbox is changed
            prop.addListener((obs, oldVal, newVal) -> {
                task.setCompleted(newVal);
                updateStatistics();
                taskTable.refresh();
            });
            
            return prop;
        });
        completedCol.setCellFactory(column -> new CheckBoxTableCell<>());
        completedCol.setEditable(true);

        // Name column - Editable
        nameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setCellFactory(column -> new TextFieldTableCell<Task, String>(new DefaultStringConverter()) {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Task task = getTableView().getItems().get(getIndex());
                    if (task.isCompleted()) {
                        getStyleClass().add("completed-task");
                    } else {
                        getStyleClass().remove("completed-task");
                    }
                }
            }
        });
        nameCol.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            String newValue = event.getNewValue().trim();
            if (!newValue.isEmpty()) {
                task.setName(newValue);
                taskTable.refresh();
            } else {
                // If empty name, revert to old value
                taskTable.refresh();
                showAlert("Error", "Task name cannot be empty!");
            }
        });
        nameCol.setEditable(true);

        // Description column - Editable
        descriptionCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDescription()));
        descriptionCol.setCellFactory(column -> new TextFieldTableCell<Task, String>(new DefaultStringConverter()) {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Task task = getTableView().getItems().get(getIndex());
                    if (task.isCompleted()) {
                        getStyleClass().add("completed-task");
                    } else {
                        getStyleClass().remove("completed-task");
                    }
                }
            }
        });
        descriptionCol.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setDescription(event.getNewValue());
            taskTable.refresh();
        });
        descriptionCol.setEditable(true);

        // Priority column - Editable with ComboBox
        ObservableList<Priority> priorities = FXCollections.observableArrayList(Priority.values());
        priorityCol.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getPriority()));
        priorityCol.setCellFactory(column -> new ComboBoxTableCell<Task, Priority>(priorities) {
            @Override
            public void updateItem(Priority priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(priority.toString());
                    Task task = getTableView().getItems().get(getIndex());
                    
                    // Apply completed style if task is completed
                    if (task.isCompleted()) {
                        getStyleClass().add("completed-task");
                    } else {
                        getStyleClass().remove("completed-task");
                        
                        // Apply priority styling only if not completed
                        if (priority == Priority.HIGH) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else if (priority == Priority.MEDIUM) {
                            setStyle("-fx-text-fill: orange;");
                        } else {
                            setStyle("-fx-text-fill: green;");
                        }
                    }
                }
            }
        });
        priorityCol.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setPriority(event.getNewValue());
            updateStatistics();
            taskTable.refresh();
        });
        priorityCol.setEditable(true);

        // Deadline column
        deadlineCol.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        deadlineCol.setCellFactory(column -> new TableCell<Task, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();
            
            {
                // Configure date picker
                datePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(date.isBefore(LocalDate.now()));
                    }
                });
                
                // Handle date selection
                datePicker.setOnAction(e -> {
                    commitEdit(datePicker.getValue());
                });
            }
            
            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    datePicker.setValue(getItem());
                    setText(null);
                    setGraphic(datePicker);
                }
            }
            
            @Override
            public void cancelEdit() {
                super.cancelEdit();
                updateItem(getItem(), false);
            }
            
            @Override
            public void commitEdit(LocalDate newValue) {
                super.commitEdit(newValue);
                Task task = getTableView().getItems().get(getIndex());
                task.setDeadline(newValue);
                updateStatistics();
                taskTable.refresh();
            }
            
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                
                if (empty || date == null) {
                    setText(null);
                    setStyle("");
                    setGraphic(null);
                } else {
                    setGraphic(null); // Reset graphic to null when not in edit mode
                    setText(date.format(DateTimeFormatter.ISO_DATE));
                    
                    Task task = getTableView().getItems().get(getIndex());
                    if (task.isCompleted()) {
                        getStyleClass().add("completed-task");
                    } else {
                        getStyleClass().remove("completed-task");
                        
                        // Only apply overdue styling if not completed
                        if (date.isBefore(LocalDate.now())) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            }
        });
        deadlineCol.setEditable(true);

        // Category column - Editable with ComboBox
        ObservableList<String> categoryNames = FXCollections.observableArrayList(taskService.getCategories().keySet());
        categoryCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCategory().getName()));
        categoryCol.setCellFactory(column -> new ComboBoxTableCell<Task, String>(new DefaultStringConverter(), categoryNames) {
            @Override
            public void updateItem(String category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(category);
                    Task task = getTableView().getItems().get(getIndex());
                    if (task.isCompleted()) {
                        getStyleClass().add("completed-task");
                    } else {
                        getStyleClass().remove("completed-task");
                    }
                }
            }
        });
        categoryCol.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            Category category = taskService.getCategories().get(event.getNewValue());
            task.setCategory(category);
            taskTable.refresh();
        });
        categoryCol.setEditable(true);
    }
    
    /**
     * Sets up the filter controls and their event handlers.
     */
    private void setupFilters() {
        // Set initial state
        showCompletedCheck.setSelected(true);
        
        // Add listeners to filter controls
        filterPriorityCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterCategoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        showCompletedCheck.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        showOverdueCheck.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }
    
    /**
     * Applies filters to the task list based on the selected filter options.
     */
    private void applyFilters() {
        taskService.setFilterPredicate(task -> {
            // Priority filter - null means "All Priorities"
            Priority selectedPriority = filterPriorityCombo.getValue();
            if (selectedPriority != null && !task.getPriority().equals(selectedPriority)) {
                return false;
            }
            
            // Category filter
            if (!filterCategoryCombo.getValue().equals("All Categories") && 
                !task.getCategory().getName().equals(filterCategoryCombo.getValue())) {
                return false;
            }
            
            // Completed filter
            if (!showCompletedCheck.isSelected() && task.isCompleted()) {
                return false;
            }
            
            // Overdue filter
            if (showOverdueCheck.isSelected() && 
                (!task.getDeadline().isBefore(LocalDate.now()) || task.isCompleted())) {
                return false;
            }
            
            return true;
        });
        
        updateStatistics();
    }
    
    /**
     * Shows a dialog to add a new task.
     *
     * @param event The action event
     */
    @FXML
    private void handleAddTaskDialog(ActionEvent event) {
        // Create a custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");
        dialog.setHeaderText("Create New Task");
        
        // Create the layout for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Create form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Task name");
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);
        
        ComboBox<Priority> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(Priority.values());
        priorityBox.setValue(Priority.MEDIUM);
        
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(taskService.getCategories().keySet());
        if (!categoryBox.getItems().isEmpty()) {
            categoryBox.setValue(categoryBox.getItems().get(0));
        }
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        
        // Add fields to the grid
        grid.add(new Label("Task Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Priority:"), 0, 2);
        grid.add(priorityBox, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryBox, 1, 3);
        grid.add(new Label("Deadline:"), 0, 4);
        grid.add(datePicker, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Wait for the user's response
        Optional<ButtonType> result = dialog.showAndWait();
        
        // Process the result
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showAlert("Error", "Task name cannot be empty!");
                return;
            }
            
            String description = descArea.getText();
            Priority priority = priorityBox.getValue();
            String categoryName = categoryBox.getValue();
            LocalDate deadline = datePicker.getValue();
            
            taskService.addTask(name, description, priority, deadline, categoryName);
            updateStatistics();
        }
    }

    /**
     * Handles the delete task button click event.
     *
     * @param event The action event
     */
    @FXML
    private void handleDeleteTask(ActionEvent event) {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskService.deleteTask(selected);
            updateStatistics();
        } else {
            showAlert("Error", "No task selected!");
        }
    }

    /**
     * Updates the statistics labels with current task counts.
     */
    private void updateStatistics() {
        long total = taskService.getTotalTaskCount();
        long completed = taskService.getCompletedTaskCount();
        long overdue = taskService.getOverdueTaskCount();
        long highPriority = taskService.getHighPriorityTaskCount();

        // Update label texts - numerical values only for card style
        totalTasksLabel.setText(String.valueOf(total));
        completedTasksLabel.setText(String.valueOf(completed));
        overdueTasksLabel.setText(String.valueOf(overdue));
        highPriorityLabel.setText(String.valueOf(highPriority));
        
        // Update completion progress bar
        double completionRate = total > 0 ? (double) completed / total : 0;
        completionProgressBar.setProgress(completionRate);
        
        // Add tooltips with more detailed information
        totalTasksLabel.setTooltip(new Tooltip("Total Tasks: " + total));
        completedTasksLabel.setTooltip(new Tooltip("Completed: " + completed + 
            " (" + (total > 0 ? (completed * 100 / total) : 0) + "%)"));
        overdueTasksLabel.setTooltip(new Tooltip("Overdue Tasks: " + overdue));
        highPriorityLabel.setTooltip(new Tooltip("High Priority Tasks: " + highPriority));
    }

    /**
     * Shows an alert dialog with the specified title and message.
     *
     * @param title The alert title
     * @param message The alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}