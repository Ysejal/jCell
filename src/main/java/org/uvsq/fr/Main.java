package org.uvsq.fr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;

public class Main extends Application {

  // Attributs
  private ScrollPane root = new ScrollPane();
  private ArrayList<Button> buttonList = new ArrayList<Button>();
  private Scene mainScene = new Scene(root, 1100, 800);
  private Button firstProfil = new Button("Einstein");
  private Button secondProfil = new Button("Monroe");
  private Button thirdProfil = new Button("Quinn");
  private Button addProfil = new Button("Add new profil");
  private TilePane tilePane = new TilePane(50, 50);
  private VBox vbox = new VBox();

  /**
   * @param stage window
   * @throws IOException
   */
  @Override
  public void start(Stage stage) throws IOException {
    stage.getIcons().add(new Image("images/logo.png"));
    stage.setResizable(false);

    ////////////////////////////////////// Configuration du layout parent et
    ////////////////////////////////////// composants//////////////////////////////////////

    // Configuration du layout root (Vbar & Hbar)
    init_Root_Layout();

    // Application du fichier CSS sur notre scene
    mainScene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

    // Création des composants (3 pré-boutton profil + le boutton de création de
    // profil)
    init_PreButton_List();
    init_Add_Profil_Button();

    // Configuration du TilePane
    init_Tile_Pane();

    // Configuration Vbox et ajout du boutton création et du tilepane en tant que
    // sous-composant
    vbox.getChildren().addAll(/* newProfil, */tilePane);

    // Ajout du layout au parent
    root.setContent(vbox);

    ////////////////////////////////////// Gestion des
    ////////////////////////////////////// evênements//////////////////////////////////////

    // Boutton d'ajout de profil préssé
    addProfil.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // Effet fenêtre principale
        BoxBlur bb = new BoxBlur();
        tilePane.setEffect(bb);

        // Création layout et scene
        VBox secondRoot = new VBox();
        secondRoot.getStyleClass().add("secondRoot");

        Scene secondaryScene = new Scene(secondRoot, 500, 300);
        secondaryScene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        // Création de la fenêtre pop-up
        Stage secondaryStage = new Stage();
        init_Create_Profil_Window(stage, secondaryStage, secondaryScene);

        // Configuration des composants

        HBox firstHbox = new HBox();
        firstHbox.getStyleClass().add("firstHBox");

        HBox secondHbox = new HBox();
        secondHbox.getStyleClass().add("secondHBox");

        // Création des composant de la fenêtre de création de profil
        Label ajoutProfil = new Label("Création d'un profil");
        ajoutProfil.getStyleClass().add("labelAjout");
        Label ajoutExplain = new Label("Créer votre propre profil pour retrouver vos simulations réalisées.");
        ajoutExplain.getStyleClass().add("labelExplain");
        Separator firstSeparator = new Separator();
        Separator secondSeparator = new Separator();
        Button continu = new Button("Continuer");
        continu.getStyleClass().add("continuButton");
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("cancelButton");

        ComboBox<ImageView> imageBox = new ComboBox(FXCollections.observableArrayList(init_ComboBox_items()));
        imageBox.getStyleClass().add("comboBox");
        imageBox.setVisibleRowCount(1);
        imageBox.setTooltip(new Tooltip("Image choices for your avatar"));

        Image choiceIMG = new Image(getClass().getResource("/images/choice/choice.png").toString());
        ImageView choiceIMGView = new ImageView(choiceIMG);

        imageBox.setValue(choiceIMGView);

        TextField nameProfil = new TextField();
        nameProfil.getStyleClass().add("nameProfilButton");
        nameProfil.setPromptText("Name");

        firstHbox.getChildren().addAll(imageBox, nameProfil);
        secondHbox.getChildren().addAll(continu, cancel);

        // Ajout des composants au layout
        secondRoot.getChildren().addAll(ajoutProfil, ajoutExplain, firstSeparator, firstHbox, secondSeparator,
                secondHbox);

        // Clic sur le boutton "continu" qui créer le nouveau profil et ferme la fenêtre
        // de création de profil
        continu.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {

            if (imageBox.getValue().getImage().getUrl().equals("/images/choice/choice.png")
                    || nameProfil.getText().isEmpty()) {

              if (secondHbox.getChildren().get(secondHbox.getChildren().size() - 1) instanceof Label)
                ;
              else {
                Label errorLabel = new Label("Veuillez remplir les champs");
                errorLabel.getStyleClass().add("errorLabel");
                secondHbox.getChildren().add(errorLabel);
              }

            }

            else {
              update_Main_Window(nameProfil, imageBox);
              buttonPressed(stage);
              close_Window(secondaryStage);
              tilePane.setEffect(null);
            }

          }
        });

        // Clic sur le boutton "cancel" qui provoque la fermeture de la fenêtre de
        // création de profil
        cancel.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent event) {

            close_Window(secondaryStage);
            tilePane.setEffect(null);

          }
        });

        secondaryStage.setOnCloseRequest(new javafx.event.EventHandler<WindowEvent>() {

          @Override
          public void handle(WindowEvent event) {

            tilePane.setEffect(null);

          }
        });

        // Afficher la nouvelle fenêtre
        display_Window(secondaryStage);
      }

    });

    // Test si un boutton de profil est préssé
    buttonPressed(stage);

    ////////////////////////////////////// Affichage de la
    ////////////////////////////////////// fenêtre//////////////////////////////////////

    // Configuration fenêtre
    init_Main_Window(stage);
    display_Window(stage);

  }

  // First Window
  public void init_Root_Layout() {

    root.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    root.getStyleClass().add("root");
  }

  public void init_PreButton_List() {

    Image firstProfilIMG = new Image(getClass().getResource("/images/256px/einstein.png").toString());
    ImageView firstProfilIMGView = new ImageView(firstProfilIMG);

    Image image = new Image(getClass().getResource("/images/256px/harley-quinn.png").toString());
    ImageView imageview = new ImageView(image);

    Image image2 = new Image(getClass().getResource("/images/256px/monroe.png").toString());
    ImageView imageview2 = new ImageView(image2);

    firstProfil.setGraphic(firstProfilIMGView);
    secondProfil.setGraphic(imageview2);
    thirdProfil.setGraphic(imageview);

    firstProfil.getStyleClass().add("firstProfilButton");
    secondProfil.getStyleClass().add("secondProfilButton");
    thirdProfil.getStyleClass().add("thirdProfilButton");

    buttonList.add(firstProfil);
    buttonList.add(secondProfil);
    buttonList.add(thirdProfil);

  }

  public void init_Add_Profil_Button() {

    Image addIMG = new Image(getClass().getResource("/images/256px/plus.png").toString());
    ImageView addIMGView = new ImageView(addIMG);

    addProfil.setGraphic(addIMGView);
    addProfil.getStyleClass().add("addProfilButton");
  }

  public void init_Tile_Pane() {

    tilePane.getStyleClass().add("myTilePane");
    tilePane.setPrefColumns(3);
    tilePane.getChildren().addAll(firstProfil, secondProfil, thirdProfil, addProfil);
  }

  public void init_Main_Window(Stage stage) {

    stage.setTitle("Authentification");
    stage.setScene(mainScene);
    stage.setResizable(false);
  }

  public void update_Main_Window(TextField text, ComboBox<ImageView> box) {
    int alea;

    tilePane.getChildren().clear();

    String url48px = box.getValue().getImage().getUrl();

    System.out.println(url48px);

    String url256px = url48px.substring(0, 31) + "256px" + url48px.substring(35, url48px.length()); // Changer le
    // chiffre par
    // rapport au chemin
    // ligne 358

    Image IMG = new Image(url256px);
    ImageView IMGView = new ImageView(IMG);

    buttonList.add(new Button(text.getText(), IMGView));

    alea = (int) (Math.random() * 3);

    if (alea == 0)
      buttonList.get(buttonList.size() - 1).getStyleClass().add("firstProfilButton");
    else if (alea == 1)
      buttonList.get(buttonList.size() - 1).getStyleClass().add("secondProfilButton");
    else if (alea == 2)
      buttonList.get(buttonList.size() - 1).getStyleClass().add("thirdProfilButton");

    for (Button b : buttonList) {
      tilePane.getChildren().add(b);
    }

    tilePane.getChildren().add(addProfil);
  }

  public void buttonPressed(Stage currentStage) {

    for (Button b : buttonList) {
      b.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {

          close_Window(currentStage);

          Stage newStage = new Stage();
          // Group group = new Group();
          // Scene scene = new Scene(group, 500, 300, Color.WHITE);

          try {
            Parent parent = FXMLLoader
                    .load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/main.fxml")));
            newStage.setScene(new Scene(parent));
            newStage.getIcons().add(new Image("images/logo.png"));
            newStage.setTitle("jCell");
            newStage.setResizable(false);

          } catch (IOException e) {
            e.printStackTrace();
          }

          // newStage.setScene(scene);
          // newStage.setTitle("Choice Simulation");
          display_Window(newStage);
        }
      });
    }
  }

  // Second Window
  public void init_Create_Profil_Window(Stage primaryStage, Stage secondaryStage, Scene secondaryScene) {

    // Initialisation du nom de la fenêtre et de sa scène
    secondaryStage.setTitle("Profil création");
    secondaryStage.setScene(secondaryScene);

    // Spécifie la fenêtre parent
    secondaryStage.initOwner(primaryStage);

    // Mode de la fenêtre
    secondaryStage.initModality(Modality.WINDOW_MODAL);

    // Modifie la position de la nouvelle fenêtre
    secondaryStage.setX(primaryStage.getX() + 250);
    secondaryStage.setY(primaryStage.getY() + 250);
  }

  public ArrayList<ImageView> init_ComboBox_items() {
    ArrayList<ImageView> items = new ArrayList<ImageView>();

    File f = null;
    File[] paths;

    // Création d'un nouveau fichier
    f = new File("src/main/resources/images/48px/"); // Changer le
    // chemin
    // pour autre
    // pc
    // f = new
    // File(getClass().getClassLoader().getResource("/images/48px/").toString());

    // Retourne les noms de chemins pour un répertoire
    paths = f.listFiles();

    // Pour chaque nom de chemin du tableau de chemin
    for (File path : paths) {

      Image IMG = new Image("file:" + path.getPath());
      ImageView IMGView = new ImageView(IMG);

      items.add(IMGView);
    }

    return items;
  }

  // All Window
  public void display_Window(Stage stage) {

    stage.show();
  }

  public void close_Window(Stage stage) {

    stage.close();
  }

  /** @param args no args for jcell */
  public static void main(String[] args) {
    launch(args);
  }
}
