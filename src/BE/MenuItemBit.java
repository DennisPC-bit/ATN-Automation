package BE;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class MenuItemBit {
    MenuItem menuItem;

    public MenuItemBit(String menuItem, EventHandler<ActionEvent> eventHandler){
        this.menuItem = new MenuItem(menuItem);
        this.menuItem.setOnAction(eventHandler);
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
