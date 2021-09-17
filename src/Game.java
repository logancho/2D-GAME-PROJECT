import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */

public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        
        final JFrame frame = new JFrame("THE TRIAL");
        
        frame.setLocation(0, 0);
        
        String username = JOptionPane.showInputDialog(frame, "Welcome to *_>T h e  T r i a l<_*\n"
                + "Please enter a username without the character, '^'", 
                "Username", JOptionPane.PLAIN_MESSAGE);
        
        if (username == null) {
            System.exit(0);
        }
        
        while (username.contains("^")) {
            username = JOptionPane.showInputDialog(frame, "Sorry, that username contains '^'."
                    + " Please try again:", 
                    "Username", JOptionPane.PLAIN_MESSAGE);
        }
        
        /******************PANELS******************************************************************/
        
        // Status Panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        //HighScore Panel
        final JPanel highscore_panel = new JPanel();
        frame.add(highscore_panel, BorderLayout.EAST);

        //Highscore leaderboard textpane
        
        final JTextPane leaderboard = new JTextPane();
        leaderboard.setEditable(false);
        
        leaderboard.setText("okay\n");
        highscore_panel.add(leaderboard);

        //////Status Labels//////
        
        //Health Label
        final JLabel health = new JLabel("Health: ");
        status_panel.add(health);
        //
        
        //Score Label
        final JLabel score = new JLabel("Score: ");
        status_panel.add(score);
        
        /////// Main playing area//////
        final GameCourt court = new GameCourt(status, health, score, leaderboard, username);
        final JPanel spacer = new JPanel();
        spacer.add(court);
        frame.add(spacer, BorderLayout.CENTER);
        
        ////Buttons
        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        
        //Instruction Button
        JButton instruct = new JButton("Instructions");
        //testing out a window
        
        String instructions = "In TRIAL, your goal is to defeat as many of the endless waves of"
                + " monsters as possible.\nThere are two kinds with different colours and ways of "
                + " attacking you. The red will chase you and the purple will move"
                + " unpredictably, bouncing off of the walls of the map."
                + "\n" + "\n" + "You must use your sword and wits to dodge and defeat them, using"
                + " the 'W','A','S','D' keys to control your movements, and 'P' to launch sword"
                + " strikes in the direction that you are facing."
                + "\nPlay in FULLSCREEN for the best intended gameplay and"
                + " remember, if your health reaches zero it's game over!"
                + "\n\nThough unrelated to the code, all of the sprites and the background"
                + " are my own designs!\nAnimating the players movements was something that took"
                + " a while but I think it was worth it in the end.\nHope you"
                + " have fun! : )";
        
        instruct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                court.pause();
                JOptionPane.showMessageDialog(frame, instructions, "Instructions", 
                      JOptionPane.PLAIN_MESSAGE);
                court.requestFocusInWindow();
                court.unpause();
            }
        });
        control_panel.add(instruct);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Start the game!!!
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}