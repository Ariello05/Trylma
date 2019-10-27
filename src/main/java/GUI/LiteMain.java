package GUI;

import Controllers.*;
import Logic.Board;
import Logic.GameSettings;
import Other.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LiteMain extends Application {

    public static Stage primaryStage;
    private AnchorPane menuLayout;
    private static Model model;
    private static Canvas canvas;
    private MenuOptionController menuController;
    private Observer observer;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setAlwaysOnTop(true);
        Platform.setImplicitExit(false);

        this.model = new Model();
        model.setStage(primaryStage);
        this.canvas = new Canvas(GameSettings.SCREEN_SIZE, GameSettings.SCREEN_SIZE);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Trylma - menu");

        initMenuLayout();
        overView();
//        initMain();

    }

    public void initMenuLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/menu_red.fxml"));
            menuLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(menuLayout);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            menuController = loader.getController();
            menuController.setCanvas(canvas);
            menuController.setModel(model);


            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initMain() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Main.setModel(model);
        //Main.setCanvas(canvas);
    }

    public void overView() {
        observer = new Observer(menuController);
        observer.listenToGames(model);

    }
}

