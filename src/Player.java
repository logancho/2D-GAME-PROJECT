import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
Player.java:
-   Public class that implements Character
-   This is the class for the player's character that moves and attacks via keypresses
-   Perhaps the most important class
-   Uses inheritance to define special move, attack and draw methods specific to the player
-   The draw() method uses fields I've dubbed animation counters, specifically animationCounter
    attackCounter in order to have sprite animations for run cycles, idle state and 
    attack animations. To better explain this, I used if-else statements with the counters
    to determine which frame of animation should be displayed, and for how long, incrementing/
    resetting the counters in the process.
*/

public class Player extends Character {
    private static final int INIT_POS_X = 500;
    private static final int INIT_POS_Y = 200;
    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    
    private static final int P_SIZE = 90;
    private static final int INTERVAL = 3;
    private static final int COOLDOWN = 0;
    private static final int DAMAGE = 6;
    private static final int MAXHEALTH = 150;
    
    //Images
    private static BufferedImage img;
    
    //Idle Images
    
    private static final String IMG_IDOWN = "files/player/idle/IDown.png";
    private static final String IMG_IUP = "files/player/idle/IUp.png";
    private static final String IMG_ILEFT = "files/player/idle/ILeft.png";
    private static final String IMG_IRIGHT = "files/player/idle/IRight.PNG";
    
    //Movement run cycles
    //Left
    private static final String IMG_RL1 = "files/player/left/RL1.png";
    private static final String IMG_RL2 = "files/player/left/RL2.png";
    private static final String IMG_RL3 = "files/player/left/RL3.png";
    private static final String IMG_RL4 = "files/player/left/RL4.png";
    
    //Right
    private static final String IMG_RR1 = "files/player/right/RR1.png";
    private static final String IMG_RR2 = "files/player/right/RR2.png";
    private static final String IMG_RR3 = "files/player/right/RR3.png";
    private static final String IMG_RR4 = "files/player/right/RR4.png";
    
    //Down
    private static final String IMG_RD1 = "files/player/down/RD1.png";
    private static final String IMG_RD2 = "files/player/down/RD2.png";
    private static final String IMG_RD3 = "files/player/down/RD3.png";
    private static final String IMG_RD4 = "files/player/down/RD4.png";
    
    //Up
    private static final String IMG_RU1 = "files/player/up/RU1.png";
    private static final String IMG_RU2 = "files/player/up/RU2.png";
    private static final String IMG_RU3 = "files/player/up/RU3.png";
    private static final String IMG_RU4 = "files/player/up/RU4.png";
    
    
    //Attack cycles
    //Down
    private static final String IMG_DA1 = "files/player/downAttack/DA1.png";
    private static final String IMG_DA2 = "files/player/downAttack/DA2.png";
    private static final String IMG_DA3 = "files/player/downAttack/DA3.png";
    private static final String IMG_DA4 = "files/player/downAttack/DA4.png";
    private static final String IMG_DA5 = "files/player/downAttack/DA5.png";
    //Up
    private static final String IMG_UA1 = "files/player/upAttack/UA1.png";
    private static final String IMG_UA2 = "files/player/upAttack/UA2.png";
    private static final String IMG_UA3 = "files/player/upAttack/UA3.png";
    private static final String IMG_UA4 = "files/player/upAttack/UA4.png";
    private static final String IMG_UA5 = "files/player/upAttack/UA5.png";
    //Left
    private static final String IMG_LA1 = "files/player/leftAttack/LA1.png";
    private static final String IMG_LA2 = "files/player/leftAttack/LA2.png";
    private static final String IMG_LA3 = "files/player/leftAttack/LA3.png";
    private static final String IMG_LA4 = "files/player/leftAttack/LA4.png";
    private static final String IMG_LA5 = "files/player/leftAttack/LA5.png";
    //Right
    private static final String IMG_RA1 = "files/player/rightAttack/RA1.png";
    private static final String IMG_RA2 = "files/player/rightAttack/RA2.png";
    private static final String IMG_RA3 = "files/player/rightAttack/RA3.png";
    private static final String IMG_RA4 = "files/player/rightAttack/RA4.png";
    private static final String IMG_RA5 = "files/player/rightAttack/RA5.png";

