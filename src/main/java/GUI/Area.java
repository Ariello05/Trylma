package GUI;

import Logic.*;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Area extends Pane {
    private static final long SERIAL_VERSION = 1L;
    private Main mainStage;
    private ArrayList<Point<HexCell<Piece>>> possibleMoves;
    private int currentPlayerIndex;
    private int winPlayerIndex;
    private GraphicsContext graphicsContext;

    public Area(Main mainStage) {
        this.mainStage = mainStage;
        currentPlayerIndex = 0;
        winPlayerIndex = -1;
        possibleMoves = new ArrayList<Point<HexCell<Piece>>>();
    }

    public int getWinPlayerIndex() {
        return winPlayerIndex;
    }

    public void setWinPlayerIndex(int winPlayerIndex) {
        this.winPlayerIndex = winPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void drawLatticing(GraphicsContext graphicsContext) {
        for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {
            graphicsContext.setStroke(Color.BLACK);

            graphicsContext.strokeOval(p.getPxCord(), p.getPyCord(), 1.1* GameSettings.PIECE_DIAMETER, 1.1 * GameSettings.PIECE_DIAMETER);
        }
    }

    public void draw(GraphicsContext graphicsContext) {
        /** Draws Win Message */
        if (winPlayerIndex >= 0) {
            graphicsContext.strokeText("The " + GameSettings.PLAYER_NAMES[winPlayerIndex] + " player won!", GameSettings.SCREEN_SIZE / 20, GameSettings.SCREEN_SIZE / 20);

            for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {

                /**draw if there's place*/
                if (p.getKey().getKey() != null) {
                    if (p.getKey().getKey().getPlayer().equals(GameSettings.PLAYERS[winPlayerIndex])) {
                        graphicsContext.setFill(p.getKey().getKey().getColor().brighter());

                    } else {
                        graphicsContext.setFill(p.getKey().getKey().getColor());
                    }
                }
                /** draws move-assistant */
                else if (GameSettings.MOVE_ASSISTANCE && this.isPossibleMove(p)) {
                    graphicsContext.setFill(Color.gray(0.2, 0.2));
                    graphicsContext.fillOval(p.getPxCord(), p.getPyCord(), 1.1* GameSettings.PIECE_DIAMETER, 1.1 * GameSettings.PIECE_DIAMETER);
                }
                /** if there*s no piece */
                else {
                }

            }
        }
        /** no-win sequence */
        else {
           graphicsContext.strokeText("It's the " + GameSettings.PLAYER_NAMES[currentPlayerIndex] + " player's turn", GameSettings.SCREEN_SIZE / 20, GameSettings.SCREEN_SIZE / 20);
            for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {

                /** draw if there's place */
                if (p.getKey().getKey() != null) {
                    graphicsContext.setFill(p.getKey().getKey().getColor());
                    graphicsContext.fillOval(p.getPxCord(), p.getPyCord(), 1.1* GameSettings.PIECE_DIAMETER, 1.1 * GameSettings.PIECE_DIAMETER);
                }
                /** move assistant mechanism */
                else if (GameSettings.MOVE_ASSISTANCE && isPossibleMove(p)) {
                    graphicsContext.setFill(Color.gray(0.2, 0.2));
                    graphicsContext.fillOval(p.getPxCord(), p.getPyCord(), 1.1* GameSettings.PIECE_DIAMETER, 1.1 * GameSettings.PIECE_DIAMETER);
                }
                /** if no piece and it's not possilbe move */
                else {
               }
            }
        }
    }

    public void reDraw() {
        graphicsContext = mainStage.getCanvas().getGraphicsContext2D();
        graphicsContext.clearRect(0,0, mainStage.getCanvas().getWidth(), mainStage.getCanvas().getHeight());
        drawLatticing(graphicsContext);
        draw(graphicsContext);
    }

    public boolean isPossibleMove(Point<HexCell<Piece>> p) {
        for(Point<HexCell<Piece>> poss : possibleMoves) {
            if(p.equals(poss)) return true;
        }
        return false;
    }

    public void paintComponent(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        draw(graphicsContext);
    }



    public void updatePossibleMoves(HexCell<Piece> src) {
        possibleMoves.clear();
        if (src == null || src.getKey() == null) {
            return;
        }
        for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {
            if (p.getKey() != null && mainStage.getBoard().isValidMove(src, p.getKey())) {
                possibleMoves.add(p);
            }
        }
    }

    public boolean isPlayer(Point<HexCell<Piece>> p) {
        if(p == null || p.getKey().getKey() == null) return false;
        return (p.getKey().getKey().getPlayer().equals(GameSettings.PLAYERS[currentPlayerIndex]));
    }

    public void unhighlight() {
        for(Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {
            if (p.getKey().getKey() != null) p.getKey().getKey().highlight(false);
            possibleMoves.clear();
        }
    }

    public void nextPlayer() {
        if(currentPlayerIndex == GameSettings.PLAYERS.length - 1)
            currentPlayerIndex = 0;
        else
            currentPlayerIndex++;
    }

    public void runComputerPlayer() {
        Point<HexCell<Piece>> cornerPiece = findCornerPiece(currentPlayerIndex).pointConversion();
        Point<HexCell<Piece>> destPiece = mainStage.getBoard().getNearestOpenPoint(cornerPiece);

        Point<HexCell<Piece>> bestPiece = null;
        Point<HexCell<Piece>> bestMove = null;
        double bestScore = Math.pow(GameSettings.BOARD_RADIUS - 1, 4);

        /** For each of the current player's pieces... */
        for(Point<HexCell<Piece>> p : mainStage.getBoard().getPlayerPoints(currentPlayerIndex)) {
            /** For each possible move from the current piece... */
            updatePossibleMoves(p.getKey());
            for(Point<HexCell<Piece>> poss : possibleMoves) {
                /** Record the move if it is better than the best previous one */
                if(calculateScore(p.getKey(), poss.getKey(), destPiece.getKey()) < bestScore) {
                    bestPiece = p;
                    bestMove = poss;
                    bestScore = calculateScore(p.getKey(), poss.getKey(), destPiece.getKey());
                }
            }
        }

        //if(bestPiece != null && bestMove != null)
            //mainStage.getBoard().move(bestPiece.getKey(), bestMove.getKey());TODO:UNCOMMENT
        nextPlayer();
    }

    /**
     * Helper Method for AI. Finds the nearest open location from the farthest winLoc
     * @param playerIndex computer player running
     * @return nearest open location from farthest winLoc
     */
    public HexCell<Piece> findCornerPiece(int playerIndex) {
        ArrayList<HexCell<Piece>> winLocs = mainStage.getBoard().getWinLocs(currentPlayerIndex);
        HexCell<Piece> cornerPiece = null;
        for(HexCell<Piece> n : winLocs) {
            if(Math.abs(n.getX()) >= (GameSettings.BOARD_RADIUS-1)*2 ||
                    Math.abs(n.getY()) >= (GameSettings.BOARD_RADIUS-1)*2 ||
                    Math.abs(n.getZ()) >= (GameSettings.BOARD_RADIUS-1)*2) {
                cornerPiece = n;
            }
        }
        return cornerPiece;
    }

    /**
     * Helper Method for AI. Calculates the score for a possible move. A score is
     * the total (mean-square) distance of each piece from the open location closest
     * to the corner winLoc.
     * @param src possible source node
     * @param dst possible destination node
     * @param goal open location closest to the corner winLoc
     * @return score
     */
    public double calculateScore(HexCell<Piece> src, HexCell<Piece> dst, HexCell<Piece> goal) {
        double score = 0;
        //mainStage.getBoard().move(src, dst);TODO:UNCOMMENT

        for(Point<HexCell<Piece>> p : mainStage.getBoard().getPlayerPoints(currentPlayerIndex)) {
            score += Math.pow(mainStage.getBoard().getDistance(p.getKey(), goal), 2);
        }

        //mainStage.getBoard().move(dst, src);TODO:UNCOMMENT
        return Math.sqrt(score);
    }

    /**
     * Runs the Win Sequence after a player won the game.
     */
    public void runWinSequence(int playerIndex) {
        winPlayerIndex = playerIndex;
    }

}




