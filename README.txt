=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

Instructions for playing:

1. Run Game.java
2. Enjoy : )

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Inheritance/subtyping

I used Inheritance/subtyping with my Character abstract class which is the superclass to the three
subtypes, Player, Monster1 and Monster2. This is an approriate use of subtyping because while the
three subtypes share very much in common, they differ significantly in the way that they move,
attack and are drawn. As such, the move, attack and draw methods are the only abstract 
methods in the class. 

For instance, the Player's attack method is entirely different to that of the Monsters. For the 
Player, the attack method updates the position of the top-left corner of the hitbox to one of four
possible locations, up, down, left or right of the player. This is integral because I wanted attacks
to be in the same direction as wherever the player is facing.

As for Monsters Monster1 and Monster2, their attack methods are different as their hitbox is 
actually their bodies themselves-meaning if the player just comes in contact with them, they will
take damage.

In addition, with the move method, the Player's movement is the most simple, relying on key strokes 
which update it's x and y velocities. 

Monster1 has an entirely different move method, independent of keystrokes,
and using a 'relativeDir' method in the Character class to find the direction between itself and 
the player, which it then uses to get closer to the player.

Monster2 again has an entirely different move method which relies on the bounce and hitwall methods
as well as a randomly generated velocity to move erratically across the map.

More importantly, by subtyping the monsters as Characters, I was able to store them as a set of 
Characters in my GameCourt class- which wouldn't have been possible were they not within the 
same hierarchy.

  2. Appropriately modeling state using collections

I used a TreeSet to store all monsters in the game. This is handled entirely within the GameCourt
class as the collection of monsters is only relevant when the game is active. 

I used a TreeSet because I needed a data structure that I could efficiently add monsters into, when 
spawning new monsters at regular intervals throughout the game, and also remove monsters that have
0 health. Sets are great for insertion and removal. I didn't use a LinkedList because I had no need
to keep track of any type of head/tail, and I didn't need to keep track of any type of key with
the monsters and so I obviously didn't need to use a map.

