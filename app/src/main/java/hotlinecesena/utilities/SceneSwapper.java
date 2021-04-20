package hotlinecesena.utilities;

import java.io.IOException;
import hotlinecesena.model.dataccesslayer.JSONDataAccessLayer;
import hotlinecesena.view.loader.ImageType;
import hotlinecesena.view.loader.ProxyImage;
import hotlinecesena.view.loader.SceneType;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneSwapper {
	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;
    private static final String PATH = "GUI";
    private static final String SEP = "/";
    private final ProxyImage proxyImage = new ProxyImage();

    public void swapScene(final Initializable controller, final String fxml, final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(PATH + SEP + fxml));
        loader.setController(controller);
        Pane pane;
        updateDim(stage);
        pane = loader.load();
        stage.setScene(new Scene(pane));
    }
    
    public void setUpStage(Stage stage) {
    	updateDim(stage);
        stage.setResizable(true);
        stage.setTitle("HotLine Cesena");
        stage.centerOnScreen();
        stage.getIcons().add(proxyImage.getImage(SceneType.MENU, ImageType.ICON));
        
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
        	JSONDataAccessLayer.getInstance().getSettings().setDefaultHeight((int) stage.getHeight());
        	JSONDataAccessLayer.getInstance().getSettings().setDefaultWidth((int) stage.getWidth());
        };
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }
    
    private void updateDim(Stage stage) {
    	int width = JSONDataAccessLayer.getInstance().getSettings().getDefaultWidth();
    	int height = JSONDataAccessLayer.getInstance().getSettings().getDefaultHeight();
    	stage.setWidth(width);
        stage.setHeight(height);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
    }
}