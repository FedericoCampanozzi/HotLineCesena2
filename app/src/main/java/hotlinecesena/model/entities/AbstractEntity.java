package hotlinecesena.model.entities;

import javafx.geometry.Point2D;

/**
 * 
 * Template for generic entities.
 */
public abstract class AbstractEntity implements Entity {
    
    private Point2D position;
    private double angle;
    
    protected AbstractEntity(final Point2D pos, final double angle) {
        this.position = pos;
        this.angle = angle;
    }

    @Override
    public Point2D getPosition() {
        return this.position;
    }
    
    /**
     * 
     * @param pos
     */
    protected void setPosition(final Point2D pos) {
        this.position = pos;
    }
    
    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void setAngle(final double angle) {
        this.angle = angle;
    }

}