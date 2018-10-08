import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Display extends JPanel {

	private int boardSz = 4, populationSz = 100, numGen = 200, currGen = 1, mutation = 60;
	private int selectionFactor = 4, pauseTime = 50, width = 720, height = 720, squareWidth = width / boardSz;
	private boolean isFinished = false;
	private QSet bestBoard, population[];
	
	public Display() {
		generateStartingPopulation();
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.DARK_GRAY);
	      
	    bestBoard = getBestBoard();
	    repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		drawBoard(g);
		placeQueen(g);
	}
	
	public QSet getBestBoard() {
		QSet board = population[0];
		int minFitness = Integer.MAX_VALUE;
		for(int i=0; i<populationSz; i++) {
			int fitness = population[i].getFitness();
			if(fitness < minFitness) {
				minFitness = fitness;
				board = population[i];
			}
		}
		
		return board;
	}
	
	private int[] generatePositions() {
		int[] pos = new int[boardSz];
		for(int i=0; i<boardSz; i++)
			pos[i] = (int) Math.floor(Math.random() * boardSz);
		
		return pos;
	}
	
	public void generateStartingPopulation() {
		population = new QSet[populationSz];
		for(int i=0; i<populationSz; i++)
	         population[i] = new QSet(generatePositions());
	}
	
	public void update() {
		if(currGen < numGen && bestBoard.getFitness() > 0) {
			try {
	             Thread.sleep(pauseTime);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			QSet[] parentArr = population.clone();
			for(int i=0; i<populationSz; i+=2) {
				QSet[] parents = selectParents(parentArr), children = reproduce(parents); 
				population[i] = children[0]; 
				
				if(i != populationSz - 1)
					population[i + 1] = children[1];
			}
			
			currGen++;
			bestBoard = getBestBoard();
			repaint();
		}
		else
			isFinished = true;
	}
	
	public void updateVar(int mutationProb, int selFactor, int numGens, int pauseT) {
		mutation = mutationProb;
		selectionFactor = selFactor;
		numGen = numGens;
		pauseTime = pauseT;
	}
	
	public void restart(int boardSz, int popSize) {
		this.boardSz = boardSz;
		squareWidth = width / boardSz;
		populationSz = popSize;
		generateStartingPopulation();
		currGen = 1;
		bestBoard = getBestBoard();
		isFinished = false;
		
		repaint();
	}
	
	public int[] split(int[] arr, int start, int end) {
		int len = end - start, brr[] = new int[len];
		for(int i=0; i<len; i++)
			brr[i] = arr[start+i];
		
		return brr;
	}
	
	public int[] stitch(int[] arr, int[] brr) {
		int[] crr = new int[arr.length + brr.length];
		for(int i=0; i<arr.length; i++)
			crr[i] = arr[i];
		
		for(int i=0; i<brr.length; i++)
			crr[arr.length + i] = brr[i];
		
		return crr;
	}
	
	public int[] mutate(int[] pos) {
		int rand = (int) Math.round(Math.random() * 100);
		if(rand <= mutation) {
			int randIdx = (int) Math.round(Math.random() * (boardSz-1));
			pos[randIdx] = (int) Math.round(Math.random() * (boardSz-1));
		}
		
		if(rand <= mutation/2) {
			//This avoids double mutation, which may occur
			int randIdx = (int) Math.round(Math.random() * (boardSz-1));
			pos[randIdx] = (int) Math.round(Math.random() * (boardSz-1));
		}
		
		return pos;
	}
	
	public QSet[] reproduce(QSet[] parents) {
		QSet[] child = new QSet[2];
		
		int[] firstParentPos, secondParentPos, firstChildPos, seconddChildPos;
		int partition = (int) (Math.random() * (boardSz - 1)) + 1;
		
		firstParentPos = split(parents[0].getPos(), 0, partition);
		secondParentPos = split(parents[1].getPos(), partition, boardSz);
		firstChildPos = stitch(firstParentPos, secondParentPos);
		
		firstParentPos = split(parents[0].getPos(), partition, boardSz);
		secondParentPos = split(parents[1].getPos(), 0, partition);
		seconddChildPos = stitch(secondParentPos, firstParentPos);
		
		firstChildPos = mutate(firstChildPos);
		seconddChildPos = mutate(seconddChildPos);
		
		child[0] = new QSet(firstChildPos);
		child[1] = new QSet(seconddChildPos);
		
		return child;
	}
	
	public QSet[] selectParents(QSet[] parentArr) {
		QSet[] parents = new QSet[2];
		Arrays.sort(parentArr, new Comparator<QSet>() {
			public int compare(QSet q1, QSet q2) {
				int fitness1 = q1.getFitness(), fitness2 = q2.getFitness();
	            
	            if(fitness1 < fitness2) 
	               return -1;
	            else if (fitness1 > fitness2)
	               return 1;
	            else
	               return 0;
			}
		});
	            
	    //The board configurations which are more fit should be given more chance to get into next generations
		for(int i=0; i<parents.length; i++) { 
			double rand = Math.random();
			int randIdx = (int) (populationSz * Math.pow(rand, selectionFactor));
			parents[i] = parentArr[randIdx];
		}
		
		return parents;
	}
	
	public int getCurrGen() {
		return currGen;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public Image loadImage() {
		try {
			return ImageIO.read(this.getClass().getResource("image.png"));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void drawBoard(Graphics g) {
		boolean yellow = false;
		int x, y;
		
		for(int i=0; i<boardSz; i++) {
			if(boardSz % 2 == 0)
				yellow = !yellow;
			
			for(int j=0; j<boardSz; j++) {
				x = j * squareWidth;
				y = i * squareWidth;
				
				if (yellow)
					g.setColor(new Color(107, 2, 7));
				else
					g.setColor(new Color(244, 232, 66));
				
				yellow = !yellow;
				g.fillRect(x, y, squareWidth, squareWidth); 
			}
		}
	}
	
	public void placeQueen(Graphics g) {
		int offset = squareWidth / 8, imgLen = squareWidth * 6 / 8, pos[] = bestBoard.getPos();
		Image img = loadImage();
		
		for(int i=0; i<pos.length; i++)
			g.drawImage(img, i*squareWidth + offset, pos[i]*squareWidth + offset, imgLen, imgLen, null);
	}
	
}
