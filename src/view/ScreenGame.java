package view;

import controller.GameController;
import controller.ScreenController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.*;
import model.entities.*;
import view.entities.*;
import view.graphical.Splash;

import java.util.LinkedList;

/**
 * ScreenGame is a VIEW of the Game
 */
public class ScreenGame extends Screen{

    private BorderPane borderPane;
    private TextField cheat_console;
    private GameController gameController;

    public MenuBar menubar;
    public Pane wrapperPane;
    public Pane arena;

    private static double sprite_ratio = 1.0;
    private double lastArenaW = 0;
    private double lastArenaH = 0;

    private ImageView WIN_SPLASH = new ImageView(new Image("images/youwin.png"));
    private ImageView LOOSE_SPLASH = new ImageView(new Image("images/gameover.png"));
    private ImageView END_SPLASH = new ImageView();


    public ScreenGame(){
        screenController = new ScreenController();
        this.setWidth(860);
        this.setHeight(640);


        this.LOOSE_SPLASH.setFitWidth(500);
        this.LOOSE_SPLASH.setFitHeight(300);
        this.WIN_SPLASH.setFitWidth(500);
        this.WIN_SPLASH.setFitHeight(200);

        borderPane = new BorderPane();
        borderPane.setBackground(new Background(new BackgroundImage(new Image("images/sand1.jpg"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));


        menubar = new MenuBar();
        wrapperPane = new Pane();
        arena = new Pane();
        arena.setBorder(new Border(new BorderStroke(Color.SIENNA, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(5.0))));

        wrapperPane.getChildren().add(arena);

        cheat_console = new TextField();
        cheat_console.setFocusTraversable(false);

        borderPane.setTop(menubar);
        borderPane.setCenter(wrapperPane);
        borderPane.setBottom(cheat_console);
        this.getChildren().addAll(borderPane);

        menubar.getButton_menu().setOnAction(
                event -> {
                    gameController.pauseGame();
                    this.screenController.screensController.setScreen("menu");
                }
        );

        menubar.getButton_options().setOnAction(
                event -> {
                    gameController.pauseGame();
                    ConfigDialog options = new ConfigDialog();
                }
        );

        menubar.getButton_play().setOnMouseClicked(
                event -> {
                    if(menubar.paused_property().getValue()){
                        gameController.startGame();
                    }
                    else{
                        gameController.pauseGame();
                    }
                    wrapperPane.requestFocus();
                }
        );

        menubar.getSliderPlayerSpeed().valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

                gameController.setPlayerSpeed(new_val.intValue());
                wrapperPane.requestFocus();
            }
        });


        menubar.getSliderVampSpeed().valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

                gameController.setVampSpeed(new_val.intValue());
                wrapperPane.requestFocus();
            }
        });


        wrapperPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                //System.out.println("Key Pressed: " + event.getCode());

                if(event.getCode() == KeyCode.R){
                   gameController.newGame();
                }
                else{
                    gameController.notifyEvent(event);
                }

                wrapperPane.requestFocus();
            }
        });

        wrapperPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                //System.out.println("Key Released: " + event.getCode());
                gameController.notifyEvent(event);
                wrapperPane.requestFocus();
            }
        });

        arena.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gameController.notifyEvent(event);
            }
        });

        arena.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gameController.notifyEvent(event);
            }
        });

        cheat_console.setOnKeyPressed(
                (event) -> {
                    if (event.getCode().equals(KeyCode.ENTER))
                    {
                        gameController.cheatCode(cheat_console.getText());
                        cheat_console.clear();
                        wrapperPane.requestFocus();
                    }

                });

        final ChangeListener<Number> resizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {

                if(lastArenaW > 0 && lastArenaH > 0){
                    double ratio = arena.getWidth() / lastArenaW;
                    sprite_ratio = ratio;

                    //EntityView.setScale(ratio);
                    gameController.resizeGame(ratio);
                }

                lastArenaW = arena.getWidth();
                lastArenaH = arena.getHeight();
            }
        };

        arena.prefWidthProperty().bind(wrapperPane.widthProperty());
        arena.prefHeightProperty().bind(wrapperPane.heightProperty());

        arena.layoutXProperty().bind(wrapperPane.widthProperty().subtract(arena.widthProperty()).divide(2));
        arena.layoutYProperty().bind(wrapperPane.heightProperty().subtract(arena.heightProperty()).divide(2));

        arena.maxWidthProperty().bind(arena.prefHeightProperty().multiply(1.5));
        arena.maxHeightProperty().bind(arena.prefWidthProperty().divide(1.5));

        arena.widthProperty().addListener(resizeListener);
        arena.heightProperty().addListener(resizeListener);

        Platform.runLater(() -> wrapperPane.requestFocus());
    }

    /**
     * Link the controller with the view and init new game
     * @param controller GameController
     */
    public void bindController(GameController controller){
        this.gameController = controller;
        this.gameController.newGame();
    }

    /**
     * Update the view with the game model
     * @param game Game model
     */
    public void update(Game game){
        arena.getChildren().clear();

        // AFFICHAGE DES ENTITY (ROCK, BOX, etc)
        LinkedList<Entity> entities = game.getEntities();
        for(Entity entity : entities){

            EntityView entityView = entity.getEntityView();
            entityView.setScale(sprite_ratio);
            entityView.update(entity);
            arena.getChildren().add(entityView);

            if(gameController.debug) {
                if(entity instanceof Player){
                    showCollisionBox(entity.getBounds(), Color.BLUE);
                }
                else if(entity instanceof CharacterEntity){
                    showCollisionBox(entity.getBounds(), Color.PURPLE);
                }
                else if(entity instanceof MoveableEntity){
                    showCollisionBox(entity.getBounds(), Color.GREENYELLOW);
                }
                else if(entity instanceof StaticEntity){
                    showCollisionBox(entity.getBounds(), Color.RED);
                }
            }
        }
        //sprite_ratio = 1.0;

        this.END_SPLASH.setLayoutX( (arena.getWidth() - END_SPLASH.getFitWidth()) / 2 );
        this.END_SPLASH.setLayoutY( (arena.getHeight() - END_SPLASH.getFitHeight()) / 2);
        arena.getChildren().add(END_SPLASH);

    }

    /**
     * Show the collisions box for debug
     * @param box Collision box
     * @param color Color of the box
     */
    public void showCollisionBox(BoundingBox box, Color color){
        Pane pane = new Pane();
        pane.setLayoutX(box.getMinX());
        pane.setLayoutY(box.getMinY());
        pane.setPrefWidth(box.getWidth());
        pane.setPrefHeight(box.getHeight());
        pane.setBorder(new Border(new BorderStroke(color,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        arena.getChildren().add(pane);
    }

    /**
     * Modify the splash to show
     * @param splash Splash to show
     */
    public void setSplash(Splash splash){
        switch (splash){
            case NONE:
                this.END_SPLASH = new ImageView();
                break;
            case WIN:
                this.END_SPLASH = WIN_SPLASH;
                break;
            case GAME_OVER:
                this.END_SPLASH = LOOSE_SPLASH;
                break;
        }
    }

    public void displayError(String err){
        System.out.println(err);
    }
}
