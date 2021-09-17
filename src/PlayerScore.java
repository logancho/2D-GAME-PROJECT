/*
 * PlayerScore.java

- Public class that implements the Comparable interface
- Used for storing pairs of Usernames and Highscores
- Explained in the IO component, but this class' main function is to allow for the 
- storage of username, highscore pairs in descending order from largest to smallest.
 */
public class PlayerScore implements Comparable<PlayerScore> {
    //fields
    private String username;
    private int highscore;
    
    //constructor
    
    public PlayerScore(String username, int highscore) {
        this.username = username;
        this.highscore = highscore;
    }
    
    //getters
    
    public String getName() {
        return this.username;
    }
    
    public int getHighScore() {
        return this.highscore;
    }
    
    //setters
    
    public void setName(String name) {
        this.username = name;
    }
    
    public void setHighScore(int score) {
        this.highscore = score;
    }
    
    //Comparable interface - reversed because I want my treeset to be in descending order
    
    @Override
    public int compareTo(PlayerScore that) {
        if (this.username == that.username) {
            return 0;
        } else {
            if (Integer.compare(this.highscore, that.getHighScore()) == 0) {
                return (this.username.compareTo(that.getName()));
            } else {
                return  -(Integer.compare(this.highscore, that.getHighScore()));
            }
        }
    }
        
    
}