package Controllers;

import GUI.Area;
import GUI.Model;
import GUI.Point2D;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.ResourceBundle;

public class Observer {

    private MenuOptionController menuOptionController;

    public Observer() {
    }

    public Observer(MenuOptionController menuOptionController) {
        this.menuOptionController = menuOptionController;
    }

//    public void listenToPoints(Model model) {
//        this.model = model;
//        this.model.getPoints().addListener((ListChangeListener.Change<? extends Point2D> change) -> {
//            while (change.next()) {
//                if (change.wasAdded()) {
//                    for (Point2D p : change.getAddedSubList()) {
//                        System.out.println("Detected click on coords: " + p.getX() + ", " + p.getY());
//                        reDraw();
//                    }
//                }
//            }
//        } );
//    }

    public void listenToGames(Model model) {
        model.getRunningPorts().addListener((ListChangeListener.Change<? extends Integer> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Integer i : change.getAddedSubList()) {
                        System.out.println("Detected running port: " + i);
                        menuOptionController.updateAvaibleGames();
                    }
                }
            }
        });
    }


}
