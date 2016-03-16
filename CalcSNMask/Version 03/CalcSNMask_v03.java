//=================================================================================|
// The intent of this program is to calculate both binary and dotted quad          |
// details based off of an IP address and subnet mask extension input by the user. |
// The details printed include the following: IP Address in Binary and Dotted Quad,|  
// Subnet Mask Extension, Subnet Mask in Binary and Dotted Quad,                   |
// First Address in Binary and Dotted Quad, Last Address in Binary and Dotted Quad,|
// and Total Possible Addresses on the Network									   |
//=================================================================================|
// This program was primarily written for learning purposes for CS 344 - Java      |
// Programming and CS 361 Networks I (Pensacola Christian College Courses)         |
//=================================================================================|
// Note: For best results the screen should be expanded horizontally so that all   |
// text field labels and all 32-bits of binary may be viewable.                    |
//=================================================================================|
// Date of Last Revision: 3/14/2015                                                |
// Version Number: 03															   |
// Author: Andrew Whiting                                                          |
//=================================================================================|
//=================================================================================|
// Version Notes: Separated calculations and text field modifications into         |
// a calcAddresses() function and added enter key functionality to process info.   |
//==================================================================================
// Changelog:                                                                      |
// 3/4/2015 - Implemented enter key press functionality for calculate button, and  |
//             corrected some old comments.                                        |
// 3/5/2015 - Reordered code to allow p2 (Calculate Button) to be focusable.       |
//          - Changed a couple for loops to run array.length number of times       |
//            instead of hard coding the terminating value.                        |
//==================================================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.lang.Math;

public class CalcSNMask_v03 extends JFrame
{
	// Create text fields for the IP address, subnet mask extension,
	// IP address in binary, Subnet Mask in binary, first IP address
	// on the network, and second IP address on the network.
	private JTextField jtfIpAddress        = new JTextField();
	private JTextField jtfSubMaskExtension = new JTextField();
	private JTextField jtfBinIpAddress     = new JTextField();
	private JTextField jtfBinSubMask       = new JTextField();
	private JTextField jtfFirstAddr        = new JTextField();
	private JTextField jtfLastAddr         = new JTextField();
	private JTextField jtfTotalAddr        = new JTextField();
	private JTextField jtfSubnetMask       = new JTextField();
	private JTextField jtfBinFirstAddr     = new JTextField();
	private JTextField jtfBinLastAddr      = new JTextField();
	
	// Create a Calculate button
	private JButton jbtCalcResults = new JButton("Calculate Results");
	
	// Create program window
	public CalcSNMask_v03()
	{
		// Panel p1 to hold labels and text fields
		JPanel p1 = new JPanel(new GridLayout(5, 2));
		p1.add(new JLabel("IP Address"));
		p1.add(jtfIpAddress);
		p1.add(new JLabel("Subnet Mask Extension"));
		p1.add(jtfSubMaskExtension);
		p1.add(new JLabel("Subnet Mask in Binary"));
		p1.add(jtfBinSubMask);
		p1.add(new JLabel("First Address in Binary"));
		p1.add(jtfBinFirstAddr);
		p1.add(new JLabel("Last Address in Binary"));
		p1.add(jtfBinLastAddr);
		p1.setBorder(new TitledBorder("Enter IP Address and Subnet Mask Extension"));
		
		// Panel p2 to hold the button
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p2.add(jbtCalcResults);
		
		// Set focus for key events
		p1.setFocusable(true);
		p2.setFocusable(true);

		// Panel p3 to hold a second set of text fields
		JPanel p3 = new JPanel(new GridLayout(5,2));
		p3.add(new JLabel("IP Address in Binary"));
		p3.add(jtfBinIpAddress);
		p3.add(new JLabel("Total Possible Addresses"));
		p3.add(jtfTotalAddr);
		p3.add(new JLabel("Subnet Mask"));
		p3.add(jtfSubnetMask);
		p3.add(new JLabel("First Address on Network"));
		p3.add(jtfFirstAddr);
		p3.add(new JLabel("Last Address on Network"));
		p3.add(jtfLastAddr);
		p3.setBorder(new TitledBorder("Results"));
		
		// Add the panels to the frame
		add(p1, BorderLayout.WEST);
		add(p2, BorderLayout.SOUTH);
		add(p3, BorderLayout.EAST);
		
		// Register Listener
		jbtCalcResults.addActionListener(new ButtonListener());
		jbtCalcResults.addKeyListener(new KeyAdapter());
		jtfIpAddress.addKeyListener(new KeyAdapter());
		jtfSubMaskExtension.addKeyListener(new KeyAdapter());
	}
	
