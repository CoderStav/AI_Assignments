package eightPuzzleSolver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.function.Consumer;

@SuppressWarnings("unused")

public class Main {
	
	public static void main(String[] args) {
		
		int[] easyConfig = {1, 3, 4, 8, 6, 2, 7, 0, 5};
		int[] medConfig = {2, 8, 1, 0, 4, 3, 7, 6, 5};
		int[] hardConfig = {5, 6, 7, 4, 0, 8, 3, 2, 1};
		
		int[] customConfig = {5, 4, 0, 6, 1, 8, 7, 3, 2};
		
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
		
		JFrame guiFrame = new JFrame("EightPuzzleSolver");
		guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel globalPanel = new JPanel();
		globalPanel.setLayout(new BoxLayout(globalPanel, BoxLayout.Y_AXIS));
		
		JPanel searchType = new JPanel();
		ButtonGroup searches = new ButtonGroup();
		
		JRadioButton breadthFirstSearchBtn = new JRadioButton("BFS", true);
		JRadioButton depthFirstSearchBtn = new JRadioButton("DFS", false);
		JRadioButton uniformCostSearchBtn = new JRadioButton("UCS", false);
		JRadioButton bestFirstSearchBtn = new JRadioButton("GBFS", false);
		JRadioButton A1Search = new JRadioButton("A1", false);
		JRadioButton A2Search = new JRadioButton("A2", false);
		searches.add(breadthFirstSearchBtn);
		searches.add(depthFirstSearchBtn);
		searches.add(uniformCostSearchBtn);
		searches.add(bestFirstSearchBtn);
		searches.add(A1Search);
		searches.add(A2Search);
		searchType.add(breadthFirstSearchBtn);
		searchType.add(depthFirstSearchBtn);
		searchType.add(uniformCostSearchBtn);
		searchType.add(bestFirstSearchBtn);
		searchType.add(A1Search);
		searchType.add(A2Search);
		
		Dimension diffBtnSize = new Dimension(90, 25);
		
		JPanel topBar = new JPanel();
		topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
		
		JPanel testButtonPanel = new JPanel();
		testButtonPanel.setLayout(new BoxLayout(testButtonPanel, BoxLayout.X_AXIS));
		topBar.add(testButtonPanel);
		
		JPanel outputPanel = new JPanel();
		
		JTextArea outputTextArea = new JTextArea(25, 40);
		Font outputFont = new Font("Courier", Font.PLAIN, 12);
		outputTextArea.setFont(outputFont);
		
		PrintStream printStream = new PrintStream(new GUIOutputStream(outputTextArea));
		System.setOut(printStream);
		System.setErr(printStream);
		
		JScrollPane outputScroll = new JScrollPane(outputTextArea);
		outputPanel.add(outputScroll);
		
		Container guiContent = guiFrame.getContentPane();
		Dimension frameDimensions = new Dimension(500, 500);
		guiContent.setPreferredSize(frameDimensions);
		
		Consumer<EightPuzzle> runSearch = (EightPuzzle puzz) -> {
			if(breadthFirstSearchBtn.isSelected())
				theSearch.breadthFirst(puzz);
			else if(depthFirstSearchBtn.isSelected())
				theSearch.depthFirst(puzz, 31);
			else if(uniformCostSearchBtn.isSelected())
				theSearch.uniformCost(puzz);
			else if(bestFirstSearchBtn.isSelected())
				theSearch.bestFirst(puzz);
			else if(A1Search.isSelected())
				System.out.println("Not implemeneted");
			else if(A2Search.isSelected())
				System.out.println("Not implemented");
			else
				System.out.println("Not implemeneted");
		};
		
		JButton runEasy = new JButton("Easy");
		runEasy.setMaximumSize(diffBtnSize);
		runEasy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runSearch.accept(easy);
			}
			
		});
		
		JButton runMed = new JButton("Med");
		runMed.setMaximumSize(diffBtnSize);
		runMed.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runSearch.accept(med);
			}
			
		});
		
		JButton runHard = new JButton("Hard");
		runHard.setMaximumSize(diffBtnSize);
		runHard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runSearch.accept(hard);
			}
			
		});
		
		JButton runCustom = new JButton("Custom");
		runCustom.setMaximumSize(diffBtnSize);
		runCustom.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runSearch.accept(custom);
			}
			
		});
		
		globalPanel.add(searchType);
		globalPanel.add(topBar);
		globalPanel.add(outputPanel);
		
		testButtonPanel.add(runEasy);
		testButtonPanel.add(runMed);
		testButtonPanel.add(runHard);
		testButtonPanel.add(runCustom);
		guiFrame.add(globalPanel);
		
		guiFrame.pack();
		guiFrame.setVisible(true);
		
	}

}
