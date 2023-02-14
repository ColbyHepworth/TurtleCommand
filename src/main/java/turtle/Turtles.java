package turtle;
import java.util.HashMap;

public class Turtles {

    private final HashMap<Integer, Turtle> turtles;

    public Turtles() {
        this.turtles = new HashMap<>();
    }

    public void newTurtle(int id) {
        this.turtles.put(id, new Turtle(id));
    }

    public void addTurtle(Turtle turtle) {
        this.turtles.put(turtle.getId(), turtle);
    }

    public void removeTurtle(int id) {
        this.turtles.remove(id);
    }

    public Turtle getTurtle(int index) {
        return this.turtles.get(index);
    }

    public void updateTurtle(int index, Turtle turtle) {
        this.turtles.get(index).updateTurtle(turtle);

    }

    public int getNumberOfTurtles() {
        return this.turtles.size();
    }

    public HashMap<Integer, Turtle> getTurtles() {
        return turtles;
    }

    @Override
    public String toString() {
        return this.turtles.toString();
    }

}

