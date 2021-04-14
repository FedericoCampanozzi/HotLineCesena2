package hotlinecesena;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import hotlinecesena.model.entities.actors.AbstractActor;
import hotlinecesena.model.entities.actors.Actor;
import hotlinecesena.model.entities.actors.ActorStatus;
import hotlinecesena.model.entities.actors.DirectionList;
import hotlinecesena.model.entities.items.CollectibleType;
import hotlinecesena.model.entities.items.Weapon;
import hotlinecesena.model.entities.items.WeaponImpl;
import hotlinecesena.model.entities.items.WeaponType;
import hotlinecesena.model.inventory.Inventory;
import hotlinecesena.model.inventory.NaiveInventoryImpl;
import javafx.geometry.Point2D;

/**
 * Runs a series of tests on methods common to all Actors.
 */
@TestInstance(Lifecycle.PER_METHOD)
class ActorModelTest {

    private static final double SPEED = 500;
    private static final int ANGLE = 270;
    private static final double WIDTH = 100;
    private static final double HEIGHT = 300;
    private static final double MAX_HP = 100;
    private Actor actor;

    private void setup() {
        final Inventory inv = new NaiveInventoryImpl(
                new WeaponImpl(WeaponType.PISTOL),
                Map.of(CollectibleType.PISTOL_AMMO, 30));
        actor = new TestActor(Point2D.ZERO, ANGLE, WIDTH, HEIGHT, SPEED, MAX_HP, inv);
    }

    @BeforeEach
    void reset() {
        this.setup();
    }

    @Test
    void actorMove() {
        actor.move(DirectionList.NORTH.get());
        assertThat(actor.getPosition(), equalTo(DirectionList.NORTH.get().multiply(SPEED)));
    }

    @Test
    void actorRotate() {
        actor.setAngle(90.0);
        assertThat(actor.getAngle(), is(90.0));
    }

    @Test
    void actorAttack() throws InterruptedException {
        assertThat(actor.getInventory().getWeapon(), not(Optional.empty()));
        final Weapon w = actor.getInventory().getWeapon().get();
        assertThat(actor.getInventory().getQuantityOf(w.getCompatibleAmmunition()), not(0));
        assertFalse(actor.getInventory().isReloading());
        Thread.sleep((long) w.getRateOfFire());
        actor.attack();
        assertEquals(ActorStatus.ATTACKING, actor.getActorStatus());
    }

    @Test
    void actorReload() throws InterruptedException {
        assertThat(actor.getInventory().getWeapon(), not(Optional.empty()));
        final Weapon w = actor.getInventory().getWeapon().get();
        final double reloadTime = w.getReloadTime();
        assertThat(actor.getInventory().getQuantityOf(w.getCompatibleAmmunition()), not(0));
        assertFalse(actor.getInventory().isReloading());
        Thread.sleep((long) actor.getInventory().getWeapon().get().getReloadTime());
        actor.attack();
        actor.reload();
        actor.getInventory().update(reloadTime / 2.0);
        assertTrue(actor.getInventory().isReloading());
        actor.getInventory().update(reloadTime);
        assertFalse(actor.getInventory().isReloading());
    }

    @Test
    void actorCannotInitiateReloadingWhileAlreadyReloading() throws InterruptedException {
        final double reloadTime = actor.getInventory().getWeapon().get().getReloadTime();
        Thread.sleep((long) actor.getInventory().getWeapon().get().getRateOfFire());
        actor.attack();
        actor.reload();
        actor.getInventory().update(reloadTime / 2.0);
        assertTrue(actor.getInventory().isReloading()); //Still reloading
        actor.reload();
        actor.getInventory().update(reloadTime / 2.0);
        assertFalse(actor.getInventory().isReloading());
    }

    @Test
    void actorTakeDamage() {
        final double damage = 50.0;
        actor.takeDamage(damage);
        assertThat(actor.getCurrentHealth(), is(MAX_HP - damage));
        final double unrealDamage = 2000.0;
        actor.takeDamage(unrealDamage);
        assertThat(actor.getCurrentHealth(), is(0.0));
    }

    @Test
    void actorDoesNotMoveWhenDead() {
        actor.takeDamage(MAX_HP);
        actor.move(DirectionList.SOUTH.get());
        assertThat(actor.getPosition(), equalTo(Point2D.ZERO));
    }

    @Test
    void actorDoesNotRotateWhenDead() {
        actor.takeDamage(MAX_HP);
        actor.setAngle(0.0);
        assertThat(actor.getAngle(), is(not(0.0)));
    }

    @Test
    void actorDoesNotHealWhenDead() {
        actor.takeDamage(MAX_HP);
        final double unrealHp = 50000.0;
        actor.heal(unrealHp);
        assertThat(actor.getCurrentHealth(), is(0.0));
    }

    private final class TestActor extends AbstractActor {

        private TestActor(final Point2D position, final double angle, final double width, final double height,
                final double speed, final double maxHealth, final Inventory inventory) {
            super(position, angle, width, height, speed, maxHealth, inventory);
        }

        /*
         * Identical to PlayerImpl's implementation, minus the
         * collision checks.
         */
        @Override
        protected void executeMovement(final Point2D direction) {
            if (this.isAlive()) {
                final Point2D oldPos = this.getPosition();
                final Point2D newPos = oldPos.add(direction.multiply(this.getSpeed()));
                this.setPosition(newPos);
                this.setActorStatus(ActorStatus.MOVING);
            }
        }
    }
}
