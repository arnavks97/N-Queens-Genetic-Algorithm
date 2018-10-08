import java.awt.*;
import javax.swing.*;

public class Tester extends JFrame {

	private Display dis;
	private Control con;
	
	public Tester() {
		super("N Queens Problem Solver");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		dis = new Display();
		con = new Control(dis);
		
		add(con, BorderLayout.WEST);
		add(dis, BorderLayout.EAST);
		pack();
		setLocationRelativeTo(null); //Puts the window in center
		setVisible(true);
	}
	
	public void start() {
		while(true) {
			con.update();
			
			try {
				dis.update();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String... args) {
		Tester t = new Tester();
		t.start();
	}
	
}
