package rawReader;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;

public class MeanOfRegion implements ActionListener{
    
    private JFrame MeanOfRegionFrame;
    private JTextField upperLeftXField;
    private JTextField upperLeftYField;
    private JTextField lowerRightXField;
    private JTextField lowerRightYField;
    private JButton findMeanButton;
    private RawImage image;
    private JLabel meanResultLabel;
    
    public MeanOfRegion(RawImage image){
        this.image = image;
    }
    
    /**
     * @wbp.parser.entryPoint
     */
    public void make(){
        
        // initalizing gui window
        MeanOfRegionFrame = new JFrame("Mean Of Region Calculator");
        MeanOfRegionFrame.setResizable(false);
        MeanOfRegionFrame.setTitle( "Mean Of Region Calculator");
        MeanOfRegionFrame.setSize( 548,370 );
        MeanOfRegionFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        MeanOfRegionFrame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel panel = new JPanel();
        MeanOfRegionFrame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(4, 0, 0, 0));
        
        JPanel upperLeftBoundaryPanel = new JPanel();
        panel.add(upperLeftBoundaryPanel);
        
        JLabel upperLeftBoundaryLabel = new JLabel("Upper Left Boundary");
        upperLeftBoundaryLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        upperLeftBoundaryPanel.add(upperLeftBoundaryLabel);
        
        JLabel upperLeftXLabel = new JLabel("x:");
        upperLeftBoundaryPanel.add(upperLeftXLabel);
        
        upperLeftXField = new JTextField();
        upperLeftBoundaryPanel.add(upperLeftXField);
        upperLeftXField.setColumns(10);
        upperLeftXField.addActionListener( this );
        
        JLabel upperLeftYLabel = new JLabel("y:");
        upperLeftBoundaryPanel.add(upperLeftYLabel);
        
        upperLeftYField = new JTextField();
        upperLeftBoundaryPanel.add(upperLeftYField);
        upperLeftYField.setColumns(10);
        upperLeftYField.addActionListener(this);
        
        JPanel lowerRightBoundaryPanel = new JPanel();
        panel.add(lowerRightBoundaryPanel);
        
        JLabel lowerRightBoundaryLabel = new JLabel("Lower Right Boundary");
        lowerRightBoundaryLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lowerRightBoundaryPanel.add(lowerRightBoundaryLabel);
        
        JLabel lowerRightXLabel = new JLabel("x:");
        lowerRightBoundaryPanel.add(lowerRightXLabel);
        
        lowerRightXField = new JTextField();
        lowerRightBoundaryPanel.add(lowerRightXField);
        lowerRightXField.setColumns(10);
        lowerRightXField.addActionListener( this );
        
        JLabel lowerRightYLabel = new JLabel("y:");
        lowerRightBoundaryPanel.add(lowerRightYLabel);
        
        lowerRightYField = new JTextField();
        lowerRightBoundaryPanel.add(lowerRightYField);
        lowerRightYField.setColumns(10);
        lowerRightYField.addActionListener( this );
        
        findMeanButton = new JButton("Find Mean");
        panel.add(findMeanButton);
        findMeanButton.addActionListener( this );
        
        JPanel meanResultPanel = new JPanel();
        panel.add(meanResultPanel);
        
        meanResultLabel = new JLabel("Mean Of Region: ");
        meanResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        meanResultLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        meanResultPanel.add(meanResultLabel);
        
        MeanOfRegionFrame.pack();
        MeanOfRegionFrame.setVisible( true );

        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    	
        if( e.getSource() == findMeanButton || e.getSource() == upperLeftXField || e.getSource() == upperLeftYField 
        || e.getSource() == lowerRightXField || e.getSource() == lowerRightYField ){
        	PixelFieldChecker upperLeftXCheck = new PixelFieldChecker(upperLeftXField.getText(), image.getWidth(), PixelFieldChecker.WIDTH, MeanOfRegionFrame);
        	PixelFieldChecker upperLeftYCheck = new PixelFieldChecker(upperLeftYField.getText(), image.getHeight(), PixelFieldChecker.HEIGHT, MeanOfRegionFrame);
        	PixelFieldChecker lowerRightXCheck= new PixelFieldChecker(lowerRightXField.getText(), image.getWidth(), PixelFieldChecker.WIDTH, MeanOfRegionFrame);
        	PixelFieldChecker lowerRightYCheck= new PixelFieldChecker(lowerRightYField.getText(), image.getHeight(), PixelFieldChecker.HEIGHT, MeanOfRegionFrame);
        	boolean validUpperLeftX = upperLeftXCheck.verifyValid();
        	boolean validUpperLeftY = upperLeftYCheck.verifyValid();
        	boolean validLowerRightX = lowerRightXCheck.verifyValid();
        	boolean validLowerRightY= lowerRightYCheck.verifyValid();
        	
        	if( validUpperLeftX && validUpperLeftY && validLowerRightX && validLowerRightY){
        		
				int upperLeftX = Integer.parseInt( upperLeftXField.getText() );
				int upperLeftY = Integer.parseInt( upperLeftYField.getText() );
				int lowerRightX = Integer.parseInt( lowerRightXField.getText() );
				int lowerRightY = Integer.parseInt( lowerRightYField.getText() );
				
				if( upperLeftX > lowerRightX || upperLeftY > lowerRightY ){
			        JOptionPane.showMessageDialog( MeanOfRegionFrame,
			        "Cannot calculate; ensure upper left coordinates are less than lower right coordinates.", "Error", JOptionPane.PLAIN_MESSAGE);
				}else{
					int sum = 0;
					int pixelAmt = 0;
					for(int l = upperLeftY; l <= lowerRightY; l++){
						for(int w = upperLeftX; w<=lowerRightX; w++){
							try {
								sum = sum + image.getPixelValue( w, l );
								//System.out.println( "NOW CALCULATING PIXEL AT X = "+ w + " Y = " + l + ". VALUE IS: " + image.getPixelValue( w, l ));
								pixelAmt++;
							}catch (Exception e2) {
								JOptionPane.showMessageDialog( MeanOfRegionFrame,
								"Fatal issue encountered. Aborting.", "Error", JOptionPane.PLAIN_MESSAGE);
							}
						}
					}
					meanResultLabel.setText( "Mean of Region: " + (sum/pixelAmt) );
				}
        	}
        }
        
    }
}
