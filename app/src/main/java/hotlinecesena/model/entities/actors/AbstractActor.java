package hotlinecesena.model.entities.actors;

import java.util.Optional;

import hotlinecesena.model.entities.AbstractMovableEntity;
import hotlinecesena.model.entities.items.Weapon;
import hotlinecesena.model.events.AttackPerformedEvent;
import hotlinecesena.model.events.DamageReceivedEvent;
import hotlinecesena.model.events.DeathEvent;
import hotlinecesena.model.inventory.Inventory;
import javafx.geometry.Point2D;

/**
 *
 * Base class to extend when creating new Actor specializations.
 *
 */
public abstract class AbstractActor extends AbstractMovableEntity implements Actor {

    private final double maxHealth;
    private double currentHealth;
    private ActorStatus status = ActorStatus.NORMAL;
    private final Inventory inventory;

    protected AbstractActor(final Point2D pos, final double angle, final double speed,
            final double maxHealth, final Inventory inv) {
        super(pos, angle, speed);
        this.maxHealth = currentHealth = maxHealth;
        inventory = inv;
    }

    /**
     * Overridden to prohibit movements when the actor is dead.
     */
    @Override
    public void move(final Point2D direction) {
        if (this.isAlive()) {
            super.move(direction);
        }
    }

    /**
     * Overridden to prohibit rotations when the actor is dead.
     */
    @Override
    public void setAngle(final double angle) {
        if (this.isAlive()) {
            super.setAngle(angle);
        }
    }

    /**
     * Can be overridden if a concrete implementation is not based on an inventory system.
     */
    @Override
    public void attack() {
        if (this.isAlive() && !inventory.isReloading()) {
            final Optional<Weapon> weapon = inventory.getWeapon();
            if (weapon.isPresent() && weapon.get().getCurrentAmmo() > 0) {
                final Weapon w = weapon.get();
                w.usage().get().accept(this);
                this.publish(new AttackPerformedEvent(this, w));
            }
        }
    }

    /**
     * Can be overridden if a concrete implementation is not based on an inventory system.
     */
    @Override
    public void reload() {
        if (this.isAlive()) {
            inventory.reloadWeapon();
        }
    }

    @Override
    public final void takeDamage(final double damage) {
        if (this.isAlive()) {
            currentHealth = (currentHealth > damage) ? (currentHealth - damage) : 0;
            this.publish(new DamageReceivedEvent(this, damage));
        }
        if (!this.isAlive()) {
            status = ActorStatus.DEAD; // TODO Discard statuses in favor of events?
            this.publish(new DeathEvent(this));
        }
    }

    @Override
    public final void heal(final double hp) {
        if (this.isAlive()) {
            currentHealth = (currentHealth + hp < maxHealth) ? (currentHealth + hp) : maxHealth;
        }
    }

    @Override
    public final double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public final double getCurrentHealth() {
        return currentHealth;
    }

    protected final boolean isAlive() {
        return currentHealth > 0;
    }

    @Override
    public final Inventory getInventory() {
        return inventory;
    }

    @Override
    public final ActorStatus getActorStatus() {
        return status;
    }

    @Override
    public final void setActorStatus(final ActorStatus s) {
        status = s;
    }
}
