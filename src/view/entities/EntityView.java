package view.entities;

import javafx.geometry.BoundingBox;
import javafx.geometry.Dimension2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.Direction;
import model.entities.Entity;

import java.awt.*;
import java.util.HashMap;

/**
 * Graphic representation of an Entity
 */
public abstract class EntityView extends StackPane {

    protected Direction direction;

    protected HashMap<String, ImageView> sprites = new HashMap<>();

    protected ImageView sprite;

    /**
     * Resize the sprite
     * @param factor Scale ratio
     */
    public void setScale(double factor){
        for(ImageView img : sprites.values()){
            img.setFitWidth(img.getFitWidth() * factor);
            img.setFitHeight(img.getFitHeight() * factor);
        }
    }

    /**
     * Show the sprite
     * @param name Sprite name
     */
    public void setSprite(String name){
        this.sprite = this.sprites.get(name);

        if(sprite != null){
            this.getChildren().clear();
            this.getChildren().add(sprite);
        }
    }

    /**
     * Add a sprite
     * @param name Sprite name
     * @param view ImageView of sprite
     * @param width width of sprite
     * @param height height of sprite
     */
    public void addSprite(String name, ImageView view, double width, double height){
        view.setFitWidth(width);
        view.setFitHeight(height);
        this.sprites.put(name, view);
    }

    /**
     * Update the sprite according to the entity model
     * @param entity Entity model
     */
    public void update(Entity entity){
        BoundingBox obj_box = entity.getBounds();
        this.setLayoutX(obj_box.getMinX()-(WIDTH()/2)+(obj_box.getWidth() / 2));
        this.setLayoutY(obj_box.getMinY()-(HEIGHT()/2)+(obj_box.getHeight() / 2));
    }

    public double WIDTH(){
        return this.sprite.getFitWidth();
    }
    public double HEIGHT() {
        return this.sprite.getFitHeight();
    }
    public ImageView getSprite(){ return this.sprite; }
    public HashMap<String, ImageView> getSprites(){ return this.sprites; }
    public void setSprites(HashMap<String, ImageView> sprites){ this.sprites = sprites; }
}
