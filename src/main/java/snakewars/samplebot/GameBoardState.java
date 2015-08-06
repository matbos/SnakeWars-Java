package snakewars.samplebot;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
        return getOccupiedCells(null);
    }

    public Set<PointDTO> getOccupiedCells(String mySnakeId) {

        final Set<PointDTO> collect = Stream.concat(gameState.getWalls().stream(),
                                                    gameState.getSnakes().stream().flatMap(snake -> snake.getCells().stream()))
                                            .collect(Collectors.toSet());
        collect.addAll(getFoodCellsGraniczaceWithSnakeHead());
        //collect.addAll(getSnakeHeads(mySnakeId));
        return collect;
    }

    private Collection<? extends PointDTO> getSnakeHeads(String mySnakeId) {
        Set<PointDTO> adjecentToHeads = new HashSet<>();
        final Set<PointDTO> heads = gameState.getSnakes().stream().filter(s -> !s.getId().equals(mySnakeId)).map(snakeDTO -> snakeDTO.getHead())
                                             .collect(Collectors.toSet());
        for(PointDTO head : heads) {
            adjecentToHeads.add(new PointDTO(head.getX() - 1, head.getY() - 1));
            adjecentToHeads.add(new PointDTO(head.getX() - 1, head.getY()));
            adjecentToHeads.add(new PointDTO(head.getX() - 1, head.getY() + 1));
            adjecentToHeads.add(new PointDTO(head.getX(), head.getY() + 1));
            adjecentToHeads.add(new PointDTO(head.getX() + 1, head.getY() + 1));
            adjecentToHeads.add(new PointDTO(head.getX() + 1, head.getY()));
            adjecentToHeads.add(new PointDTO(head.getX() + 1, head.getY() - 1));
            adjecentToHeads.add(head);
        }
        return adjecentToHeads;
    }

    private Set<PointDTO> getFoodCellsGraniczaceWithSnakeHead() {
        final List<PointDTO> food = gameState.getFood();
        final Set<PointDTO> heads = gameState.getSnakes().stream().map(snake -> snake.getHead()).collect(Collectors.toSet());

        return food.stream().filter(foodDTO -> hasAnySnakeHaedAdjecent(foodDTO, heads)).collect(Collectors.toSet());
    }

    private boolean hasAnySnakeHaedAdjecent(PointDTO food, Set<PointDTO> heads) {
        for(PointDTO head : heads) {
            final int deltaX = delta(head.getX(), food.getX());
            final int deltaY = delta(head.getY(), food.getY());
            if((deltaX == 1 && deltaY == 0) || (deltaY == 1 && deltaX == 0)) {
                return false;
            }
        }
        return true;
    }

    private int delta(int p1, int p2) {
        return Math.abs(p1 - p2);
    }

    public Set<PointDTO> getFoodCells() {
        return gameState.getFood().stream().collect(Collectors.toSet());
    }

    public Set<SnakeDTO> getSnakeCells() {
        return gameState.getSnakes().stream().collect(Collectors.toSet());
    }
}
