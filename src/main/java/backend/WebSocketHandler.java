package backend;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class WebSocketHandler {

    private final TurtleServer turtleServer;

    public WebSocketHandler(TurtleServer turtleServer) {
        this.turtleServer = turtleServer;
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {

    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {

    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        turtleServer.onMessage(message, user);
    }

}