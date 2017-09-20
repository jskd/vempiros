package view;

import controller.GameController;
import controller.ScreenController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Score;
import java.io.*;
import java.util.*;


public class ScreenMenu extends Screen{

    private static String FILE_SCORES = "scores";
    private int nb_scores = 6;

    public ScreenMenu(){
        this.screenController = new ScreenController();

        VBox vbox = new VBox();
        vbox.setMaxSize(500, 400);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.2f, 0.2f, 0.2f));

        ImageView background = new ImageView(new Image("images/first_screen.jpg"));
        background.setOpacity(0.8);

        Text title = new Text("Pistolero VS Vampiros");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/something_strange.ttf"), 80));
        title.setFill(Color.CRIMSON);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(2);
        title.setEffect(ds);


        VBox scores = new VBox();
        scores.setMaxSize(300, 100);
        scores.setAlignment(Pos.CENTER);
        scores.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-background-radius: 10;");
        this.readScores(scores);


        Text label_pseudo = new Text("Your name : ");
        label_pseudo.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/something_strange.ttf"), 30));
        label_pseudo.setFill(Color.CRIMSON);
        label_pseudo.setStroke(Color.BLACK);
        label_pseudo.setStrokeWidth(1);
        label_pseudo.setEffect(ds);

        TextField field_pseudo = new TextField();
        field_pseudo.setPromptText("Player");

        HBox name_form = new HBox();
        name_form.getChildren().addAll(label_pseudo, field_pseudo);
        name_form.setAlignment(Pos.CENTER);

        Button button_play = new Button("START GAME");
        button_play.setMinSize(350, 50);
        button_play.setId("button-start");

        vbox.getChildren().addAll(title, scores, name_form, button_play);
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(scores, new Insets(30, 0, 0, 0));
        VBox.setMargin(name_form, new Insets(30, 0, 0, 0));
        VBox.setMargin(button_play, new Insets(10, 0, 0, 0));

        StackPane.setAlignment(vbox, Pos.CENTER);

        this.getChildren().addAll(background, vbox);

        button_play.setOnAction(
                event -> {

                    String pseudo;
                    if(field_pseudo.getText().equals("")){
                        pseudo = field_pseudo.getPromptText();
                    }
                    else{
                        pseudo = field_pseudo.getText();
                    }

                    ScreenGame screenGame = new ScreenGame();
                    this.screenController.screensController.loadScreen("game", screenGame);
                    this.screenController.screensController.setScreen("game");

                    GameController controller = new GameController(pseudo, screenGame);
                    screenGame.bindController(controller);

                });

        field_pseudo.setOnKeyPressed(
                (event) -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {

                        String pseudo;
                        if(field_pseudo.getText().equals("")){
                            pseudo = field_pseudo.getPromptText();
                        }
                        else{
                            pseudo = field_pseudo.getText();
                        }

                        ScreenGame screenGame = new ScreenGame();
                        this.screenController.screensController.loadScreen("game", screenGame);
                        this.screenController.screensController.setScreen("game");

                        GameController controller = new GameController(pseudo, screenGame);
                        screenGame.bindController(controller);
                    }
                });

        Platform.runLater(() -> this.requestFocus());
    }

    public void readScores(VBox box){

        List<Score> scores = new ArrayList<>();

        File jarPath = new File(ConfigDialog.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String bindingsPath = jarPath.getParentFile().getAbsolutePath() + "/" + FILE_SCORES;

        try {

            File file = new File(bindingsPath);
            file.createNewFile();

            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line;

            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");
                scores.add(new Score(args[0], Integer.parseInt(args[1])));
            }

            reader.close();
            fr.close();

            Collections.sort(scores,  Collections.reverseOrder());

            int temp = 0;
            for(Score sc : scores){
                if(temp >= nb_scores) break;

                GridPane field = new GridPane();
                field.getColumnConstraints().add(new ColumnConstraints(250));
                field.setAlignment(Pos.CENTER_LEFT);

                Text pseudo = new Text(sc.getName());
                Text value = new Text(sc.getValue().toString());
                pseudo.setFill(Color.WHITE);
                pseudo.setFont(Font.font ("Verdana", 20));
                value.setFill(Color.WHITE);
                value.setFont(Font.font ("Verdana", 20));

                GridPane.setConstraints(pseudo, 0, 0);
                GridPane.setConstraints(value, 1, 0);
                field.getChildren().addAll(pseudo,value);

                box.setMargin(field, new Insets(5, 10, 5, 10));
                box.getChildren().add(field);
                System.out.println(String.format("%s %s", sc.getName(), sc.getValue()));

                temp++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
