package hotlinecesena.model.entities.actors;

import java.util.Optional;

import hotlinecesena.model.entities.AbstractMovableEntity;
import hotlinecesena.model.entities.items.Weapon;
import hotlinecesena.model.inventory.Inventory;
import javafx.geometry.Point2D;

public abstract class AbstractActor extends AbstractMovableEntity implements Actor {

    private final double maxHealth;
    private double currentHealth;
    private ActorState state = ActorStateList.IDLE;
    private final Inventory inventory;
    
    protected AbstractActor(final Point2D pos, final double angle, final double speed,
            final double maxHealth, final Inventory inv) {
        super(pos, angle, speed);
        this.maxHealth = this.currentHealth = maxHealth;
        this.inventory = inv;
    }
    
    @Override
    public void attack() {
        final Optional<Weapon> weapon = this.inventory.getEquippedWeapon();
        if (weapon.isPresent()) {
            weapon.get().use(Optional.empty());
        }
    }
    
    @Override
    public void reload() {
        this.inventory.reloadWeapon();
    }


    @Override
    public void takeDamage(double damage) {
        if (this.currentHealth > 0) {
            this.currentHealth = this.currentHealth > damage ? this.currentHealth - damage : 0;
        }
    }
    
    @Override
    public double getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public double getCurrentHealth() {
        return this.currentHealth;
    }
    
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public ActorState getState() {
        return this.state;
    }
    
    @Override
    public void setState(ActorState s) {
        this.state = s;
    }

}
