package model;

/**
 * Representation of a score
 */
public class Score implements Comparable<Score>{

    private String name;
    private int value;

    public Score(String name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public Integer getValue(){
        return this.value;
    }

    public int compareTo(Score score)
    {
        return getValue().compareTo(score.getValue());
    }
}
