import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import java.awt.*;
import java.util.ArrayList;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.animation.PathTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.animation.PathTransition.OrientationType;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.VLineTo;
import javafx.scene.shape.LineTo;

public class App extends Application {

    private Group group;

    private Circle circle;

    private HBox hbox;

    private double x;

    private double y;

    private double xStartLine;

    private double yStartLine;

    private double xEndLine;

    private double yEndLine;

    private Line lowestLine = null;

    private boolean firstClick = true;

    private boolean hitLine = false;

    private ArrayList<Circle> circles = new ArrayList<Circle>();

    private ArrayList<Line> lines = new ArrayList<Line>();

    private boolean isPressed[] = new boolean[4]; // Up(0) Down(1) Right(2) Left(3)

    private double velX = 0;
    private double velY = 0;

    @Override
    public void start(Stage stage) {
        group = new Group();

        hbox = new HBox();

        hbox.setPrefHeight(1000);
        hbox.setPrefWidth(1000);

        circle = new Circle();


        EventHandler<KeyEvent> circleHandlerPressed = (e) -> {

            circleCollision();

            if(e.getCode() == KeyCode.W) { // Up
                isPressed[0] = true;
            } else if(e.getCode() == KeyCode.A) { // Left
                isPressed[3] = true;
            } else if(e.getCode() == KeyCode.S) { // Down
                isPressed[1] = true;
            } else if(e.getCode() == KeyCode.D) { // Right
                isPressed[2] = true;
            } else if(e.getCode() == KeyCode.SPACE) {
                Point mouseLoc = MouseInfo.getPointerInfo().getLocation();

                gravity(mouseLoc);
            }

            if(isPressed[0] == true && isPressed[2] == true) { // Up-Right
                velY = 15;
                velX = 15;
                upRightTick();
            } else if(isPressed[3] == true && isPressed[0] == true) { // Up-Left
                velY = 15;
                velX = 15;
                upLeftTick();
            } else if(isPressed[1] == true && isPressed[2] == true) { // Down-Right
                velY = 15;
                velX = 15;
                downRightTick();
            } else if(isPressed[1] == true && isPressed[3] == true) { // Down-Left
                velY = 15;
                velX = 15;
                downLeftTick();
            } else if(isPressed[0] == true) { // Up
                velY = 15;
                upTick();
            } else if(isPressed[3] == true) { // Left
                velX = 15;
                leftTick();
            } else if(isPressed[1] == true) { // Down
                velY = 15;
                downTick();
            } else if(isPressed[2] == true) { // Right
                velX = 15;
                rightTick();
            }

        };

        group.setOnKeyPressed(circleHandlerPressed);

        EventHandler<MouseEvent> mouseHandler = (e) -> {
            if(e.isPrimaryButtonDown()) {

                makeCircle(e.getX(), e.getY());

            } else if(e.isSecondaryButtonDown() && firstClick) {
                xStartLine = e.getX();
                yStartLine = e.getY();
                firstClick = false;
            } else {
                Line newLine = new Line(xStartLine, yStartLine, e.getX(), e.getY());
                group.getChildren().add(newLine);
                lines.add(newLine);
                firstClick = true;
            }

        };

        group.setOnMousePressed(mouseHandler);

        /*
        group.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                x = stage.getX() - mouseEvent.getScreenX();
                y = stage.getY() - mouseEvent.getScreenY();

                System.out.println("X: " + x + " Y: " + y);
            }
        });

        group.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + x);
                stage.setY(mouseEvent.getScreenY() + y);

                System.out.println("new x: " + x + " new y: " + y);
            }
        });
        */

        EventHandler<KeyEvent> circleHandlerUnpressed = (e) -> {

            if(e.getCode() == KeyCode.W) { // Up
                isPressed[0] = false;
                velY = 0;
            } else if(e.getCode() == KeyCode.A) { // Left
                isPressed[3] = false;
                velX = 0;
            } else if(e.getCode() == KeyCode.S) { // Down
                isPressed[1] = false;
                velY = 0;
            } else if(e.getCode() == KeyCode.D) { // Right
                isPressed[2] = false;
                velX = 0;
            }
        };

        group.setOnKeyReleased(circleHandlerUnpressed);

        circle.setRadius(15.0f);

        hbox.getChildren().addAll(circle);

        group.getChildren().add(hbox);

        Scene scene = new Scene(group);

        stage.setTitle("Stuff");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        group.requestFocus();
    }

    public void upTick() {
        if(circle.getCenterY() > 40.0f) {
            circle.setTranslateY(circle.getTranslateY() - velY);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            //System.out.println("Y:  " + circle.getCenterY());
        }
    }

    public void downTick() {
        if(circle.getCenterY() < 960.0f) {
            circle.setTranslateY(circle.getTranslateY() + velY);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            //System.out.println("Y:  " + circle.getCenterY());
        }
    }

    public void rightTick() {
        if(circle.getCenterX() < 960.0f) {
            circle.setTranslateX(circle.getTranslateX() + velX);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            //System.out.println("X:  " + circle.getCenterX());
        }
    }

    public void leftTick() {
        if(circle.getCenterX() > 40.0f) {
            circle.setTranslateX(circle.getTranslateX() - velX);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            //System.out.println("X:  " + circle.getCenterX());
        }
    }

    public void upRightTick() {
        if(circle.getTranslateX() < 960 && circle.getTranslateY() > 40) {
            circle.setTranslateX(circle.getTranslateX() + velX);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            circle.setTranslateY(circle.getTranslateY() - velY);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
        }
    }

