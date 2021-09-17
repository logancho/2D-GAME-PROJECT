import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;



//Hit box tests! 

//For my testing component, I used JUnit tests to do comprehensive tests on my core attack system
//which uses an attack hitbox position with atkwidth and atkheight and a boolean that checks 
//whether the hitbox is active or not.

public class HitboxTest {
    
    //Test 1
    @Test
    public void willAttackNormalTest() {
        Character p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        int atkWidth = p.getatkWidth();
        int atkHeight = p.getatkHeight();
        
        //Choosing a position for q that will DEFINITELY be within p's hitbox
        int qx = (atkWidth) / 2;
        int qy = (atkHeight) / 2;
        
        Character q = new Player(1000, 1000);
        q.setPx(qx);
        q.setPy(qy);
        
        assertTrue(p.willAttack(q));
    }

    //Test 2
    @Test
    public void willAttackConertest1() {
        Character p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        int atkWidth = p.getatkWidth();
        int atkHeight = p.getatkHeight();
        
        //Bottom right corner of p's hitbox
        int qx = atkWidth;
        int qy = atkHeight;
        
        Character q = new Player(1000, 1000);
        q.setPx(qx);
        q.setPy(qy);
        
        assertTrue(p.willAttack(q));
    }
    
    //Test 3
    @Test
    public void willAttackConertest2() {
        Character p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        int atkWidth = p.getatkWidth();
        
        //Top right corner of p's hitbox
        int qx = atkWidth;
        int qy = 0;
        
        Character q = new Player(1000, 1000);
        q.setPx(qx);
        q.setPy(qy);
        
        assertTrue(p.willAttack(q));
    }

    //Test 4
    @Test
    public void willAttackConertest3() {
        Character p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        
        //Top left corner of p's hitbox
        int qx = 0;
        int qy = 0;
        
        Character q = new Player(1000, 1000);
        q.setPx(qx);
        q.setPy(qy);
        
        assertTrue(p.willAttack(q));
    }

    //Test 5
    @Test
    public void willAttackConertest4() {
        Character p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        int atkHeight = p.getatkHeight();
        
        //Bottom left corner of p's hitbox
        int qx = 0;
        int qy = atkHeight;
        
        Character q = new Player(1000, 1000);
        q.setPx(qx);
        q.setPy(qy);
        
        assertTrue(p.willAttack(q));
    }
    
    //Test 3
    @Test
    public void playerMonster1WillAttackTest() {
        Player p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        int atkWidth = p.getatkWidth();
        int atkHeight = p.getatkHeight();
        
        //Bottom right corner of p's hitbox
        int qx = atkWidth;
        int qy = atkHeight;
        
        //Initiating a monster1 to attack
        Monster1 q = new Monster1(1000, 1000, p);
        
        q.setPx(qx);
        q.setPy(qy);
        
        assertTrue(p.willAttack(q));
    }
    
    //Test 4
    @Test
    public void monster2WillAttackPlayerTest() {
        Player p = new Player(1000, 1000);
        
        //Initiating a monster1 to attack
        Monster1 q = new Monster1(1000, 1000, p);
        q.setAtkX(0);
        q.setAtkY(0);
        
        int atkWidth = q.getatkWidth();
        int atkHeight = q.getatkHeight();
        
        //Bottom right corner of p's hitbox
        int px = atkWidth;
        int py = atkHeight;

        p.setPx(px);
        p.setPy(py);
        
        assertTrue(q.willAttack(p));
    }
    
    
    //Test 5
    @Test
    public void playerOnMonsterinflictDamageTest() {
        Player p = new Player(1000, 1000);

        p.setAtkX(0);
        p.setAtkY(0);
        
        p.setAttacking(true);
        
        int atkWidth = p.getatkWidth();
        int atkHeight = p.getatkHeight();
        
        //Bottom right corner of p's hitbox
        int qx = atkWidth;
        int qy = atkHeight;
        
        Monster1 q = new Monster1(1000, 1000, p);
        q.setPx(qx);
        q.setPy(qy);
        int expectedHealth = q.getHealth() - p.getDamage();
        
        assertTrue(p.inflictDamage(q));
        assertEquals(q.getHealth(), expectedHealth);
    }


    //Test 6
    @Test
    public void monsterOnPlayerinflictDamageTest() {
        Player p = new Player(1000, 1000);
        Monster1 q = new Monster1(1000, 1000, p);
        
        
        q.setAtkX(0);
        q.setAtkY(0);  
        q.setAttacking(true);
        
        int atkWidth = q.getatkWidth();
        int atkHeight = q.getatkHeight();
        
        //Bottom right corner of p's hitbox
        int px = atkWidth;
        int py = atkHeight;
        
        
        p.setPx(px);
        p.setPy(py);
        
        int expectedHealth = p.getHealth() - q.getDamage();
        
        assertTrue(q.inflictDamage(p));
        assertEquals(p.getHealth(), expectedHealth);
    }
}


