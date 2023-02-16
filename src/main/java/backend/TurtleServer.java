package backend;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import javafx.geometry.Point3D;
import javafx.scene.effect.Light;
import render.World;
import spark.Spark;
import turtle.Turtle;
import turtle.Turtles;
import org.eclipse.jetty.websocket.api.Session;
import world.Block;
import world.Position;

import static spark.Spark.*;

public class TurtleServer {

    private static final int PORT = 8080;
    private static final Turtles turtles = new Turtles();
    private static final Map<Session, String> clients = new ConcurrentHashMap<>();



    public void start() {
        WebSocketHandler webSocketHandler = new WebSocketHandler(this);
        webSocket("/turtles", webSocketHandler);
        port(PORT);
        init();
    }

    public Turtles getTurtles() {
        return turtles;
    }

    void onMessage(String message, Session session) {
        Turtle turtle = Deserializer.deserializeTurtle(message);
        turtles.addTurtle(turtle);
    }

    void broadcastMessage(String sender, String message) {
        clients.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString("hey");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

