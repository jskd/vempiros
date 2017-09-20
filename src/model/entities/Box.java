package model.entities;

import model.Modification;
import view.entities.BoxView;

/**
 * Box entity
 */
public class Box extends MoveableEntity{

    public Box(){
        this.entityView = new BoxView();
    }

    public boolean canMovedBy(Entity entity){
        if(entity instanceof Player){
            return true;
        }
        return false;
    }

    public void collidedBy(Entity other){
        if(other instanceof Bullet){
            game.getModifications().add(new Modification(Modification.ModificationType.REMOVE, other));
        }
        else if(other instanceof Player){
            this.direction = ((Player) other).getDirection();
            this.move(((Player) other).getSpeed(), ((Player) other).getDirection());
        }
    }
}
