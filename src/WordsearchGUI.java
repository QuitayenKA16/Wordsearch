import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WordsearchGUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel optionPanel = new JPanel();
	private JPanel listPanel = new JPanel();
	private JLabel messageLbl = new JLabel();
	private JPanel boardPanel = new JPanel();
	private JPanel colorPanel = new JPanel();
	private JPanel bankPanel = new JPanel(new GridLayout(12,1));
	private JPanel previewPanel = new JPanel();
	
	private myJButton[][] letters;
	private JTextField[] input = new JTextField[11];
	private JLabel[] words = new JLabel[10];
	
	private JButton create = new JButton("Create List");
	private JButton open = new JButton("New Game");
	private JButton change = new JButton("Change Color");
	private JButton preview = new JButton("Preview");
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu[] menu = {new JMenu("File"), new JMenu("Color"), new JMenu("About"), new JMenu("Current Game")};
	private JMenuItem[] subItems = {new JMenuItem("New Game"), new JMenuItem("Create New List"), new JMenuItem("Exit"), 
								   new JMenuItem("Change"), new JMenuItem("Reset"),
								   new JMenuItem("Resume Game"), new JMenuItem("Give up"), new JMenuItem("Credits")};
	
	private Vector<String[]> wordLists = new Vector<String[]>();
	private JComboBox<String> list;
	private JComboBox<String> highlight;
	private JComboBox<String> background;
	private JComboBox<String> text;
	
	private boolean startChosen;
	private boolean[] found;
	private int[] index1, index2;
	private int[][] wordLocations = new int[10][5];
	private int numFound;
	
	private String textColor;
	private Color highlightColor, backgroundColor;
	private ColorsList colors = new ColorsList();
	
	public static void main(String[] args){
		new WordsearchGUI();
	}
	
	WordsearchGUI()
	{
		//set up frame
		setTitle("Wordsearch");
		setLocation(20,20);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//set up panels
		optionPanel.setLocation(40,0);
		optionPanel.setSize(190,200);
		
		colorPanel.setLocation(30,0);
		previewPanel.setSize(110,150);
		previewPanel.setLocation(60,170);
		
		listPanel.setLocation(0,0);
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		boardPanel.setBackground(Color.WHITE);
		boardPanel.setSize(625,625);
		boardPanel.setLocation(0,0);
		
		bankPanel.setBackground(Color.WHITE);
		bankPanel.setSize(175,670);
		bankPanel.setLocation(650,0);
		
		//buttons
		open.addActionListener(this);
		change.addActionListener(this);
		create.addActionListener(this);
		preview.addActionListener(this);
		
		//default colors
		highlightColor = colors.getColor(2);
		backgroundColor = colors.getColor(1);
		textColor = colors.getHTML(0);
		
		createMenuBar();
		setJMenuBar(menuBar);
		getContentPane().setLayout(null);
		setVisible(true);
		about();
	}
	
	public void createMenuBar()
	{
		//setup menu bar
		int i;
		
		//premade lists
		wordLists.addElement(new String[]{"School Subjects", "Math","English","Science","Spanish","Art",
			"Music","Social Studies","Lunch","Health","Physical Education"});
		wordLists.addElement(new String[]{"Restaurants", "McDonalds","Applebee's","Texas Roadhouse",
			"Cafe Teresa","Chipotle","T-Bones","Lucciano's","Boston Market","Panera","iHop"});
		wordLists.addElement(new String[]{"HIMYM Characters", "Teddy Westside","Barndoor","Robin Sparkles",
				"Marshmallow","Lilypad","The Mother","The Slutty Pumpkin","The Captain", "DAMMIT PATRICE", "Ranjeet"});
		wordLists.addElement(new String[]{"Barney Stinson Quotes", "Legendary","Suit up","Wait-for-it","High five",
				"What up","The Bro Code","The Playbook","Laser Tag","True Story","Challenge Accepted"});
		wordLists.addElement(new String[]{"Colors", "Red","Orange","Yellow","Green","Blue","Indigo","Viotet",
				"Black","White","Gray"});
		wordLists.addElement(new String[]{"Animals", "Dog","Cat","Beaver","Flamingo","Dinosaur","Lion","Elephant",
				"Great Blue Whale","Walrus","Snow Leopard"});
		
		//add same actionlistener to all menu items
		for (i = 0; i < 8; i++)
			subItems[i].addActionListener(this);
		//add items to file menu option
		for (i = 0; i < 3; i++)
			menu[0].add(subItems[i]);
		//add items to color menu option
		for (i = 3; i < 5; i++)
			menu[1].add(subItems[i]);
		//add resume and solve to currGame
		menu[3].add(subItems[5]);
		menu[3].add(subItems[6]);
		//add credit to "about" menu option
		menu[2].add(subItems[7]);
		//add menu items to actual menu bar
		for (i = 0; i < 3; i++)
			menuBar.add(menu[i]);
	}
	
	public void actionPerformed (ActionEvent e)
	{
		//open a wordsearch
		if (e.getSource() == subItems[0])
			newGame();
		//create new list
		else if (e.getSource() == subItems[1])
			setListPanel();
		//exit program
		else if (e.getSource() == subItems[2])
			System.exit(0);
		//change GUI colors
		else if (e.getSource() == subItems[3] )
			setColor();
		else if (e.getSource() == subItems[4])
			changeColor(colors.getColor(2),colors.getColor(1), colors.getHTML(0));
		//reveal answers
		else if (e.getSource() == subItems[5])
			returnGame();
		else if (e.getSource() == subItems[6])
			revealWords();
		//about
		else if (e.getSource() == subItems[7])
			about();
		//button presses
		else if (e.getSource() == create)
			createNewList();
		else if (e.getSource() == open)
			startGame(list.getSelectedIndex());
		else if (e.getSource() == change)
			changeColor(colors.getColor(highlight.getSelectedIndex()),
					    colors.getColor(background.getSelectedIndex()), 
						colors.getHTML(text.getSelectedIndex()));
		else if (e.getSource() == preview)
			updatePreviewButtons();
	}
	
	public void newGame()
	{
		subItems[5].setEnabled(true);
		subItems[6].setEnabled(false);	
		setSize(270,200);
		getContentPane().removeAll();
		optionPanel.removeAll();
		
		String[] options = new String[wordLists.size()];
		for (int i = 0; i < wordLists.size(); i++)
			options[i] = wordLists.elementAt(i)[0];
		
		list = new JComboBox<String>(options);
		list.setSelectedIndex(0);
		
		optionPanel.add(new JLabel("Choose wordsearch:"));
		optionPanel.add(list);
		optionPanel.add(open);
		refresh(optionPanel);
	}
	
	public void startGame(int i)
	{
		menuBar.add(menu[3]);
		subItems[5].setEnabled(false);
		subItems[6].setEnabled(true);		
		//setSize(850,670);
		setSize(850,675);
		
		found = new boolean[10];
		for (int j = 0; j < 10; j++)
			found[j] = false;
		getNumFound();
		
		//create wordsearch
		Wordsearch wordsearch = new Wordsearch(wordLists.elementAt(i));
		wordsearch.fillBlanks();
		
		for (int x = 0; x < 10; x++)
			for (int y = 0; y < 5; y++)
				wordLocations[x][y] = wordsearch.getLocations()[x][y];
		
		startChosen = false;
		index1 = new int[2]; index2 = new int[2];
		
		//create gui
		getContentPane().removeAll();
		createWordBank(i);
		boardPanel.removeAll();
		letters = new myJButton[25][25];
		for (int row = 0; row < 25; row++)
			for (int col = 0; col < 25; col++)
			{
				myJButton b = new myJButton(textColor, "" + wordsearch.getBoard()[row][col]);
				b.setBackground(backgroundColor);
				b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						letterPicked(e);
					}});
				letters[row][col] = b;
				boardPanel.add(b);				
			}
		
		refresh(boardPanel);
	}
	
	public void letterPicked(ActionEvent e)
	{
		int x, y;
		for (int r = 0; r < 25; r++)
			for (int c = 0; c < 25; c++)
			{
				if (e.getSource() == letters[r][c])
				{
					letters[r][c].setBackground(highlightColor);
					if (startChosen == false) //first card picking
					{
						index1[0] = r;
						index1[1] = c;
						startChosen = true;
						
						for (x = 0; x < 25; x++)
							for(y = 0; y < 25; y++)
							{
								letters[x][y].setEnabled(false);
								int changeX = Math.abs(index1[0] - x);
								int changeY = Math.abs(index1[1] - y);
								if (x == index1[0] || y == index1[1] || changeX == changeY)
									letters[x][y].setEnabled(true);
							}
					}
					else
					{
						index2[0] = r;
						index2[1] = c;
						for (x = 0; x < 25; x++)
							for(y = 0; y < 25; y++)
								letters[x][y].setEnabled(true);
						checkWord();
						startChosen = false;
					}
					break;
				}
			}
	}
	
	public void checkWord()
	{
		boolean wordFound = false;
		for (int i = 0; i < 10; i++)
		{
			if (index1[0] == wordLocations[i][0] &&
				index1[1] == wordLocations[i][1] &&
				index2[0] == wordLocations[i][2] &&
				index2[1] == wordLocations[i][3])
				
			{
				//found a word
				wordFound = true;
				int d = getDirection(index1[0], index1[1], index2[0], index2[1]);
				highlightWord(index1[0], index1[1], index2[0], index2[1], d);
				
				getContentPane().remove(bankPanel);
				words[i].setText("<html><font color = 'red'>"+words[i].getText());
				found[i] = true;
				getNumFound();
				refresh(bankPanel);
				break;
			}
		}
		if (wordFound)
		{
			if (numFound == 10)
				winner();
		}
		else
		{
			letters[index1[0]][index1[1]].setBackground(backgroundColor);
			letters[index2[0]][index2[1]].setBackground(backgroundColor);
		}
	}
	
	public int getDirection(int startX, int startY, int endX, int endY)
	{
		int direction = 0;
		if (startY == endY)
		{
			if (startX < endX)
				direction = 1;
			else
				direction = 2;
		}
		else if (startX == endX)
		{
			if (startY < endY)
				direction = 3;
			else
				direction = 4;
		}
		else if (startX < endX && startY < endY) //TOPLEFT-BOTTOMRIGHT
			direction = 5;
		else if (startX < endX && startY > endY) //TOPRIGHT-BOTTOMLEFT
			direction = 6;
		else if (startX > endX && startY < endY) //BOTTOMLEFT-TOPRIGHT
			direction = 7;
		else if (startX > endX && startY > endY) //BOTTOMRIGHT-TOPLEFT
			direction = 8;
		return direction;
	}
	
	public void winner()
	{
		revealWords();
		getContentPane().removeAll();
		bankPanel.removeAll();
		JLabel[] youWin = new JLabel[4];
		youWin[0] = new JLabel("HEY,");
		youWin[1] = new JLabel("YOU");
		youWin[2] = new JLabel("W0N");
		youWin[3] = new JLabel("!!!");
		for (int i = 0; i < 4; i++)
		{
			youWin[i].setFont(new Font("Consolas", Font.PLAIN, 75));
			bankPanel.add(youWin[i]);
		}
		getContentPane().add(boardPanel);
		refresh(bankPanel);
	}
	
	public void createWordBank(int i)
	{
		getContentPane().remove(bankPanel);
		bankPanel.removeAll();
		
		JLabel title = new JLabel("<html><b>Word Bank:");
		title.setFont(new Font("Consolas", Font.PLAIN, 20));
		bankPanel.add(title);
		
		for (int x = 0; x < 10; x++)
		{
			words[x] = new JLabel(wordLists.elementAt(i)[x+1]);
			words[x].setFont(new Font("Consolas", Font.PLAIN, 16));
			bankPanel.add(words[x]);
		}
		refresh(bankPanel);
	}
	
	public void getNumFound()
	{
		numFound = 0;
		for (int i = 0; i < 10; i++)
			if (found[i])
				numFound++;
	}
	
	public void returnGame()
	{
		subItems[5].setEnabled(false);
		if (numFound == 10)
			subItems[6].setEnabled(false);
		else
			subItems[6].setEnabled(true);
		
		//setSize(850,670);
		setSize(850,675);
		getContentPane().removeAll();
		//make sure colors are updated
		for (int r = 0; r < 25; r++)
			for (int c = 0; c < 25; c++)
				letters[r][c].setBackground(backgroundColor);
		
		for (int i = 0; i < 10; i++)
			if (found[i])
			{
				int d = getDirection(wordLocations[i][0], wordLocations[i][1], wordLocations[i][2], wordLocations[i][3]);
				highlightWord(wordLocations[i][0], wordLocations[i][1], wordLocations[i][2], wordLocations[i][3], d);
			}
		
		getContentPane().add(boardPanel);
		refresh(bankPanel);
	}
	
	public void revealWords()
	{
		subItems[5].setEnabled(false);
		subItems[6].setEnabled(false);
		getContentPane().remove(boardPanel);
		boardPanel.removeAll();
		int r, c;
		
		//same buttons but no actionlistener so button press does nothing
		for (r = 0; r < 25; r++)
			for (c = 0; c < 25; c++)
			{
				letters[r][c] = new myJButton(textColor, letters[r][c].getText());
				letters[r][c].setBackground(backgroundColor);
				boardPanel.add(letters[r][c]);
			}
		
		//highlight words in wordsearch
		for (int i = 0; i < 10; i++)
		{
			found[i] = true;
			getNumFound();
			highlightWord(wordLocations[i][0],wordLocations[i][1],wordLocations[i][2],wordLocations[i][3],wordLocations[i][4]);
		}
		refresh(boardPanel);
	}
	
	public void highlightWord(int startX, int startY, int endX, int endY, int direction)
	{
		int wordLength;
		int r = startX;
		int c = startY;
		
		if (direction > 2)
			wordLength = Math.abs(endY - startY);
		else
			wordLength = Math.abs(endX - startX);
		
		for (int i = 0; i <= wordLength; i++)
		{
			letters[r][c].setBackground(highlightColor);
			switch (direction)
			{
				case 1: r++; break;
				case 2: r--; break;
				case 3: c++; break;
				case 4: c--; break;
				case 5: r++; c++; break;
				case 6: r++; c--; break;
				case 7: r--; c++; break;
				case 8: r--; c--; break;
			}
		} 
	}
	
	public void createNewList()
	{	
		int i;
		boolean titleGood = true, wordsGood = true;
		
		getContentPane().remove(listPanel);
		listPanel.remove(messageLbl);
		
		for (i = 0; i < wordLists.size(); i++) //same title as another list
			if (input[0].getText().compareTo(wordLists.elementAt(i)[0]) == 0)
				titleGood = false;
		if (input[0].getText().length() == 0)
			titleGood = false;
		
		for (i = 1; i < 11; i++)
			if (input[i].getText().length() < 3 || input[i].getText().length() > 25)
				wordsGood = false;
		
		if (!titleGood)
			messageLbl.setText("Must enter unique title.");
		else if (!wordsGood)
			messageLbl.setText("All words must be 3-25 letters.");
		else
		{
			String[] newList = new String[11];
			newList[0] = input[0].getText();
			for (i = 1; i < 11; i++)
				newList[i] = input[i].getText();
			wordLists.addElement(newList);
			messageLbl.setText("New words list created.");
			listPanel.removeAll();
			listPanel.setSize(200,200);
		}
		listPanel.add(messageLbl);
		refresh(listPanel);
	}
	public void setListPanel()
	{
		menuBar.remove(menu[3]);
		setSize(215,590);
		getContentPane().removeAll();
		listPanel.removeAll();
		listPanel.setSize(200,540);
		
		listPanel.add(new JLabel("Enter List Title:"));
		input[0] = new JTextField("", 13);
		listPanel.add(input[0]);
		for (int i = 1; i < 11; i++)
		{
			listPanel.add(new JLabel("Word #" + i + ":"));
			input[i] = new JTextField("", 13);
			listPanel.add(input[i]);
		}
		
		messageLbl.setText("   ");
		listPanel.add(create);
		listPanel.add(messageLbl);
		refresh(listPanel);
	}
	
	public void about()
	{
		subItems[5].setEnabled(true);
		subItems[6].setEnabled(false);
		setSize(270,320);
		getContentPane().removeAll();
		JPanel aboutPnl = new JPanel();
		aboutPnl.setSize(270,320);
		aboutPnl.setLocation(0, 0);
		
		JLabel title = new JLabel("Wordsearch GUI");
		title.setFont(new Font("Consolas", Font.BOLD, 25));
		aboutPnl.add(title);
		JLabel period = new JLabel("G Period JAVA");
		period.setFont(new Font("Consolas", Font.PLAIN, 20));
		aboutPnl.add(period);
		aboutPnl.add(new JLabel(new ImageIcon("src/Images/Other/Motivational Corgi.gif")));
		JLabel author = new JLabel("Karamel Quitayen");
		author.setFont(new Font("Consolas", Font.PLAIN, 20));
		aboutPnl.add(author);
		refresh(aboutPnl);
	}
	
	public void setColor()
	{
		getContentPane().removeAll();
		colorPanel.removeAll();
		setSize(270,400);
		subItems[5].setEnabled(true);
		subItems[6].setEnabled(false);
	
		highlight = new JComboBox<String>(colors.getList());
		highlight.setSelectedIndex(2);
		background = new JComboBox<String>(colors.getList());
		background.setSelectedIndex(1);
		text = new JComboBox<String>(colors.getList());
		text.setSelectedIndex(0);

		colorPanel.setSize(170,170);
		colorPanel.add(new JLabel("Choose highlight color:"));
		colorPanel.add(highlight);
		colorPanel.add(new JLabel("Choose background color:"));
		colorPanel.add(background);
		colorPanel.add(new JLabel("Choose text color:"));
		colorPanel.add(text);
		
		updatePreviewButtons();
		refresh(colorPanel);
	}
	
	public void changeColor(Color h, Color b, String t)
	{
		subItems[5].setEnabled(true);
		subItems[6].setEnabled(false);
		highlightColor = h;
		backgroundColor = b;
		textColor = t;
		setSize(270,200);
		getContentPane().removeAll();
		
		colorPanel.setSize(200,170);
		colorPanel.removeAll();
		colorPanel.add(new JLabel("Colors successfully updated."));
		refresh(colorPanel);
	}
	
	public void updatePreviewButtons()
	{
		//change colors of preview buttons to selected colors of combo boxes
		getContentPane().removeAll();
		previewPanel.removeAll();
		
		highlightColor = colors.getColor(highlight.getSelectedIndex());
		backgroundColor = colors.getColor(background.getSelectedIndex());
		textColor = colors.getHTML(text.getSelectedIndex());
		
		JLabel lbl = new JLabel("Preview");
		lbl.setFont(new Font("Consolas", Font.BOLD, 20));
		previewPanel.add(lbl);
		previewPanel.add(new myJButton(backgroundColor, textColor));
		previewPanel.add(new myJButton(highlightColor, textColor));
		previewPanel.add(preview);
		previewPanel.add(change);
		getContentPane().add(colorPanel);
		refresh(previewPanel);
	}
	
	public void refresh(JPanel pnl)
	{   //reduce lines of code
		getContentPane().add(pnl);
		getContentPane().repaint();
		getContentPane().validate();
	}
}

class myJButton extends JButton
{
	private static final long serialVersionUID = 1L;
	
	//wordsearch button
	myJButton(String htmlClr, String text)
	{
		setText(htmlClr + text);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		setPreferredSize(new Dimension(20,20));
		setOpaque(true); 
		setBorderPainted(false);
		setFont(new Font("Arial", Font.PLAIN, 12));
	}	
	
	//preview button
	myJButton(Color b, String t)
	{
		setText(t + "A");
		setBackground(b);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		setPreferredSize(new Dimension(50,50));
		setOpaque(true); 
		setBorderPainted(false);
		setFont(new Font("Arial", Font.PLAIN, 24));
	}
}
