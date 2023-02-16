package render;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import turtle.Turtle;
import turtle.Turtles;
import world.Position;

import java.util.HashMap;
import java.util.Map;

public class World extends Group {

    private final Map<Point3D, BorderedCube> cubes = new HashMap<>();
    private final Turtles turtles;
    private Map<Turtle, BorderedCube> turtleCubes;


    public World(Turtles turtles) {
        super();
        this.turtles = turtles;
        this.turtleCubes = new HashMap<>();
        renderTurtles();
    }

    public BorderedCube getCube(Point3D point) {
        return cubes.get(point);
    }

    public void addCube(Point3D point) {
        if (cubes.containsKey(point)) {
            return;
        }
        BorderedCube cube = new BorderedCube(point);
        cubes.put(point, cube);
        super.getChildren().add(cube);

    }

    public void addCube(Point3D point, int width, int height, int depth) {
        BorderedCube cube = new BorderedCube(point, width, height, depth);
        cubes.put(point, cube);
        super.getChildren().add(cube);
    }

    public void removeCube(Point3D point) {
        BorderedCube cube = cubes.get(point);
        cubes.remove(point);
        super.getChildren().remove(cube);
    }

    public void moveCube(Point3D point, Point3D newPoint) {
        BorderedCube cube = cubes.get(point);
        cube.moveCube(newPoint);
        cubes.remove(point);
        cubes.put(newPoint, cube);
    }

    public void rotateCube(Point3D point, double angle) {
        BorderedCube cube = cubes.get(point);
        cube.rotateCube(angle);
    }

    public void renderTurtles() {
        for (Turtle turtle : turtles.getTurtles().values()) {
            if (!turtleCubes.containsKey(turtle)) {
                BorderedCube cube = new BorderedCube(turtle.getPosition().toPoint3D().multiply(100), 50, 50, 50);
                turtleCubes.put(turtle, cube);
                super.getChildren().add(cube);
            }
            BorderedCube cube = turtleCubes.get(turtle);
            cube.moveCube(turtle.getPosition().toPoint3D());
        }
    }

    public void moveTurtle(Turtle turtle, Point3D position) {
        System.out.println("Moving turtle to " + position);
        turtleCubes.get(turtle).moveCube(position);
    }

    public void renderTurtle(Turtle turtle) {
        BorderedCube cube = new BorderedCube(turtle.getPosition().toPoint3D().multiply(100), 50, 50, 50);
        if (turtleCubes.containsKey(turtle)) {
            moveTurtle(turtle, turtle.getPosition().toPoint3D().multiply(100));
        } else {
            turtleCubes.put(turtle, cube);
            super.getChildren().add(cube);
        }
    }

    public static class BorderedCube extends Group {

        protected BorderedCube(Point3D point) {
            this(point, 100, 100, 100);
        }

        BorderedCube(Point3D point, int width, int height, int depth) {
            super();
            Cube cube = new Cube(point, width, height, depth);
            super.getChildren().add(cube);

        }

        void moveCube(Point3D point) {
            super.getTransforms().add(new Translate(point.getX(), point.getY(), point.getZ()));
        }

        void rotateCube(double angle) {
            super.getTransforms().add(new Rotate(angle, 0, 0, 0, Rotate.Y_AXIS));
        }

        Position getPosition() {
            return new Position((int) super.getTranslateX(), (int) super.getTranslateY(), (int) super.getTranslateZ());
        }

        private class Cube extends Box {

            Cube(Point3D point) {
                this(point, 100, 100, 100);
            }

            Cube(Point3D point, int width, int height, int depth) {
                super(width, height, depth);
                PhongMaterial material = new PhongMaterial(Color.rgb(206, 216, 247));

                super.setTranslateX(point.getX());
                super.setTranslateY(point.getY());
                super.setTranslateZ(point.getZ());
                super.setMaterial(material);
                createBoxLines();
            }

            private void createBoxLines() {

                double x = super.getTranslateX();
                double y = super.getTranslateY();
                double z = super.getTranslateZ();
                double halfWidth = super.getWidth() / 2;
                double halfHeight = super.getHeight() / 2;
                double halfDepth = super.getDepth() / 2;

                Point3D p1 = new Point3D(x - halfWidth, y - halfHeight, z - halfDepth);
                Point3D p2 = new Point3D(x + halfWidth, y - halfHeight, z - halfDepth);
                Point3D p3 = new Point3D(x - halfWidth, y + halfHeight, z - halfDepth);
                Point3D p4 = new Point3D(x + halfWidth, y + halfHeight, z - halfDepth);
                Point3D p5 = new Point3D(x - halfWidth, y - halfHeight, z + halfDepth);
                Point3D p6 = new Point3D(x + halfWidth, y - halfHeight, z + halfDepth);
                Point3D p7 = new Point3D(x - halfWidth, y + halfHeight, z + halfDepth);
                Point3D p8 = new Point3D(x + halfWidth, y + halfHeight, z + halfDepth);

                createLine(p1, p2);
                createLine(p1, p3);
                createLine(p3, p4);
                createLine(p2, p4);
                createLine(p5, p6);
                createLine(p5, p7);
                createLine(p7, p8);
                createLine(p6, p8);
                createLine(p1, p5);
                createLine(p2, p6);
                createLine(p3, p7);
                createLine(p4, p8);
            }

            private void createLine(Point3D origin, Point3D target) {

                Point3D yAxis = new Point3D(0, 1, 0);
                Point3D diff = target.subtract(origin);
                double height = diff.magnitude();

                Point3D mid = target.midpoint(origin);
                Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

                Point3D axisOfRotation = diff.crossProduct(yAxis);
                double angle = Math.acos(diff.normalize().dotProduct(yAxis));
                Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);


                Cylinder line = new Cylinder(1, height);
                line.setMaterial(new PhongMaterial(Color.BLACK));
                line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

                BorderedCube.super.getChildren().add(line);
            }
        }
    }
}


