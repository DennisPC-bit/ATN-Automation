package GUI;

import BE.*;
import BLL.PersonManager;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author DennisPC-bit
 */

public class ViewController implements Initializable {
    public TextField field;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button attendBtn;
    @FXML
    private BorderPane centerPane;
    @FXML
    private Label dateLabel;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Button btn;
    private Main main;
    private ArrayList<Person> personList = new ArrayList<>();
    private final static PersonManager personManager = new PersonManager();
    private Person selectedPerson;
    private ObjectProperty<LocalDateTime> dateTimeObjectProperty = new SimpleObjectProperty<>();
    private ComboBox<String> comboBox;
    private BorderPane borderPane = new BorderPane();
    private ComboBox<String> personComboBox;
    private final ContextMenu contextMenuPerson = new ContextMenu();
    private final ContextMenu contextMenuStats = new ContextMenu();
    private static final PieChartUtils PIE_CHART_UTILS = new PieChartUtils();
    private static final BarChartUtils BAR_CHART_UTILS = new BarChartUtils();
    EditPane editPane;
    Button confirmButton = new Button("Confirm");
    Button cancelButton = new Button("Cancel");
    StringProperty confirmationType = new SimpleStringProperty("");
    private static final String NEW_CONFIRMATION = "new";
    private static final String EDIT_CONFIRMATION = "edit";
    private static final String DELETE_CONFIRMATION = "del";
    Thread thread = new Thread();

    public void setMain(Main main) {
        this.main = main;
    }

