package BE;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EditPane {
    BorderPane a;
    GridPane g;
    List<Label> labels = new ArrayList<>();
    List<TextField> textFields = new ArrayList<>();

    public EditPane(Button confirmButton, Button cancelButton, List<Label> labels, List<TextField> textFields) {
        /*
        this.labels = labels;
        this.textFields = textFields;
        g = new TilePane();
        labels.forEach(l-> l.setPadding(new Insets(0,5,0,5)));
        for(int i = 0 ; i < labels.size() ; i++){
        g.getChildren().add(labels.get(i));
                g.getChildren().add(textFields.get(i));
        }
        g.getChildren().add(confirmButton);
        g.getChildren().add(cancelButton);
        g.setPrefColumns(2);
        g.setTileAlignment(Pos.TOP_LEFT);


        VBox vBox = new VBox();
        labels.forEach(l-> l.setPadding(new Insets(0,5,0,5)));
        labels.forEach(l->vBox.getChildren().add(l));
        VBox vBox1 = new VBox();
        textFields.forEach(t->vBox1.getChildren().add(t));
        g = new HBox(vBox,vBox1);

        this.labels = labels;
        this.textFields = textFields;
        */

        g = new GridPane();
        this.textFields = textFields;
        this.labels = labels;
        labels.forEach(l -> l.setPadding(new Insets(0, 5, 0, 5)));

        if (labels.size() == textFields.size()) {
            AtomicInteger i = new AtomicInteger(0);
            labels.forEach(l -> {
                g.add(l, 0, i.get());
                i.incrementAndGet();
            });
            i.set(0);
            textFields.forEach(t -> {
                g.add(t, 1, i.get());

                i.incrementAndGet();
            });
            g.add(confirmButton, 0, textFields.size());
            g.add(cancelButton, 1, textFields.size());
        }

        a = new BorderPane(g);

    }

    public List<TextField> getTextFields() {
        return textFields;
    }

    public BorderPane get() {
        return a;
    }
}

