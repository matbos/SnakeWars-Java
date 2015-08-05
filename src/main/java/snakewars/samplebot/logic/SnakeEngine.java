package snakewars.samplebot.logic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import snakewars.samplebot.GameBoardState;
import snakewars.samplebot.dtos.PointDTO;
import snakewars.samplebot.dtos.SnakeDTO;
import snakewars.samplebot.dtos.SnakeDirection;

import static snakewars.samplebot.dtos.SnakeDirection.Down;
import static snakewars.samplebot.dtos.SnakeDirection.Left;
import static snakewars.samplebot.dtos.SnakeDirection.Right;
import static snakewars.samplebot.dtos.SnakeDirection.Up;

public class SnakeEngine {

    private final String mySnakeId;
    private final Random random = new Random();

    public SnakeEngine(String mySnakeId) {
        this.mySnakeId = mySnakeId;
    }

    public Move getNextMove(GameBoardState gameBoardState) {
        //===========================
        // Your snake logic goes here
        //===========================

        SnakeDTO mySnake = gameBoardState.getSnake(mySnakeId);
        if(mySnake.isAlive()) {
            PointDTO nearestFood = findNearestFood(gameBoardState, mySnake);
            if(nearestFood == null) {
                return randomMove(gameBoardState);
            }
            Move nextMove = determineNextMove(nearestFood, mySnake);

            // fallback
            if(nextMove != null) {
                return nextMove;
            } else {
                return randomMove(gameBoardState);
            }
        }
        return Move.NONE;
    }

    private Move findNextMove(GameBoardState gameBoardState, SnakeDTO mySnake) {
        PointDTO nearestFood = findNearestFood(gameBoardState, mySnake);
        if(nearestFood == null) {
            return randomMove(gameBoardState);
        }
        Move nextMove = determineNextMove(nearestFood, mySnake);
    }

    private Move randomMove(GameBoardState gameBoardState) {
        Set<PointDTO> occupiedCells = gameBoardState.getOccupiedCells();

        // Check possible moves in random order.
        List<Move> moves = new LinkedList<>(Arrays.asList(Move.LEFT, Move.RIGHT, Move.STRAIGHT));

        while(moves.stream().anyMatch(predicate -> true)) {
            // Select random move.
            int randomIndex = random.nextInt(moves.size());
            Move move = moves.get(randomIndex);
            moves.remove(move);

            PointDTO newHead = gameBoardState.getSnakeNewHeadPosition(mySnakeId, move);
            if(!occupiedCells.contains(newHead)) {
                return move;
            }
        }
        return Move.NONE;
    }

    private Move determineNextMove(PointDTO nearestFood, SnakeDTO mySnake) {
        final SnakeDirection currentDirection = mySnake.getDirection();
        final PointDTO currentPosition = mySnake.getHead();

        SnakeDirection nextDirection = determineNextDirection(currentPosition, nearestFood);

        if(currentDirection == nextDirection) {
            return Move.STRAIGHT;
        } else if(currentDirection == Down && nextDirection == Up || currentDirection == Up && nextDirection == Down || currentDirection == Left
                && nextDirection == Right || currentDirection == Right && nextDirection == Left) {
            return Move.LEFT;
        } else if(currentDirection == Up) {
            return nextDirection == Left ? Move.LEFT : Move.RIGHT;
        } else if(currentDirection == Down) {
            return nextDirection == Right ? Move.RIGHT : Move.LEFT;
        } else if(currentDirection == Right) {
            return nextDirection == Up ? Move.LEFT : Move.RIGHT;
        } else if(currentDirection == Left) {
            return nextDirection == Up ? Move.RIGHT : Move.LEFT;
        }
        return Move.STRAIGHT;
    }

    private SnakeDirection determineNextDirection(PointDTO currentPosition, PointDTO nearestFood) {
        int dx = currentPosition.getX() - nearestFood.getX();
        int dy = currentPosition.getY() - nearestFood.getY();
        if(Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? SnakeDirection.Left : SnakeDirection.Right;
        } else {
            return dy > 0 ? Down : Up;
        }
    }

    private int delta(int p1, int p2) {
        return p1 - p2;
    }

    private PointDTO findNearestFood(GameBoardState gameBoardState, SnakeDTO mySnake) {
        final PointDTO myPosition = mySnake.getHead();
        Double minDistance = Double.MAX_VALUE;
        PointDTO nearestPoint = null;
        final Set<PointDTO> foodCells = gameBoardState.getFoodCells();
        Double tempDist;
        for(PointDTO foodCell : foodCells) {
            tempDist = calculateDistance(myPosition, foodCell);
            if(minDistance > tempDist) {
                minDistance = tempDist;
                nearestPoint = foodCell;
            }
        }
        return nearestPoint;
    }

    private Double calculateDistance(PointDTO from, PointDTO to) {
        return Math.pow(from.getX() - to.getX(), 2) + Math.pow((from.getY() - to.getY()), 2);
    }
}
