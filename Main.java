/**
 * This program is a simulation of insect moving game
 *
 * @version 4.0
 * @author Vasilev Ivan
 */
 
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
 
public class Main {
    private static Board gameBoard;
 
    /**
     * This is the main method of my program
     *
     * @param args is necessary for main method
     */
    public static void main(String[] args) {
        final int maxAmountOfInsects = 16;
        final int maxAmountOfFoodPoints = 200;
        FileReader input;
        FileWriter output = null;
        try {
            input = new FileReader("Input.txt");
            output = new FileWriter("Output.txt");
 
            /*list of positions of all entered insects*/
            List<EntityPosition> insectPositionsEntered = new ArrayList<>();
 
            Scanner in = new Scanner(input);
            while (in.hasNext()) {
                int boardSize = Integer.parseInt(in.nextLine());
 
                /* initializing gaming board */
                gameBoard = new Board(boardSize);
 
                int amountOfInsects = Integer.parseInt(in.nextLine());
 
                /* checking if the entered amount of insects is correct */
                if (amountOfInsects < 1 || amountOfInsects > maxAmountOfInsects) {
                    throw new InvalidNumberOfInsectsException();
                }
 
                int amountOfFoodPoints = Integer.parseInt(in.nextLine());
 
                /* checking if the entered amount of food points is correct */
                if (amountOfFoodPoints < 1 || amountOfFoodPoints > maxAmountOfFoodPoints) {
                    throw new InvalidNumberOfFoodPointsException();
                }
 
                /*declaring 2 counters for reading file correctly*/
                int insectCounter = 0;
                int foodPointCounter = 0;
 
                /* cycle for reading insects parameters from input file */
                while (insectCounter < amountOfInsects) {
 
                    /* reading entered Line as all parameters for insect*/
                    String[] arr = in.nextLine().split(" ");
                    String color = arr[0];
                    String insectType = arr[1];
                    int xCoordinate = Integer.parseInt(arr[2]);
                    int yCoordinate = Integer.parseInt(arr[2 + 1]);
 
                    InsectColor insectColor = InsectColor.toColor(color);
 
                    /* creating insect of entered type if no such type throw an exception */
                    if (insectType.equals("Grasshopper")) {
                        EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
                        Grasshopper grasshopper = new Grasshopper(position, insectColor);
                        gameBoard.addEntity(grasshopper);
                        insectCounter++;
                        insectPositionsEntered.add(position);
                    } else if (insectType.equals("Spider")) {
                        EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
                        Spider spider = new Spider(position, insectColor);
                        gameBoard.addEntity(spider);
                        insectCounter++;
                        insectPositionsEntered.add(position);
                    } else if (insectType.equals("Butterfly")) {
                        EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
                        Butterfly butterfly = new Butterfly(position, insectColor);
                        gameBoard.addEntity(butterfly);
                        insectCounter++;
                        insectPositionsEntered.add(position);
                    } else if (insectType.equals("Ant")) {
                        EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
                        Ant ant = new Ant(position, insectColor);
                        gameBoard.addEntity(ant);
                        insectCounter++;
                        insectPositionsEntered.add(position);
                    } else {
                        throw new InvalidInsectTypeException();
                    }
                }
 
                /* cycle for reading food parameters from input file */
                while (foodPointCounter < amountOfFoodPoints) {
 
                    /* reading entered line as all parameters for creating FoodPoint */
                    int foodAmount = in.nextInt();
                    int xCoordinate = in.nextInt();
                    int yCoordinate = in.nextInt();
                    EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
                    FoodPoint foodPoint = new FoodPoint(position, foodAmount);
                    gameBoard.addEntity(foodPoint);
                    foodPointCounter++;
                }
            }
            in.close();
 
            /*
             * output the result of executing the program
             */
            Output.outputResult(gameBoard, insectPositionsEntered, output);
            output.close();
        } catch (Exception e) {
            try {
 
                /*
                 * catching all possible exceptions
                 * and closing the file
                 */
                output.write(e.getMessage());
                output.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
 
/**
 * Exception to board size if entered board size is out of bounds
 */
class InvalidBoardSizeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid board size";
    }
}
 
/**
 * Exception to number of insects if entered number of insects is out of bounds
 */
class InvalidNumberOfInsectsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of insects";
    }
}
 
