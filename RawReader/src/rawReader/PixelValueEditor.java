package rawReader;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

public class PixelValueEditor implements ActionListener{

    private JTextField xTextField;
    private JTextField yTextField;
    private JButton findButton;
    private JButton changeButton;
    private int xPixel;
    private int yPixel;
    private RawImage image;
    private JLabel PixelValueLabel;
    private JFrame PixelValueEditorFrame;
    private JTextField pixelValueField;
    private static final int MAX_PIXEL_INTENSITY = (int) Math.pow(2,10);
    
    public PixelValueEditor(RawImage image){
        this.image = image;
    }

    // Creates GUI elements for editor window 
    
    public void make(){
    	
        PixelValueEditorFrame = new JFrame("Pixel Value Editor");
        PixelValueEditorFrame.setResizable(false);
        PixelValueEditorFrame.setSize( 548,370 );
        PixelValueEditorFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        
        JPanel inputPanel = new JPanel();
        PixelValueEditorFrame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        
        JLabel xLabel = new JLabel("x:");
        inputPanel.add(xLabel);
        
        xTextField = new JTextField();
        inputPanel.add(xTextField);
        xTextField.setColumns(10);
        xTextField.addActionListener( this );
        
        JLabel yLabel = new JLabel("y:");
        inputPanel.add(yLabel);
        
        yTextField = new JTextField();
        inputPanel.add(yTextField);
        yTextField.setColumns(10);
        yTextField.addActionListener( this );
        
        findButton = new JButton("Find");
        inputPanel.add(findButton);
        findButton.addActionListener( this );
        
        JPanel infoPanel = new JPanel();
        PixelValueEditorFrame.getContentPane().add(infoPanel, BorderLayout.CENTER);
        
        PixelValueLabel = new JLabel("Pixel Value: ");
        infoPanel.add(PixelValueLabel);
        
        pixelValueField = new JTextField();
        infoPanel.add(pixelValueField);
        pixelValueField.setColumns(10);
        pixelValueField.addActionListener( this );
        
        changeButton = new JButton("Change");
        infoPanel.add(changeButton);
        changeButton.addActionListener( this );
        
        PixelValueEditorFrame.pack();
        PixelValueEditorFrame.setVisible( true );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
        if(e.getSource()==findButton||e.getSource()==xTextField || e.getSource() == yTextField){
        	
        	// checking if x and y values fields are entered in correctly
        	PixelFieldChecker pixelCheckX = new PixelFieldChecker(xTextField.getText(), image.getWidth()-1, PixelFieldChecker.WIDTH, PixelValueEditorFrame);
        	PixelFieldChecker pixelCheckY = new PixelFieldChecker(yTextField.getText(), image.getHeight()-1, PixelFieldChecker.HEIGHT, PixelValueEditorFrame);
        	boolean validX = pixelCheckX.verifyValid();
        	boolean validY = pixelCheckY.verifyValid();

        	if(validX&&validY){
                try {
                    xPixel = Integer.parseInt( xTextField.getText() );
                    yPixel = Integer.parseInt(  yTextField.getText() );
                    pixelValueField.setText( "" + image.getPixelValue( xPixel, yPixel ) );
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog( PixelValueEditorFrame,
                    "Pixel data could not be read." ,
                    "Error", JOptionPane.PLAIN_MESSAGE);
                }
        	}
        
        }else if (e.getSource() == changeButton||e.getSource()==pixelValueField){
        	
        	PixelFieldChecker pixelCheckX = new PixelFieldChecker(xTextField.getText(), image.getWidth()-1, PixelFieldChecker.WIDTH, PixelValueEditorFrame);
        	PixelFieldChecker pixelCheckY = new PixelFieldChecker(yTextField.getText(), image.getHeight()-1, PixelFieldChecker.HEIGHT, PixelValueEditorFrame);
        	PixelFieldChecker pixelValueCheck = new PixelFieldChecker(pixelValueField.getText(), MAX_PIXEL_INTENSITY, PixelFieldChecker.PIXEL_VALUE, PixelValueEditorFrame);
        	boolean validX = pixelCheckX.verifyValid();
        	boolean validY = pixelCheckY.verifyValid();
        	boolean validPixelValue = pixelValueCheck.verifyValid();
        	
        	if(validX && validY && validPixelValue){
				try {
					xPixel = Integer.parseInt( xTextField.getText() );
					yPixel = Integer.parseInt( yTextField.getText() );
					int oldValue=0;
					oldValue = image.getPixelValue( xPixel, yPixel );
					int newValue = Integer.parseInt( pixelValueField.getText());
					image.setPixelValue( xPixel, yPixel, newValue );
					//Main.editHistory( "Changed pixel value at ( " + xPixel + ", "+ yPixel + " )"+ " from "+oldValue + " to " + newValue +"." );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        }
        
        
    }
}
