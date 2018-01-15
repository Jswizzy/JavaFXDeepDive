package sample;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class OnTheSceneMain extends Application {
    DoubleProperty fillVals = new SimpleDoubleProperty(255.0);
    Scene sceneRef;
    ObservableList cursors = FXCollections.observableArrayList(
            Cursor.DEFAULT,
            Cursor.CROSSHAIR,
            Cursor.WAIT,
            Cursor.TEXT,
            Cursor.HAND,
            Cursor.MOVE,
            Cursor.N_RESIZE,
            Cursor.NE_RESIZE,
            Cursor.E_RESIZE,
            Cursor.SE_RESIZE,
            Cursor.S_RESIZE,
            Cursor.SW_RESIZE,
            Cursor.W_RESIZE,
            Cursor.NW_RESIZE,
            Cursor.NONE
    );
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage stage) {
        Slider sliderRef;
        ChoiceBox choiceBoxRef;
        Text textSceneX;
        Text textSceneY;
        Text textSceneW;
        Text textSceneH;
        Label labelStageX;
        Label labelStageY;
        Label labelStageW;
        Label labelStageH;
        final ToggleGroup toggleGrp = new ToggleGroup();
        sliderRef = new Slider(0, 255, 255);
        sliderRef.setOrientation(Orientation.VERTICAL);
        choiceBoxRef = new ChoiceBox(cursors);
        HBox hbox = new HBox(sliderRef, choiceBoxRef);
        hbox.setSpacing(10);
        textSceneX = new Text();
        textSceneX.getStyleClass().add("emphasized-text");
        textSceneY = new Text();
        textSceneY.getStyleClass().add("emphasized-text");
        textSceneW = new Text();
        textSceneW.getStyleClass().add("emphasized-text");
        textSceneH = new Text();
        textSceneH.getStyleClass().add("emphasized-text");
        textSceneH.setId("sceneHeightText");
        Hyperlink hyperlink = new Hyperlink("lookup");
        hyperlink.setOnAction((javafx.event.ActionEvent e) -> {
            System.out.println("sceneRef:" + sceneRef);
            Text textRef = (Text) sceneRef.lookup("#sceneHeightText");
            System.out.println(textRef.getText());
        });
        RadioButton radio1 = new RadioButton("onTheScene.css");
        radio1.setSelected(true);
        radio1.setToggleGroup(toggleGrp);
        RadioButton radio2 = new RadioButton("changeOfScene.css");
        radio2.setToggleGroup(toggleGrp);
        labelStageX = new Label();
        labelStageX.setId("stageX");
        labelStageY = new Label();
        labelStageY.setId("stageY");
        labelStageW = new Label();
        labelStageH = new Label();
        FlowPane sceneRoot = new FlowPane(Orientation.VERTICAL, 20, 10, hbox,
                textSceneX, textSceneY, textSceneW, textSceneH, hyperlink,
                radio1, radio2,
                labelStageX, labelStageY,
                labelStageW,
                labelStageH);
        sceneRoot.setPadding(new Insets(0, 20, 40, 0));
        sceneRoot.setColumnHalignment(HPos.LEFT);
        sceneRoot.setLayoutX(20);
        sceneRoot.setLayoutY(40);
        sceneRef = new Scene(sceneRoot, 600, 250);
        sceneRef.getStylesheets().add("onTheScene.css");
        stage.setScene(sceneRef);
        choiceBoxRef.getSelectionModel().selectFirst();
        // Setup various property binding
        textSceneX.textProperty().bind(new SimpleStringProperty("Scene x: ")
                .concat(sceneRef.xProperty().asString()));
        textSceneY.textProperty().bind(new SimpleStringProperty("Scene y: ")
                .concat(sceneRef.yProperty().asString()));
        textSceneW.textProperty().bind(new SimpleStringProperty("Scene width: ")
                .concat(sceneRef.widthProperty().asString()));
        textSceneH.textProperty().bind(new SimpleStringProperty("Scene height: ")
                .concat(sceneRef.heightProperty().asString()));
        labelStageX.textProperty().bind(new SimpleStringProperty("Stage x: ")
                .concat(sceneRef.getWindow().xProperty().asString()));
        labelStageY.textProperty().bind(new SimpleStringProperty("Stage y: ")
                .concat(sceneRef.getWindow().yProperty().asString()));
        labelStageW.textProperty().bind(new SimpleStringProperty("Stage width: ")
                .concat(sceneRef.getWindow().widthProperty().asString()));
        labelStageH.textProperty().bind(new SimpleStringProperty("Stage height: ")
                .concat(sceneRef.getWindow().heightProperty().asString()));
        sceneRef.cursorProperty().bind(choiceBoxRef.getSelectionModel()
                .selectedItemProperty());
        fillVals.bind(sliderRef.valueProperty());
        // When fillVals changes, use that value as the RGB to fill the scene
        fillVals.addListener((ov, oldValue, newValue) -> {
            Double fillValue = fillVals.getValue() / 256.0;
            sceneRef.setFill(new Color(fillValue, fillValue, fillValue, 1.0));
        });
        // When the selected radio button changes, set the appropriate style sheet
        toggleGrp.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            String radioButtonText = ((RadioButton) toggleGrp.getSelectedToggle())
                    .getText();
            sceneRef.getStylesheets().clear();
            sceneRef.getStylesheets().addAll(radioButtonText);
        });
        stage.setTitle("On the Scene");
        stage.show();
        // Define an unmanaged node that will display Text
        Text addedTextRef = new Text(0, -30, "");
        addedTextRef.setTextOrigin(VPos.TOP);
        addedTextRef.setFill(Color.BLUE);
        addedTextRef.setFont(Font.font("Sans Serif", FontWeight.BOLD, 16));
        addedTextRef.setManaged(false);
        // Bind the text of the added Text node to the fill property of the Scene
        addedTextRef.textProperty().bind(new SimpleStringProperty("Scene fill: ").
                concat(sceneRef.fillProperty()));
        // Add to the Text node to the FlowPane
        ((FlowPane) sceneRef.getRoot()).getChildren().add(addedTextRef);
    }
}