/**
 * Exception to number of food points if entered number of food points is out of bounds
 */
class InvalidNumberOfFoodPointsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of food points";
    }
}
 
/**
 * Exception to color of insects if entered color is not Red or Yellow or Blue or Green
 */
class InvalidInsectColorException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid insect color";
    }
}
 
/**
 * Exception to type of insect if entered type of insect is not Ant or Grasshopper or Butterfly or Spider
 */
class InvalidInsectTypeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid insect type";
    }
}
 
/**
 * Exception to position of entity if entered position is out of bounds
 */
class InvalidEntityPositionException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid entity position";
    }
}
 
/**
 * Exception to insect if insect of the same type have duplicated colors
 */
class DuplicateInsectException extends Exception {
    @Override
    public String getMessage() {
        return "Duplicate insects";
    }
}
 
/**
 * Exception to entities position if two or more entities share the same position
 */
class TwoEntitiesOnSamePositionException extends Exception {
    @Override
    public String getMessage() {
        return "Two entities in the same position";
    }
}
 
/**
 * enumerator which contains all possible movement directions
 */
enum Direction {
    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");
    private String textRepresentation;
 
    /**
     * method which assigning entered direction to direction in enumerator
     *
     * @param direction entered string direction
     */
    private Direction(String direction) {
        this.textRepresentation = direction;
    }
 
    /**
     * method to return enum direction as a text
     *
     * @return direction as a text (S = South)
     */
    @Override
    public String toString() {
        return textRepresentation;
    }
}
 
/**
 * class that contains coordinates of insect
 */
class EntityPosition {
    private int x;
    private int y;
 
    /**
     * method is used for create the position of some entity in coordinates (x, y)
     *
     * @param x - x coordinate of the insect
     * @param y - y coordinate of the insect
     */
    public EntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
 
    /**
     * getter to get the x coordinate of some entity
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }
 
    /**
     * getter to get the y coordinate of some entity
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }
 
    /**
     * method to returning the coordinates of entity as a String
     *
     * @return String of the coordinates
     */
    @Override
    public String toString() {
        return x + " " + y;
    }
 
    /**
     * overriding method "equals" to compare entities positions
     *
     * @param o some entity
     * @return true if the entities have same position else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityPosition position = (EntityPosition) o;
        return x == position.x && y == position.y;
    }
 
    /**
     * this method is used for correctly working of "equals" method
     *
     * @return hashcode of some class
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
 
/**
 * class for output the result of executing the program
 */
class Output {
    /**
     * output the result to the output file as: COLOR INSECT_TYPE DIRECTION AMOUNT_OF_EATEN_FOOD
     *
     * @param board        board which contains all entities
     * @param positionList list where we store positions of all entered entities
     * @param out          output file
     * @throws IOException if program can not access the output file
     */
    public static void outputResult(Board board, List<EntityPosition> positionList, FileWriter out) throws IOException {
        for (int i = 0; i < positionList.size(); i++) {
            EntityPosition position = positionList.get(i);
            BoardEntity entity = board.getEntity(position);
            Direction direction = board.getDirection((Insect) entity);
            int amountOfFood = board.getDirectionSum((Insect) entity);
            if (entity instanceof Ant) {
                InsectColor color = ((Ant) entity).color;
                out.write(color + " Ant " + direction + " " + amountOfFood + "\n");
            } else if (entity instanceof Grasshopper) {
                InsectColor color = ((Grasshopper) entity).color;
                out.write(color + " Grasshopper " + direction + " " + amountOfFood + "\n");
            } else if (entity instanceof Butterfly) {
                InsectColor color = ((Butterfly) entity).color;
                out.write(color + " Butterfly " + direction + " " + amountOfFood + "\n");
            } else if (entity instanceof Spider) {
                InsectColor color = ((Spider) entity).color;
                out.write(color + " Spider " + direction + " " + amountOfFood + "\n");
            }
        }
    }
}
 
/**
 * class board which contain all our entities and be main part of the game
 */
class Board {
    /* Map for storing all entered entities */
    private Map<EntityPosition, BoardEntity> boardData = new HashMap<>();
    /* int size contain Size of the board (size X size) */
    private int size;
    private final int minBoardSize = 4;
    private final int maxBoardSize = 1000;
 
