package world;

import com.google.gson.annotations.SerializedName;

public class Block {

    @SerializedName("name")
    private String[] name; // This is only an array because the client has to return it as one

    @SerializedName("position")
    private Position position;

    public String getName() {
        return name[0];
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Boolean isEmpty() {
        return name[0].equals("empty");
    }
}
