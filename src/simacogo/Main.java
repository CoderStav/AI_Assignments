package simacogo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;

public class Main {

	public static void main(String[] args) {
		
		Minimax ai = new Minimax();
		
		/* GUI initialization start */
		
		/* JFrame initialization */
		JFrame guiFrame = new JFrame("Simacogo");
			guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			guiFrame.setResizable(false);
		
			Container guiContent = guiFrame.getContentPane();
			Dimension frameDimensions = new Dimension(500, 500);
			guiContent.setPreferredSize(frameDimensions);
		
		/* global JPanel initialization */
		JPanel globalPanel = new JPanel();
			globalPanel.setLayout(new BorderLayout());
			
			/* GUI output panel initialization */
			JPanel outputPanel = new JPanel();
				outputPanel.setLayout(new FlowLayout(FlowLayout.CENTER,3,3));
				
				JTextArea outputTA = new JTextArea();
					outputTA.setEnabled(false);
					outputTA.setColumns(65);
					outputTA.setRows(26);
					outputTA.setFont(new Font("Courier", Font.PLAIN, 12));
					outputTA.setDisabledTextColor(new Color(0, 0, 0));
					outputTA.setBackground(new Color(210, 210, 210));
				
				PrintStream ps = new PrintStream(new GUIOutputStream(outputTA));	
				System.setOut(ps);
				System.setErr(ps);
				
				outputPanel.add(outputTA);
			
			/* new game settings panel initialization */
			JPanel newGamePanel = new JPanel();
				newGamePanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));
				
				JLabel numTurnsLabel = new JLabel("Turns");
				
				Integer[] numTurnsOptions = {10, 20, 30, 40, 50};
				JComboBox<Integer> numTurns = new JComboBox<Integer>(numTurnsOptions);
				
				/* drop down to set AI minmax ply */
				JLabel plyLabel = new JLabel("Ply");
				Integer[] plyLevels = {1, 2, 3, 4, 5, 6};
				JComboBox<Integer> plyDropDown = new JComboBox<Integer>(plyLevels);
				
				JButton startGame = new JButton("New Game");
				startGame.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						outputTA.setText("");
						ai.newGame((int)numTurns.getSelectedItem(), (int)plyDropDown.getSelectedItem());
					}
					
				});
				
				newGamePanel.add(plyLabel);
				newGamePanel.add(plyDropDown);
				newGamePanel.add(numTurnsLabel);
				newGamePanel.add(numTurns);
				newGamePanel.add(startGame);
		
			/* move settings panel initialization */
			JPanel movePanel = new JPanel();
				movePanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));
				movePanel.setSize(new Dimension(50, 50));
				
				/* drop down to select move */
				JLabel moveLabel = new JLabel("Column");
				Integer[] moveOptions = {0, 1, 2, 3, 4, 5, 6, 7, 8};
				JComboBox<Integer> moveDropDown = new JComboBox<Integer>(moveOptions);
				
				/* button to execute game move */
				JButton moveButton = new JButton("Move");
					moveButton.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							outputTA.setText("");
							ai.executeMove((int)moveDropDown.getSelectedItem());
						}
						
					});
				
				movePanel.add(moveLabel);
				movePanel.add(moveDropDown);
				movePanel.add(moveButton);

		
			globalPanel.add(newGamePanel, BorderLayout.PAGE_START);
			globalPanel.add(outputPanel, BorderLayout.CENTER);
			globalPanel.add(movePanel, BorderLayout.PAGE_END);
		
		guiFrame.add(globalPanel);
		guiFrame.pack();
		guiFrame.setVisible(true);
		
		/* GUI initialization end */
		
	}

}
