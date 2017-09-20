package view.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Representation of rock
 */
public class RockView extends EntityView{

    private static final Image SPRITE_ROCK = new Image("images/rock.png");

    private int WIDTH = 50;
    private int HEIGHT = 50;

    public RockView(){
        this.addSprite("SPRITE_ROCK", new ImageView(SPRITE_ROCK), WIDTH, HEIGHT);
        this.setSprite("SPRITE_ROCK");
    }
}
