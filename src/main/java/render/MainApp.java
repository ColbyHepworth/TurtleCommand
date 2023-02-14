package render;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final int WIDTH = 1400;
    private static final int HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    static Group group = new Group();



    @Override
    public void start(Stage primaryStage) {


        PhongMaterial material = new PhongMaterial();


        WorldGroup group = new WorldGroup();


        group.addCube(new Point3D(0, 0, 0));
        group.addCube(new Point3D(100, 100, 100));





        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

        AmbientLight light = new AmbientLight(Color.WHITE);
        light.setColor(Color.WHITE);
        light.setTranslateX(-200);
        light.setTranslateY(-200);
        light.setTranslateZ(-200);

        group.getChildren().add(light);

        scene.setFill(Color.rgb(0, 84, 166));
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2.0);
        group.translateYProperty().set(HEIGHT / 2.0);
        group.translateZProperty().set(-1500);

        initMouseControl(group, scene);

        primaryStage.setTitle("Testing");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    private void initMouseControl(Group group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() - delta);
        });


    }

    public static void main(String[] args) {
        launch(args);
    }





}