    public void upLeftTick() {
        if(circle.getTranslateX() > 40 && circle.getTranslateY() > 40) {
            circle.setTranslateX(circle.getTranslateX() - velX);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            circle.setTranslateY(circle.getTranslateY() - velY);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
        }
    }

    public void downRightTick() {
        System.out.println("VEL Y: " + velY + " VEL X: " + velX);
        if(circle.getTranslateX() < 960 && circle.getTranslateY() < 960) {
            circle.setTranslateX(circle.getTranslateX() + velX);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            circle.setTranslateY(circle.getTranslateY() + velY);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            System.out.println("X: " + circle.getTranslateX() + "Y: " + circle.getTranslateY());
        }
    }

    public void downLeftTick() {
        if(circle.getTranslateX() > 40 && circle.getTranslateY() < 960) {
            System.out.println("VEL Y: " + velY + " VEL X: " + velX);
            circle.setTranslateX(circle.getTranslateX() - velX);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            circle.setTranslateY(circle.getTranslateY() + velY);
            circle.setCenterY(circle.getTranslateY() + circle.getRadius());
            circle.setCenterX(circle.getTranslateX() + circle.getRadius());
            System.out.println("X: " + circle.getTranslateX() + "Y: " + circle.getTranslateY());
        }
    }

    public void makeCircle(double x, double y) {
        Circle newCircle = new Circle();
        newCircle.setRadius(25.0f);

        group.getChildren().add(newCircle);

        newCircle.setCenterX(x);
        newCircle.setCenterY(y);

        //System.out.println("X " + newCircle.getCenterX());

        circles.add(newCircle);
    }


    public void circleCollision() {
        for(int i = 0;i < circles.size();i++) {
            if((circle.getCenterX() + circle.getRadius()) > circles.get(i).getCenterX() - circles.get(i).getRadius() && (circle.getCenterX() - circle.getRadius()) < circles.get(i).getCenterX() + circles.get(i).getRadius()) {
                if((circle.getCenterY() + circle.getRadius()) > circles.get(i).getCenterY() - circles.get(i).getRadius() && (circle.getCenterY() - circle.getRadius()) < circles.get(i).getCenterY() + circles.get(i).getRadius()) {
                    circles.get(i).setFill(javafx.scene.paint.Color.RED);
                }
            }
        }
    }


    public void gravity(Point loc) {

        Circle circle = new Circle(20);

        circle.setTranslateX(loc.getX()-460); // Not sure why it is off by 460
        circle.setTranslateY(loc.getY());

        circle.setFill(javafx.scene.paint.Color.ORANGE);

        group.getChildren().add(circle);

        Path path = new Path();
        path.getElements().add(new MoveTo(loc.getX()-460, loc.getY()));

        double bot = findBot(circle.getTranslateY(), circle);

        path.getElements().add(new LineTo(loc.getX()-460, bot));

        PathTransition pathTransition = new PathTransition();

        pathTransition.setDuration(Duration.millis(bot));
        pathTransition.setNode(circle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(true);

        EventHandler<ActionEvent> slopeHandler = (e) -> {

            // Overly complicated boolean expression
            /*
            if(isSlanted(lowestLine) && ((circle.getTranslateX() >= lowestLine.getStartX() && circle.getTranslateX() <= lowestLine.getEndX())
                    || (circle.getTranslateX() <= lowestLine.getStartX() && circle.getTranslateX() >= lowestLine.getEndX()))
                    && circle.getTranslateY() <= lowestLine.getStartY()) {

                slopeGravity(circle);
            }

             */
            if(isSlanted(lowestLine) && hitLine) {
                slopeGravity(circle);
            }
        };

        pathTransition.setOnFinished(slopeHandler);

        pathTransition.play();

    }


    public void slopeGravity(Circle circle) {

        double slope = (lowestLine.getEndY() - lowestLine.getStartY()) / ((lowestLine.getEndX() - lowestLine.getStartX()));
        if(slope < 0) {
            slope *= -1;
        }

        System.out.println("Slope: " + slope);

        Path path = new Path();
        path.getElements().add(new MoveTo(circle.getTranslateX(), circle.getTranslateY()));

        if(lowestLine.getStartY() > lowestLine.getEndY()) {
            path.getElements().add(new LineTo(lowestLine.getStartX(),lowestLine.getStartY() - 19));
        } else {
            path.getElements().add(new LineTo(lowestLine.getEndX(),lowestLine.getEndY() - 19));
        }


        PathTransition pathTransition = new PathTransition();

        pathTransition.setDuration(Duration.millis(500/slope));
        pathTransition.setNode(circle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(true);

        pathTransition.play();
    }


    public double findBot(double yPos, Circle circle) {

        if(lines.size() == 0) {
            return 979;
        }

        Line circleLine = new Line(circle.getTranslateX(), circle.getTranslateY(), circle.getTranslateX(), 1000);

        double minY = 979;
        boolean intersects = false;
        for(int i = 0;i < lines.size();i++) {
            Shape shape = Shape.intersect(circleLine, lines.get(i));
            if(shape.getBoundsInLocal().getWidth() != -1) {
                intersects = true;
                if(shape.getBoundsInLocal().getMinY()-19 < minY) {
                    minY = shape.getBoundsInLocal().getMinY()-19;
                    lowestLine = lines.get(i);
                }
            }
        }

        while(yPos < minY) {
            yPos += 1;
        }

        if(minY != 979) {
            hitLine = true;
        } else {
            hitLine = false;
        }

        return yPos;
    }

    public boolean isSlanted(Line line) {

        if(line == null) {
            return false;
        }

        if(line.getStartY() == line.getEndY()) {
            return false;
        } else {
            return true;
        }
    }
}