    //Swing Timer Counters:
    
    //Image loop counter
    private int animationCounter = 0;
    
    //attack loop counter
    private int attackCounter = 0;
    
    //attack cooldown counter
    private int attackCoolDown = 0;
    
    //Direction Record
    private Direction dir;

    public Player(int courtWidth, int courtHeight) {
        //int maxHealth, int health, int damage, int vx, int vy, int px, int py, 
        //int width, int height, int atkWidth, int atkHeight, int courtWidth, int courtHeight
        
        super(MAXHEALTH, MAXHEALTH, DAMAGE, INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, 
                P_SIZE, P_SIZE, P_SIZE, P_SIZE, courtWidth, courtHeight);
    }
    
    /*
     * Evaluates whether we are currently moving and returns a direction if we are
     * */
    
    private Direction currentDir() {
        if (this.getVx() != 0 || this.getVy() != 0) {
            if (this.getVx() < 0) {
                return Direction.LEFT;
            } else if (this.getVx() > 0) {
                return Direction.RIGHT;
            } else if (this.getVy() < 0) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        } else {
            return null;
        }
    }
    
    public int getCoolDown() {
        return this.attackCoolDown;
    }
    
    public void decrementCoolDown() {
        if (attackCoolDown > 0) {
            this.attackCoolDown--;
        }
    }
    
    public void resetAttackCounter() {
        attackCounter = 0;
        attackCoolDown = 0;
    }
    
    public void move() {
        int vX = this.getVx();
        int vY = this.getVy();
        this.updatePositions(vX, vY);
    }
    
    //attack method positions, and activates the hitbox based on the player's direction
    
    public void attack() {
        //make sure to activate the hitbox
        this.setAttacking(true);
        
        int px = this.getPx();
        int py = this.getPy();
        int width = this.getatkWidth();
        int height = this.getatkHeight();
        
        Direction cur = currentDir();
        
        if (cur != null) {
            if (cur == Direction.UP) {
                this.setAtkX(px);
                this.setAtkY(py - height);
            } else if (cur == Direction.DOWN) {
                this.setAtkX(px);
                this.setAtkY(py + height);
            } else if (cur == Direction.LEFT) {
                this.setAtkX(px - width);
                this.setAtkY(py);
            } else if (cur == Direction.RIGHT) {
                this.setAtkX(px + width);
                this.setAtkY(py);
            } else {
                this.setAtkX(px);
                this.setAtkY(py + height);
            }
        } else {
            if (dir == Direction.UP) {
                this.setAtkX(px);
                this.setAtkY(py - height);
            } else if (dir == Direction.DOWN) {
                this.setAtkX(px);
                this.setAtkY(py + height);
            } else if (dir == Direction.LEFT) {
                this.setAtkX(px - width);
                this.setAtkY(py);
            } else if (dir == Direction.RIGHT) {
                this.setAtkX(px + width);
                this.setAtkY(py);
            } else {
                this.setAtkX(px);
                this.setAtkY(py + height);
            }
        }
        //position the hitbox's top left corner accordingly to the direction we are attacking in
        
    }
    
