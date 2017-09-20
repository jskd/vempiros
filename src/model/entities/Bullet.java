package model.entities;

import model.Direction;
import view.entities.BulletView;

/**
 * Bullet entity
 */
public class Bullet extends MoveableEntity{

    public Bullet(Direction direction){
        this.direction = direction;
        this.entityView = new BulletView(this.direction);
        this.speed = 20;
    }

    public boolean canMovedBy(Entity entity){
        return false;
    }

    public void collidedBy(Entity other){
    }
}
