
import java.awt.Graphics;


/*
 * Character.java:
 * Summary: Abstract class template for all characters including the player and the monsters
 
-   Public abstract class
-   The foundation for all three characters, the player, Monster1 and Monster2
-   Very important fields include: 
    -   Position of character
    -   Character's Attack hitbox position
    -   Character's x and y velocities
    -   Character's attack state- is it attacking? True or False?
    -   etc.
*/
public abstract class Character implements Comparable<Character> {
    //Constant:
    //Maximum health of character
    
    private int maxHealth;
    
    //Fields
    /*Health*/
    private int health;
    
    /*Top-left coordinate position of Character*/
    private int px; 
    private int py;
    
    /* Size of character, in pixels. */
    private int width;
    private int height;

    /*Top-left coordinate position of Character's attack hit box*/
    /*the same as px and py when initialized*/
    private int atkX;
    private int atkY;
    
    /*Size of attack hit box*/
    
    //Varies depending on the direction of attack, and type of character, 
    //e.g. monster1 vs monster2
    
    private int atkWidth;
    private int atkHeight;
    
    //Attack damage
    
    private int damage;
    
    //Attack boolean- controls whether hitbox is active or not
    private boolean attacking = false;
    
    //Alive/Dead State boolean
    
    private boolean dead = false;
    
    /* Velocity: number of pixels to move every time move() is called. */
    private int vx;
    private int vy;
    
    /* 
     * Upper bounds of the area in which the object can be positioned. Maximum permissible x, y
     * positions for the upper-left hand corner of the object.
     */
    
    private int maxX;
    private int maxY;
    
    //Constructor
    
    public Character(int maxHealth, int health, int damage, int vx, int vy, int px, int py, 
            int width, int height, int atkWidth, int atkHeight, int courtWidth, int courtHeight) {
        
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.health = health;
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.atkWidth = atkWidth;
        this.atkHeight = atkHeight;
        this.atkX = px;
        this.atkY = py;
        this.width  = width;
        this.height = height;
            
            // take the width and height into account when setting the bounds for the upper left 
            // corner of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    //Methods
    
    /*** GETTERS **********************************************************************************/
    public int getHealth() {
        return this.health;
    }
    
    public boolean getAttackState() {
        return this.attacking;
    }
    
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getAtkX() {
        return this.atkX;
    }

    public int getAtkY() {
        return this.atkY;
    }
    
    public int getatkWidth() {
        return this.atkWidth;
    }
    
    public int getatkHeight() {
        return this.atkHeight;
    }
    
    public int getVx() {
        return this.vx;
    }
    
    public int getVy() {
        return this.vy;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }

    public boolean getDeadState() {
        return this.dead;
    }
    
    public int getDamage() {
        return this.damage;
    }
    
    /*** SETTERS **********************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }
    
    public void setAtkX(int atkX) {
        this.atkX = atkX;
        clip();
    }
    
    public void setAtkY(int atkY) {
        this.atkY = atkY;
        clip();
    }
    
    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public void setDeadState() {
        this.dead = true;
        this.setAttacking(false);
    }
    
    /*** UPDATES AND OTHER METHODS ****************************************************************/
    
    //updates px, py, atkX, atkY for move functions in subtypes
    public void updatePositions(int vx, int vy) {
        this.px += vx;
        this.py += vy;
        this.atkX += vx;
        this.atkY += vy;
        clip();
    }
    
    //this function updates health
    private void updateHealth(int change) {
        this.health = Math.min((Math.max(this.health + change, 0)), maxHealth);
    }
    
    //avoids out of boundary positions for both the character and the hitbox
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
        this.atkX = Math.min(Math.max(this.atkX, 0), this.maxX);
        this.atkY = Math.min(Math.max(this.atkY, 0), this.maxY);
    }
    
    /**
    Inflicts damage on another character if this character is attacking, and the other character
    is within range.
     */

    public boolean inflictDamage(Character that) {
        if (attacking && willAttack(that)) {
            that.updateHealth(-damage);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Checks whether another character is within range of our hitbox
     */
    
    public boolean willAttack(Character that) {
        return (this.atkX + this.atkWidth >= that.getPx()
                && this.atkY + this.atkHeight >= that.getPy()
                && that.getPx() + that.getWidth() >= this.atkX 
                && that.getPy() + that.getHeight() >= this.atkY);
    }
    
    
    public void bounce(Direction d) {
        if (d == null) {
            return;
        }
        
        switch (d) {
            case UP:
                this.vy = Math.abs(this.vy);
                break;  
            case DOWN:
                this.vy = -Math.abs(this.vy);
                break;
            case LEFT:
                this.vx = Math.abs(this.vx);
                break;
            case RIGHT:
                this.vx = -Math.abs(this.vx);
                break;
            default:
                break;
        }
    }
    
    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Direction of impending wall, null if all clear.
     */
    
    public Direction hitWall() {
        if (this.px + this.vx < 0) {
            return Direction.LEFT;
        } else if (this.px + this.vx > this.maxX) {
            return Direction.RIGHT;
        }

        if (this.py + this.vy < 0) {
            return Direction.UP;
        } else if (this.py + this.vy > this.maxY) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }
    
    /**
     * Returns the direction of another Character from this character in the next tick
     */
    
    public Direction relativeDir(Character that) {
        
        int dx = (that.px - this.px);
        int dy = (that.py - this.py);
//        double theta = Math.atan(dy/dx);
        if (dx > 0 && dy > 0) {
            return Direction.UPRIGHT;
        } else if (dx > 0 && dy < 0) {
            return Direction.DOWNRIGHT;
        } else if (dx < 0 && dy > 0) {
            return Direction.UPLEFT;
        } else if (dx < 0 && dy < 0) {
            return Direction.DOWNLEFT;
        } else {
            if (dx == 0) {
                if (dy == 0) {
                    return null;
                } else {
                    if (dy > 0) {
                        return Direction.UP;
                    } else {
                        return Direction.DOWN;
                    }
                }
            } else {
                if (dy == 0) {
                    if (dx > 0) {
                        return Direction.RIGHT;
                    } else {
                        return Direction.LEFT;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    //Comparable interface
    
    //Positive output, +1, if equal main fields, and negative, -1, otherwise
    
    @Override
    public int compareTo(Character that) {
        if (this.px == that.px && this.py == that.py && this.width == that.width &&
            this.height == that.height && this.damage == that.damage) {
            return 1;
        } else {
            return -1;
        }
    }
    
    
    
    //Abstract methods
    //move function will update both the position and the attack hitbox position
    //type of update depends on the subtype
    public abstract void move();
    
    //attack will activate the hitbox/shoot a projectile/etc., depends on the subtype
    public abstract void attack();
    
    //draw will use swing timer animations
    public abstract void draw(Graphics g);

}