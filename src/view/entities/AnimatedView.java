package view.entities;

import model.Direction;
import view.graphical.ImageViewAnimation;

/**
 * Animated entity view
 */
public abstract class AnimatedView extends EntityView{

    public final static int FPS_ANIM = 15;

    public enum Animations {
        WALK,
        ATTACK,
        DEAD,
        IDLE
    }

    public void startAnimation(){
        ((ImageViewAnimation)sprite).start();
    }
    public void stopAnimation() {
        ((ImageViewAnimation) sprite).stop();
    }

    public abstract void setAnimation(AnimatedView.Animations anim, Direction dir);
}
