package org.uvsq.fr.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.uvsq.fr.simulation.*;
import org.uvsq.fr.simulation.util.*;
import org.uvsq.fr.xml.XMLParserEP;
import org.uvsq.fr.xml.XMLParserGOL;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class Window implements Initializable {

  private static int GRID_WIDTH = 50; // 50 avant
  private static int GRID_HEIGHT = 50; // 50 avant
  private static int RECTANGLE_SIZE = 18; // 13 avant
  private static double RECTANGLE_STROKE_WIDTH = 1.5; // 0.1 avant
  private static final Duration FRAME_DURATION = Duration.millis(1000 / 5); // 15 FPS

  private final Timeline timeline; // animation controller

  private Grid grid; // In-memory population representation
  private Generation selectedAutomaton; // Selected automaton
  private List<Generation> automatons = new ArrayList<>();
  private GameOfLifeAutomaton gol;
  private EpidemicAutomaton epi;
  private HashMap<Integer, HashMap<String, Integer>> stats = new HashMap<Integer, HashMap<String, Integer>>();
  private boolean modeFile;
  private boolean gameoL;
  private boolean epidemic;

  @FXML
  private Button loadButton;
  @FXML
  private Button playButton;
  @FXML
  private Button pauseButton;
  @FXML
  private Button statButton;
  @FXML
  private Button onStepButton;
  @FXML
  private ComboBox<String> automatonSelector;
  @FXML
  private ChoiceBox sizeChoice;
  @FXML
  private TilePane tilePane; // main grid display pane

  public Window() {
    this.timeline = new Timeline(new KeyFrame(FRAME_DURATION, e -> selectedAutomaton.nextStep()));

    this.timeline.setCycleCount(Animation.INDEFINITE);

    this.grid = new Grid(GRID_HEIGHT, GRID_WIDTH);
    gol = new GameOfLifeAutomaton(this.grid);
    epi = new EpidemicAutomaton(this.grid);
    automatons.add(gol);
    automatons.add(epi);
  }

  /**
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    resizeTilePane(tilePane);
    // populate dropdown with selection of automatons
    automatons.forEach(automaton -> automatonSelector.getItems().add(automaton.name()));
    selectedAutomaton = automatons.get(0); // start with default
    automatonSelector.getSelectionModel().select(0);

    sizeChoice.getItems().addAll("10*10", "20*20", "30*30", "40*40", "50*50");
    sizeChoice.setValue("50*50");

    // fill tile pane with array of cell rectangles
    for (int i = 0; i < GRID_HEIGHT; i++) {
      for (int k = 0; k < GRID_WIDTH; k++) {
        final Rectangle rectangle = initRectangle(grid.getCellAt(i, k));
        tilePane.getChildren().add(rectangle);
      }
    }

    updateButtons();
  }

  @FXML
  private void onSizeUpdate() {
    tilePane.getChildren().clear(); // delete every rectangle of Tilepane graphic grid
    setSizeGrid(sizeChoice);
    resizeTilePane(tilePane);

    for (int i = 0; i < GRID_HEIGHT; i++) {
      for (int k = 0; k < GRID_WIDTH; k++) {
        final Rectangle rectangle = initRectangle(grid.getCellAt(i, k));
        tilePane.getChildren().add(rectangle);
      }
    }
    updateButtons();
  }

  @FXML
  private void onReset() {
    timeline.stop();
    if (selectedAutomaton.name().equals("Epidemic")) {
      epi.resetStatistics();
    } else {
      gol.resetStatistics();
    }
    if (modeFile) {
      if (epidemic) {
        epi.resetStatistics();
      } else if (gameoL) {
        gol.resetStatistics();
      }
    }
    stats.clear();
    grid.reset();
    onStepButton.setDisable(true);
    updateButtons();
  }

  @FXML
  private void onLoad() {
    selectedAutomaton.newLoad();
    onStepButton.setDisable(false);

  }

  @FXML
  private void onPlay() {
    timeline.play();
    if (selectedAutomaton.name().equals("Epidemic")) {
      stats.put(epi.getNumGeneration(), epi.statGeneration);
    } else {
      stats.put(gol.getNumGeneration(), gol.statGeneration);
    }
    if (modeFile) {
      if (epidemic) {
        stats.put(epi.getNumGeneration(), epi.statGeneration);
      } else if (gameoL) {
        stats.put(gol.getNumGeneration(), gol.statGeneration);
      }
    }
    updateButtons();
  }

  @FXML
  private void onPause() {
    timeline.stop();
    updateButtons();
  }

  @FXML
  private void onStep() {
    timeline.stop();
    selectedAutomaton.nextStep();

    if (selectedAutomaton.name().equals("Epidemic")) {
      stats.put(epi.getNumGeneration(), epi.statGeneration);
    } else {
      stats.put(gol.getNumGeneration(), gol.statGeneration);
    }
    updateButtons();
  }

  @FXML
  private void onAutomatonUpdate() {
    selectedAutomaton = automatons.get(automatonSelector.getSelectionModel().getSelectedIndex());
  }

  public void setSizeGrid(ChoiceBox choiceBox) {
    String choiceUser = choiceBox.getValue().toString();

    if (choiceUser == "50*50") {
      GRID_HEIGHT = 50;
      GRID_WIDTH = 50;
      RECTANGLE_SIZE = 18;
      RECTANGLE_STROKE_WIDTH = 1.5;

    } else if (choiceUser == "40*40") {
      GRID_HEIGHT = 40;
      GRID_WIDTH = 40;
      RECTANGLE_SIZE = 22;
      RECTANGLE_STROKE_WIDTH = 0.5;

    } else if (choiceUser == "30*30") {
      GRID_HEIGHT = 30;
      GRID_WIDTH = 30;
      RECTANGLE_SIZE = 30;
      RECTANGLE_STROKE_WIDTH = 2.5;
    } else if (choiceUser == "20*20") {
      GRID_HEIGHT = 20;
      GRID_WIDTH = 20;
      RECTANGLE_SIZE = 45;
      RECTANGLE_STROKE_WIDTH = 1.0;

    } else if (choiceUser == "10*10") {
      GRID_HEIGHT = 10;
      GRID_WIDTH = 10;
      RECTANGLE_SIZE = 90;
      RECTANGLE_STROKE_WIDTH = 2.0;
    }
  }

  /**
   * Ensures tile pane can accommodate all cells on the grid
   */
  private static void resizeTilePane(TilePane tilePane) {
    tilePane.setMinHeight(GRID_HEIGHT * (tilePane.getVgap() + RECTANGLE_SIZE));
    tilePane.setMinWidth(GRID_WIDTH * (tilePane.getHgap() + RECTANGLE_SIZE));
    tilePane.setPrefColumns(GRID_WIDTH);
    tilePane.setPrefRows(GRID_HEIGHT);
  }

  /**
   * Configures rectangle display properties and attaches state listeners
   */
  private static Rectangle initRectangle(Cell cell) {
    Rectangle rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE);

    // configure display properties
    rectangle.setFill(Color.WHITE);
    rectangle.setStroke(Color.SILVER);
    rectangle.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
    rectangle.setStrokeType(StrokeType.INSIDE);

    // update rectangle state on cell state update
    cell.attachListener(new CellUpdate(rectangle));
    attachMouseEvents(rectangle, cell);

    return rectangle;
  }

  /**
   * Updates cell state on mouse click
   */
  private static void attachMouseEvents(Rectangle rectangle, Cell cell) {
    rectangle.setOnMousePressed(event -> {
      cell.toggle();
      /* need to get the right color for each Simulation */
      /* Methods does not exist yet */

    });
  }

  /**
   * Enables / disables buttons depending on current state of the machine
   */
  private void updateButtons() {
    if (timeline.getStatus() == Animation.Status.RUNNING) {
      playButton.setDisable(true);
      pauseButton.setDisable(false);
      loadButton.setDisable(true);
      automatonSelector.setDisable(true);
      statButton.setDisable(false);
    } else {
      playButton.setDisable(false);
      pauseButton.setDisable(true);
      loadButton.setDisable(false);
      automatonSelector.setDisable(false);
      statButton.setDisable(false);
    }
  }

  /* Open window statistics */
  private void windowStatistics() {
    NumberAxis x = new NumberAxis();
    NumberAxis y = new NumberAxis();
    Scene scene;

    if (selectedAutomaton.name().equals("Epidemic")) {
      EpidemicStatistics epidemic = new EpidemicStatistics(x, y);
      Iterator<Map.Entry<Integer, HashMap<String, Integer>>> parent = stats.entrySet().iterator();
      while (parent.hasNext()) {
        /*
         * parentPair key is the generation and parentPair value is the couple of state
         * of this generation
         */
        Map.Entry<Integer, HashMap<String, Integer>> parentPair = parent.next();
        epidemic.setAutomatonData(parentPair.getKey(), parentPair.getValue());
      }

      Group root = new Group(epidemic);
      scene = new Scene(root, 600, 600);
      scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
      Stage stage = new Stage();
      stage.setResizable(false);
      stage.setTitle("Statistiques automate");
      stage.setScene(scene);

      stage.show();
    } else if (selectedAutomaton.name().equals("Game of Life")) {
      GameOfLifeStatistics gameOfLife = new GameOfLifeStatistics(x, y);
      Iterator<Map.Entry<Integer, HashMap<String, Integer>>> parent = stats.entrySet().iterator();
      while (parent.hasNext()) {
        /*
         * parentPair key is the generation and parentPair value is the couple of state
         * of this generation
         */
        Map.Entry<Integer, HashMap<String, Integer>> parentPair = parent.next();
        gameOfLife.setAutomatonData(parentPair.getKey(), parentPair.getValue());

      }

      Group root = new Group(gameOfLife);
      scene = new Scene(root, 600, 600);
      scene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());

      Stage stage = new Stage();
      stage.setResizable(false);
      stage.setTitle("Statistiques automate");
      stage.setScene(scene);

      stage.show();
    }

  }

  @FXML
  private void onStatistics() throws IOException {
    windowStatistics();

  }

  @FXML
  public void startMaze() throws IOException {
    // Lancement du solveur de labyrinth en parallele
    Runtime.getRuntime().exec("java -jar src/main/resources/jcell-maze.jar");
  }

  /*
   * Methode afin de charger un fichier xml epidemic (epi) ou game of life (gol)
   */
  @FXML
  public void onOpen() throws URISyntaxException, ParserConfigurationException, SAXException, IOException {
    modeFile = true;
    Stage stage = new Stage();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("src/main/resources/xml"));
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
    File file = fileChooser.showOpenDialog(stage);
    String filename = file.getName();

    /* si le nom du fichier commence par epi */
    if (filename.substring(0, 3).equals("epi")) {
      XMLParserEP epidemicParser = new XMLParserEP("/xml/" + filename);
      System.out.println(epidemicParser.getGridHeight());
      System.out.println(epidemicParser.getGridWidth());
      System.out.println(epidemicParser.getProbCellInfected());
      System.out.println(epidemicParser.getProbCellHealthy());
      System.out.println(epidemicParser.getProbCellVaccinated());
      GRID_HEIGHT = epidemicParser.getGridHeight();
      GRID_WIDTH = epidemicParser.getGridWidth();
      this.grid = new Grid(GRID_HEIGHT, GRID_WIDTH);
      resizeTilePane(tilePane);
      for (int i = 0; i < GRID_HEIGHT; i++) {
        for (int k = 0; k < GRID_WIDTH; k++) {
          final Rectangle rectangle = initRectangle(grid.getCellAt(i, k));
          tilePane.getChildren().add(rectangle);
        }
      }
      updateButtons();
      epi = new EpidemicAutomaton(this.grid);

      epi.load2(epidemicParser.getProbCellInfected(), epidemicParser.getProbCellHealthy(),
          epidemicParser.getProbCellVaccinated(), GRID_WIDTH, GRID_HEIGHT);

      System.out.println("FIN PARSE EPI");
    } else if (filename.substring(0, 3).equals("gol")) {
      XMLParserGOL gameOfLifeParser = new XMLParserGOL("/xml/" + filename);
      GRID_HEIGHT = gameOfLifeParser.getGridHeight();
      GRID_WIDTH = gameOfLifeParser.getGridWidth();
      this.grid = new Grid(GRID_HEIGHT, GRID_WIDTH);
      resizeTilePane(tilePane);
      for (int i = 0; i < GRID_HEIGHT; i++) {
        for (int k = 0; k < GRID_WIDTH; k++) {
          final Rectangle rectangle = initRectangle(grid.getCellAt(i, k));
          tilePane.getChildren().add(rectangle);
        }
      }
      updateButtons();

      gol = new GameOfLifeAutomaton(this.grid);

      gol.load2(gameOfLifeParser.getProbCellAlive(), GRID_WIDTH, GRID_HEIGHT);
      resizeTilePane(tilePane);

      System.out.println("FIN PARSE GOL");
    }

  }
}
