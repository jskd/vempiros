package model;

import common.Sounds;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.BoundingBox;
import model.entities.*;
import view.entities.AnimatedView;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Game model class. Contains all methods to interact with the model.
 */
public class Game {

    private Player player;
    private LinkedList<Entity> entities;
    private LinkedList<Modification> modifications;

    private Bullet bulletSchema;

    private int NB_VAMP = 5;
    private int MAX_VAMP = 20;

    private IntegerProperty ALIVE_VAMP;
    private IntegerProperty DEAD_VAMP;

    private double INITIAL_WIDTH = 820;
    private double INITIAL_HEIGHT = 550;

    private DoubleProperty ARENA_WIDTH = new SimpleDoubleProperty(INITIAL_WIDTH);
    private DoubleProperty ARENA_HEIGHT = new SimpleDoubleProperty(INITIAL_HEIGHT);

    private HashMap<String, Dimension> entity_dimensions;

    public Game(){

    }

    /**
     * Init the model
     */
    public void init(){
        this.modifications = new LinkedList<>();
        this.entities = new LinkedList<>();
        this.bulletSchema = new Bullet(Direction.EAST);
        this.bulletSchema.setBounds(new BoundingBox(0,0,30,10));

        this.entity_dimensions = new HashMap<>();
        this.entity_dimensions.put("model.entities.Rock", new Dimension(40, 40));
        this.entity_dimensions.put("model.entities.Box", new Dimension(40, 40));
        this.entity_dimensions.put("model.entities.Vampire", new Dimension(20, 50));
        this.entity_dimensions.put("model.entities.Player", new Dimension(20, 70));

        int NB_ROCK = (int)(INITIAL_WIDTH/150);
        int NB_BOX = (int)(INITIAL_WIDTH/150);
        NB_VAMP = (int)(INITIAL_WIDTH/100);

        ALIVE_VAMP = new SimpleIntegerProperty(NB_VAMP);
        DEAD_VAMP = new SimpleIntegerProperty(0);

        this.spawnEntity(entities, "model.entities.Rock", NB_ROCK);
        this.spawnEntity(entities, "model.entities.Box", NB_BOX);
        this.spawnEntity(entities, "model.entities.Vampire", NB_VAMP);
        this.spawnEntity(entities, "model.entities.Player", 1);
    }