	// Handle the Calculate Results button
	private class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			calcAddresses();
		}
	}

    private class KeyAdapter implements KeyListener
	{
	    @Override
		public void keyPressed(KeyEvent e) 
		{
		   switch (e.getKeyCode())
			{
				// Process input on enter key pressed
				case KeyEvent.VK_ENTER:
				   calcAddresses();
				   break;
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
		}
		@Override
		public void keyTyped(KeyEvent e)
		{
		}
    }
	public static void main(String[] args)
	{
		CalcSNMask_v03 frame = new CalcSNMask_v03();
		frame.pack();
		frame.setTitle("Subnet Mask Calculator");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
    public void calcAddresses()
	{
		int[]    binaryValues = {128, 64, 32, 16, 8, 4, 2, 1}, // Reference array for 8 bit binary values
				 ipAddr       = {0, 0, 0, 0},                  // Dotted Quad IP address
				 firstAddr    = {0, 0, 0, 0},	               // Dotted Quad first IP address on network
				 lastAddr     = {0, 0, 0, 0},                  // Dotted Quad last IP address on network
				 subMaskAddr  = {0, 0, 0, 0};                  // Dotted Quad Subnet Mask address
		int      subMaskExtension,                             // Subnet mask extension value (ranges from 0-32)
				 totalAddr    = 0;                             // Total possible addresses on the network
		String[] binIpAddress = {"", "", "", ""},              // Binary version of ip address
				 ArrBinFirstAddr,                              // String array version of first address in binary
				 ArrBinLastAddr;							   // String array version of last address in binary
		String   binFirstAddr = "",                            // String to store first address in binary
				 binLastAddr  = "",                            // String to store last address in binary
				 binMaskExt   = "";                            // String to store subnet mask in binary
				 
		// Get the ip address from the IP address text box
		String[] ArrIpAddress = jtfIpAddress.getText().split("\\.");
		
		// Get integer values of ipAddr from String[] ipAddress
		for(int i = 0; i < ArrIpAddress.length; i++)
		{
			ipAddr[i] = Integer.parseInt(ArrIpAddress[i]);
		}
		
		// Get the integer Subnet Mask Extension from the Subnet Mask Extension text box
		subMaskExtension = Integer.parseInt(jtfSubMaskExtension.getText());
		
		// Calculate the total possible ip addresses on the network
		totalAddr = (int)Math.pow(2, (32 - (subMaskExtension)));
		
		// Generate a binary version of the user entered IP address
		for(int outer = 0; outer < 4; outer++) 
		{
			for(int inner = 0; inner < 8; inner++)
			{
				if(ipAddr[outer] >= binaryValues[inner])
				{
					binIpAddress[outer] += "1";
					ipAddr[outer]       -= binaryValues[inner];
				}
				else
					binIpAddress[outer] += "0";
			}
		}
		
		// Set binary versions of first and last IP address equal 
		// to the binary version of the user entered IP address and store 
		// binary addresses in related String arrays
		for(int i = 0; i < binIpAddress.length; i++)
		{
			binFirstAddr += binIpAddress[i];
			binLastAddr  += binIpAddress[i];
		}
		ArrBinFirstAddr = binFirstAddr.split("");
		ArrBinLastAddr  = binLastAddr.split("");

		// Set binary versions of the subnet mask
		for(int i = 1; i <= subMaskExtension; i++)
			binMaskExt += "1";	
		for(int i = 1; i <= (32 - subMaskExtension); i++)
			binMaskExt += "0";
		
		// Fill in String array versions of the binary first and last addresses
		// and reset the regular String first and last addresses to zero
		for(int i = subMaskExtension; i < 32; i++)
		{
			ArrBinFirstAddr[i] = "0";
			ArrBinLastAddr[i]  = "1";
		}
		binFirstAddr = "";
		binLastAddr  = "";
		
		// Update regular String version of binary first and last addresses with correct values
		for(int i = 0; i < 32; i++)
		{
			binFirstAddr += ArrBinFirstAddr[i];
			binLastAddr  += ArrBinLastAddr[i];
		}
		
		//Generate dotted quad version of the Subnet Mask
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(i == 0)
					if(binMaskExt.charAt(j) == '1')
						subMaskAddr[i] += binaryValues[j];
				if(i == 1)
					if(binMaskExt.charAt(j + 8) == '1')
						subMaskAddr[i] += binaryValues[j];
				if(i == 2)
					if(binMaskExt.charAt(j + 16) == '1')
						subMaskAddr[i] += binaryValues[j];
				if(i == 3)
					if(binMaskExt.charAt(j + 24) == '1')
						subMaskAddr[i] += binaryValues[j];
			}
		}
		
		// Generate dotted quad version of first IP address on the network
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(i == 0)
					if(binFirstAddr.charAt(j) == '1')
						firstAddr[i] += binaryValues[j];
				if(i == 1)
					if(binFirstAddr.charAt(j + 8) == '1')
						firstAddr[i] += binaryValues[j];
				if(i == 2)
					if(binFirstAddr.charAt(j + 16) == '1')
						firstAddr[i] += binaryValues[j];
				if(i == 3)
					if(binFirstAddr.charAt(j + 24) == '1')
						firstAddr[i] += binaryValues[j];
			}
		}	
		
		// Generate dotted quad version of the last address on the network
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(i == 0)
					if(binLastAddr.charAt(j) == '1')
						lastAddr[i] += binaryValues[j];
				if(i == 1)
					if(binLastAddr.charAt(j + 8) == '1')
						lastAddr[i] += binaryValues[j];
				if(i == 2)
					if(binLastAddr.charAt(j + 16) == '1')
						lastAddr[i] += binaryValues[j];
				if(i == 3)
					if(binLastAddr.charAt(j + 24) == '1')
						lastAddr[i] += binaryValues[j];
			}
		}
		// Set text fields for the panel p1
		jtfBinSubMask.setText(String.format("%s", binMaskExt));
		jtfBinFirstAddr.setText(String.format("%s", binFirstAddr));
		jtfBinLastAddr.setText(String.format("%s", binLastAddr));
		
		// Set text fields for panel p2 
		jtfBinIpAddress.setText(String.format("%s%s%s%s",
			binIpAddress[0], binIpAddress[1], binIpAddress[2], binIpAddress[3]));
		jtfTotalAddr.setText(String.format("%d (0 - %d)", totalAddr, (totalAddr - 1)));
		jtfSubnetMask.setText(String.format("%d.%d.%d.%d", subMaskAddr[0], subMaskAddr[1],
			subMaskAddr[2], subMaskAddr[3]));
		jtfFirstAddr.setText(String.format("%d.%d.%d.%d", 
			firstAddr[0], firstAddr[1], firstAddr[2], firstAddr[3]));
		jtfLastAddr.setText(String.format("%d.%d.%d.%d", 
			lastAddr[0], lastAddr[1], lastAddr[2], lastAddr[3]));
	}
}