package model.entities;

import common.Sounds;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.BoundingBox;
import model.Direction;
import view.entities.AnimatedView;
import view.entities.PlayerView;

/**
 * Player entity
 */
public class Player extends CharacterEntity {

    private DoubleProperty total_bullets;
    private DoubleProperty current_bullets;
    private NumberBinding progress_bullets;

    private long time_last_hit = 0;
    private double TIME_BEFORE_HIT = 500; //ms
    private boolean walking = false;

    public Player(){
        this.setInitialSpeed(8);
        this.total_life = 3;

        this.current_life.set(this.total_life);
        this.setDirection(Direction.EAST);
        this.entityView = new PlayerView();

        this.total_bullets = new SimpleDoubleProperty(25);
        this.current_bullets = new SimpleDoubleProperty(total_bullets.getValue());
        this.progress_bullets = Bindings.divide(current_bullets, total_bullets);
    }


    public boolean canMovedBy(Entity entity){
        return false;
    }

    public void collidedBy(Entity other){
        if(other instanceof Vampire){

            if(time_last_hit == 0 || (System.currentTimeMillis() - time_last_hit >= TIME_BEFORE_HIT) ) {
                this.removeLife(1);
                time_last_hit = System.currentTimeMillis();
            }
        }
    }

    /**
     * Gun fire, new bullet appears
     */
    public void shoot(){
        Bullet bullet = new Bullet(this.direction);

        BoundingBox player_box = this.bounds;
        BoundingBox old_box = game.getBulletSchema().getBounds();
        BoundingBox bullet_box = null;

        double bullet_W = old_box.getWidth();
        double bullet_H = old_box.getHeight();

        double player_minX = player_box.getMinX();
        double player_minY = player_box.getMinY();
        double player_maxX = player_box.getMaxX();
        double player_maxY = player_box.getMaxY();

        double player_W = player_box.getWidth();
        double player_H = player_box.getHeight();

        switch (bullet.getDirection()){

            case NORTH:
                bullet_box = new BoundingBox(player_minX + (player_W / 2), player_minY - bullet_W, bullet_H, bullet_W);
                break;
            case SOUTH:
                bullet_box = new BoundingBox(player_minX + (player_W / 2), player_minY + player_H, bullet_H, bullet_W);
                break;
            case EAST:
                bullet_box = new BoundingBox(player_minX + player_W, player_minY + (player_H / 2), bullet_W, bullet_H);
                break;
            case WEST:
                bullet_box = new BoundingBox(player_minX  - bullet_W , player_minY + (player_H / 2), bullet_W, bullet_H);
                break;
            case NORTH_EAST:
                bullet_box = new BoundingBox(player_maxX, player_minY, bullet_W, bullet_W);
                break;
            case NORTH_WEST:
                bullet_box = new BoundingBox(player_minX - bullet_W, player_minY, bullet_W, bullet_W);
                break;
            case SOUTH_EAST:
                bullet_box = new BoundingBox(player_maxX, player_maxY, bullet_W, bullet_W);
                break;
            case SOUTH_WEST:
                bullet_box = new BoundingBox(player_minX - bullet_W, player_maxY, bullet_W, bullet_W);
                break;
        }

        bullet.setBounds(bullet_box);
        bullet.setSpeed(game.getBulletSchema().getSpeed());
        bullet.setGame(this.game);

        if(bullet.move(bullet.getSpeed(), bullet.getDirection())){
            game.getEntities().add(bullet);
        }

        System.out.println(" >> Shoot !");
        this.removeBullet();
        Sounds.play(Sounds.SoundType.GUN_FIRE);
    }

    /**
     * Add bullets
     * @param n Add n bullets
     */
    public void addBullets(int n) {
        if(n <= total_bullets.getValue())
            this.current_bullets.set(current_bullets.getValue() + n);
        else
            this.current_bullets.set(total_bullets.getValue());
    }

    /**
     * Remove one bullet
     */
    public void removeBullet(){
        if(current_bullets.getValue() > 0)
            current_bullets.set(current_bullets.getValue() - 1);
    }

    public DoubleProperty total_bullets() {
        return total_bullets;
    }

    public DoubleProperty current_bullets() {
        return current_bullets;
    }

    public NumberBinding progress_bullets() {
        return progress_bullets;
    }

    public boolean isWalking(){
        return this.walking;
    }

    public void setWalking(boolean bool){
        this.walking = bool;

        if(this.walking){
            ((AnimatedView)this.entityView).startAnimation();
        }
        else{
            ((AnimatedView)this.entityView).stopAnimation();
        }
    }

}
