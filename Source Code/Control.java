import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Control extends JPanel {

	private Display dis;
	private Font f = new Font("Ariel", Font.BOLD, 24), f1 = new Font("Ariel", Font.BOLD, 18);
	private String[] board, mutation, selection, population, generation, pause;
	private JButton update, restart;
	private JComboBox boardMenu, mutationMenu, selectionMenu, populationMenu, generationMenu, pauseMenu;
	
	public Control(Display dis) {
		this.dis = dis;
		setPreferredSize(new Dimension(300, 720));
		setBackground(new Color(196, 228, 255));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(new EmptyBorder(new Insets(150, 50, 150, 25))); //top, left, bottom, right
		
		board = new String[] {"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "36", "48", "60", "72", "80", "96", "100", "120", "144", "160", "180", "240", "360", "720"};
		mutation = new String[] {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
		selection = new String[] {"2", "3", "4", "5", "10", "15", "20", "25"};
		population = new String[] {"25", "50", "75", "100", "150", "200", "250", "300"};
		generation = new String[] {"50", "100", "200", "400", "1000", "4000", "Infinite"};
		pause = new String[] {"0", "10", "50"};
		
		boardMenu = new JComboBox(board);
	    mutationMenu = new JComboBox(mutation);
	    selectionMenu = new JComboBox(selection);
	    populationMenu = new JComboBox(population);
	    generationMenu = new JComboBox(generation);
	    pauseMenu = new JComboBox(pause);
	    
	    boardMenu.setSelectedItem("4");
	    mutationMenu.setSelectedItem("60");
	    selectionMenu.setSelectedItem("4");
	    populationMenu.setSelectedItem("100");
	    generationMenu.setSelectedItem("200");
	    pauseMenu.setSelectedItem("10");
	    
	    JLabel sizeLabel = new JLabel("BOARD SIZE:          ");
	    JLabel mutationLabel = new JLabel("MUTATION PROBABILITY:");
	    JLabel selectionLabel = new JLabel("SELECTION FACTOR:    ");
	    JLabel populationLabel = new JLabel("POPULATION:          ");
	    JLabel generationsLabel = new JLabel("NO. OF GENERATIONS:  ");
	    JLabel pauseLabel = new JLabel("PAUSE (ms):          ");
	    
	    update = new JButton("UPDATE");
	    
	    update.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent a) {
	    		dis.updateVar(getMutationChance(), getSelectionFactor(), getNumGenerations(), getPauseLength());
	    	}
	    });
	    
	    restart = new JButton("RESTART");
	    
	    restart.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent a) {
	    		dis.updateVar(getMutationChance(), getSelectionFactor(), getNumGenerations(), getPauseLength());
	    		dis.restart(getBoardSize(), getPopulationSize());
	    		repaint();
	    	}
	    });
	    
	    sizeLabel.setForeground(Color.BLACK);
	    mutationLabel.setForeground(Color.BLACK);
	    selectionLabel.setForeground(Color.BLACK);
	    populationLabel.setForeground(Color.BLACK);
	    generationsLabel.setForeground(Color.BLACK);
	    pauseLabel.setForeground(Color.BLACK);
	    
	    int offSet = 50;
	    
	    add(sizeLabel);
	    add(boardMenu);
	    add(Box.createRigidArea(new Dimension(0, offSet)));
	    add(mutationLabel);
	    add(mutationMenu);
	    add(Box.createRigidArea(new Dimension(0, offSet)));
	    add(selectionLabel);
	    add(selectionMenu);
	    add(Box.createRigidArea(new Dimension(0, offSet)));
	    add(populationLabel);
	    add(populationMenu);
	    add(Box.createRigidArea(new Dimension(0, offSet)));
	    add(generationsLabel);
	    add(generationMenu);
	    add(Box.createRigidArea(new Dimension(0, offSet)));
	    add(pauseLabel);
	    add(pauseMenu);
	    add(Box.createRigidArea(new Dimension(0, offSet)));
	    add(update);
	    add(Box.createRigidArea(new Dimension(10, 0)));
	    add(restart);
	}
	
	public void update() {
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		QSet bb = dis.getBestBoard();
		int fitness = bb.getFitness(), currGen = dis.getCurrGen();
		
		g.setColor(Color.BLACK);
		g.setFont(f);
	    g.drawString("N QUEENS", 80, 70);
	    g.setFont(f1);
		g.drawString("CURRENT GENERATION = " + currGen, 10, 600);
	    g.drawString("CURRENT FITNESS = " + fitness, 10, 650);
	}
	
	public int getBoardSize() {
		return Integer.parseInt(board[boardMenu.getSelectedIndex()]);
	}
	
	public int getMutationChance() {
		return Integer.parseInt(mutation[mutationMenu.getSelectedIndex()]);
		}
	
	public int getSelectionFactor() {
		return Integer.parseInt(selection[selectionMenu.getSelectedIndex()]);
	}
	
	public int getPopulationSize() {
		return Integer.parseInt(population[populationMenu.getSelectedIndex()]);
	}
	
	public int getNumGenerations() {
		String choice = generation[generationMenu.getSelectedIndex()];
		if(choice.equals("Infinite"))
			return (int) Double.POSITIVE_INFINITY;
		
		return Integer.parseInt(choice);
	}
	
	public int getPauseLength() {
		return Integer.parseInt(pause[pauseMenu.getSelectedIndex()]);
	}
	
}
