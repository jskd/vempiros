package view.entities;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Direction;
import model.entities.Entity;
import view.graphical.ImageViewAnimation;

/**
 * Representation of player
 */
public class PlayerView extends AnimatedView{

    private static final Image SPRITE_COWBOY = new Image("images/cowboy.png");
    private static final Image RIP_COWBOY = new Image("images/rip.png");

    // WALKING ANIM
    private final ImageViewAnimation WALK_NORTH = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128*5, 128, 128, FPS_ANIM);
    private final ImageViewAnimation WALK_SOUTH = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128*9, 128, 128, FPS_ANIM);
    private final ImageViewAnimation WALK_EAST = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 0, 128, 128, FPS_ANIM);
    private final ImageViewAnimation WALK_WEST = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128, 128, 128, FPS_ANIM);

    private final ImageViewAnimation WALK_NORTH_EAST = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128*4, 128, 128, FPS_ANIM);
    private final ImageViewAnimation WALK_SOUTH_EAST = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128*2, 128, 128, FPS_ANIM);
    private final ImageViewAnimation WALK_NORTH_WEST = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128*6, 128, 128, FPS_ANIM);
    private final ImageViewAnimation WALK_SOUTH_WEST = new ImageViewAnimation(
            SPRITE_COWBOY, 8, 8, 0, 128*8, 128, 128, FPS_ANIM);

    // DEAD
    private final ImageViewAnimation DEAD = new ImageViewAnimation(
            RIP_COWBOY, 1, 1, 0, 0, 128, 128, FPS_ANIM);

    private int WIDTH = 100;
    private int HEIGHT = 100;

    public PlayerView(){
        this.addSprite("WALK_NORTH", WALK_NORTH, WIDTH, HEIGHT);
        this.addSprite("WALK_SOUTH", WALK_SOUTH, WIDTH, HEIGHT);
        this.addSprite("WALK_EAST", WALK_EAST, WIDTH, HEIGHT);
        this.addSprite("WALK_WEST", WALK_WEST, WIDTH, HEIGHT);
        this.addSprite("WALK_NORTH_EAST", WALK_NORTH_EAST, WIDTH, HEIGHT);
        this.addSprite("WALK_SOUTH_EAST", WALK_SOUTH_EAST, WIDTH, HEIGHT);
        this.addSprite("WALK_NORTH_WEST", WALK_NORTH_WEST, WIDTH, HEIGHT);
        this.addSprite("WALK_SOUTH_WEST", WALK_SOUTH_WEST, WIDTH, HEIGHT);
        this.addSprite("DEAD", DEAD, 60, 60);

        this.setAnimation(Animations.WALK, Direction.EAST);
    }

    public void update(Entity entity) {
        BoundingBox obj_box = entity.getBounds();
        this.setLayoutX(obj_box.getMinX() - (WIDTH() / 2) + (obj_box.getWidth() / 2));
        this.setLayoutY(obj_box.getMinY() - (HEIGHT() / 2) + (obj_box.getHeight() / 2) - 8);
    }


    public void setAnimation(Animations anim, Direction dir){
        if(this.direction == null || this.direction != dir){
            if(sprite != null){
                ((ImageViewAnimation)sprite).stop();
            }

            if(anim == Animations.WALK){
                switch (dir){

                    case NORTH:
                        this.setSprite("WALK_NORTH");
                        break;
                    case SOUTH:
                        this.setSprite("WALK_SOUTH");
                        break;
                    case EAST:
                        this.setSprite("WALK_EAST");
                        break;
                    case WEST:
                        this.setSprite("WALK_WEST");
                        break;
                    case NORTH_EAST:
                        this.setSprite("WALK_NORTH_EAST");
                        break;
                    case NORTH_WEST:
                        this.setSprite("WALK_NORTH_WEST");
                        break;
                    case SOUTH_EAST:
                        this.setSprite("WALK_SOUTH_EAST");
                        break;
                    case SOUTH_WEST:
                        this.setSprite("WALK_SOUTH_WEST");
                        break;
                }
            }
            else if(anim == Animations.ATTACK){

            }
            else if(anim == Animations.DEAD){
                this.setSprite("DEAD");
            }
        }
        this.direction = dir;
    }
}
