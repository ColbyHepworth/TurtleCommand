package turtle;

import com.google.gson.annotations.SerializedName;
import world.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Turtle {

    @SerializedName("id")
    private final int id;

    @SerializedName("direction")
    private String direction;

    @SerializedName("position")
    private Position position;

    @SerializedName("path")
    private List<Position> path;

    @SerializedName("inventory")
    private Inventory inventory = new Inventory();

    @SerializedName("fuel")
    private int fuel;

    @SerializedName("status")
    private Map<String, Boolean> status;

    Turtle(int id) {
        this.id = id;
        this.position = new Position(0, 0, 0);
        this.direction = "north";
        this.path = new ArrayList<>();
        this.fuel = 0;
        this.status = Map.of(
                "isMoving", false,
                "isDigging", false);
    }

    public void updateTurtle(Turtle newTurtle) {
        this.direction = newTurtle.getDirection();
        this.position = newTurtle.getPosition();
        this.fuel = newTurtle.getFuel();
        this.path = newTurtle.getPath();
        this.inventory = newTurtle.getInventory();
        this.status = newTurtle.status;

    }

    public void updateDirection(String direction) {
        this.direction = direction;
    }

    public void updatePosition(Position position) {
        this.position = position;
    }

    public void updatePath(Position position) {
        this.path.add(position);
    }

    public void setPath(List<Position> path) {
        this.path.clear();
        this.path.addAll(path);
    }

    public List<Position> getPath() {
        return path;
    }

    public void setStatus(String status, boolean value) {
        this.status.put(status, value);
    }

    public boolean getStatus(String status) {
        return this.status.get(status);
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public void addFuel(int fuel) {
        this.fuel += fuel;
    }

    public void removeFuel(int fuel) {
        this.fuel -= fuel;
    }

    public int getFuel() {
        return this.fuel;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public String getDirection() {
        return direction;
    }

    public HashMap<String, String> getDirectionData() {
        HashMap<String, String> directionData = new HashMap<>();
        directionData.put("direction", this.direction);
        return directionData;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getZ() {
        return position.getZ();
    }

    @Override
    public String toString() {
        return Map.of(
                "id", this.id,
                "direction", this.direction,
                "position", this.position,
                "path", this.path,
                "inventory", this.inventory,
                "fuel", this.fuel,
                "status", this.status).toString();
    }
}