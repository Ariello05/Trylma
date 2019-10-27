package Other;

import GUI.Main;
import Logic.GameSettings;
import Logic.HexCell;
import Logic.Piece;
import Logic.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ServerBots {
    private int currentPlayerIndex;
    private int winPlayerIndex;
    private Game game;
    private ArrayList<Point<HexCell<Piece>>> possibleMoves;


    public ServerBots(Game game) {
        this.game = game;
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

    public boolean isPossibleMove(Point<HexCell<Piece>> p) {
        for(Point<HexCell<Piece>> poss : possibleMoves) {
            if(p.equals(poss)) return true;
        }
        return false;
    }


    public void updatePossibleMoves(HexCell<Piece> src) {
        possibleMoves.clear();
        if (src == null || src.getKey() == null) {
            return;
        }
        for (Point<HexCell<Piece>> p : game.getBoard().getPoints()) {
            if (p.getKey() != null && game.getBoard().isValidMove(src, p.getKey())) {
                possibleMoves.add(p);
            }
        }
    }

    public boolean isPlayer(Point<HexCell<Piece>> p) {
        if(p == null || p.getKey().getKey() == null) return false;
        return (p.getKey().getKey().getPlayer().equals(GameSettings.PLAYERS[currentPlayerIndex]));
    }

    public void unhighlight() {
        for(Point<HexCell<Piece>> p : game.getBoard().getPoints()) {
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
        Point<HexCell<Piece>> destPiece = game.getBoard().getNearestOpenPoint(cornerPiece);

        Point<HexCell<Piece>> bestPiece = null;
        Point<HexCell<Piece>> bestMove = null;
        double bestScore = Math.pow(GameSettings.BOARD_RADIUS - 1, 4);

        /** For each of the current player's pieces... */
        for(Point<HexCell<Piece>> p : game.getBoard().getPlayerPoints(currentPlayerIndex)) {
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

        if(bestPiece != null && bestMove != null) {
            if(game.getBoard().isValidMove(bestPiece.getKey(), bestMove.getKey()))
            {
                game.updatePlayers(bestPiece.getKey(), bestMove.getKey());
            }
            game.getBoard().move(bestPiece.getKey(), bestMove.getKey());
        }
        nextPlayer();

    }

    /**
     * Helper Method for AI. Finds the nearest open location from the farthest winLoc
     * @param playerIndex computer player running
     * @return nearest open location from farthest winLoc
     */
    public HexCell<Piece> findCornerPiece(int playerIndex) {
        ArrayList<HexCell<Piece>> winLocs = game.getBoard().getWinLocs(currentPlayerIndex);
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
        game.getBoard().move(src,dst);

        for(Point<HexCell<Piece>> p : game.getBoard().getPlayerPoints(currentPlayerIndex)) {
            score += Math.pow(game.getBoard().getDistance(p.getKey(), goal), 2);
        }

        game.getBoard().move(dst,src);

        return Math.sqrt(score);
    }
}
