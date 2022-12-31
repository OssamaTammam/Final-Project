package Game.Controller.Utils;

import Game.Controller.Factories.ShapeFactory;
import Game.Model.Shapes.ImageObject;
import Game.Model.Shapes.Shape;
import Game.Model.Shapes.ShapeState;
import eg.edu.alexu.csd.oop.game.GameObject;

import java.awt.font.GlyphMetrics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ObjectPool {
    private long deadTime;
    private int width;
    private int height;
    private double averageVelocity;
    private double distanceBetweenRods;
    private int diffShapes;
    private int shelfLevel;
    private Random rand = new Random();
    private HashMap<GameObject, Long> inUse;
    private HashMap<GameObject, Long> available;

    public ObjectPool(long deadTime, int screenWidth, int screenHeight, double averageVelocity, int diffShapes, int shelfLevel, double distanceBetweenRods) {
        this.deadTime = deadTime;
        inUse = new HashMap<>();
        available = new HashMap<>();
        this.width = screenWidth;
        this.height = screenHeight;
        this.averageVelocity = averageVelocity;
        this.diffShapes = diffShapes;
        this.shelfLevel = shelfLevel;
        this.distanceBetweenRods = distanceBetweenRods;
    }

//    public synchronized Shape getShape() {
//        long now = System.currentTimeMillis();
//        if (!available.isEmpty()) {
//            for (Map.Entry<Shape, Long> entry : available.entrySet()) {
//                long test = now - entry.getValue();
//                if (now - entry.getValue() > deadTime) {
//                    available.remove(entry);
//                } else {
//                    boolean state = rand.nextDouble() > 0.5;
//                    Shape shape = entry.getKey();
//                    shape.setX((state ? 0 : width));
//                    shape.setY((int) Math.round(distanceBetweenRods * height) * (rand.nextInt(shelfLevel) + 1));
//                    shape.setState(new ShapeState(getRandomDouble(state ? 3 : Math.min(-averageVelocity, -3), state ? Math.max(averageVelocity, 3) : -3), 0, 0, 0.0000000001, 0));
//                    shape.setY(shape.getY() - shape.getHeight());
//                    shape.setRandomImage();
//                    available.remove(shape);
//                    inUse.put(shape, now);
//                    return shape;
//                }
//            }
//        }
//        boolean state = rand.nextDouble() > 0.5;
//        Shape sh = ShapeFactory.getInstance().getRandomShape(diffShapes,
//                (state ? 0 : width),
//                (int) Math.round(distanceBetweenRods * height) * (rand.nextInt(shelfLevel) + 1),
//                width, height,
//                new ShapeState(getRandomDouble(state ? 3 : Math.min(-averageVelocity, -3), state ? Math.max(averageVelocity, 3) : -3), 0, 0, 0.0000000001, 0));
//        sh.setY(sh.getY() - sh.getHeight());
//        inUse.put(sh, now);
//        return sh;
//    }

    public synchronized GameObject getPlate() {
        long now = System.currentTimeMillis();
        if (!available.isEmpty()) {
            for (Map.Entry<GameObject, Long> entry : available.entrySet()) {
                //long test = now - entry.getValue();
                if (now - entry.getValue() > deadTime) {
                    available.remove(entry);
                } else {
                    boolean state = rand.nextDouble() > 0.5;
                    GameObject shape = entry.getKey();
                    ((ImageObject)shape).setState(new ShapeState(getRandomDouble(state ? 3 : Math.min(-averageVelocity, -3), state ? Math.max(averageVelocity, 3) : -3), 0, 0, 0.0000000001, 0));
                    shape.setX((state ? 0 : width));
                    shape.setY((int) Math.round(distanceBetweenRods * height) * (rand.nextInt(shelfLevel) + 1));
                    shape.setY(shape.getY() - shape.getHeight());
                    available.remove(shape);
                    inUse.put(shape, now);
                    return shape;
                }
            }
        }
        boolean state = rand.nextDouble() > 0.5;
        GameObject sh = ShapeFactory.getInstance().getRandomImage(
                (state ? 0 : width),
                (int) Math.round(distanceBetweenRods * height) * (rand.nextInt(shelfLevel) + 1),
                width, height);
        ((ImageObject)sh).setState(new ShapeState(getRandomDouble(state ? 3 : Math.min(-averageVelocity, -3), state ? Math.max(averageVelocity, 3) : -3), 0, 0, 0.0000000001, 0));
        sh.setY(sh.getY() - sh.getHeight());
        inUse.put(sh, now);
        return sh;
    }

    private double getRandomDouble(double min, double max) {
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }

    public void releaseShape(GameObject sh) {
        available.put(sh, System.currentTimeMillis());
        inUse.remove(sh);
    }
}