To elaborate, the method spawnMonster() adds a new monster to the set if the Monster spawn cooldown,
(A counter that spaces out spawns such that they aren't spawned at every tick()), is 0, and the 
handleMonsters() function uses an iterator to go through the set of monsters and call .move() and 
.inflictDamage(player) on all of them!!

Finally, I also use a drawMonsters() method which similarly iterates through the set on a repaint 
call and calls .draw(Graphics g) on all of the existing monsters.

  3. File I/O: using I/O to parse a novel file format

I use File I/O to read the high scores of all past players of the game and write to the file to
update it with the high score of the current player. To be more specific, the file in the Files 
folder, "Highscores.txt", stores the usernames and highscores of all past users in the format:
username^score, where username is a string the current player enters when running the application,
and score is their highest score. Each username is listed on a different line.

I used a bufferedReader r in my readHighScores() method where I looped through the file, handling
IOExceptions via. a try-catch format, such that if r.readLine() is not null, that means we can
split it with the regular expression I have, in my case, "\\^" because I am using the "^" as a
divider between username and score. E.g. "loganCho^78" would be a possible string read by the 
reader. I use the String.split() method to split this into a username and a score- and then convert
the score into an integer using the Integer.parseInt() method and appropriately handle 
NumberFormatExceptions via a try-catch format. 

With the string username and int score of a past player, I then store them as a new object which 
I've dubbed, PlayerScore. PlayerScore is a class that has fields username and highScore, and 
appropriate get and set methods. To elaborate, PlayerScore's constructor takes in a String username 
and int score. 

More importantly, PlayerScore implements the Comparable interface,
meaning I can store them in a set. I implemented the compareTo method such return the negative value 
of the Integer comparison of their high scores. If they were the same score, then it would return
the String.compareTo of their usernames. Negative integer comparison, because I know that sets are 
naturally in ascending order, but I want to store high scores in descending order. 

Hence, after instantiating a new PlayerScore object with a read username and score from the 
highscores txt file, I then add it to a TreeSet of PlayerScores called highscores.

For reference, "private Set<PlayerScore> highScores;" GameCourt.java line 56.

The best part about the set collection here is that I can store PlayerScores in descending order
of scores, meaning that if I want to display the 5 greatest high scores and their corresponding
usernames, I can simply display the 5 first elements of the set at any given moment. 

As for the current player, in the GameCourt constructor, a new PlayerScore with their username
and a score of 0 is initialized and stored as a field.

I use a counter, scoreCounter, and a local highscore int, currentHighScore, 
to keep track of their high score in the current application. If they reach
a new highScore, that is, if the counter, scoreCounter, exceeds currentHighScore at any time,
then I remove the current player's PlayerScore from the set (stored as a field) and then create 
and add a new PlayerScore with the same username but a different new higher score. The reason
for this is because it maintains the descending order of all PlayerScores, including the
current player, meaning that I can most accurately update the leaderboard of high scores in 
real-time.

Finally, as for IO Writing, my method, writeHighScores(), uses a BufferedWriter by iterating
through highscores (the set) starting from the highest scoring PlayerScore, and uses the methods
bw.write() and bw.newLine() as well as string concatenation, + "^" +, to write the current updated 
state of the highscore leaderboard into the Highscores.txt file. I am replacing the contents
of the file, and hence, the append boolean of my writer is set to false.

Note:

Usernames will never have the character "^" because I ensure in my Game.java file when using 
showInputDialog to receive a username from the player, that if their username contains "^", they
must reenter a new one until they enter one that does not contain "^".

  4. Using JUnit on a testable component of your game

The core component of my game that I test is the hitbox collisions. Characters, including the 
player, Monster1 and Monster2 all have a hitbox position field stored as atkX and atkY, which 
are the coordinates of the top-left corner of their hitbox. The width and height of the 
hitbox are stored as atkWidth and atkHeight.

In addition, they have a boolean attacking, which activates/deactivates the hitbox. 

All of these fields are crucial for the character methods, willAttack(Character that) and 
inflictDamage(Character that). willAttack checks for whether the attack hitbox of the 
"this" Character intersects another Character, that, regardless of whether the hitbox is active
or not. inflictDamage checks for whether willAttack is possible, and whether the hitbox is active,
to then alter the health field of the that Character.

My comprehensive test cases focus on the intersecting nature of hitboxes by testing all four corner
edge cases for willAttack and also testing between all three types of Characters, primarily 
through assertTrue, because willAttack and inflictDamage are actually boolean methods that
return true if a successful attack is made/possible. In addition,health decrements are tested 
through AssertEquals.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Character.java:

-	Public abstract class
-	The foundation for all three characters, the player, Monster1 and Monster2
-	Very important fields include: 
	-	Position of character
	-	Character's Attack hitbox position
	-	Character's x and y velocities
	-	Character's attack state- is it attacking? True or False?
	-	etc.

Player.java:

-	Public class that implements Character
-	This is the class for the player's character that moves and attacks via keypresses
-	Perhaps the most important class
-	Uses inheritance to define special move, attack and draw methods specific to the player
-	The draw() method uses fields I've dubbed animation counters, specifically animationCounter
	attackCounter in order to have sprite animations for run cycles, idle state and 
	attack animations. To better explain this, I used if-else statements with the counters
	to determine which frame of animation should be displayed, and for how long, incrementing/
	resetting the counters in the process.
	
Monster1.java

- Public class that implements Character
- Similar to Player, except Monster1 is a type of NPC mob that moves irrespective of key controls
- Their sole function is to attack the player

Monster2.java

- Same as Monster1 but simply with different move and draw methods.

Direction.java

- Stores an enum called Direction - I took this from the Mushroom of Doom game example because I 
- thought it would be really useful. I added four new directions, UPLEFT, UPRIGHT, DOWNLEFT, 
  DOWNRIGHT which I used in my Character - relativeDirection method.
  
PlayerScore.java

- Public class that implements the Comparable interface
- Used for storing pairs of Usernames and Highscores
- Explained in the IO component, but this class' main function is to allow for the 
- storage of username, highscore pairs in descending order from largest to smallest.

GameCourt.java

- Public class that extends JPanel
- "holds the primary game logic for how different objects interact with one another." 
- endstate: if the player's health reaches 0 - it's game over.

- Main method:

tick() - 
	In this method, which is called at every INTERVAL of my swing timer, 
	
	I call move on the player and all existing monsters, I check to see if the player's current 
	score has exceeded their local highscore and proceed to update the highscores set if yes, and
	also write to the highscores file the new updated highscore leaderboard.
	
	I also call draw on all Characters including the player.
	
	I also decrement the Player's attack Cooldown, which is a counter that adds space between
	the player's attacks such that they have to wait a (very short) bit before launching their
	next attack.

Game.java
-	Main class that specifies the frame and widgets of the GUI

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

	Debugging wise, I found the IO component the most difficult initially, however, overall, 
	I felt my decision to use subtyping made things a lot more convenient when designing the game.
	
	I think the animations might have been the greatest struggle overall because of the way that
	draw() relies on the top-left corner as its position, I had to control the custom sizes of all
	sprite images to sync them up in the correct places and with the correct dimensions.
	
- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

	Private state is relatively well encapsulated I believe as the fields of all my characters are 
	private and the only way to access them is through get/set methods. Same applies to GameCourt.
	
	I would perhaps try to improve the refactoring of some of the methods in my 
	Character classes if I had another chance.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

  All of the sprite images used in my game, including the player animations and the monsters, 
  are my own original drawings, including the background.
  