package hotlinecesena.view.camera;

import java.util.Objects;

import hotlinecesena.model.camera.Camera;
import hotlinecesena.model.entities.Entity;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;

/**
 * Simple camera controller for JavaFX.
 *
 */
public class CameraControllerImpl implements CameraController {

    private static final float VIEW_SCALE = 2.0f;
    private static final float CENTERING_X = 2.5f;
    private static final float CENTERING_Y = 2.8f;
    private final Camera camera;
    private Pane pane;
    private final Translate paneTranslate = new Translate();

    public CameraControllerImpl(final Camera camera, final Pane pane) {
        this.camera = Objects.requireNonNull(camera);
        this.setPane(pane);
    }

    @Override
    public void setPane(final Pane pane) throws NullPointerException {
        this.pane = Objects.requireNonNull(pane);
        pane.getTransforms().add(paneTranslate);
    }

    @Override
    public void removePane() {
        pane.getTransforms().remove(paneTranslate);
    }

    @Override
    public void setEntity(final Entity entity) throws NullPointerException {
        camera.attachTo(Objects.requireNonNull(entity));
    }

    @Override
    public void update(final double deltaTime) {
        camera.update(deltaTime); //TODO Should not be updated from the View. Perhaps from DAL?
        final double transX = camera.getCameraPosition().getX()*VIEW_SCALE - pane.getScene().getWidth()/CENTERING_X;
        final double transY = camera.getCameraPosition().getY()*VIEW_SCALE - pane.getScene().getHeight()/CENTERING_Y;
        paneTranslate.setX(-transX);
        paneTranslate.setY(-transY);
    }
}
