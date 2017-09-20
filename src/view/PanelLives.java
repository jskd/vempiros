package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Lives panel, show life level of player
 */
public class PanelLives extends HBox{

    private int nbLives;
    private IntegerProperty current_life = new SimpleIntegerProperty(0);

    private static Image HEART = new Image("images/heart.png");
    private static Image HEART_OFF = new Image("images/heart-off.png");

    /**
     * Constructor
     * @param nbLives Number of maximum lives
     */
    public PanelLives(int nbLives){
        this.nbLives = nbLives;
        this.current_life.set(nbLives);

        current_life.addListener(
                (observable, oldValue, newValue) -> {
            System.out.println(String.format("LIFE CHANGED : %d -> %d", oldValue, newValue));
            this.repaint();
        });

        this.repaint();
    }

    public IntegerProperty current_life(){
        return this.current_life;
    }

    /**
     * Update the number of full heart
     */
    public void repaint(){
        this.getChildren().clear();

        for(int i=0; i<nbLives; i++){
            ImageView heart;

            if(i < current_life.getValue())
                heart = new ImageView(HEART);
            else
                heart = new ImageView(HEART_OFF);

            heart.setFitWidth(40);
            heart.setFitHeight(45);
            this.getChildren().add(heart);
        }
    }
}