    /**
     * Add new entity on a list
     * @param liste List of entities
     * @param entity_name Class name of entity
     * @param number Number of entity to spawn
     */
    public void spawnEntity(LinkedList<Entity> liste, String entity_name, int number){
        Random rand = new Random();

        double ratio = this.ARENA_WIDTH.doubleValue() / this.INITIAL_WIDTH;

        double width = entity_dimensions.get(entity_name).getWidth() * ratio;
        double height = entity_dimensions.get(entity_name).getHeight() * ratio;

        for(int i=0; i < number; i++){
            int MAX_X = (int)(ARENA_WIDTH.getValue() - width);
            int MAX_Y = (int)(ARENA_HEIGHT.getValue() - height);
            BoundingBox box;

            do{
                int x = rand.nextInt(MAX_X);
                int y = rand.nextInt(MAX_Y);
                box = new BoundingBox(x, y, width, height);
            }while(intersectEntity(box));

            try{
                Entity object =  (Entity) Class.forName(entity_name).newInstance();

                object.setBounds(box);
                object.setGame(this);

                if(object instanceof MoveableEntity){
                    int random_dir = rand.nextInt(Direction.values().length);
                    Direction new_dir = Direction.values()[random_dir];
                    ((MoveableEntity)object).setDirection(new_dir);

                    ((MoveableEntity)object).setSpeed(((MoveableEntity)object).getSpeed() * ratio);

                    if(object instanceof CharacterEntity){
                        ((AnimatedView)object.getEntityView()).setAnimation(AnimatedView.Animations.WALK,
                                ((CharacterEntity)object).getDirection());

                        if(object instanceof Player){
                            this.player = (Player) object;
                        }
                    }
                }

                object.getEntityView().setScale(ratio);

                liste.add(object);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Move all entities
     */
    public void moveEntities(){
        for(Entity entity : entities){

            if(!entityModified(entity)){

                if(entity instanceof Bullet){
                    Bullet bullet = (Bullet)entity;
                    if(!bullet.move(bullet.getSpeed(), bullet.getDirection())){
                        modifications.add(new Modification(Modification.ModificationType.REMOVE, bullet));
                    }
                }

                else if(entity instanceof Vampire){
                    Vampire vamp = (Vampire)entity;
                    Random rand = new Random();

                    LinkedList<Direction> possible_dir = new LinkedList<>();
                    possible_dir.add(Direction.NORTH);
                    possible_dir.add(Direction.SOUTH);
                    possible_dir.add(Direction.EAST);
                    possible_dir.add(Direction.WEST);
                    possible_dir.add(Direction.NORTH_WEST);
                    possible_dir.add(Direction.SOUTH_WEST);
                    possible_dir.add(Direction.NORTH_EAST);
                    possible_dir.add(Direction.SOUTH_EAST);

                    while(!vamp.move(vamp.getSpeed(), vamp.getDirection())){
                        possible_dir.remove(vamp.getDirection());

                        if(possible_dir.size() > 0){
                            int random_dir = rand.nextInt(possible_dir.size());
                            Direction new_dir = possible_dir.get(random_dir);
                            vamp.setDirection(new_dir);
                        }
                        else{ break; }
                    }
                }
            }
        }
    }

    /**
     * Test if an entity is modified
     * @param entity Entity
     * @return True if modified
     */
    public boolean entityModified(Entity entity){
        for(Modification modif : modifications){
            if(modif.getEntity() == entity)
                return true;
        }
        return false;
    }

    /**
     * Apply all modifications to the game
     */
    public void applyModifications(){

        for(Modification modif : modifications){

            switch (modif.getType()){

                case KILL:
                    entities.remove(modif.getEntity());
                    Sounds.play(Sounds.SoundType.DEATH_VAMP);
                    this.alive_vamp().set(this.alive_vamp().getValue() - 1);
                    this.dead_vamp().set(this.dead_vamp().getValue() + 1);

                    break;
                case REMOVE:
                    entities.remove(modif.getEntity());
                    if(modif.getEntity() instanceof Vampire){
                        this.alive_vamp().set(this.alive_vamp().getValue() - 1);
                    }
                    break;
                case SPAWN:
                    if(modif.getEntity() instanceof Vampire){
                        if(this.alive_vamp().getValue() < this.getMAX_VAMP()) {
                            this.spawnEntity(entities, "model.entities.Vampire", 1);
                            this.alive_vamp().set(this.alive_vamp().getValue() + 1);
                        }
                    }
                    break;
            }
        }

        modifications.clear();
    }

    /**
     * Check collisions by and entity
     * @param current entity
     */
    public void objectCollision(Entity current){
        for(Entity entity : entities){
            if(entity != current){

                if(current.collidesWith(entity)){
                    entity.collidedBy(current);
                    current.collidedBy(entity);
                }
            }
        }
    }

    /**
     * Return the list of all entities collided
     * @param box Collision box of the entity
     * @return List of collided entities
     */
    public LinkedList<Entity> collidedEntities(BoundingBox box){
        LinkedList<Entity> collided = new LinkedList<>();

        for(Entity entity : entities){
            if(box.intersects(entity.getBounds())){
                collided.add(entity);
            }
        }
        return collided;
    }

    /**
     * Test the collision
     * @param box Collision box of the entity
     * @return True of collided
     */
    public boolean intersectEntity(BoundingBox box){
        for(Entity entity : entities){
            BoundingBox objet_box = entity.getBounds();
            if(box.intersects(objet_box)){ return true; }
        }
        return false;
    }

    /**
     * Test the presence in the arena
     * @param box Collision box of the entity
     * @return True if not in arena
     */
    public boolean outOfArena(BoundingBox box){
        BoundingBox arena = new BoundingBox(0, 0, ARENA_WIDTH.getValue(), ARENA_HEIGHT.getValue());
        return !arena.contains(box);
    }

    /**
     * Kill all vampires
     */
    public void killAll(){
        for(Entity entity : entities){
            if(entity instanceof Vampire){
                modifications.add(new Modification(Modification.ModificationType.KILL, entity));
            }
        }
        applyModifications();
    }

    /**
     * Apply the action send by GameController
     * @param action Action type
     * @throws Exception Error
     */
    public void apply(ActionType action) throws Exception{

        if(!player.isAlive()){
            throw new Exception("Vous êtes mort.");
        }

        switch (action){
            case MOVE:
                if(!player.move(player.getSpeed(), player.getDirection())){
                    throw new Exception("Mouvement impossible.");
                }
                break;

            case SHOOT:
                if(player.current_bullets().getValue() > 0) {
                    player.shoot();
                }
                else {
                    throw new Exception("Pas assez de munitions.");
                }
                break;
        }
    }

    /**
     * Translate a collision box
     * @param box Collision box
     * @param dir Direction of translation
     * @param value Offset of translation
     * @return New boundingbox
     */
    public BoundingBox translateBounds(BoundingBox box, Direction dir, double value){
        BoundingBox new_box = null;
        switch (dir){
            case NORTH:
                new_box = new BoundingBox(box.getMinX(), box.getMinY() - value, box.getWidth(),
                        box.getHeight());
                break;
            case SOUTH:
                new_box = new BoundingBox(box.getMinX(), box.getMinY() + value, box.getWidth(),
                        box.getHeight());
                break;
            case EAST:
                new_box = new BoundingBox(box.getMinX() + value, box.getMinY(), box.getWidth(),
                        box.getHeight());
                break;
            case WEST:
                new_box = new BoundingBox(box.getMinX() - value, box.getMinY(), box.getWidth(),
                        box.getHeight());
                break;
            case NORTH_EAST:
                new_box = new BoundingBox(box.getMinX() + (value / 2), box.getMinY() - (value / 2), box.getWidth(),
                        box.getHeight());
                break;
            case NORTH_WEST:
                new_box = new BoundingBox(box.getMinX() - (value / 2), box.getMinY() - (value / 2), box.getWidth(),
                        box.getHeight());
                break;
            case SOUTH_EAST:
                new_box = new BoundingBox(box.getMinX() + (value / 2), box.getMinY() + (value / 2), box.getWidth(),
                        box.getHeight());
                break;
            case SOUTH_WEST:
                new_box = new BoundingBox(box.getMinX() - (value / 2), box.getMinY() + (value / 2), box.getWidth(),
                        box.getHeight());
                break;
        }
        return new_box;
    }

    /**
     * Resize all entities
     * @param ratio Scale ratio
     */
    public void resizeBoundingBox(double ratio){
        double new_X, new_Y, new_W, new_H;

        if(entities != null){
            for(Entity entity : this.entities){
                BoundingBox old_box = entity.getBounds();

                new_X = old_box.getMinX() * ratio;
                new_Y = old_box.getMinY() * ratio;
                new_W = old_box.getWidth() * ratio;
                new_H = old_box.getHeight() * ratio;

                BoundingBox new_box = new BoundingBox(new_X, new_Y, new_W, new_H);
                entity.setBounds(new_box);

                if(entity instanceof MoveableEntity){
                    ((MoveableEntity)entity).setSpeed(((MoveableEntity)entity).getSpeed() * ratio);
                }
            }
        }

        // MODIFICATION DES DIMENSIONS PAR DEFAUT
        for(HashMap.Entry<String, Dimension> entry : entity_dimensions.entrySet()) {
            Dimension dimension = entry.getValue();
            dimension.setSize(dimension.getWidth() * ratio, dimension.getHeight() * ratio);
        }

        // MODIFICATION DU SCHEMA DE LA BALLE
        this.bulletSchema.setBounds(new BoundingBox(0,0,
                bulletSchema.getBounds().getWidth() * ratio, bulletSchema.getBounds().getHeight() * ratio));
        this.bulletSchema.setSpeed(bulletSchema.getSpeed() * ratio);
    }

    public Player getPlayer(){
        return this.player;
    }

    public LinkedList<Entity> getEntities(){
        return this.entities;
    }

    public LinkedList<Modification> getModifications(){
        return this.modifications;
    }

    public DoubleProperty arena_width(){
        return this.ARENA_WIDTH;
    }

    public DoubleProperty arena_height(){
        return this.ARENA_HEIGHT;
    }

    public IntegerProperty alive_vamp(){
        return this.ALIVE_VAMP;
    }

    public IntegerProperty dead_vamp(){
        return this.DEAD_VAMP;
    }

    public Bullet getBulletSchema(){
        return this.bulletSchema;
    }

    public int getMAX_VAMP(){ return this.MAX_VAMP; }

}