    /**
     * method to create board with entered size of the board
     *
     * @param boardSize entered size of the board
     * @throws InvalidBoardSizeException if size of the board is invalid
     */
    Board(int boardSize) throws InvalidBoardSizeException {
        /* checking that our entered size of the board is not out of bounds */
        if (boardSize < minBoardSize || boardSize > maxBoardSize) {
            throw new InvalidBoardSizeException();
        }
        this.size = boardSize;
    }
 
    /**
     * method which add entity on the board(to the map BoardData)
     *
     * @param entity entity that supposed to be added on the board
     * @throws TwoEntitiesOnSamePositionException if entity is supposed to be assigned on position with other entity
     * @throws InvalidEntityPositionException     if position of entered entity is out of bounds
     */
    public void addEntity(BoardEntity entity) throws TwoEntitiesOnSamePositionException,
            InvalidEntityPositionException {
        int x = entity.entityPosition.getX();
        int y = entity.entityPosition.getY();
 
        /* checking that our entered coordinates is out of bounds */
        if (x < 1 || x > size || y < 1 || y > size) {
            throw new InvalidEntityPositionException();
        }
 
        /* checking that we do not have more than 1 entity at the same position */
        if (this.boardData.get(entity.entityPosition) != null) {
            throw new TwoEntitiesOnSamePositionException();
        }
        this.boardData.put(entity.entityPosition, entity);
    }
 
    /**
     * Getter-method which get the entity via it's position
     *
     * @param position entity's position
     * @return entity on this position
     */
    public BoardEntity getEntity(EntityPosition position) {
        return boardData.get(position);
    }
 
    /**
     * Getter-method which get the direction which insect will choose via insect
     *
     * @param insect insect for which we need to find the direction
     * @return best direction of entity
     */
    public Direction getDirection(Insect insect) {
        return insect.getBestDirection(boardData, size);
    }
 
    /**
     * Getter-method which get the sum of "eaten" at this direction
     *
     * @param insect insect for which we need to find the sum of "eaten" food
     * @return amount of "eaten" food
     */
    public int getDirectionSum(Insect insect) {
        return insect.travelDirection(this.getDirection(insect), boardData, size);
    }
}
 
/**
 * abstract class which is extended by all possible entities
 */
abstract class BoardEntity {
    protected EntityPosition entityPosition;
}
 
/**
 * class which implements food points on the board
 */
class FoodPoint extends BoardEntity {
    protected int value;
 
    /**
     * creates the food point
     *
     * @param position is position of the food point
     * @param value    is the amount of food contained at this food point
     */
    public FoodPoint(EntityPosition position, int value) {
        this.value = value;
        this.entityPosition = position;
    }
 
    /**
     * Getter-method to get the amount of food at some food point
     *
     * @return amount of food
     */
    public int getValue() {
        return this.value;
    }
}
 
/**
 * enumerator which contains all possible colors of the insects
 */
enum InsectColor {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow");
    private final String textRepresentation;
 
    /**
     * method which assigning entered color to color in enumerator
     *
     * @param string entered string color
     */
    InsectColor(String string) {
        this.textRepresentation = string;
    }
 
    /**
     * method that get string and assign it to the enum color respectively
     *
     * @param s string
     * @return enum color
     * @throws InvalidInsectColorException if the entered string is invalid(does not contain color)
     */
    public static InsectColor toColor(String s) throws InvalidInsectColorException {
        switch (s) {
            case "Blue":
                return BLUE;
            case "Red":
                return RED;
            case "Green":
                return GREEN;
            case "Yellow":
                return YELLOW;
            default:
                throw new InvalidInsectColorException();
        }
    }
 
    /**
     * method to return enum color as a text
     *
     * @return color as a text (BLUE = Blue)
     */
    @Override
    public String toString() {
        return textRepresentation;
    }
}
 
/**
 * abstract class which is extended by all movable creatures
 */
abstract class Insect extends BoardEntity {
    /* color of the insect */
    protected InsectColor color;
 
    /**
     * method to create the insect
     */
    public Insect() {
    }
 
    /**
     * method to create insect with some parameters
     *
     * @param position position of the insect
     * @param color    color of the insect
     */
    public Insect(EntityPosition position, InsectColor color) {
        this.entityPosition = position;
        this.color = color;
    }
 
