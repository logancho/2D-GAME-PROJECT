import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/*
 * Monster1.java
    - Public class that implements Character
    - Similar to Player, except Monster1 is a type of NPC mob that moves 
      irrespective of key controls
    - Their sole function is to attack the player
 */
public class Monster1 extends Character {
    
    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;

    private static final int P_SIZE = 60;
    private static final int INTERVAL = 10;
    
    //monster1 has constant speed V
    private int v = 6;
    
    private Player player;
    
    //Images + Counter
    private static BufferedImage img;
    //Normal Image
    private static final String IMG_NORM = "files/enemy/Monster1/Normal.png";
    
    //Dead Images
    private static final String IMG_DEAD1 = "files/enemy/Monster1/Dead1.png";
    private static final String IMG_DEAD2 = "files/enemy/Monster1/Dead2.png";
    
    private int animationCounter = 0;
    
    //Dead boolean- tracks whether the enemy is dead or not
//    private boolean dead = false;
    
    public Monster1(int courtWidth, int courtHeight, Player player) {
        super(50, 50, 1, INIT_VEL_X, INIT_VEL_Y, 0, 0, P_SIZE, P_SIZE, 
                50, 50, courtWidth, courtHeight);
        
        //Proper random initialization of spawn position:
        //four possible pos: (0,0), (0, courtHeight), (courtWidth, 0),(courtWidth, courtHeight)
        //spawn position will be randomised via Math.random()
        
        Double r = Math.random();
        if (r <= 0.25) {
            this.setPx(0);
            this.setPy(0);
            //Do nothing, stick with (0,0) spawn
        } else if (r <= 0.5) {
            //(courtWidth, 0) spawn
            this.setPx(courtWidth);
            this.setPy(0);
            this.setAtkX(courtWidth);
            this.setAtkY(0);
        } else if (r <= 0.75) {
            //(0, courtHeight) spawn
            this.setPx(0);
            this.setPy(courtHeight);
            this.setAtkX(0);
            this.setAtkY(courtHeight);
        } else {
            //(courtWidth, courtHeight) spawn
            this.setPx(courtWidth);
            this.setPy(courtHeight);
            this.setAtkX(courtWidth);
            this.setAtkY(courtHeight);
        }
        
        //player
        this.player = player;
    }
    
    //setter function turns monsters dead
    
    //randomized movement that goes in the direction of the player
    public void move() {
        if (!(this.getDeadState())) {
            Direction d = this.relativeDir(this.player);
            
            int vX = 0;
            int vY = 0;
            
            if (d == Direction.UP) {
                vY = v;
            } else if (d == Direction.DOWN) {
                vY = -v;
            } else if (d == Direction.LEFT) {
                vX = -v;
            } else if (d == Direction.RIGHT) {
                vX = v;
            } else if (d == Direction.UPRIGHT) {
                vX = v;
                vY = v;
            } else if (d == Direction.UPLEFT) {
                vX = -v;
                vY = v;
            } else if (d == Direction.DOWNLEFT) {
                vX = -v;
                vY = -v;
            } else if (d == Direction.DOWNRIGHT) {
                vX = v;
                vY = -v;
            }
            
            this.updatePositions(vX, vY);
        }

    }
    
    public void attack() {
        this.setAttacking(true);
        
        this.setAtkX(this.getPx());
        this.setAtkY(this.getPy());
    }
    
    public void draw(Graphics g) {
        try {
            
            if (!(this.getDeadState())) {
                img = ImageIO.read(new File(IMG_NORM));
                g.drawImage(img, this.getPx(), this.getPy(), P_SIZE, 
                        P_SIZE, null);
            } else {
                //play the dead animation
                if (animationCounter <= INTERVAL) {
                    img = ImageIO.read(new File(IMG_DEAD1));
                    animationCounter++;
                    g.drawImage(img, this.getPx(), this.getPy(), P_SIZE, 
                            P_SIZE, null);
                } else if (animationCounter <= INTERVAL * 2) {
                    img = ImageIO.read(new File(IMG_DEAD2));
                    animationCounter++;
                    g.drawImage(img, this.getPx(), this.getPy(), P_SIZE, 
                            P_SIZE, null);
                    //no need to reset the counter because every enemy only dies once
                }
            }
            
            
            //Hitbox drawing
            
//            g.fillRect(this.getAtkX(), this.getAtkY(), this.getatkWidth(), this.getatkHeight());
            
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

    }
}