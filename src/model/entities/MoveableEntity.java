package model.entities;

import javafx.geometry.BoundingBox;
import model.Direction;
import view.entities.AnimatedView;

import java.util.LinkedList;

/**
 * Moveable entity (Box, player, etc)
 */
public abstract class MoveableEntity extends Entity{

    protected double speed;
    protected double initial_speed;
    protected Direction direction;

    /**
     * Move the entity
     * @param offset Offset of movement
     * @param dir Direction of movement
     * @return Success
     */
    public boolean move(double offset, Direction dir){

        boolean move_done = true;
        BoundingBox old_box = this.getBounds();
        BoundingBox new_box = game.translateBounds(bounds, dir, offset);
        LinkedList<Entity> collided = game.collidedEntities(new_box);

        if(game.outOfArena(new_box)){
            return false;
        }

        for(Entity entity : collided){
            if(entity != this){
                if(entity instanceof StaticEntity){
                    move_done = false;
                    break;
                }
                else if(entity instanceof MoveableEntity){

                    if( !((MoveableEntity) entity).canMovedBy(this) ){
                        move_done = false;
                        break;
                    }
                    else if ( !((MoveableEntity) entity).move(offset, dir) ){
                        move_done = false;
                        break;
                    }
                }
            }
        }

        this.setBounds(new_box);
        game.objectCollision(this);

        if(!move_done){
            this.setBounds(old_box);
        }

        return move_done;
    }

    /**
     * Test if the entity can be moved by another one
     * @param entity Other entity
     * @return True if is possible
     */
    public abstract boolean canMovedBy(Entity entity);

    public Direction getDirection(){
        return this.direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;

        if(this.entityView instanceof AnimatedView){
            ((AnimatedView)this.entityView).setAnimation(AnimatedView.Animations.WALK, this.direction);
        }
    }

    public double getSpeed() { return this.speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public double getInitialSpeed() { return this.initial_speed; }
    public void setInitialSpeed(double speed) {
        this.initial_speed = speed;
        this.speed = speed;
    }

}
