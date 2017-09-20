package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import view.Screen;

import java.util.HashMap;

/**
 * Screens manager
 */
public class ScreensController extends StackPane {

    private HashMap<String, Node> screens = new HashMap<>();

    /**
     * Add a new screen
     * @param name Name of screen
     * @param screen Screen
     */
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    /**
     * Load the screen
     * @param name Name of screen
     * @param screen Screen
     * @return Success
     */
    public boolean loadScreen(String name, Screen screen) {
        try {

            Screen loadScreen = screen;
            ScreenController screenController = screen.getController();
            screenController.setScreenParent(this);
            addScreen(name, loadScreen);

            return true;
        }catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Show the screen
     * @param name Name of screen
     * @return Success
     */
    public boolean setScreen(final String name) {

        if (screens.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();

            //Is there is more than one screen
            if (!getChildren().isEmpty()) {

                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(100),
                                new EventHandler() {

                                    @Override
                                    public void handle(Event t) {
                                        //remove displayed screen
                                        getChildren().remove(0);
                                        //add new screen
                                        getChildren().add(0, screens.get(name));


                                        Timeline fadeIn = new Timeline(
                                                new KeyFrame(Duration.ZERO,
                                                        new KeyValue(opacity, 0.0)),
                                                new KeyFrame(new Duration(100),
                                                        new KeyValue(opacity, 1.0)));
                                        fadeIn.play();
                                    }

                                }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {

                //no one else been displayed, then just show
                setOpacity(0.0);
                getChildren().add(screens.get(name));

                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(100),
                                new KeyValue(opacity, 1.0)));
                fadeIn.play();

            }

            return true;
        } else {
            System.out.println("screen hasn't been loaded!\n");
            return false;
        }

    }

    /**
     * Unload the screen
     * @param name Name of screen
     * @return Success
     */
    public boolean unloadScreen(String name) {
        if(screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }


}
