package hotlinecesena.model.entities;

import hotlinecesena.util.Point2D;

public interface GameEntity {

    Point2D<Double, Double> getPosition();

    State getState();

    void setState(State s);
}
