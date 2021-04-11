package hotlinecesena.controller.entities.player;

import java.util.Collection;
import java.util.function.Consumer;

import com.google.common.eventbus.Subscribe;

import hotlinecesena.controller.input.InputInterpreter;
import hotlinecesena.model.entities.actors.player.Command;
import hotlinecesena.model.entities.actors.player.Player;
import hotlinecesena.model.events.DeathEvent;
import hotlinecesena.model.events.MovementEvent;
import hotlinecesena.model.events.RotationEvent;
import hotlinecesena.model.events.Subscriber;
import hotlinecesena.model.events.WeaponPickUpEvent;
import hotlinecesena.view.camera.CameraView;
import hotlinecesena.view.entities.Sprite;
import hotlinecesena.view.input.InputListener;
import hotlinecesena.view.loader.ImageLoader;
import hotlinecesena.view.loader.ImageType;
import hotlinecesena.view.loader.ProxyImage;
import hotlinecesena.view.loader.SceneType;
import javafx.scene.image.Image;

/**
 *
 * {@link PlayerController} implementation.
 *
 */
public final class PlayerControllerFX implements PlayerController, Subscriber {

    private final Player player;
    //TODO Listener to be held by the GameController/WorldView
    private final InputListener listener;
    private final InputInterpreter interpreter;
    private final Sprite sprite;
    //TODO Camera to be held by the GameController/WorldView
    private final CameraView camera;
    private final ImageLoader loader = new ProxyImage();

    public PlayerControllerFX(final Player player, final Sprite sprite, final InputInterpreter interpreter,
            final CameraView camera, final InputListener listener) {
        this.player = player;
        this.sprite = sprite;
        this.listener = listener;
        this.interpreter = interpreter;
        this.camera = camera;

        this.setup();
    }

    private void setup() {
        player.register(this);
        sprite.updatePosition(player.getPosition());
        sprite.updateRotation(player.getAngle());
    }

    @Override
    public Consumer<Double> getUpdateMethod() {
        return deltaTime -> {
            player.update(deltaTime);
            final Collection<Command> commands = interpreter.interpret(
                    listener.deliverInputs(), sprite.getPositionRelativeToScene(), deltaTime
                    );
            if (!commands.isEmpty()) {
                commands.forEach(c -> c.execute(player));
            }
            //TODO To be updated from the GameLoop
            camera.update(sprite.getPositionRelativeToParent(), deltaTime);
        };
    }

    @Override
    public CameraView getCamera() {
        return camera;
    }

    @Subscribe
    private void handleMovementEvent(final MovementEvent<Player> e) {
        sprite.updatePosition(e.getSource().getPosition());
    }

    @Subscribe
    private void handleRotationEvent(final RotationEvent<Player> e) {
        sprite.updateRotation(e.getSource().getAngle());
    }

    @Subscribe
    private void handleDeathEvent(final DeathEvent<Player> e) {
        sprite.updateImage(loader.getImage(SceneType.GAME, ImageType.PLAYER_DEAD));
    }

    @Subscribe
    private void handleWeaponPickUpEvent(final WeaponPickUpEvent<Player> e) {
        Image image = null;
        switch (e.getItemType()) {
        case PISTOL:
            image = loader.getImage(SceneType.GAME, ImageType.PLAYER_PISTOL);
            break;
        case SHOTGUN:
            image = loader.getImage(SceneType.GAME, ImageType.PLAYER_SHOTGUN);
            break;
        case RIFLE:
            image = loader.getImage(SceneType.GAME, ImageType.PLAYER_RIFLE);
            break;
        default:
            image = loader.getImage(SceneType.GAME, ImageType.PLAYER);
            break;
        }
        sprite.updateImage(image);
    }
}
