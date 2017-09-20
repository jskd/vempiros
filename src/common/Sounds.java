package common;

import javafx.scene.media.AudioClip;

/**
 * Library of sounds
 */
public class Sounds {

    public enum SoundType{
        DEATH_VAMP, GAME_OVER, GAME_WIN, GUN_FIRE, GUN_RELOAD;
    }

    public static AudioClip GUN_FIRE = new AudioClip(Sounds.class.getClassLoader().getResource("sounds/gun_fire.mp3").toString());
    public static AudioClip GUN_RELOAD = new AudioClip(Sounds.class.getClassLoader().getResource("sounds/gun_reload.mp3").toString());
    public static AudioClip DEATH_VAMP = new AudioClip(Sounds.class.getClassLoader().getResource("sounds/dead_sound.mp3").toString());
    public static AudioClip GAME_OVER = new AudioClip(Sounds.class.getClassLoader().getResource("sounds/game_over.mp3").toString());
    public static AudioClip GAME_WIN = new AudioClip(Sounds.class.getClassLoader().getResource("sounds/game_win.mp3").toString());


    /**
     * Play a sound in library
     * @param type Type of the sound
     */
    public static void play(SoundType type){
        switch (type){

            case DEATH_VAMP:
                DEATH_VAMP.play();
                break;
            case GAME_OVER:
                GAME_OVER.play();
                break;
            case GAME_WIN:
                GAME_WIN.play();
                break;
            case GUN_FIRE:
                GUN_FIRE.play();
                break;
            case GUN_RELOAD:
                GUN_RELOAD.play();
                break;
        }
    }
}