    public ViewController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPersonView();
        selectPerson();
        autoUpdateTime();
        statSwitch();
        personSwitch();
        initContextMenuPerson();
        initContextMenuStats();
        confirmationFunctionality();
    }

    private void confirmationFunctionality() {
        confirmationType.addListener(a -> {
            switch (confirmationType.get()) {
                case (NEW_CONFIRMATION):
                    confirmButton.setOnAction(v -> {
                        if (!editPane.getTextFields().isEmpty() && !editPane.getTextFields().get(1).getText().isBlank()) {
                            newPerson(editPane.getTextFields().get(1).getText());
                            showAttendance();
                        } else
                            editPane.get().setTop(new Label("Come on a man's gotta have a name"));
                    });
                    cancelButton.setOnAction(v -> showAttendance());
                    break;
                case (EDIT_CONFIRMATION):
                    confirmButton.setOnAction(d -> {
                        if (!editPane.getTextFields().isEmpty() && !editPane.getTextFields().get(0).getText().isBlank()) {
                            selectedPerson.setName(editPane.getTextFields().get(0).getText());
                            showAttendance();
                        } else
                            editPane.get().setTop(new Label("Come on a man's gotta have a name"));
                    });
                    cancelButton.setOnAction(e -> showAttendance());
                    break;
                case (DELETE_CONFIRMATION):
                    confirmButton.setOnAction(d -> {
                                flowPane.getChildren().remove(selectedPerson.getPersonPane());
                                personList.remove(selectedPerson);
                                selectedPerson = null;
                                centerPane.setTop(menuBar);
                            }
                    );
                    cancelButton.setOnAction(d -> {
                        centerPane.setTop(menuBar);
                    });
                    break;
                default:
                    break;
            }
        });
    }

    private void initContextMenuStats() {
        List<MenuItem> menuItems = Arrays.asList(new MenuItemBit("go Back", v -> showAttendance()).getMenuItem());
        menuItems.forEach(e -> contextMenuStats.getItems().add(e));

        borderPane.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY))
                contextMenuStats.show(main.getPrimaryStage());
            else
                contextMenuStats.hide();
        });
    }

    private void initContextMenuPerson() {
        List<MenuItem> menuItems = Arrays.asList(
                new MenuItemBit("Attend", v -> attendSchool()).getMenuItem()
                , new SeparatorMenuItem()
                , new MenuItemBit("New Person", v -> newPerson()).getMenuItem()
                , new MenuItemBit("Edit Person", v -> editPerson()).getMenuItem()
                , new MenuItemBit("Delete Person", v -> deletePerson()).getMenuItem()
                , new SeparatorMenuItem()
                , new MenuItemBit("Show graph", v -> {
                    showStats();
                    centerPane.setCenter(BAR_CHART_UTILS.getTotalAttendanceBarChart(selectedPerson));
                }).getMenuItem()
                , new MenuItemBit("Show pie", v -> {
                    showStats();
                    centerPane.setCenter(PIE_CHART_UTILS.getPersonPieChart(selectedPerson));
                }).getMenuItem());
        menuItems.forEach(e -> contextMenuPerson.getItems().add(e));
    }

    private void personSwitch() {
        personComboBox = new ComboBox<String>();
        personComboBox.getItems().add("All");
        personList.forEach(p -> {
            if (!personComboBox.getItems().contains(p.getName()))
                personComboBox.getItems().add(p.getName());
        });
        personComboBox.setOnAction(c -> {
            String name = personComboBox.getSelectionModel().getSelectedItem();
            if (name == "All") {
                String selectedItem = comboBox.getSelectionModel().getSelectedItem();
                if (comboBox.getSelectionModel().getSelectedItem().equals("Person Attendance"))
                    BAR_CHART_UTILS.getTotalAttendanceBarChart(personList);
                else if (selectedItem.equals("PieChart"))
                    PIE_CHART_UTILS.getPersonPieChart(personList);
            }
            personList.forEach(p -> {
                        if (p.getName() == name) {
                            switch (personComboBox.getSelectionModel().getSelectedItem()) {
                                case ("Person Attendance") -> borderPane.setCenter(BAR_CHART_UTILS.getTotalAttendanceBarChart(p));
                                case ("PieChart") -> borderPane.setCenter(PIE_CHART_UTILS.getPersonPieChart(p));
                                default -> borderPane.setCenter(new AnchorPane());
                            }
                        }
                    }
            );
        });
    }

    private void statSwitch() {
        comboBox = new ComboBox<String>();
        borderPane.setCenter(BAR_CHART_UTILS.getTotalIndividualAttendanceBarChart(personList));
        comboBox.getItems().addAll("Total Attendance", "Person Attendance", "PieChart", "");
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(c -> {
            if (comboBox.getSelectionModel().getSelectedItem().equals("Person Attendance")
                    || comboBox.getSelectionModel().getSelectedItem().equals("PieChart")) {
                borderPane.setTop(new FlowPane(comboBox, personComboBox));
                personComboBox.getSelectionModel().clearSelection();
            } else
                borderPane.setTop(new FlowPane(comboBox));
            switch (comboBox.getSelectionModel().getSelectedItem()) {
                case ("Total Attendance") -> showAttendancePerDayPieChart();
                case ("Person Attendance") -> showPersonAttendance();
                case ("PieChart") -> showPieChart();
                default -> borderPane.setCenter(new AnchorPane());
            }
        });
    }

    private void showAttendancePerDay() {
        borderPane.setCenter(BAR_CHART_UTILS.getAttendancePerDay(personList));
    }

    private void showIndividualAttendance() {
        borderPane.setCenter(BAR_CHART_UTILS.getTotalAttendanceBarChart(selectedPerson));
    }

    private void showTotalAttendance() {
        borderPane.setCenter(BAR_CHART_UTILS.getTotalAttendanceBarChart(personList));
    }

    private void showAttendancePerDayPieChart() {
        borderPane.setCenter(PIE_CHART_UTILS.getAttendancePerDayPieChart(personList));
    }

    private void showPersonAttendance() {
        borderPane.setCenter(BAR_CHART_UTILS.getTotalAttendanceBarChart(personList));
    }

    @FXML
    private void showAttendance() {
        attendBtn.setVisible(true);
        centerPane.setCenter(flowPane);
    }

    @FXML
    private void showStats() {
        borderPane.setTop(new FlowPane(comboBox));
        attendBtn.setVisible(false);
        centerPane.setCenter(borderPane);
    }

    private void showPieChart() {
        borderPane.setCenter(PIE_CHART_UTILS.getPersonPieChart(personList));
    }

    private void initPersonView() {
        personList.addAll(personManager.getPersonList());
        for (Person person : personList)
            flowPane.getChildren().add(person.getPersonPane());
    }

    private void updateTime() {
        Platform.runLater(new Thread(() -> dateTimeObjectProperty.set(LocalDateTime.now())));
    }

    private void selectPerson() {
        flowPane.setOnMouseClicked(e -> {
            contextMenuPerson.hide();
            AtomicBoolean check = new AtomicBoolean(false);
            flowPane.getChildren().forEach(p -> {
                        if (p.getAccessibleText() == e.getPickResult().getIntersectedNode().getAccessibleText()
                                ^ p.getAccessibleText() == e.getPickResult().getIntersectedNode().getParent().getAccessibleText()
                                ^ p.getAccessibleText() == e.getPickResult().getIntersectedNode().getParent().getParent().getAccessibleText()) {
                            p.setStyle("-fx-background-radius: 15;-fx-background-color: lightblue;-fx-border-style: solid;-fx-border-color: grey;-fx-border-radius: 15");
                            personList.forEach(person -> {
                                if (person.getName() == p.getAccessibleText()) {
                                    this.selectedPerson = person;
                                    main.getPrimaryStage().setTitle("Hello " + person.getName());
                                    check.set(true);
                                }
                            });
                            if (e.getButton() == MouseButton.SECONDARY) {
                                contextMenuPerson.show(flowPane, e.getScreenX(), e.getScreenY());
                            }
                        } else {
                            p.setStyle("-fx-border-style: solid;-fx-border-radius: 15;-fx-border-color: grey;");
                        }
                    }
            );
            if (!check.get()) {
                selectedPerson = null;
                main.getPrimaryStage().setTitle("Hello World");
            }
        });
    }

    @FXML
    private void newPerson() {
        List<Label> labels = Arrays.asList(new Label("Id"), new Label("Name"));
        List<TextField> textFields = Arrays.asList(new TextField(), new TextField());
        this.editPane = new EditPane(confirmButton, cancelButton, labels, textFields);
        confirmationType.set(NEW_CONFIRMATION);
        centerPane.setCenter(editPane.get());
    }

    private void newPerson(String name) {
        personList.sort(Comparator.comparingInt(Person::getId));
        Person p = new Person(personList.get(0).getId() + 1, name);
        personList.add(p);
        flowPane.getChildren().add(p.getPersonPane());
    }

    @FXML
    private void editPerson() {
        /*
        String s = "";
        while (s.isBlank() || s.isEmpty()) {
            s = (String) JOptionPane.showInputDialog(null, "Enter the name", "Edit Person: " + selectedPerson.getName(), JOptionPane.PLAIN_MESSAGE);
            if (s == null)
                break;
            personList.sort(Comparator.comparingInt(Person::getId));
            selectedPerson.setName(s);
        }

         */
        if(selectedPerson!=null){

        List<Label> labels = Arrays.asList(new Label("Name"));
        List<TextField> textFields = Arrays.asList(new TextField(selectedPerson.getName()));
        this.editPane = new EditPane(confirmButton, cancelButton, labels, textFields);
        confirmationType.set(EDIT_CONFIRMATION);
        centerPane.setCenter(editPane.get());
        }
    }

    @FXML
    private void deletePerson() {

        /*
        if (selectedPerson != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Confirm deletion");
            alert.showAndWait();
            if (alert.getResult().getButtonData().isDefaultButton()) {
                flowPane.getChildren().remove(selectedPerson.getPersonPane());
                personList.remove(selectedPerson);
                selectedPerson = null;
            }
        }
         */
        if (selectedPerson != null) {
            confirmationType.set(DELETE_CONFIRMATION);
            BorderPane instanceBorderPane = new BorderPane();
            instanceBorderPane.setLeft(new Label("Confirm deletion of " + selectedPerson.getName()));
            GridPane instanceGridPane = new GridPane();
            instanceGridPane.add(confirmButton, 0, 0);
            instanceGridPane.add(cancelButton, 1, 0);
            instanceBorderPane.setRight(instanceGridPane);
            centerPane.setTop(instanceBorderPane);
        }
    }

    @FXML
    private void attendSchool() {
        if (selectedPerson != null) {
            selectedPerson.attend();
            flowPane.getChildren().remove(selectedPerson.getPersonPane());
            System.out.printf("%-20s%1s%s%n", LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), "", selectedPerson.getName());
        }
    }

    private void autoUpdateTime() {
        thread.setPriority(4);
        updateTime();
        dateTimeObjectProperty.addListener((observableValue, localDateTime, t1) -> {
                    dateLabel.setText(t1.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
                    if (main.getPrimaryStage().isShowing()) {
                        try {
                            thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateTime();
                    }
                }
        );
    }

    @FXML
    private void closeWindow() {
        main.getPrimaryStage().close();
    }

}
