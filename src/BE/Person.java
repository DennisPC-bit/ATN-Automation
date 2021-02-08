package BE;

import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Author DennisPC-bit
 *
 */

public class Person {
    private static DoubleProperty IMAGE_SIZE = new SimpleDoubleProperty(140);
    private Image image = new Image("GUI/IMG/noIMG.png");
    private IntegerProperty id;
    private StringProperty name;
    private BorderPane personPane = new BorderPane();
    private Label nameLabel = new Label();
    private List<LocalDateTime> daysAttended = new ArrayList<>();


    public static void setImageSize(double imageSize) {
        IMAGE_SIZE.set(imageSize);
    }

    public Person(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public Person(int id, String name, Image image) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.image = image;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        nameLabel.setText(name);
    }

    public BorderPane getPersonPane() {
        personPane.setPadding(new Insets(IMAGE_SIZE.get()/20, IMAGE_SIZE.get()/10, IMAGE_SIZE.get()/20, IMAGE_SIZE.get()/10));
        nameLabel.setText(name.getValue());
        personPane.topProperty().setValue(nameLabel);
        personPane.setStyle("-fx-border-style: solid;-fx-border-radius: 15;-fx-border-color: grey;");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(IMAGE_SIZE.get());
        imageView.setFitWidth(IMAGE_SIZE.get());
        personPane.setAccessibleText(name.getValue());
        personPane.centerProperty().setValue(imageView);
        return personPane;
    }

    public void attend(){
        daysAttended.add(LocalDateTime.now());
    }

    public void attend(LocalDateTime localDateTime){
        daysAttended.add(localDateTime);
    }

    public List<LocalDateTime> getDaysAttended(){
        return daysAttended;
    }
}
