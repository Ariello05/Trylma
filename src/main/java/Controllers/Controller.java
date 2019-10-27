package Controllers;

import GUI.Area;
import GUI.Main;
import GUI.Model;
import GUI.Point2D;
import Logic.GameSettings;
import Logic.HexCell;
import Logic.Piece;
import Logic.Point;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Runnable {
    private Thread runner;
    private boolean isRunning;
    private HexCell<Piece> toMove;
    private Model model;
    private Area area;
    private Canvas canvas;
    private Main main;

    private boolean hasServerPrivilage;

    public Controller() {
        isRunning = true;
        runner = new Thread();
        runner.start();
        hasServerPrivilage = false;
    }

    /**
     *
     * @param privilage Can make server like decisions if not sure just type false
     */
    public void setPrivilage(boolean privilage){
        this.hasServerPrivilage = privilage;
    }

    public void setArea(Area area) {
        this.area = area;
        //if(area != null && main.getClient() != null)
        //   this.area.setCurrentPlayerIndex(main.getClient().getControlIndex());
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @FXML
    private void initialize() {
    }

    public void initModel(Model model) {
        this.model = model;
    }

    /**
     * handler that adds a point
     */
    @FXML
    private void addPoint(MouseEvent event) {
        Point2D point2D = new Point2D(event.getX(), event.getY());
        model.getPoints().add(point2D);
//        System.out.println("Point of coordinates: " + event.getX() + ", " + event.getY() + " added");
    }

    public EventHandler pointsAdder = new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    addPoint(event);
                }
            };

    public EventHandler mouseClicked = new javafx.event.EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
//            area.updatePossibleMoves(null);

            if(!main.getClient().getValidTurn()){
                //TODO: Some kind of message "NOT YOUR TURN etc..."
                area.reDraw();
                return;
            }

            for (Point<HexCell<Piece>> p : main.getBoard().getPoints()) {

                /** If there's a player's piece at this position */
                if (p.getKey().getKey() != null && area.isPlayer(p)) {
                    /** Highlight it if the user clicked on it */
                    p.getKey().getKey().highlight(p.contains(event.getX(), event.getY()));
                    /** If the user clicked on it, update possible moves and update toMove field */
                    if (p.contains(event.getX(), event.getY())) {
                        System.out.println("Point within a piece - 1st if");
                        model.getPoints().add(new Point2D(event.getX(),  event.getY()));
                        toMove = p.getKey();
                        area.updatePossibleMoves(toMove);
                    }
                }
                else if (p.getKey().getKey() != null) {
                    /** If the user clicked on it ... */
                    if (p.contains(event.getX(), event.getY())) {
                        System.out.println("Point within a piece - 2st if");
                        model.getPoints().add(new Point2D(event.getX(),  event.getY()));

                        /** reset move-assistance */
                        toMove = null;
                        area.unhighlight();
                    }
                }

                /** If there is not a piece at this position ... */
                else {
                    /** Move the toMove piece here if possible and reset move-assistance */
                    if(toMove != null && p.contains(event.getX(), event.getY())) {
                        System.out.println("Point within a piece - 3st if");
                        model.getPoints().add(new Point2D(event.getX(),  event.getY()));

                        /*when lauch form Main.class use static reference Main.getClient... and so on*/
                        if(main.getBoard().move(toMove, p.getKey())) {
                            /** Test for winner and run win sequence */
                            main.getClient().moveCommand(toMove.toString() + " " + p.getKey().toString());
                            if(main.getBoard().won() >= 0)
                                area.runWinSequence(main.getBoard().won());
                            /** Move to next player and run Computer Player */
                            if(hasServerPrivilage) {
                                area.nextPlayer();
                                while (area.getCurrentPlayerIndex() >= GameSettings.NUM_HUMAN_PLAYERS) {
                                    area.runComputerPlayer();
                                    if (main.getBoard().won() >= 0) {
                                        area.runWinSequence(main.getBoard().won());
                                        break;
                                    }
                                }
                            }
                        }
                        toMove = null;
                        area.unhighlight();
                    }
                }
            }
            area.reDraw();
            model.clearPointList();
        }
    };



    @Override
    public void run() {
        while(isRunning) {

            /** If a player won, wait 5 seconds, then create a new game */
            if(area.getWinPlayerIndex() >= 0) {
//                repaint();

                try {
                    Thread.sleep(5000);
                } catch(InterruptedException e) {
                    Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, e);
                }

                area.setWinPlayerIndex(-1);
//                app.newGame();
            }

//            /** Repaint */
//            try {
//                Thread.sleep(50);
//            } catch(InterruptedException e) {
//                Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, e);
//            }
//            repaint();
        }
    }
}

