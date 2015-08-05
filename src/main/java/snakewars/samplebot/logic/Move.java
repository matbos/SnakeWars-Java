package snakewars.samplebot.logic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import snakewars.samplebot.dtos.PointDTO;
import snakewars.samplebot.dtos.SizeDTO;
import snakewars.samplebot.dtos.SnakeDTO;
import snakewars.samplebot.dtos.SnakeDirection;

public class Move {

    public static final Move LEFT = new Move("LEFT", -1);
    public static final Move RIGHT = new Move("RIGHT", 1);
    public static final Move STRAIGHT = new Move("STRAIGHT", 0);
    public static final Move NONE = new Move("", 0);
    private static List<DirectionOffset> offsets = Arrays.asList(
            new DirectionOffset(new Offset(0, 1), SnakeDirection.Up),
            new DirectionOffset(new Offset(1, 0), SnakeDirection.Right),
            new DirectionOffset(new Offset(0, -1), SnakeDirection.Down),
            new DirectionOffset(new Offset(-1, 0), SnakeDirection.Left)
    );
    private String command;
    private int moveOffset;

    private Move(String command, int moveOffset) {
        this.command = command;
        this.moveOffset = moveOffset;
    }

    public PointDTO getSnakeNewHead(SnakeDTO snake, SizeDTO boardSize) {
        int index = IntStream.range(0, offsets.size())
                             .filter(indexOfOffset -> offsets.get(indexOfOffset).SnakeDirection.equals(snake.getDirection()))
                             .findFirst().getAsInt();
        DirectionOffset offset = offsets.get((index + moveOffset + 4) % 4);
        PointDTO newHead = offsetModule(snake.getHead(), offset.Offset, boardSize);
        return newHead;
    }

    private PointDTO offsetModule(PointDTO head, Offset offset, SizeDTO boardSize) {
        return new PointDTO((head.getX() + offset.DX + boardSize.getWidth()) % boardSize.getWidth(),
                            (head.getY() + offset.DY + boardSize.getHeight()) % boardSize.getHeight());
    }

    public String getCommand() {
        return command;
    }
}
