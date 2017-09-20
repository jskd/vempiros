package model.entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Entity with life
 */
public abstract class CharacterEntity extends MoveableEntity{

    protected int total_life;
    protected IntegerProperty current_life = new SimpleIntegerProperty(0);
    protected BooleanProperty alive = new SimpleBooleanProperty(true);
    private boolean immortel = false;

    /**
     * Add lives
     * @param n Add n lives
     */
    public void addLife(int n){
        if(current_life.getValue() + n <= total_life)
            current_life.set(current_life.getValue() + n);
        else
            current_life.set(total_life);

        this.alive.set(isAlive());
    }

    /**
     * Remove lives
     * @param n Remove n lives
     */
    public void removeLife(int n){
        if(!immortel){
            if(n <= current_life.getValue())
                current_life.set(current_life.getValue() - n);
            else
                current_life.set(0);
        }

        this.alive.set(isAlive());
    }

    public BooleanProperty alive(){
        return this.alive;
    }
    public IntegerProperty current_life(){
        return this.current_life;
    }
    public boolean isAlive(){
        return (current_life.getValue() > 0);
    }
    public void setImmortel(boolean bool) { this.immortel = bool; }
    public boolean getImmortel() { return this.immortel; }
}
