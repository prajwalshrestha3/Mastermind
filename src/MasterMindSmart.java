import java.awt.BorderLayout;import java.awt.Color;	//imports
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MasterMindSmart extends JFrame implements ActionListener {
	ArrayList<String> Pset=new ArrayList<String>();	//all the possible combinations are added to this arraylist
	int width;	//width is the number of pegs across for which the colours must be assigned
	int height;	//height is the number of chances given to the player
	int numColors;	//number of different coloured pegs
	int numGuesses;	//number of guesses made
	JButton[][] colouredPegs; //2d array of type JButton named colouredPegs,whites and blacks
	JButton[][] whites;       
	JButton[][] blacks;       
	JButton[] computerGuess;	//array of type JButton named computerGuess
	int state[][];	//2d array of type int named state(colours of the boxes in the middle)
	int[] hiddenGuess;	//array of type int named hiddenGuess
	JButton guess = new JButton("Guess");	//new button created with text "Guess" which when clicked is going to try and match with the computer's combination
	JPanel colouredPanel = new JPanel();	//creating Jpanel objects named colouredPanel, whitesPanel, blacksPanel and computerGuessPanel
	JPanel whitesPanel = new JPanel();
	JPanel blacksPanel = new JPanel();
	JPanel computerGuessPanel = new JPanel();
	JPanel panel2 = new JPanel();
	Random rand;	//random variable
	boolean check1=true;
	boolean check2=true;
	static String strNum;
	static int blacks (int [] one, int [] two)	//method that takes in two arrays which checks if the pegs are in the right place and the right colour
	{
		int val=0;	//val set to 0
		for (int i=0;i<one.length;i++)	//loop that runs for array one's length
		{
			if (one[i]==two[i]) 	//if the peg's colour and the position are the same and the computer's answer then the value of val is incremented
				val++;		//val is incremented so that black pegs can be displayed
		}
		return val;	//val returned
	}

	static int whites (int [] one, int [] two)	//method that takes in two arrays and returns an int named one and two
	{
		boolean found;	//boolean variable named found declared
		int [ ] oneA = new int[one.length]; 	//array named oneA created with the length as length of one
		int [ ] twoA = new int[one.length];		//array named twoA created with the length as length of one
		for (int i=0;i<one.length;i++)			//loop that runs for array one's length
		{
			oneA[i]=one[i]; 	//the value of one array copied to oneA array
			twoA[i]=two[i];		//the value of two array copied to twoA array
		}
		int val=0;		//int variable val set to 0
		for (int i=0;i<one.length;i++)		//loop that runs for array one's length
			if (oneA[i]==twoA[i]) {		//if the value of array one and two are same at index i then 
				oneA[i]=0-i-10;		//set the value of oneA[i] as 0-index-10
				twoA[i]=0-i-20;		//set the value of twoA[i] as 0-index-20
			}

		for (int i=0;i<one.length;i++)	//loop that runs 0 to length of one array
		{ 
			found=false;	//found set to false
			for (int j=0;j<one.length && !found;j++)	//loop that runs 0 to when found=true 
			{ 
				if (i!=j && oneA[i]==twoA[j])	//the pegs in the same postion are not compared so to find if the pegs are in the wrong place
				{
					val++;	//increment the value of val
					oneA[i]=0-i-10;		//set the value of oneA[i] as 0-index-10
					//System.out.println(oneA[i]);
					twoA[j]=0-j-20; 	//set the value of oneA[i] as 0-index-20
					found=true;		//set found to true
				}
			}

		}
		return val;		//val returned
	}

	static Color choose(int i)	//method called choose which return a color
	{
		if (i==0) 	//if i=0	return red 
			return Color.red;
		if (i==1) 	//if i=1 return green 
			return Color.green; 			
		if (i==2) 	//if i=2 return blue 
			return Color.blue;
		if (i==3) 	//if i=3 return orange 
			return Color.orange;
		if(i==4)
			return Color.BLACK;
		else 	//else return yellow
			return Color.yellow;

	}

	public ArrayList<String> getList(){
		return this.Pset;
	}


	public MasterMindSmart(int h, int w, int c) {	//constructor that takes height, width and the color
		for(int i=0;i<=1295;i++){	//this loop saves all the possible combinations in base 6 as there are only 6 colours
			strNum=Long.toString(i,6);
			if(strNum.length()==1){	//adding 0s in front of the string
				strNum="000"+strNum;
			}
			else if(strNum.length()==2){
				strNum="00"+strNum;
			}
			else if(strNum.length()==3){
				strNum="0"+strNum;
			}
			Pset.add(strNum);
		}
		

		width=w; 	//variable width is the number of tiles across to guess
		height=h; 	//height is the number of chances that you get
		numColors=c;	//variable numColors is the number of different colours
		numGuesses=0;	//numGuess is the counter for the number of guesses that have been made
		hiddenGuess = new int[width];		//hidden guess is an array which holds the answer
		rand=new Random();		//rand initialised which will produce random numbers to guess the combination
		state = new int[height][width];	//2d array state to keep track of the current height and the tile which is being guessed
		colouredPegs= new JButton[height][width];	// 2D Jbuttons colouredPegs,whites,blacks created with height and width
		whites= new JButton[height][width];
		blacks= new JButton[height][width];
		computerGuess= new JButton[width];	//JButton array computerGuess with length width
		colouredPanel.setLayout(new GridLayout(height,width));	//gridlayout applied to colouredPanel blacksPanel and whitesPanel 
		blacksPanel.setLayout(new GridLayout(height,width));
		whitesPanel.setLayout(new GridLayout(height,width));
		computerGuessPanel.setLayout(new GridLayout(1,width));
		colouredPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));	//applying invisible border to colouredPanel,whitesPanel,blacksPanel with 20px each side
		whitesPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		blacksPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));



		class bing implements ActionListener	//class bing which implements actionlister interface
		{   
			int x; //var x,y
			int y;

			public void actionPerformed(ActionEvent e) {	//method that implements actionlistener
				state[x][y]=(state[x][y]+1)%numColors; 	//when the pegs are clicked the colours change
				//System.out.println(x + " " +y);		//the state of what the height is and the tile which is being worked on
				((JButton)(e.getSource())).setBackground(choose(state[x][y]));	//setting the background of JButton 
			}

			public bing (int p,int q)	//constructor bing which takes int p & q 
			{
				x=p;
				y=q;	//x set to p and y set to q
			}
		}

		for (int k = 0; k < width; k++)		//loop makes the computers guess
		{
			computerGuess[k]= new JButton(); 	//4 JButtons created for computer's guess one by one
			computerGuess[k].setVisible(true);	//computer guess made visible
			computerGuessPanel.add(computerGuess[k]);	//4 button added to the panel one by one
			hiddenGuess[k]=rand.nextInt(numColors);// random colors assigned by the computer 
			computerGuess[k].setBackground(choose(hiddenGuess[k]));		//setting the background for computerGuess button
		}

		for (int i = 0; i < height; i++) 	//loop that runs from 0 to the height 
			for (int j = 0; j < width; j++)		//loop that runs from 0 to the width
			{
				//System.out.println(i +" "+j);
				state[i][j]=0;	//state is set to 0 so that to start off every color is red
				colouredPegs [i][j]= new JButton();	//colouredPegs[i][j] made a new button
				colouredPegs [i][j].addActionListener(new bing(i,j));	//whenever the action is called/button is clicked the new bing object is created which changes the background of the pegs


				colouredPegs [i][j].setBackground(choose(state[i][j]));	//color of the tiles set to 0, when state[i][j]=0 and 0 means red color background
				whites [i][j]= new JButton();	//white pegs created as buttons
				whites [i][j].setVisible(false);	//the white pegs are set to invisible so that they only display when the color is correct but the position is wrong
				whites [i][j].setBackground(Color.white);	//setting the background white
				blacks [i][j]= new JButton();	//black pegs created as buttons
				blacks [i][j].setVisible(false);//the black pegs are set to invisible so that they only display when the color and the position is correct
				blacks [i][j].setBackground(Color.black);//background set to black

				colouredPanel.add(colouredPegs [i][j]); 	//pegs added to the panel
				whitesPanel.add(whites [i][j]);	//white pegs added to the whitepanel
				blacksPanel.add(blacks [i][j]);	//black pegs added to the black panel
				if (i>0)	//this if statement only shows only one line of coloured pegs at a time	
					colouredPegs[i][j].setVisible(false);	//makes pegs invisible
			}

		setLayout(new BorderLayout());	
		add(blacksPanel, "West");	//left side of the screen made as the blacksPanel
		add(colouredPanel, "Center");//center of the screen made as the colouredPanel
		add(whitesPanel, "East");//right side of the screen made as the whitesPanel
		JPanel guessPanel = new JPanel();	//new panel is created for the guess 
		guessPanel.setLayout(new FlowLayout());	//flowlayout used for the guessPanel
		guessPanel.add(guess);	//guess buttton added under the guessPanel
		add(guessPanel,"South");	//guessPanel added to the screen 
		JPanel topPanel = new JPanel();	//new panel is created 
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));	//border with 20 pixels on each side
		topPanel.setLayout(new GridLayout(1,3));	//new gridlayout is set in the topPanel which has 1 row and 3 coulmns
		topPanel.add(new JLabel("Blacks",JLabel.CENTER));	//new label added to the top panel which says Blacks in the center of the panel
		topPanel.add(computerGuessPanel);	//This line adds the computers guess on the top of the screen
		topPanel.add(new JLabel("Whites",JLabel.CENTER));	//new label added to the top panel which says Whites in the center of the panel
		add(topPanel,"North");	//topPanel is created
		setDefaultCloseOperation(3);	//window closed when 'X' is pressed
		setTitle("Mastermind");	//Title of the screen set to Mastermind
		setMinimumSize(new Dimension(width*50,height*50));	//minimum size of the screen set
		pack();	//causes this screen to be sized to fit the preferred size and layouts of its subcomponents.
		setVisible(true);	//making the window visible
		guess.addActionListener(this);	//when guess button is clicked some action is performed
	}

	
	
	public void actionPerformed(ActionEvent e) {
		int randomnumber=(int) (Math.random()*(Pset.size()));	//randomnumber variable that tries other combinations apart from 0011 which is the first try randomly
		int whiteThings=0;	//number of white pegs to be displayed
		int blackThings=0;	//number of white pegs to be displayed
		int [] strtointarr=new int[4];	//array that will hold the array elements in the current Pset string 
		if(check1==true){	//this if statement is used so that the combination 0011 is only used once, boolean is used to check that
			for(int i=0;i<strtointarr.length;i++){	//loop that add the element from Pset to strtointarr
				strtointarr[i]=Character.getNumericValue(Pset.get(7).charAt(i));	//Pset.get(7) because this is 0011
				check1=false;
			}
			whiteThings=whites(strtointarr,hiddenGuess);	//stores the number of white pegs to be displayed(whites and blacks only displaying for first row)
			blackThings=blacks(strtointarr,hiddenGuess);	//stores the number of black pegs to be displayed
		}else{
			for(int i=0;i<strtointarr.length;i++){	//loop that add the element from Pset to strtointarr
				strtointarr[i]=Character.getNumericValue(Pset.get(randomnumber).charAt(i));	//problem with pset.get(i)
				//System.out.println(Pset.get(i));
			}
			whiteThings=whites(strtointarr,hiddenGuess);	//stores the number of white pegs to be displayed(whites and blacks only displaying for first row)
			blackThings=blacks(strtointarr,hiddenGuess);	//stores the number of black pegs to be displayed
		}

		
		for (int i=0;i<width;i++)	//this loop makes the row that's already been guessed unclickable
			colouredPegs[numGuesses][i].setEnabled(false);

		if (blackThings==width)	//if the black pegs are complete then
		{  
			
			for(int i=0;i<width;i++)
				colouredPegs[numGuesses][i].setBackground(choose(strtointarr[i]));
				
			for (int i=0;i<blackThings;i++)	//displays the 4 black pegs when the game is won
				blacks[numGuesses][i].setVisible(true); 

			for (int i=0;i<width;i++) //makes the computer's guess visible
				computerGuess[i].setVisible(true);
			
			int n = JOptionPane.showConfirmDialog(this,	//saves the value of the option that is provided when the game is won
					"You've Won! Would you like to play again?", "",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION) //if n=NO_OPTION then program is closed
				System.exit(0);
			else { //else the program starts again
				dispose();
				new MasterMindSmart(height,width,numColors);
			}
		}
		if (numGuesses < height)	//if there are more chances remaining
		{
			//get(266) is 0011
			int index1=0;
			int index2=0;

			//Code may be written here
			if(check2==true){
				for(int i=0;i<width;i++){	//this loop make the buttons click 0011 combination which is the first combination according to the five guess algorithm
					for(int j=0;j<Character.getNumericValue((Pset.get(7).charAt(index1)));j++){
						colouredPegs[numGuesses][i].doClick();
					}
					colouredPegs [numGuesses][i].setBackground(choose(Character.getNumericValue((Pset.get(7).charAt(index1)))));
					index1++;
				}
				check2=false;
			}else{
				for(int i=0;i<width;i++){	//this loop make the buttons click 0011 combination which is the first combination according to the five guess algorithm
					for(int j=0;j<Character.getNumericValue((Pset.get(randomnumber).charAt(index2)));j++){
						colouredPegs[numGuesses][i].doClick();
					}
					colouredPegs [numGuesses][i].setBackground(choose(Character.getNumericValue((Pset.get(randomnumber).charAt(index2)))));
					index2++;
				}
			}

			System.out.println(Pset.size());
			Pset.remove(randomnumber);	//remove the random index element from the array that has already been used


			for(int i=0;i<Pset.size();i++){		//this is the loop to check if the black and the white pegs match with the other comibinations
				int [] checkarray=new int[4];	//array that contains elements that are in the Pset array, it changes every loop  
				for(int j=0;j<checkarray.length;j++){
					checkarray[j]=Character.getNumericValue((Pset.get(i).charAt(j)));
				}
				//if the number of white and the black pegs are not the same as the ones gotten from strtointarr for checkarray then discard then from the array Pset
				if(whites(strtointarr,hiddenGuess)!=whites(checkarray,strtointarr) | blacks(strtointarr,hiddenGuess)!=blacks(checkarray,strtointarr)){
					System.out.println(whites(strtointarr,hiddenGuess)+"whites");
					System.out.println(blacks(strtointarr,hiddenGuess)+"blacks");
					Pset.remove(i);	//remove the ones that doesnt fill the criteria
					i-=1;	//when the item is removed the index is reduced by one so that an item is not skipped
				}
			}



			for (int i=0;i<whiteThings;i++) 	//loop that makes white pegs visible
				whites[numGuesses][i].setVisible(true);

			for (int i=0;i<blackThings;i++)	//loop that makes black pegs visible
				blacks[numGuesses][i].setVisible(true);
			numGuesses++;	//numGuesses incremented once the black pegs and white pegs are displayed for that particular row


			if (numGuesses< height){ //if there are more chances remaining then the coloured pegs are set to visible
				for (int i=0;i<width;i++) {
					colouredPegs[numGuesses][i].setVisible(true);
				}
			}
			else { 
				for (int i=0;i<width;i++) //if the chances are finished then the computer guess is shown
					computerGuess[i].setVisible(true);
				int n = JOptionPane.showConfirmDialog(this,	//dialogue printed and option provided
						"You've lost! Would you like to play again?", "You've Lost!",
						JOptionPane.YES_NO_OPTION); 
				if (n == JOptionPane.NO_OPTION){ 	//if no was clicked then program is closed
					System.exit(0);
				}
				else { 	//if yes was clicked then game starts again
					check1=true;
					check2=true;
					dispose();
					new MasterMindSmart(height,width,numColors);
				}
			}
		}		
	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());	//changing the look and feel
		} catch (Exception e) {
			System.out.println(e.toString());	//if there was an error the error is printed as a string
		}
		new MasterMindSmart(10,4,5);	//new Mastermind obj created which gives 10 chances has 4 tiles across and 5 different colours
	}
}
