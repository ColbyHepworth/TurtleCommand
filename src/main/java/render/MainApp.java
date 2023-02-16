package render;

import backend.TurtleServer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import turtle.Turtle;
import turtle.Turtles;
import world.Block;

import java.util.Objects;

public class MainApp extends Application {

    private static final double SENSITIVITY = 1;
    private static final double ZOOM_SENSITIVITY = 2;

    private static final int ZOOM_MIN = 200;
    private static final int ZOOM_MAX = 10000;

    private final double sceneWidth = 1280;
    private final double sceneHeight = 720;

    private static final String texture = "res/wood.png";

    private double cursorLocX, cursorLocY;

    private final Translate zoom = new Translate(0, -16, -2000);

    private double angleX = 0;
    private double angleY = 0;

    private final Rotate rotateX = new Rotate(0, 0, -16, 0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, 0, -16, 0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, 0, -16, 0, Rotate.Z_AXIS);
    private final TurtleServer turtleServer = new TurtleServer();

    private final World group = new World(turtleServer.getTurtles());
    private PerspectiveCamera camera;
    private Turtles turtles;

    @Override public void start(Stage stage) {

        turtleServer.start();


        AmbientLight light = new AmbientLight(Color.WHITE);
        light.setColor(Color.WHITE);
        light.setTranslateX(-200);
        light.setTranslateY(-200);
        light.setTranslateZ(-200);
        group.getChildren().add(light);

        camera = new PerspectiveCamera(true);
        camera.setVerticalFieldOfView(false);
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);
        camera.getTransforms().addAll(rotateX, rotateY, rotateZ, zoom);

        Scene scene = new Scene(group, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.rgb(74, 109, 229));
        scene.setCamera(camera);

        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                scene.setCursor(Cursor.MOVE);
                cursorLocX = event.getScreenX();
                cursorLocY = event.getScreenY();
            }
        });

        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.MIDDLE)
                scene.setCursor(Cursor.DEFAULT);
        });

        scene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                rotateCameraBy(event.getScreenY() - cursorLocY, event.getScreenX() - cursorLocX);
                cursorLocX = event.getScreenX();
                cursorLocY = event.getScreenY();
            }
        });

        scene.setOnScroll(event -> zoomCameraBy(event.getDeltaY()));

        stage.setTitle("TurtleCommand");
        stage.setScene(scene);
        stage.show();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    updateScene();
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void zoomCameraBy(double diff) {
        if (diff == 0)
            return;
        if (-(zoom.getZ() + diff) >= ZOOM_MIN && -(zoom.getZ() + diff) <= ZOOM_MAX)
            zoom.setZ(zoom.getZ() + diff * ZOOM_SENSITIVITY);
    }

    private void rotateCameraBy(double diffX, double diffY) {
        if (diffX == 0 && diffY == 0)
            return;
        angleX -= diffX / sceneWidth * 360 * SENSITIVITY;
        angleY += diffY / sceneHeight * 360 * SENSITIVITY;

        rotateY.setAngle(angleY);
        rotateX.setAngle(angleX*Math.cos(Math.toRadians(angleY)));
        rotateZ.setAngle(angleX*Math.sin(Math.toRadians(angleY)));
    }

    private void updateScene() {
        Turtles turtles = turtleServer.getTurtles();
        for (Turtle turtle : turtles.getTurtles().values()) {
            group.renderTurtle(turtle);
            camera.setTranslateX(turtle.getPosition().toPoint3D().multiply(100).getX());
            camera.setTranslateY(turtle.getPosition().toPoint3D().multiply(100).getY());
            camera.setTranslateZ(turtle.getPosition().toPoint3D().multiply(100).getZ());
            for (Block block : turtle.getScanned()) {
                if (!Objects.equals(block.getName(), "empty")) {
                    Point3D position = block.getPosition().toPoint3D().multiply(100);
                    group.addCube(position);
                }
            }
        }
    }
}