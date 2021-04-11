package hotlinecesena.controller.entities.enemy;

import java.util.function.Consumer;

import com.google.common.eventbus.Subscribe;

import hotlinecesena.controller.Updatable;
import hotlinecesena.model.entities.actors.ActorStatus;
import hotlinecesena.model.entities.actors.enemy.Enemy;
import hotlinecesena.model.entities.actors.player.Player;
import hotlinecesena.model.events.DeathEvent;
import hotlinecesena.model.events.MovementEvent;
import hotlinecesena.model.events.RotationEvent;
import hotlinecesena.model.events.Subscriber;
import hotlinecesena.view.entities.Sprite;
import hotlinecesena.view.loader.ImageType;
import hotlinecesena.view.loader.ProxyImage;
import hotlinecesena.view.loader.SceneType;

/**
 * Controls the actions that each enemy
 * will perform during the game loop and
 * updates their respective image.
 * @see Enemy
 */
public final class EnemyController implements Updatable, Subscriber {

    private static final int UPDATED_INTERVAL = 350;
    private static final int UPDATE_AFTER_DEATH = 5000;
    private static final int WAIT_TO_START = 5000;

    private final ProxyImage loader;
    private final Enemy enemy;
    private final Sprite sprite;
    private final Player player;
    private long lastTime;
    private long timeAfterDeath;
    private long timeToStart;

    /**
     * Class constructor.
     * @param enemy the animated enemy
     * @param sprite the collection of all the enemies images
     * @param player the target of our enemies
     * @see Player
     * @see Sprite
     */
    public EnemyController(final Enemy enemy, final Sprite sprite,
            final Player player) {

        this.loader = new ProxyImage();
        this.enemy = enemy;
        this.enemy.register(this);
        this.sprite = sprite;
        this.player = player;
        this.lastTime = System.currentTimeMillis();
        this.timeToStart = System.currentTimeMillis();
        this.sprite.updatePosition(this.enemy.getPosition());
    }

    @Override
    public Consumer<Double> getUpdateMethod() {
        return deltaTime -> {
            final long current = System.currentTimeMillis();
                if (!this.enemy.getActorStatus().equals(ActorStatus.DEAD)
                        && !this.player.getActorStatus().equals(ActorStatus.DEAD)
                        && current - lastTime > UPDATED_INTERVAL
                        && current - timeToStart > WAIT_TO_START) {

                    this.timeAfterDeath = current;
                    this.enemy.setIsInPursuit(true);
                    this.enemy.update(deltaTime);

                    if (this.enemy.getAI().isShooting(this.player.getPosition())) {
                        this.enemy.attack();
                    } else {
                        this.enemy.move(this.enemy.getAI().getNextMove(
                                this.player.getPosition(),
                                this.enemy.isChasingTarget(),
                                this.enemy.getWalkable()));
                    }

                    this.enemy.setIsInPursuit(this.enemy.getAI().isInPursuit(this.player.getPosition(), this.player.getNoiseRadius()));
                    this.enemy.setAngle(this.enemy.getAI().getRotation(this.player.getPosition(), this.enemy.isChasingTarget()));
                    this.lastTime = System.currentTimeMillis();
                }

                if (this.enemy.getActorStatus().equals(ActorStatus.DEAD) && current - timeAfterDeath > UPDATE_AFTER_DEATH) {
                    this.sprite.updateImage(this.loader.getImage(SceneType.GAME, ImageType.BLANK));
                }
        };
    }

    /**
     * Event triggered every time an enemy moves
     * and updates its image.
     * @param e the entity that produce the event
     */
    @Subscribe
    private void updateSpriteOnMoveEvent(final MovementEvent<Enemy> e) {
        this.sprite.updatePosition(e.getPosition());
    }

    /**
     * Event triggered every time an enemy rotates
     * and updates its image.
     * @param e the entity that produce the event
     */
    @Subscribe
    private void updateSpriteOnRotationEvent(final RotationEvent<Enemy> e) {
        this.sprite.updateRotation(e.getNewAngle());
    }

    /**
     * Event triggered once an enemy dies and
     * updates its image.
     * @param e the entity that produce the event
     */
    @Subscribe
    private void onDeathEvent(final DeathEvent<Enemy> e) {
        this.sprite.updateImage(this.loader.getImage(SceneType.GAME, ImageType.ENEMY_DEAD));
        e.getSource().unregister(this);
        this.timeAfterDeath = System.currentTimeMillis();
    }
}
