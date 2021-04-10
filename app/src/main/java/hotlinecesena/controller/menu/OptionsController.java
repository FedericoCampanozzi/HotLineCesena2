package hotlinecesena.controller.menu;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import hotlinecesena.model.dataccesslayer.JSONDataAccessLayer;
import hotlinecesena.utilities.SceneSwapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.Pair;

public class OptionsController implements Initializable {
	
	@FXML
	private Slider volSlider;
	@FXML
	private Label percVolLabel;
	@FXML
	private RadioButton musicRadioButton;
	@FXML
	private RadioButton soundsRadioButton;
	@FXML
	private RadioButton fullScreenRadioButton;
	@FXML
	private ComboBox<Label> resolutionComboBox;
	@FXML
	private Button upButton;
	@FXML
	private Button leftButton;
	@FXML
	private Button downButton;
	@FXML
	private Button rightButton;
	@FXML
	private Button backButton;
	
	private SceneSwapper sceneSwapper = new SceneSwapper();
	private Map<Pair<Integer, Integer>, Label> resolutions = new LinkedHashMap<>();
	private Optional<Stage> worldStage;
	private Stage optionsStage;
	
	public OptionsController(Stage optionsStage, Optional<Stage> worldStage) {
		this.optionsStage = optionsStage;
		this.worldStage = worldStage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Initialize audio settings
		int startVolumeValue = JSONDataAccessLayer.getInstance().getSettings().getVolume();
		percVolLabel.setText(Integer.toString(startVolumeValue) + "%");
		volSlider.setValue(startVolumeValue);
		
		// Initialize graphic settings
		resolutions.put(new Pair<Integer, Integer>(600,  400), null);
		resolutions.put(new Pair<Integer, Integer>(800,  600), null);
		resolutions.put(new Pair<Integer, Integer>(1024,  768), null);
		resolutions.put(new Pair<Integer, Integer>(1280,  720), null);
		resolutions.put(new Pair<Integer, Integer>(1920,  1080), null);
		for (var res : resolutions.entrySet()) {
			Label label = new Label(res.getKey().getKey() + "x" + res.getKey().getValue());
			res.setValue(label);
		}
		resolutionComboBox.getItems().addAll(resolutions.values());

		// Initialize controls settings
		
	}	
	
	public void backClick(final ActionEvent event) throws IOException {
		if (worldStage.isPresent()) {
			sceneSwapper.swapScene(new PauseController(optionsStage, worldStage), "PauseView.fxml", optionsStage);
		}
		else {
			sceneSwapper.swapScene(new StartMenuController(optionsStage), "StartMenuView.fxml", optionsStage);
		}
	}
	
	public void volSliderValueChanged() throws IOException {
		updateVolume((int) volSlider.getValue());
	}
	
	private void updateVolume(int value) {
		percVolLabel.setText(Integer.toString(value) + "%");
		JSONDataAccessLayer.getInstance().getSettings().setVolume(value);
	}
	
	public void fullScreenRadioButtonChangedState() {
		if (fullScreenRadioButton.isSelected()) {
			JSONDataAccessLayer.getInstance().getSettings().setFullScreen(true);
		}
		else {
			JSONDataAccessLayer.getInstance().getSettings().setFullScreen(false);
		}
	}
	
	public void resolutionChoosed() {
		Pair<Integer, Integer> res = new Pair<Integer, Integer>(null, null);
		res = resolutions.keySet()
					.stream()
					.filter(key -> resolutionComboBox.getValue().equals(resolutions.get(key)))
					.findFirst().get();
		JSONDataAccessLayer.getInstance().getSettings().setMonitorX(res.getKey());
		JSONDataAccessLayer.getInstance().getSettings().setMonitorY(res.getValue());
	}
}