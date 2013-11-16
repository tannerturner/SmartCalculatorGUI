
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorGUI extends JFrame {
	
	private JButton[] integers;
	private JButton decimal, add, sub, mul, div, 
	                clear, reset, equal;
	private JTextField operand, result;
	private static final Dimension stdBtn=new Dimension(50,50);
	private boolean isNum1=false, isNum2=false, 
			        isAns=false, isOp=false;
	private double num1, num2, ans;
	private String num1Txt="", num2Txt="", 
			       opDisplay="", resDisplay="";
	private char op;
	
	public CalculatorGUI() {
		super("Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JPanel nPanel=new JPanel(new GridLayout(2,2));
		JLabel opLabel=new JLabel(" Operand");
		JLabel resLabel=new JLabel("                     "
				                  +"Result"); 
		operand=new JTextField();
		operand.setEditable(false);
		result=new JTextField();
		result.setEditable(false);
		nPanel.add(opLabel);
		nPanel.add(resLabel);
		nPanel.add(operand);
		nPanel.add(result);
		add(nPanel, BorderLayout.NORTH);
		
		JPanel mainPanel=new JPanel(new GridLayout(0,4));
		integers=new JButton[10];
		for(int i=0; i<integers.length; i++) {
			integers[i]=new JButton(String.valueOf(i));
			integers[i].setPreferredSize(stdBtn);
			integers[i].addActionListener(new NumListener());
		} //setting up integer buttons
		decimal=new JButton(".");
		decimal.setPreferredSize(stdBtn);
		decimal.addActionListener(new NumListener());
		equal=new JButton("=");
		equal.setPreferredSize(stdBtn);
		equal.addActionListener(new OtherListener());
		add=new JButton("+");
		add.setPreferredSize(stdBtn);
		add.addActionListener(new OpListener());
		sub=new JButton("-");
		sub.setPreferredSize(stdBtn);
		sub.addActionListener(new OpListener());
		mul=new JButton("*");
		mul.setPreferredSize(stdBtn);
		mul.addActionListener(new OpListener());
		div=new JButton("/");
		div.setPreferredSize(stdBtn);
		div.addActionListener(new OpListener());
		
		for(int i=7; i<10; i++)
			mainPanel.add(integers[i]);
		mainPanel.add(add);
		for(int i=4; i<7; i++)
			mainPanel.add(integers[i]);
		mainPanel.add(sub);
		for(int i=1; i<4; i++)
			mainPanel.add(integers[i]);
		mainPanel.add(mul);
		mainPanel.add(integers[0]);
		mainPanel.add(decimal);
		mainPanel.add(equal);
		mainPanel.add(div);
		add(mainPanel, BorderLayout.CENTER);
		
		JPanel clears=new JPanel(new GridLayout(1,0));
		clear=new JButton("Clear");
		clear.setPreferredSize(stdBtn);
		clear.addActionListener(new OtherListener());
		clear.setActionCommand("c");
		reset=new JButton("Reset");
		reset.setPreferredSize(stdBtn);
		reset.addActionListener(new OtherListener());
		reset.setActionCommand("r");
		clears.add(clear);
		clears.add(reset);
		add(clears, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	public void calculate() {
		if(num2Txt.equals(".")||!isNum2) {
			num2=0;
			num2Txt=String.valueOf(num2);
			isNum2=true; //if user doesn't enter a second number or just a decimal assume they meant 0
		}					
		
		num2=Double.parseDouble(num2Txt); //turn the number 2 String into a double
		switch(op)
		{
		case '+': ans=num1+num2; break;
		case '-': ans=num1-num2; break;
		case '*': ans=num1*num2; break;
		case '/': if(num2==0.0) {
					result.setText("Undefined");
					num2Txt="";
					opDisplay=num1Txt+" "+op+" "+num2Txt;
					operand.setText(opDisplay);
					isNum2=false;
					return; //if user divides by 0, second number is erased from memory and display
				} else ans=num1/num2; break;
		}
		resDisplay=String.valueOf(ans);
		result.setText(resDisplay);
		num2Txt="";
		isNum2=false;
		isOp=false;
		isAns=true;
		clear(); //after calculation is performed, display answer, signify that there is no operation
	}			 //desired, that an answer was calculated, and erase numbers in memory
	
	public void clear() {
		num1Txt="";
		opDisplay=num1Txt;
		operand.setText(opDisplay);
		isNum1=false;
		isOp=false;
	}
	
	public void reset() { 
		resDisplay="";
		result.setText(resDisplay);
		num2Txt="";
		isNum2=false;
		isAns=false;
		return;
	}

	private class NumListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton source=(JButton)e.getSource();
			char val=source.getText().charAt(0);
			
			if(!isOp) {                             //if an operation has not been designated
				if(val=='.'&&num1Txt.contains(".")) //the number being entered is the first one
					return;	//user can't add more than one decimal in one number
				num1Txt+=val;
				opDisplay=num1Txt;
				operand.setText(opDisplay);
				isNum1=true;	
				isNum2=false;	//add integers to the number (currently a String) until an operation is
				isAns=false;	//entered, and update the display for each new integer entered. signify
			} else {			//there is a 1st number, but not a 2nd or answer
				if(val=='.'&&num2Txt.contains("."))
					return;
				num2Txt+=val;
				opDisplay=num1Txt+" "+op+" "+num2Txt;
				operand.setText(opDisplay);
				isNum2=true;
			}					//does same as above, but with 2nd number
		}
	}
	
	private class OpListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(isNum2)		//if there are already 2 numbers and another operation button is pressed,
				calculate();//calculate what is currently waiting to be calculated before doing more
			
			JButton source=(JButton)e.getSource();
			op=source.getText().charAt(0);
						
			if(!isOp) {		//do all this if this is the first time operation button is being pressed
				if(isAns) { //for a new operation
					num1=Double.parseDouble(resDisplay);
					num1Txt=String.valueOf(num1);
					opDisplay=num1Txt;
					operand.setText(opDisplay);
					isNum1=true;	//if an operation button was pressed directly after an answer was
				}					//obtained and displayed from a prior calculation, treat that answer
									//as if it were the first number of a new calculation and display it
				if(num1Txt.equals(".")||!isNum1) {
					num1=0;
					isNum1=true; //if user doesn't enter a 1st number or just a decimal assume they meant 0
				} else
					num1=Double.parseDouble(num1Txt);
								 //else turn the String for number 1 into a double and store it for later	
				num1Txt=String.valueOf(num1);
				isOp=true;
			}
			opDisplay=num1Txt+" "+op+" ";
			operand.setText(opDisplay);	//display first number and operating symbol. if this is not the first
		}								//time an operation button is being pressed, it changes the operation
	}									//and updates the operation window to display the new symbol
	
	private class OtherListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton source=(JButton)e.getSource();
			char command=source.getActionCommand().charAt(0);
			
			switch(command) {
				case '=': calculate(); break;
				case 'r': reset();	//when reset button is pressed, reset() and clear() are invoked
				case 'c': clear(); break;
			}
		}
	}
	
	public static void main(String[] args) {
		new CalculatorGUI();
	}
}
