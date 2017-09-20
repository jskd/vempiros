package view.entities;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Direction;
import model.entities.Entity;

/**
 * Representation of a Bullet
 */
public class BulletView extends EntityView{

    private static final Image SPRITE_BULLET = new Image("images/bullet.png");

    private int WIDTH = 30;
    private int HEIGHT = 10;

    /**
     * Constructor
     * @param dir Direction of the bullet
     */
    public BulletView(Direction dir){
        this.addSprite("SPRITE_BULLET", new ImageView(SPRITE_BULLET), WIDTH, HEIGHT);
        this.setSprite("SPRITE_BULLET");
        this.direction = dir;

        switch (this.direction){
            case NORTH:
                this.setRotate(-90);
                break;
            case SOUTH:
                this.setRotate(90);
                break;
            case EAST:
                this.setRotate(0);
                break;
            case WEST:
                this.setRotate(180);
                break;
            case NORTH_EAST:
                this.setRotate(-45);
                break;
            case NORTH_WEST:
                this.setRotate(-135);
                break;
            case SOUTH_EAST:
                this.setRotate(45);
                break;
            case SOUTH_WEST:
                this.setRotate(135);
                break;
        }
    }

    public void update(Entity entity) {
        BoundingBox obj_box = entity.getBounds();

        this.setLayoutX(obj_box.getMinX() - (WIDTH() / 2) + (obj_box.getWidth() / 2));
        this.setLayoutY(obj_box.getMinY() - (HEIGHT() / 2) + (obj_box.getHeight() / 2));
    }

}
