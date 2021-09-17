import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
//import java.util.Map;
import java.util.Set;
//import java.util.TreeMap;
import java.util.Random;

import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GameCourt.java
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */

@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    /*FIELDS*/
    
    //The player's character 
    private Player player; 
    
    
    private boolean playing = false; // Tells us whether the game is running 
    
    //Current Player's username + highest score stored in the form of a "PlayerScore" object for 
    //set storage
    
    private PlayerScore ps;
    
    //JLabels
    private JLabel status; // Current status text, i.e. "Running..."
    
    private JLabel health; // Current player's health out of MAX_HEALTH
     
    private JLabel score; // Current playthrough's score (number of monsters slain)
    
//  leaderboard JTextPane;
    private JTextPane leaderboard;
    
    
    //Collections
    
    //Set of all monsters
    private Set<Character> monsters;
    
    //Map of all past and current players and their highest score
    private Set<PlayerScore> highScores;
    
    

    //Background Image
    private static final String IMG_BD1 = "files/Background/v6.png";
    
    //IO File Paths
    private static final String HIGHSCORESPATH = "files/Highscores.txt";
    
    // Game constants
    private static final int COURT_WIDTH = 1200;
    private static final int COURT_HEIGHT = 700;
    private static final int PLAYER_VELOCITY = 10;

    // Update interval for timer, in milliseconds
    private static final int INTERVAL = 25;
    // Spawn cooldown for monsters
    private static final int SPAWNCOOLDOWN = 40;
    private int spawnCounter = SPAWNCOOLDOWN;
    
    // Maximum number of monsters at any time
    public static final int MAXMONSTERS = 10;
    
    //Random number generator tool
    Random rand;
    
    //Score field - number of monsters slain
    private int scoreCounter;
    
    //The local highest score of the current player
    private int currentHighScore;
    
    /*Constructor**********************************************************************************/
    
    public GameCourt(JLabel status, JLabel health, JLabel score, JTextPane leaderboard, 
            String username) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //This is the main Swing Timer that controls everything via tick()
        
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        
        timer.start(); 

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    player.setVx(-PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    player.setVx(PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    player.setVy(PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    player.setVy(-PLAYER_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                player.setVx(0);
                player.setVy(0);
            }
        });
        
        //This key listener initiates player attacks! through the P key 
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    if (player.getCoolDown() == 0) {
                        player.setAttacking(true);
                        player.attack();
                    }
                } 
            }
        });
        
        //Labels:
        this.health = health;
        this.status = status;
        this.score = score;
        this.leaderboard = leaderboard;
        
        //Initializing our highscores container
        this.highScores = new TreeSet<PlayerScore>();
        
        //Initializing our current player's PlayerScore with the provided username
        this.ps = new PlayerScore(username, 0);
        
        //Initialize the high score set with past high scores!
        readHighScores();
    }
    
    
    /***METHODS************************************************************************************/
    
    //Spawns monsters periodically, chooses randomly between Monster1 and Monster2 types
    
    private void spawnMonster() {
        if (monsters.size() < MAXMONSTERS) {
//            double r = Math.random();
            rand = new Random();
            int r = rand.nextInt(10);
            if (r < 5) {
                Monster1 m = new Monster1(COURT_WIDTH, COURT_HEIGHT, player);
                m.setAttacking(true);
                monsters.add(m);
            } else {
                Monster2 m = new Monster2(COURT_WIDTH, COURT_HEIGHT, player);
                m.setAttacking(true);
                monsters.add(m);
            }
        }
    }
    
    //Monster handle function - calls move and inflict damage on all existing monsters via 
    //iterating through monsters set
    private void handleMonsters() {
        Iterator<Character> i = monsters.iterator();
        while (i.hasNext()) {
            Character c = i.next();
            //if the monster is alive, it's attackstate should remain true
            if (c.getAttackState()) { 
                c.move();
                c.inflictDamage(player);
                player.inflictDamage(c);
                
                //Check if the monster is dead now:
                if (c.getHealth() < 1) {
                    c.setDeadState();
                    scoreCounter++;
                }
            } else {
                i.remove();
            }
        }
    }
    
    
    //Helper Function - Reads the highscores file and updates the highScores set
    
    public void readHighScores() {
        
        try {
            FileReader f = new FileReader(HIGHSCORESPATH);
            BufferedReader r = new BufferedReader(f);
            
            String a = r.readLine();
            
            while (a != null) {
                String[] l = a.split("\\^");
                String username = l[0];
                int score = Integer.parseInt(l[1]);
                PlayerScore p = new PlayerScore(username, score);
                highScores.add(p);
                a = r.readLine();
            }

            r.close();
            f.close();
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }  catch (NumberFormatException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
    }
   
    /**
     * Reset the game to its initial state.
     */
    
    public void reset() {
        //1. Initialize our player, and the monster set
        player = new Player(COURT_WIDTH, COURT_HEIGHT);

        monsters = new TreeSet<Character>();
        
        playing = true;
        status.setText("Running...");
        
        //2. Update GUI statuses + Pane

        //health status
        String s = "Health: " + player.getHealth();
        health.setText(s);
        
        //score status
        scoreCounter = 0;
        String s2 = "Score: ";
        score.setText(s2);
        
        //Leaderboard text pane update
        
        String leaderboardString = leaderboardString();
        
        leaderboard.setText("Top 5 Highscores: \n" + leaderboardString);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    public void pause() {
        this.playing = false;
    }
    
    public void unpause() {
        this.playing = true;
    }
    
    //Helper Function - compiles a string of up to the top 5 high scores to display
    
    private String leaderboardString() {
        String output = "";
        int counter = 0;
        Iterator<PlayerScore> i = highScores.iterator();
        while (i.hasNext() && counter < 5) {
            PlayerScore p = i.next();
            output += p.getName() + ": " + p.getHighScore() + "\n";
            counter++;
        }
        return output;
    }
    
    //Helper IO Function - writes the full updated leaderboard into the highscore file
    
    private void writeHighScores() {
        try {
            File file = Paths.get(HIGHSCORESPATH).toFile();
            Writer fWriter = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fWriter); 
            
            Iterator<PlayerScore> i = highScores.iterator();
            while (i.hasNext()) {
                PlayerScore p = i.next();
                String output = p.getName() + "^" + p.getHighScore();
                bw.write(output);
                bw.newLine();
            }
            bw.close();
            
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    //Tick function
    
    void tick() {
        if (playing) {
            
            //1. Move the player
            player.move();
            
            //2. Handle all of the monsters including movement and inflictDamage

            handleMonsters();

            
            //3. Call spawn Monster function depending on the counter
            if (spawnCounter == 0) {
                spawnMonster();
                spawnCounter = SPAWNCOOLDOWN;
            } else {
                spawnCounter--;
            }
            
            //4. Decrement Player's attack cooldown counter (if greater than 0)
            player.decrementCoolDown();

            //5. End Game Conditions:
            if (player.getHealth() < 1) {
                playing = false;
                status.setText("Game Over");
            }
            
            //6. Update local high score
            currentHighScore = Math.max(scoreCounter, currentHighScore);
            
            //7. Add it to the leaderboard if its a new record
            if (currentHighScore > ps.getHighScore()) {
                highScores.remove(ps);
                ps = new PlayerScore(ps.getName(), currentHighScore);
                highScores.add(ps);
                
                //IO WRITING
                String leaderboardString = leaderboardString();
                
                leaderboard.setText("Top 5 Highscores: \n" + leaderboardString);

                writeHighScores();
            }
            
            //Update the labels
            
            health.setText(ps.getName() + "'s Health: " + player.getHealth());
            score.setText("Score: " + scoreCounter);

            // update the display
            repaint();
        }
    }
    
    private void drawMonsters(Graphics g) {
        Iterator<Character> i = monsters.iterator();
        while (i.hasNext()) {
            Character c = i.next();
            c.draw(g);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        try {
            
            //Draw the background
            BufferedImage img = ImageIO.read(new File(IMG_BD1));
            g.drawImage(img, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
             
            //Draw the monsters
            drawMonsters(g);
            //Draw the player
            player.draw(g);
             
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    
}