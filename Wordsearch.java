package gui_programs;
import java.util.Random;

public class Wordsearch 
{
	private char[][] b;
	private int[][] wordLocations;
	private Random randNumGen = new Random();
	private String[] wordBank;
	
	Wordsearch(String[] words)
	{
		b = createWordsearch();
		wordBank = new String[10];
		wordLocations = new int[10][5]; //0=startX, 1=startY, 2=endX, 3=endY, 4=direction
		for (int i = 0; i < 10; i++)
			wordBank[i] = editWord(words[i+1]);
		
		fillWordsearch();
	}
	public void fillWordsearch()
	{
		int direction, startRow, startCol;
		int minRow, minCol, maxRow, maxCol;
		
		char[][] temp = new char[25][25];
		
		//for each word
		for (int i = 0; i < wordBank.length; i++)
		{
			for (int r = 0; r < 25; r++)
				for (int c = 0; c < 25; c++)
					temp[r][c] = b[r][c];
					
			//reset max and min for both row and col
			minRow = 0; minCol = 0; maxRow = 24; maxCol = 24;
			//pick random direction
			direction = randNumGen.nextInt(8 - 1 + 1) + 1;
			//change bounds of starting location based on direction and word length
			switch (direction)
			{
				case 1: maxRow -= wordBank[i].length(); break;
				case 2: minRow += wordBank[i].length(); break;
				case 3: maxCol -= wordBank[i].length(); break;
				case 4: minCol += wordBank[i].length(); break;
				case 5: maxRow -= wordBank[i].length();
						maxCol -= wordBank[i].length(); break;
				case 6: maxRow -= wordBank[i].length();
						minCol += wordBank[i].length(); break;
				case 7: minRow += wordBank[i].length();
						maxCol -= wordBank[i].length(); break;
				case 8: minRow += wordBank[i].length();
						minCol += wordBank[i].length(); break;
			}
			startRow = randNumGen.nextInt(maxRow - minRow + 1) + minRow;
			startCol = randNumGen.nextInt(maxCol - minCol + 1) + minCol;
			
			temp = addWord(wordBank[i], direction, startRow, startCol, i);
			if (temp != b)
			{
				wordLocations[i][0] = startRow;
				wordLocations[i][1] = startCol;
				wordLocations[i][4] = direction;
				b = temp;
			}
			else
				i--;
		}
	}
	public String editWord(String word)
	{
		word = word.toUpperCase();
		char[] letters = new char[word.length()];
		for (int i = 0; i < word.length(); i++)
			letters[i] = word.charAt(i);
			
		String edit = "";
		for (int j = 0; j < letters.length; j++)
			if ((int)letters[j] >= 65 && (int)letters[j] <= 90)
				edit += letters[j];
		return edit;
	}
	public char[][] addWord(String word, int d, int row, int col, int index)
	{
		int r, c;
		boolean valid = true;
		char[][] temp = new char[25][25];
		for (r = 0; r < 25; r++)
			for (c = 0; c < 25; c++)
				temp[r][c] = b[r][c];
		
		//for each letter of the word
		for (int i = 0; i < word.length(); i++)
		{
			if ((temp[row][col] != ' ')&&(temp[row][col]!=word.charAt(i)))
				valid = false;
			
			temp[row][col] = word.charAt(i);
			if (i == word.length()-1)
			{
				wordLocations[index][2] = row;
				wordLocations[index][3] = col;
			}
			switch (d)
			{
				case 1: row++; break; //UP-DOWN
				case 2: row--; break; //DOWN-UP
				case 3: col++; break; //LEFT-RIGHT
				case 4: col--; break; //RIGHT LEFT
				case 5: row++; col++; break; //TOPLEFT-BOTTOMRIGHT
				case 6: row++; col--; break; //TOPRIGHT-BOTTOMLEFT
				case 7: row--; col++; break; //BOTTOMLEFT-TOPRIGHT
				case 8: row--; col--; break; //BOTTOMRIGHT-TOPLEFT
			}
		}
		
		if (valid)
			return temp;
		else
			return b;
	}
	public void fillBlanks()
	{
		for (int r = 0; r < 25; r++)
		{
			for (int c = 0; c < 25; c++)
				if (b[r][c] == ' ')					  //ASCII code
					b[r][c] = (char)(randNumGen.nextInt(90 - 65 + 1) + 65);
		}
	}
	public char[][] createWordsearch()
	{
		char[][] board = new char[25][25];
		for (int r = 0; r < 25; r++)
			for (int c = 0; c < 25; c++)
				board[r][c] = ' ';
		return board;
	}
	public void printWordsearch()
	{
		System.out.println("--------------------------------------------------");
		System.out.println("   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4");
		for (int r = 0; r < 25; r++)
		{
			if (r < 10)
				System.out.print(" " + r + " ");
			else
				System.out.print(r + " ");
			
			for (int c = 0; c < 25; c++)
			{
				System.out.print(b[r][c] + " ");
				if (c == 24)
					System.out.print(" " + r + " ");
			}
			System.out.println();
		}
		System.out.println("   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4");
		System.out.println("--------------------------------------------------");
	}
	public char[][] getBoard(){
		return b;
	}
	public String[] getList(){
		return wordBank;
	}
	public int[][] getLocations(){
		return wordLocations;
	}
}
