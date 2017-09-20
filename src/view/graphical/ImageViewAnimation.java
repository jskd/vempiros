package view.graphical;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * ImageView with animation
 */
public class ImageViewAnimation extends ImageView {

    private Animation animation;

    public ImageViewAnimation(Image image, int nbColumns, int nbFrames,
                              int offset_X, int offset_Y, int width, int heigth, int fps){
        super(image);

        this.setViewport(new Rectangle2D(offset_X, offset_Y, width, heigth));

        animation = new SpriteAnimation(
                this,
                Duration.millis((nbFrames * 1000)/fps),
                nbFrames, nbColumns,
                offset_X, offset_Y,
                width, heigth
        );
        animation.setCycleCount(Animation.INDEFINITE);

    }

    public void start(){
        animation.play();
    }

    public void stop(){
        animation.jumpTo(Duration.millis(0));
        animation.stop();
    }

}