    /**
     * abstract method for all insects to get the "Best direction"(direction with the largest amount of the food)
     *
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return one of the enum's "Direction" direction
     */
    abstract Direction getBestDirection(Map<EntityPosition, BoardEntity> boardData, int boardSize);
 
    /**
     * method that implements the move of particular insect in some direction
     *
     * @param dir       the direction in which the particular insect can move
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return the amount of food eaten by insect (insect met while move in this direction)
     */
    abstract int travelDirection(Direction dir, Map<EntityPosition, BoardEntity> boardData, int boardSize);
}
 
/**
 * class which implements one of the possible types of the insects: Grasshopper
 */
class Grasshopper extends Insect {
    /* array that contains all colors of entered insects type Grasshopper */
    private static InsectColor[] grasshoppers = {null, null, null, null};
 
    /**
     * method that creates the Grasshopper
     *
     * @param entityPosition position of the grasshopper
     * @param color          color of the Grasshopper
     * @throws DuplicateInsectException if grasshopper is supposed to be assigned on position with other entity
     */
    public Grasshopper(EntityPosition entityPosition, InsectColor color) throws DuplicateInsectException {
 
        for (int i = 0; i < grasshoppers.length; i++) {
            if (grasshoppers[i] == color) {
                throw new DuplicateInsectException();
            }
        }
        for (int i = 0; i < grasshoppers.length; i++) {
            if (grasshoppers[i] == null) {
                grasshoppers[i] = color;
                break;
            }
        }
        this.entityPosition = entityPosition;
        this.color = color;
    }
 
    /**
     * method that finds the direction with the maximum amount of food that grasshopper can "eat" (meet)
     *
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return the best direction in which the grasshopper can move
     */
    @Override
    public Direction getBestDirection(Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        /* int variables are stand for the sum of the amount of food meet at the pass with directions: N-north, etc */
        int sumN = 0;
        int sumE = 0;
        int sumS = 0;
        int sumW = 0;
 
        int insectY = entityPosition.getY();        /* y coordinate of the grasshopper*/
        int insectX = entityPosition.getX();        /* x coordinate of the grasshopper*/
 
        /* possible x coordinate for food point when insect move in North direction */
        int possibleFoodXForN = insectX;
 
        /* cycle which implements how insect move on North direction and amount of food which can be eaten*/
        for (int i = 2; insectX - i > 0; i += 2) {
            possibleFoodXForN -= 2;
            EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXForN, insectY);
            if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                sumN += possibleFood.getValue();
            }
        }
 
        /* possible x coordinate for food point when insect move in East direction */
        int possibleFoodYForE = insectY;
 
