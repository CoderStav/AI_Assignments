package eightPuzzleSolver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("unused")

public class Main {
	
	public static void main(String[] args) {
		
		int[] easyConfig = {1, 3, 4, 8, 6, 2, 7, 0, 5};
		int[] medConfig = {2, 8, 1, 0, 4, 3, 7, 6, 5};
		int[] hardConfig = {5, 6, 7, 4, 0, 8, 3, 2, 1};
		
		int[] customConfig = {2, 8, 3, 1, 6, 4, 7, 0, 5};
		
		EightPuzzle easy, med, hard, custom;
		
		try {
			easy = new EightPuzzle(easyConfig);
			med = new EightPuzzle(medConfig);
			hard = new EightPuzzle(hardConfig);
			custom = new EightPuzzle(customConfig);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			return;
		}
		
		Search theSearch = new Search();
		
		
		/* GUI elements */
		JPanel testButtonPanel = new JPanel();
		
		JFrame guiFrame = new JFrame("EightPuzzleSolver");
		guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container guiContent = guiFrame.getContentPane();
		Dimension frameDimensions = new Dimension(500, 500);
		guiContent.setPreferredSize(frameDimensions);
		
		JButton runEasy = new JButton("Easy");
		runEasy.addActionListener(e -> theSearch.breadthFirst(easy));
		
		JButton runMed = new JButton("Med");
		runMed.addActionListener(e -> theSearch.breadthFirst(med));
		
		JButton runHard = new JButton("Hard");
		runHard.addActionListener(e -> theSearch.breadthFirst(hard));
		
		JButton runCustom = new JButton("Custom");
		runCustom.addActionListener(e -> theSearch.breadthFirst(custom));
		
		testButtonPanel.add(runEasy);
		testButtonPanel.add(runMed);
		testButtonPanel.add(runHard);
		testButtonPanel.add(runCustom);
		guiFrame.add(testButtonPanel);
		
		guiFrame.pack();
		guiFrame.setVisible(true);
		
	}

}
