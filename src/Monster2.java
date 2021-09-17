import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/*
 * Monster2.java
    - Same as Monster1 but simply with different move and draw methods.
*/
public class Monster2 extends Character {
    
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    public static final int P_SIZE = 60;
    public static final int INTERVAL = 10;
    
    //Images + Counter
    private static BufferedImage img;
    //Normal Image
    public static final String IMG_NORM = "files/enemy/Monster2/Normal.png";
    
    //Dead Images
    public static final String IMG_DEAD1 = "files/enemy/Monster2/Dead1.png";
    public static final String IMG_DEAD2 = "files/enemy/Monster2/Dead2.png";
    
    private int animationCounter = 0;
    
  //Random number generator tool
    Random rand;
    
    //Dead boolean- tracks whether the enemy is dead or not
//    private boolean dead = false;
    
    public Monster2(int courtWidth, int courtHeight, Player player) {
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
        
        //Random velocities
        rand = new Random();
        int vX = rand.nextInt(10) + 1;
        int vY = rand.nextInt(10) + 1;

        this.setVx(vX);
        this.setVy(vY);

    }
    
    //setter function turns monsters dead
    
    //randomized movement and bounces off the walls
    
    public void move() {
        
        this.updatePositions(this.getVx(), this.getVy());
        this.bounce(this.hitWall());
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