        /* cycle which implements how insect move on East direction and amount of food which can be eaten*/
        for (int i = 2; insectY + i <= boardSize; i += 2) {
            possibleFoodYForE += 2;
            EntityPosition possibleFoodPos = new EntityPosition(insectX, possibleFoodYForE);
            if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                sumE += possibleFood.getValue();
            }
        }
 
        /* possible x coordinate for food point when insect move in South direction */
        int possibleFoodXForS = insectX;
 
        /* cycle which implements how insect move on South direction and amount of food which can be eaten*/
        for (int i = 2; insectX + i <= boardSize; i += 2) {
            possibleFoodXForS += 2;
            EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXForS, insectY);
            if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                sumS += possibleFood.getValue();
            }
        }
 
        /* possible x coordinate for food point when insect move in West direction */
        int possibleFoodYForW = insectY;
 
        /* cycle which implements how insect move on West direction and amount of food which can be eaten*/
        for (int i = 2; insectY - i > 0; i += 2) {
            possibleFoodYForW -= 2;
            EntityPosition possibleFoodPos = new EntityPosition(insectX, possibleFoodYForW);
            if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                sumW += possibleFood.getValue();
            }
        }
 
        /* condition which implements choose of the best direction */
        if (sumN >= sumE && sumN >= sumS && sumN >= sumW) {
            return Direction.N;
        } else if (sumE >= sumS && sumE >= sumW) {
            return Direction.E;
        } else if (sumS >= sumW) {
            return Direction.S;
        } else {
            return Direction.W;
        }
    }
 
    /**
     * method which implements the possible move of grasshopper
     *
     * @param dir       the direction in which the particular insect can move
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return amount of "eaten" food at this dir
     */
    @Override
    public int travelDirection(Direction dir, Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        int sumOfFood = 0;                          /* variable which store the amount of eaten food */
        int insectX = entityPosition.getX();        /* x coordinate of the grasshopper */
        int insectY = entityPosition.getY();        /* y coordinate of the grasshopper */
 
        /* switch case which will implements grasshopper's move in some direction
         * will update the amount of eaten food and delete it from the board(from the map BoardData)
         * and after all delete grasshopper from the board
         * or if grasshopper "meets"(visits the position with) entity different color
         * will delete grasshopper from the board
         */
        switch (dir) {
            case N:
                while (insectX > 0) {
                    insectX -= 2;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case E:
                while (insectY < boardSize) {
                    insectY += 2;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case S:
                while (insectX < boardSize) {
                    insectX += 2;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case W:
                while (insectY > 0) {
                    insectY -= 2;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            default:
                return -1;
        }
    }
}
 
/**
 * class which implements one of the possible types of the insects: Butterfly
 */
class Butterfly extends Insect implements OrthogonalMoving {
    /* array that contains all colors of entered insects type Butterfly */
    private static InsectColor[] butterflies = {null, null, null, null};
 
    /**
     * method that creates the butterfly
     *
     * @param entityPosition position of the butterfly
     * @param color          color of the butterfly
     * @throws DuplicateInsectException if butterfly is supposed to be assigned on position with other entity
     */
    public Butterfly(EntityPosition entityPosition, InsectColor color) throws DuplicateInsectException {
        for (int i = 0; i < butterflies.length; i++) {
            if (butterflies[i] == color) {
                throw new DuplicateInsectException();
            }
        }
        for (int i = 0; i < butterflies.length; i++) {
            if (butterflies[i] == null) {
                butterflies[i] = color;
                break;
            }
        }
        this.entityPosition = entityPosition;
        this.color = color;
    }
 
    /**
     * method that finds the direction with the maximum amount of food that butterfly can "eat" (meet)
     *
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return the best direction in which the butterfly can move
     */
    @Override
    public Direction getBestDirection(Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        /* int variables are stand for the sum of the amount of food meet at the pass with directions: N-north, etc */
        int sumN = getOrthogonalDirectionVisibleValue(Direction.N, entityPosition, boardData, boardSize);
        int sumE = getOrthogonalDirectionVisibleValue(Direction.E, entityPosition, boardData, boardSize);
        int sumS = getOrthogonalDirectionVisibleValue(Direction.S, entityPosition, boardData, boardSize);
        int sumW = getOrthogonalDirectionVisibleValue(Direction.W, entityPosition, boardData, boardSize);
 
        /* condition which implements choose of the best direction */
        if (sumN >= sumE && sumN >= sumS && sumN >= sumW) {
            return Direction.N;
        } else if (sumE >= sumS && sumE >= sumW) {
            return Direction.E;
        } else if (sumS >= sumW) {
            return Direction.S;
        } else {
            return Direction.W;
        }
    }
 
    /**
     * method which implements the possible move of butterfly
     *
     * @param dir       the direction in which the particular insect can move
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return amount of "eaten" food at this dir
     */
    @Override
    public int travelDirection(Direction dir, Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        return travelOrthogonally(this.getBestDirection(boardData, boardSize), entityPosition, color, boardData,
                boardSize);
    }
}
 
/**
 * class which implements one of the possible types of the insects: Ant
 */
class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {
    /* array that contains all colors of entered insects type Ant */
    private static InsectColor[] ants = {null, null, null, null};
 
    /**
     * method that creates the ant
     *
     * @param entityPosition position of the ant
     * @param color          color of the ant
     * @throws DuplicateInsectException if ant is supposed to be assigned on position with other entity
     */
    public Ant(EntityPosition entityPosition, InsectColor color) throws DuplicateInsectException {
        for (int i = 0; i < ants.length; i++) {
            if (ants[i] == color) {
                throw new DuplicateInsectException();
            }
        }
        for (int i = 0; i < ants.length; i++) {
            if (ants[i] == null) {
                ants[i] = color;
                break;
            }
        }
        this.entityPosition = entityPosition;
        this.color = color;
    }
 
    /**
     * method that finds the direction with the maximum amount of food that ant can "eat" (meet)
     *
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return the best direction in which the ant can move
     */
    @Override
    public Direction getBestDirection(Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        /* int variables are stand for the sum of the amount of food meet at the pass with directions: N-north, etc. */
        int sumN = getOrthogonalDirectionVisibleValue(Direction.N, entityPosition, boardData, boardSize);
        int sumE = getOrthogonalDirectionVisibleValue(Direction.E, entityPosition, boardData, boardSize);
        int sumS = getOrthogonalDirectionVisibleValue(Direction.S, entityPosition, boardData, boardSize);
        int sumW = getOrthogonalDirectionVisibleValue(Direction.W, entityPosition, boardData, boardSize);
        int sumNE = getDiagonalDirectionVisibleValue(Direction.NE, entityPosition, boardData, boardSize);
        int sumSE = getDiagonalDirectionVisibleValue(Direction.SE, entityPosition, boardData, boardSize);
        int sumSW = getDiagonalDirectionVisibleValue(Direction.SW, entityPosition, boardData, boardSize);
        int sumNW = getDiagonalDirectionVisibleValue(Direction.NW, entityPosition, boardData, boardSize);
 
        /* condition which implements choose of the best direction */
        if (sumN >= sumE && sumN >= sumS && sumN >= sumW && sumN >= sumNE && sumN >= sumSE && sumN >= sumSW && sumN
                >= sumNW) {
            return Direction.N;
        } else if (sumE >= sumS && sumE >= sumW && sumE >= sumNE && sumE >= sumSE && sumE >= sumSW && sumE >= sumNW) {
            return Direction.E;
        } else if (sumS >= sumW && sumS >= sumNE && sumS >= sumSE && sumS >= sumSW && sumS >= sumNW) {
            return Direction.S;
        } else if (sumW >= sumNE && sumW >= sumSE && sumW >= sumSW && sumW >= sumNW) {
            return Direction.W;
        } else if (sumNE >= sumSE && sumNE >= sumSW && sumNE >= sumNW) {
            return Direction.NE;
        } else if (sumSE >= sumSW && sumSE >= sumNW) {
            return Direction.SE;
        } else if (sumSW >= sumNW) {
            return Direction.SW;
        } else {
            return Direction.NW;
        }
    }
 
    /**
     * method which implements the possible move of ant
     *
     * @param dir       the direction in which the particular insect can move
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return amount of "eaten" food at this dir
     */
    @Override
    public int travelDirection(Direction dir, Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        Direction bestDir = getBestDirection(boardData, boardSize);
        if (bestDir == Direction.N || bestDir == Direction.E || bestDir == Direction.S || bestDir == Direction.W) {
            return travelOrthogonally(bestDir, entityPosition, color, boardData, boardSize);
        } else {
            return travelDiagonally(bestDir, entityPosition, color, boardData, boardSize);
        }
    }
}
 
/**
 * class which implements one of the possible types of the insects: Spider
 */
class Spider extends Insect implements DiagonalMoving {
    /* array that contains all colors of entered insects type Spider */
    private static InsectColor[] spiders = {null, null, null, null};
 
    /**
     * method that creates the spider
     *
     * @param entityPosition position of the spider
     * @param color          color of the spider
     * @throws DuplicateInsectException if spider is supposed to be assigned on position with other entity
     */
    public Spider(EntityPosition entityPosition, InsectColor color) throws DuplicateInsectException {
        for (int i = 0; i < spiders.length; i++) {
            if (spiders[i] == color) {
                throw new DuplicateInsectException();
            }
        }
        for (int i = 0; i < spiders.length; i++) {
            if (spiders[i] == null) {
                spiders[i] = color;
                break;
            }
        }
        this.entityPosition = entityPosition;
        this.color = color;
    }
 
    /**
     * method that finds the direction with the maximum amount of food that spider can "eat" (meet)
     *
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return the best direction in which the spider can move
     */
    @Override
    public Direction getBestDirection(Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        /* int variables are stand for the sum of the amount of food meet at the pass with directions:NE-north-east, etc
         *  */
        int sumNE = getDiagonalDirectionVisibleValue(Direction.NE, entityPosition, boardData, boardSize);
        int sumSE = getDiagonalDirectionVisibleValue(Direction.SE, entityPosition, boardData, boardSize);
        int sumSW = getDiagonalDirectionVisibleValue(Direction.SW, entityPosition, boardData, boardSize);
        int sumNW = getDiagonalDirectionVisibleValue(Direction.NW, entityPosition, boardData, boardSize);
 
        /* condition which implements choose of the best direction */
        if (sumNE >= sumSE && sumNE >= sumSW && sumNE >= sumNW) {
            return Direction.NE;
        } else if (sumSE >= sumSW && sumSE >= sumNW) {
            return Direction.SE;
        } else if (sumSW >= sumNW) {
            return Direction.SW;
        } else {
            return Direction.NW;
        }
    }
 
    /**
     * method which implements the possible move of spider
     *
     * @param dir       the direction in which the particular insect can move
     * @param boardData map with all entities on the board
     * @param boardSize size of the board which contains this insect
     * @return amount of "eaten" food at this dir
     */
    @Override
    public int travelDirection(Direction dir, Map<EntityPosition, BoardEntity> boardData, int boardSize) {
        return travelDiagonally(this.getBestDirection(boardData, boardSize), entityPosition, color, boardData,
                boardSize);
    }
}
 
/**
 * interface which implements orthogonal moving of insect on the gaming board
 */
interface OrthogonalMoving {
 
    /**
     * default method which counts the amount of possibly "eaten" food on some orthogonal direction
     *
     * @param dir            direction of insect move
     * @param entityPosition insect's position
     * @param boardData      all created entities on the board
     * @param boardSize      size of the board
     * @return amount of eaten possibly food
     */
    default int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<EntityPosition,
            BoardEntity> boardData, int boardSize) {
        int sumOfFood = 0;                      /* amount of "eaten" food */
        int insectY = entityPosition.getY();    /* y coordinate of insect */
        int insectX = entityPosition.getX();    /* x coordinate of insect */
 
        /* switch case which will implements insect's move in some direction
         * and will count amount of food in this direction
         */
        switch (dir) {
            case N:
                int possibleFoodXForN = insectX;
                for (int i = 1; insectX - i > 0; i++) {
                    possibleFoodXForN -= 1;
                    EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXForN, insectY);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            case E:
                int possibleFoodYForE = insectY;
                for (int i = 1; insectY + i <= boardSize; i++) {
                    possibleFoodYForE += 1;
                    EntityPosition possibleFoodPos = new EntityPosition(insectX, possibleFoodYForE);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            case S:
                int possibleFoodXForS = insectX;
                for (int i = 1; insectX + i <= boardSize; i++) {
                    possibleFoodXForS += 1;
                    EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXForS, insectY);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            case W:
                int possibleFoodYForW = insectY;
                for (int i = 1; insectY - i > 0; i++) {
                    possibleFoodYForW -= 1;
                    EntityPosition possibleFoodPos = new EntityPosition(insectX, possibleFoodYForW);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            default:
                return -1;
        }
    }
 
    /**
     * default method which implements tho move of insect in some orthogonal direction
     *
     * @param dir            insect's move direction
     * @param entityPosition insect's position
     * @param color          insect's color
     * @param boardData      all created entities on the board
     * @param boardSize      size of the board
     * @return amount of eaten food
     */
    default int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<EntityPosition,
            BoardEntity> boardData, int boardSize) {
        int sumOfFood = 0;
        int insectX = entityPosition.getX();
        int insectY = entityPosition.getY();
 
        /* switch case which will implements insect's move in some direction
         * will update the amount of eaten food and delete it from the board(from the map BoardData)
         * and after all delete grasshopper from the board
         * or if grasshopper "meets"(visits the position with) entity different color will delete from the board
         */
        switch (dir) {
            case N:
                while (insectX > 0) {
                    insectX -= 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case E:
                while (insectY < boardSize) {
                    insectY += 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case S:
                while (insectX < boardSize) {
                    insectX += 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case W:
                while (insectY > 0) {
                    insectY -= 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            default:
                return -1;
        }
    }
}
 
/**
 * interface which implements diagonal moving of insect on the gaming board
 */
interface DiagonalMoving {
 
    /**
     * default method which counts the amount of possibly "eaten" food on some diagonal direction
     *
     * @param dir            direction of insect move
     * @param entityPosition insect's position
     * @param boardData      all created entities on the board
     * @param boardSize      size of the board
     * @return amount of eaten possibly food
     */
    default int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<EntityPosition,
            BoardEntity> boardData, int boardSize) {
        int sumOfFood = 0;                      /* amount of "eaten" food */
        int insectY = entityPosition.getY();    /* y coordinate of insect */
        int insectX = entityPosition.getX();    /* x coordinate of insect */
 
        /* switch case which will implements insect's move in some direction
         * and will count amount of food in this direction
         */
        switch (dir) {
            case NE:
                int possibleFoodXNE = insectX;
                int possibleFoodYNE = insectY;
                for (int i = 1; insectX - i > 0 && insectY + i <= boardSize; i++) {
                    possibleFoodXNE -= 1;
                    possibleFoodYNE += 1;
                    EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXNE, possibleFoodYNE);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            case SE:
                int possibleFoodXSE = insectX;
                int possibleFoodYSE = insectY;
                for (int i = 1; insectX + i <= boardSize && insectY <= boardSize; i++) {
                    possibleFoodXSE += 1;
                    possibleFoodYSE += 1;
                    EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXSE, possibleFoodYSE);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            case SW:
                int possibleFoodXSW = insectX;
                int possibleFoodYSW = insectY;
                for (int i = 1; insectX + i <= boardSize && insectY - i > 0; i++) {
                    possibleFoodXSW += 1;
                    possibleFoodYSW -= 1;
                    EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXSW, possibleFoodYSW);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            case NW:
                int possibleFoodXNW = insectX;
                int possibleFoodYNW = insectY;
                for (int i = 1; insectX - i > 0 && insectY - i > 0; i++) {
                    possibleFoodXNW -= 1;
                    possibleFoodYNW -= 1;
                    EntityPosition possibleFoodPos = new EntityPosition(possibleFoodXNW, possibleFoodYNW);
                    if (boardData.get(possibleFoodPos) instanceof FoodPoint) {
                        FoodPoint possibleFood = (FoodPoint) boardData.get(possibleFoodPos);
                        sumOfFood += possibleFood.getValue();
                    }
                }
                return sumOfFood;
            default:
                return -1;
        }
    }
 
    /**
     * default method which implements tho move of insect in some diagonal direction
     *
     * @param dir            insect's move direction
     * @param entityPosition insect's position
     * @param color          insect's color
     * @param boardData      all created entities on the board
     * @param boardSize      size of the board
     * @return amount of eaten food
     */
    default int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<EntityPosition,
            BoardEntity> boardData, int boardSize) {
        int sumOfFood = 0;                      /* amount of "eaten" food */
        int insectY = entityPosition.getY();    /* y coordinate of insect */
        int insectX = entityPosition.getX();    /* x coordinate of insect */
 
        /* switch case which will implements insect's move in some direction
         * will update the amount of eaten food and delete it from the board(from the map BoardData)
         * and after all delete grasshopper from the board
         * or if grasshopper "meets"(visits the position with) entity different color will delete from the board
         */
        switch (dir) {
            case NE:
                while (insectX > 0 && insectY < boardSize) {
                    insectX -= 1;
                    insectY += 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case SE:
                while (insectX < boardSize && insectY < boardSize) {
                    insectX += 1;
                    insectY += 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case SW:
                while (insectX < boardSize && insectY > 0) {
                    insectX += 1;
                    insectY -= 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            case NW:
                while (insectX > 0 && insectY > 0) {
                    insectX -= 1;
                    insectY -= 1;
                    EntityPosition possiblePosition = new EntityPosition(insectX, insectY);
                    if (boardData.get(possiblePosition) instanceof FoodPoint) {
                        FoodPoint food = (FoodPoint) boardData.get(possiblePosition);
                        sumOfFood += food.getValue();
                        boardData.remove(possiblePosition);
                    } else if (boardData.get(possiblePosition) instanceof Insect) {
                        Insect insect = (Insect) boardData.get(possiblePosition);
                        if (insect.color != color) {
                            boardData.remove(entityPosition);
                            return sumOfFood;
                        }
                    }
                }
                boardData.remove(entityPosition);
                return sumOfFood;
            default:
                return -1;
        }
    }
}