    public void draw(Graphics g) {
        try {
            //if we aren't attacking
            if (!this.getAttackState()) {
                //If we are moving currently
                
                /*** RUN CYCLES ******************************************************************/ 
                if (currentDir() != null) {
                    dir = currentDir();
                    if (dir == Direction.UP) {
                        if (animationCounter <= INTERVAL) {
                            img = ImageIO.read(new File(IMG_RU1));
                            animationCounter++;
                        } else if (animationCounter <= INTERVAL * 2) {
                            img = ImageIO.read(new File(IMG_RU2));
                            animationCounter++;
                        } else if (animationCounter <= INTERVAL * 3) {
                            img = ImageIO.read(new File(IMG_RU3));
                            animationCounter++;
                        } else {
                            img = ImageIO.read(new File(IMG_RU4));
                            if (animationCounter <= INTERVAL * 4) {
                                animationCounter++;
                            } else {
                                animationCounter = 0;
                            }
                        } 
                        g.drawImage(img, this.getPx() + 21, this.getPy(), 45, 
                                90, null);
                        
                    } else if (dir == Direction.DOWN) {
                        if (animationCounter <= INTERVAL) {
                            img = ImageIO.read(new File(IMG_RD1));
                            animationCounter++;
                        } else if (animationCounter <= INTERVAL * 2) {
                            img = ImageIO.read(new File(IMG_RD2));
                            animationCounter++;
                        } else if (animationCounter <= INTERVAL * 3) {
                            img = ImageIO.read(new File(IMG_RD3));
                            animationCounter++;
                        } else {
                            img = ImageIO.read(new File(IMG_RD4));
                            if (animationCounter <= INTERVAL * 4) {
                                animationCounter++;
                            } else {
                                animationCounter = 0;
                            }
                        } 
                        g.drawImage(img, this.getPx() + 4, this.getPy(), 78, 
                                90, null);
                        
                    } else if (dir == Direction.LEFT) {
                        if (animationCounter <= INTERVAL) {
                            img = ImageIO.read(new File(IMG_RL1));
                            animationCounter++;

                        } else if (animationCounter <= INTERVAL * 2) {
                            img = ImageIO.read(new File(IMG_RL2));
                            animationCounter++;

                        } else if (animationCounter <= INTERVAL * 3) {
                            img = ImageIO.read(new File(IMG_RL3));
                            animationCounter++;

                        } else {
                            img = ImageIO.read(new File(IMG_RL4));

                            if (animationCounter <= INTERVAL * 4) {
                                animationCounter++;
                            } else {
                                animationCounter = 0;
                            }
                        }
                        g.drawImage(img, this.getPx() + 7, this.getPy() + 6, 67, 
                                90, null);

                        
                    } else if (dir == Direction.RIGHT) {
                        if (animationCounter <= INTERVAL) {
                            img = ImageIO.read(new File(IMG_RR1));
                            animationCounter++;
                        } else if (animationCounter <= INTERVAL * 2) {
                            img = ImageIO.read(new File(IMG_RR2));
                            animationCounter++;
                        } else if (animationCounter <= INTERVAL * 3) {
                            img = ImageIO.read(new File(IMG_RR3));
                            animationCounter++;
                        } else {
                            img = ImageIO.read(new File(IMG_RR4));
                            if (animationCounter <= INTERVAL * 4) {
                                animationCounter++;
                            } else {
                                animationCounter = 0;
                            }
                        }
                        g.drawImage(img, this.getPx() + 4, this.getPy() + 6, 67, 
                                90, null);
                    }
                } else {
                    /*** IDLE IMAGES *************************************************************/ 
                    if (dir == Direction.UP) {
                        img = ImageIO.read(new File(IMG_IUP));
                        
                        g.drawImage(img, this.getPx() + 18, this.getPy(), 50, 
                                90, null);
                        
                    } else if (dir == Direction.DOWN) {
                        img = ImageIO.read(new File(IMG_IDOWN));
                        //100, 160
                        g.drawImage(img, this.getPx() + 18, this.getPy(), 56, 
                                98, null);
                    } else if (dir == Direction.LEFT) {
                        img = ImageIO.read(new File(IMG_ILEFT));
                        
                        g.drawImage(img, this.getPx() + 18, this.getPy(), 45, 
                                101, null);
                    } else if (dir == Direction.RIGHT) {
                        img = ImageIO.read(new File(IMG_IRIGHT));
                        
                        g.drawImage(img, this.getPx() + 18, this.getPy(), 45, 
                                101, null);
                    } else {
                        //Down by default
                        img = ImageIO.read(new File(IMG_IDOWN));
                        //100, 160
                        g.drawImage(img, this.getPx() + 18, this.getPy(), 56, 
                                98, null);
                    }
  
                    
                }

            } else {
                /*** ATTACK ANIMATIONS *****************************************************/      
                if (dir == Direction.UP) {
                    if (attackCounter <= INTERVAL) {
                        img = ImageIO.read(new File(IMG_UA1));
                        attackCounter++;
                        g.drawImage(img, this.getPx() + 6, this.getPy(), 67, 
                                112, null);
                    } else if (attackCounter <= INTERVAL * 2) {
                        img = ImageIO.read(new File(IMG_UA2));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 3, this.getPy() - 11, 90, 
                                123, null);
                    } else if (attackCounter <= INTERVAL * 3) {
                        img = ImageIO.read(new File(IMG_UA3));
                        attackCounter++;
                        
                        g.drawImage(img, this.getPx() - 6, this.getPy() - 11, 123, 
                                129, null);
                        this.setVy(-15);
                    } else if (attackCounter <= INTERVAL * 3 + 1) {
                        img = ImageIO.read(new File(IMG_UA4));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 60, this.getPy(), 180, 
                                130, null);
                    } else {
                        this.setVy(0);
                        img = ImageIO.read(new File(IMG_UA5));
                        attackCounter++;
                        
                        g.drawImage(img, this.getPx(), this.getPy(), 90, 
                                100, null);
                        if (attackCounter <= INTERVAL * 6) {
                            attackCounter++;
                        } else {
                            attackCounter = 0;
                            attackCoolDown = COOLDOWN;
                            this.setAttacking(false);
                        }
                    } 
                    
                } else if (dir == Direction.DOWN) {
                    if (attackCounter <= INTERVAL) {
                        img = ImageIO.read(new File(IMG_DA1));
                        attackCounter++;
                        g.drawImage(img, this.getPx() + 6, this.getPy(), 67, 
                                112, null);
                    } else if (attackCounter <= INTERVAL * 2) {
                        img = ImageIO.read(new File(IMG_DA2));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 3, this.getPy() - 11, 90, 
                                123, null);
                    } else if (attackCounter <= INTERVAL * 3) {
                        img = ImageIO.read(new File(IMG_DA3));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 6, this.getPy() - 11, 123, 
                                129, null);
                        this.setVy(25);
                    } else if (attackCounter <= INTERVAL * 3 + 1) {
                        img = ImageIO.read(new File(IMG_DA4));
                        attackCounter++;
                        
                        g.drawImage(img, this.getPx() - 45, this.getPy() - 11, 202, 
                                157, null);
                    } else {
                        img = ImageIO.read(new File(IMG_DA5));
                        this.setVy(0);
                        g.drawImage(img, this.getPx() + 10, this.getPy(), 90, 
                                100, null);
                        if (attackCounter <= INTERVAL * 6) {
                            attackCounter++;
                        } else {
                            attackCounter = 0;
                            attackCoolDown = COOLDOWN;
                            this.setAttacking(false);
                        }
                    }
                    
                } else if (dir == Direction.LEFT) {
                    if (attackCounter <= INTERVAL) {
                        img = ImageIO.read(new File(IMG_LA1));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 6, this.getPy() - 7, 67, 
                                112, null);
                    } else if (attackCounter <= INTERVAL * 2) {
                        img = ImageIO.read(new File(IMG_LA2));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 28, this.getPy() - 18, 90, 
                                123, null);
                    } else if (attackCounter <= INTERVAL * 3) {
                        img = ImageIO.read(new File(IMG_LA3));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 56, this.getPy() - 18, 123, 
                                129, null);
                        this.setVx(-15);
                    } else if (attackCounter <= INTERVAL * 3 + 1) {
                        img = ImageIO.read(new File(IMG_LA4));
                        attackCounter++;
                        
                        g.drawImage(img, this.getPx() - 70, this.getPy() - 40, 162, 
                                146, null);
                    } else {
                        img = ImageIO.read(new File(IMG_LA5));
                        this.setVx(0);
                        g.drawImage(img, this.getPx() - 16, this.getPy(), 90, 
                                100, null);
                        if (attackCounter <= INTERVAL * 6) {
                            attackCounter++;
                        } else {
                            attackCounter = 0;
                            attackCoolDown = COOLDOWN;
                            this.setAttacking(false);
                        }
                    }
                    
                    
                } else if (dir == Direction.RIGHT) {
                    if (attackCounter <= INTERVAL) {
                        img = ImageIO.read(new File(IMG_RA1));
                        attackCounter++;
                        g.drawImage(img, this.getPx() + 6, this.getPy(), 67, 
                                112, null);
                    } else if (attackCounter <= INTERVAL * 2) {
                        img = ImageIO.read(new File(IMG_RA2));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 3, this.getPy() - 11, 90, 
                                123, null);
                    } else if (attackCounter <= INTERVAL * 3) {
                        img = ImageIO.read(new File(IMG_RA3));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 6, this.getPy() - 11, 123, 
                                129, null);
                        this.setVx(10);
                    } else if (attackCounter <= INTERVAL * 3 + 1) {
                        img = ImageIO.read(new File(IMG_RA4));
                        attackCounter++;
                        g.drawImage(img, this.getPx() + 11, this.getPy() - 40, 162, 
                                146, null);
                        
                    } else {
                        img = ImageIO.read(new File(IMG_RA5));
                        this.setVx(0);
                        g.drawImage(img, this.getPx() + 8, this.getPy() + 8, 90, 
                                100, null);
                        if (attackCounter <= INTERVAL * 6) {
                            attackCounter++;
                        } else {
                            attackCounter = 0;
                            attackCoolDown = COOLDOWN;
                            this.setAttacking(false);
                        }
                    }
                    
                } else {
                    //down attack by default
                    if (attackCounter <= INTERVAL) {
                        img = ImageIO.read(new File(IMG_DA1));
                        attackCounter++;
                        g.drawImage(img, this.getPx() + 6, this.getPy(), 67, 
                                112, null);
                    } else if (attackCounter <= INTERVAL * 2) {
                        img = ImageIO.read(new File(IMG_DA2));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 3, this.getPy() - 11, 90, 
                                123, null);
                    } else if (attackCounter <= INTERVAL * 3) {
                        img = ImageIO.read(new File(IMG_DA3));
                        attackCounter++;
                        g.drawImage(img, this.getPx() - 6, this.getPy() - 11, 123, 
                                129, null);
                        this.setVy(60);
                    } else if (attackCounter <= INTERVAL * 3 + 1) {
                        img = ImageIO.read(new File(IMG_DA4));
                        attackCounter++;
                        
                        g.drawImage(img, this.getPx() - 45, this.getPy() - 11, 202, 
                                157, null);
                    } else {
                        img = ImageIO.read(new File(IMG_DA5));
                        this.setVy(0);
                        g.drawImage(img, this.getPx() + 10, this.getPy(), 90, 
                                100, null);
                        if (attackCounter <= INTERVAL * 6) {
                            attackCounter++;
                        } else {
                            attackCounter = 0;
                            attackCoolDown = COOLDOWN;
                            this.setAttacking(false);
                        }
                    }
                }
                //HITBOX DRAWING!!!
                //Successful!
//                g.fillRect(this.getAtkX(), this.getAtkY(), this.getatkWidth(), 
//                        this.getatkHeight());
            }
            //Damagable size DRAWING!!!
            
//            g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
            
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
}