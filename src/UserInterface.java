import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class UserInterface implements ActionListener{
	
	private JFrame frame;
	private JPanel gamePanel;
	private GameSetup game;
	private ArrayList<JButton> buttonList;
	private Set<JButton> buttonsUsed;
	private String currWord;
	private JTextField currText;
	private JPanel textPanel;
	private JButton enter;
	private javax.swing.Action pressEnter;
	private int currIndex;
	private Set<String> words;
	private int score;
	private JLabel scoreLabel;
	private int wordCounter;
	private Set<String> possibleWords;
	private WordHuntTrie wordTrie;
	private JPanel wordsPanel;
	private ArrayList<WordLength> foundWords;
	private Timer timer;
	private int second;
	private boolean gameOver;
	private JLabel timeLeft;
	private JPanel timerPanel;
	private JButton resetButton;
	
	
	public void runGame() throws FileNotFoundException {
		score = 0;
		second = 90;
		gameOver = false;
		wordCounter = 0;
		words = new HashSet<>();
		initalizeWords();
		pressEnter = new EnterButton();
		currWord = "";
		buttonsUsed = new HashSet<>();
		game = new GameSetup();
		game.setLetters();
		frame = createFrame();
		gamePanel = setGamePanel(frame);
		currText = setText();
		enter = new JButton("Enter");
		enter.addActionListener(this);
		textPanel = setTextPanel(currText, enter, frame);
		currIndex = -1;
		scoreLabel = setScoreLabel(frame, score);
		wordTrie = new WordHuntTrie(words);
		wordTrie.addWords();
		wordTrie.loadBoard(game.getLetters());
		possibleWords = wordTrie.getSolutions();
		foundWords = new ArrayList<>();
		wordsPanel = setWordsPanel(frame, scoreLabel, foundWords);
		timerPanel = setTimerPanel(frame);
		timer.start();
		System.out.println(wordTrie.getSolutionsOrganized());
		
	}
	
	
	private JPanel setTimerPanel(JFrame frame) {
		JPanel timerPanel = new JPanel();
		Container pane = frame.getContentPane();
		pane.add(timerPanel);
		timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
		timerPanel.setBounds(385, 32, 100, 30);
		timeLeft = new JLabel("Time left: 90");
		pane.add(timeLeft);
		timeLeft.setBounds(385, 60 ,120, 20);
		resetButton = new JButton("Reset");
		timerPanel.add(resetButton);
		resetButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		resetButton.addActionListener(this);
		setupTimer();
		return timerPanel;
		
	}


	private void setupTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (second > 0) {
					second --;
				}
				if (second == 0) {
					gameOver = true;
					Container pane = frame.getContentPane();
					pane.remove(timeLeft);
					pane.validate();
					pane.repaint();
					timeLeft = new JLabel("Time left: 0");
					pane.add(timeLeft);
					timeLeft.setBounds(385, 70, 120, 20);
				}
				else {
					Container pane = frame.getContentPane();
					pane.remove(timeLeft);
					pane.validate();
					pane.repaint();
					timeLeft = new JLabel("Time left: " + second);
					pane.add(timeLeft);
					timeLeft.setBounds(385, 70, 120, 20);
				}
			}
		});
		
	}


	private JLabel setScoreLabel(JFrame frame, int score) {
		JLabel scoreLabel = new JLabel();
		return scoreLabel;
		
	}
	private JPanel setWordsPanel(JFrame frame, JLabel scoreLabel, ArrayList<WordLength> wordsAdded) {
		JPanel wordsPanel = new JPanel();
		Container pane = frame.getContentPane();
		pane.add(wordsPanel);
		wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
		wordsPanel.setBounds(370, 100, 100, 325);
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		wordsPanel.add(scoreLabel);
		scoreLabel.setText("Score: " + score);
		wordsPanel.add(new JLabel(""));
		JLabel wordHeader = new JLabel("Words Found:");
		wordHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
		wordsPanel.add(wordHeader);
		for (int i = 0; i < wordsAdded.size(); i ++) {
		JLabel word = new JLabel(wordsAdded.get(i).getWord());
		word.setAlignmentX(Component.CENTER_ALIGNMENT);
		wordsPanel.add(word);
		}
		return wordsPanel;
		
	}


	private void initalizeWords() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("scrabble.txt"));
		while (scan.hasNext()) {
			String word = scan.nextLine().strip();
			if (word.length() > 2 && word.length() < 9) {
				words.add(word);
			}
		}
		
	}


	private JFrame createFrame() {
		JFrame frame = new JFrame();
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setTitle("Word Hunt");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(500, 500);
		return frame;
	}
	
	private JPanel setGamePanel(JFrame frame) {
		Container pane = frame.getContentPane();
		JPanel panel = new JPanel();
		pane.add(panel);
		panel.setBounds(10, 100, 350, 350);
		panel.setLayout(new GridLayout(4, 4));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		buttonList = new ArrayList<>();
		for (int i = 0; i < 16; i ++) {
			JButton label = new JButton(game.getLetters()[i] + "");
			label.setFont(new Font("Arial", 0, 20));
			label.addActionListener(this);
			label.setBorder(BorderFactory.createLineBorder((Color.black)));
			panel.add(label);
			buttonList.add(label);
		}
		return panel;
		
	}
	
	private JPanel setTextPanel(JTextField text, JButton button, JFrame frame) {
		Container pane = frame.getContentPane();
		JPanel panel = new JPanel();
		pane.add(panel);
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		panel.setBounds(10, 20, 350, 50);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
		        .addComponent(text)
		        .addGap(10, 20, 75)
		        .addComponent(button));

		layout.setVerticalGroup(layout
		        .createParallelGroup(GroupLayout.Alignment.BASELINE)
		        .addComponent(text)
		        .addComponent(button));
		return panel;
	}
	
	private JTextField setText() {
		JTextField text = new JTextField();
		text.getInputMap().put(KeyStroke.getKeyStroke((char)10), "enterAction");
		text.getActionMap().put("enterAction", pressEnter);
		text.setPreferredSize(new Dimension(200, 50));
		return text;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (buttonList.contains(e.getSource()) && !buttonsUsed.contains(e.getSource()) && !gameOver) {
			int tempIndex = buttonList.indexOf(e.getSource());
			if (currIndex != -1) {
				if (currIndex + 1 == tempIndex || currIndex - 1 == tempIndex || currIndex + 4 == tempIndex
						|| currIndex - 4 == tempIndex || currIndex + 5 == tempIndex || currIndex + 3 == tempIndex
						|| currIndex - 5 == tempIndex || currIndex - 3 == tempIndex) {
					currWord = currWord + game.getLetters()[tempIndex];
					buttonsUsed.add((JButton)e.getSource());
					currText.setText(currWord);
				}
			}
			else {
				currWord = currWord + game.getLetters()[tempIndex];
				buttonsUsed.add((JButton)e.getSource());
				currText.setText(currWord);
			}
		currIndex = tempIndex;	
		}
		else if (e.getSource() == this.enter) {
			currWord = currText.getText().toLowerCase();
			if (possibleWords.contains(currWord) && !gameOver) {
				possibleWords.remove(currWord);
				wordCounter ++;
				int length = currWord.length();
				switch (length) {
				case 3:
					score += 100;
					break;
				case 4:
					score += 400;
					break;
				case 5:
					score += 800;
					break;
				case 6:
					score += 1400;
					break;
				case 7:
					score += 1800;
					break;
				case 8:
					score += 2200;
					break;
				}
				Container pane = frame.getContentPane();
				pane.remove(scoreLabel);
				pane.validate();
				pane.repaint();
				scoreLabel = setScoreLabel(frame, score);
				foundWords.add(new WordLength(currWord.toUpperCase()));
				Collections.sort(foundWords);
				pane.remove(wordsPanel);
				pane.validate();
				pane.repaint();
				wordsPanel = setWordsPanel(frame, scoreLabel, foundWords);
			}
			currWord = "";
			buttonsUsed.clear();
			currText.setText(currWord);
			currIndex = -1;
		}
		else {
			frame.dispose();
			try {
				runGame();
				timer.stop();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	private class EnterButton extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currWord = currText.getText().toLowerCase();
			if (possibleWords.contains(currWord) && !gameOver) {
				possibleWords.remove(currWord);
				int length = currWord.length();
				switch (length) {
				case 3:
					score += 100;
					break;
				case 4:
					score += 400;
					break;
				case 5:
					score += 800;
					break;
				case 6:
					score += 1400;
					break;
				case 7:
					score += 1800;
					break;
				case 8:
					score += 2200;
					break;
				}
				Container pane = frame.getContentPane();
				pane.remove(scoreLabel);
				pane.validate();
				pane.repaint();
				scoreLabel = setScoreLabel(frame, score);
				foundWords.add(new WordLength(currWord.toUpperCase()));
				Collections.sort(foundWords);
				pane.remove(wordsPanel);
				pane.validate();
				pane.repaint();
				wordsPanel = setWordsPanel(frame, scoreLabel, foundWords);
			}
			currWord = "";
			buttonsUsed.clear();
			currText.setText(currWord);
			
		}
		
	}
	
	private class WordLength implements Comparable<WordLength> {
		private String word;
		private int length;
		
		public WordLength(String word) {
			this.word = word;
			this.length = word.length();
		}
		
		public String getWord() {
			return this.word;
		}

		@Override
		public int compareTo(UserInterface.WordLength o) {
			return o.length - this.length;
		}
		
	}

}
