package backend;

import com.google.gson.Gson;
import turtle.Turtle;

public class Deserializer {
    static Gson gson = new Gson();
    public static Turtle deserializeTurtle(String json) {
        return gson.fromJson(json, Turtle.class);
    }
}