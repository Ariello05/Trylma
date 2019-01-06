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

public class Main extends Application {

    private static Board board;
    private static Area area;

    public static Stage primaryStage;
    private static BorderPane rootLayout;
    private AnchorPane menuLayout;
    private static Model model;
    private static Canvas canvas;
    private static Client client;
    private MenuOptionController menuController;
    private Observer observer;

    public static void main(String[] args) {
        launch(args);
    }

    public static void setModel(Model model) {
        Main.model = model;
    }

    public static void setCanvas(Canvas canvas) {
        Main.canvas = canvas;
    }

    public void newGame() {
        initBoard();
    }

    private void initBoard() {
        board = new Board(GameSettings.BOARD_RADIUS, GameSettings.PLAYERS);
    }

    private void initScreen() {
        area = new Area(this);
    }

    private void initClient() {
        client = new Client();
    }

    public static Board getBoard() {
        return board;
    }

    public static Client getClient() {
        return client;
    }

    public static void connectClient(int port) {
        try {
            client.connectToServer("LOCALHOST",port);//TODO: ADDRESS AND PORT OF SERVER
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initScreen();
        initBoard();
        initClient();

        /*uncomment when launch Main.class*/
//        this.model = new Model();
//        this.canvas = new Canvas(GameSettings.SCREEN_SIZE, GameSettings.SCREEN_SIZE);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Trylma");

//        initRootLayout();
//        initMenuLayout();
//        overView();


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

    /*when launch from Main.class change for static method*/
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/board.fxml"));
            rootLayout = (BorderPane) loader.load();

            /** Show the scene containing the root layout*/
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });


            /** Give the controller access to the main app */
            Controller rootController = loader.getController();
            rootController.initModel(model);
            rootController.setArea(area);
            rootController.setCanvas(canvas);
            rootController.setMain(this);

            rootLayout.setOnMouseClicked(rootController.mouseClicked);

            rootLayout.getChildren().add(canvas);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void overView() {
            observer = new Observer(menuController);
            observer.listenToGames(model);
    }
}

