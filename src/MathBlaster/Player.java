package MathBlaster;

//Emmanuel Edamwen

public class Player {
    //private String playerName;
    private int lives;
    private int score;

    public Player(int lives, int score){
        this.lives = lives;
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
