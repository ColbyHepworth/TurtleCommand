package backend;

import com.google.gson.Gson;
import turtle.Turtle;
import turtle.Turtles;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class TurtleServer {

    private static final int PORT = 8080;
    private static final Turtles turtles = new Turtles();
    private static final Map<Session, String> clients = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        webSocket("/turtles", WebSocketHandler.class);
        port(PORT);
        init();
    }

    static class Deserializer {
        static Gson gson = new Gson();
        public static Turtle deserializeTurtle(String json) {
            return gson.fromJson(json, Turtle.class);
        }
    }

    public static void onMessage(String message, Session session) {
        System.out.println(session);
        System.out.println("Received message: " + message);
    }

    public static void broadcastMessage(String sender, String message) {
        clients.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString("hey");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

