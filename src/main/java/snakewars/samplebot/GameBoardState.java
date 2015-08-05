package snakewars.samplebot;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import snakewars.samplebot.dtos.GameStateDTO;
import snakewars.samplebot.dtos.PointDTO;
import snakewars.samplebot.dtos.SnakeDTO;
import snakewars.samplebot.logic.Move;

public class GameBoardState {

    private final GameStateDTO gameState;

    public GameBoardState(GameStateDTO gameState) {
        this.gameState = gameState;
    }

    public PointDTO getSnakeNewHeadPosition(String snakeId, Move move) {
        SnakeDTO snake = getSnake(snakeId);
        PointDTO newHead = move.getSnakeNewHead(snake, gameState.getBoardSize());
        return newHead;
    }

    public SnakeDTO getSnake(String snakeId) {
        return gameState.getSnakes().stream().filter(snake -> snake.getId().equals(snakeId)).findFirst().get();
    }

    public Set<PointDTO> getOccupiedCells() {
        return Stream.concat(gameState.getWalls().stream(),
                             gameState.getSnakes().stream().flatMap(snake -> snake.getCells().stream()))
                     .collect(Collectors.toSet());
    }

    public Set<PointDTO> getFoodCells() {
        return gameState.getFood().stream().collect(Collectors.toSet());
    }

    public Set<SnakeDTO> getSnakeCells() {
        return gameState.getSnakes().stream().collect(Collectors.toSet());
    }
}
