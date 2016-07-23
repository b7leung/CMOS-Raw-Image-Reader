package rawReader;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
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
    private ProcessRaw image;
    private JLabel meanResultLabel;
    
    public MeanOfRegion(ProcessRaw image){
        this.image = image;
    }
    
    /**
     * @wbp.parser.entryPoint
     */
    public void make(){
        
        // initalizing gui window
        MeanOfRegionFrame = new JFrame("Mean Of Region Calculator");
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
        meanResultLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
        meanResultPanel.add(meanResultLabel);
        
        MeanOfRegionFrame.pack();
        MeanOfRegionFrame.setVisible( true );

        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == findMeanButton){
            int upperLeftX = Integer.parseInt( upperLeftXField.getText() );
            int upperLeftY = Integer.parseInt( upperLeftYField.getText() );
            int lowerRightX = Integer.parseInt( lowerRightXField.getText() );
            int lowerRightY = Integer.parseInt( lowerRightYField.getText() );
            int sum = 0;
            int pixelAmt = 0;
            for(int l = upperLeftY; l <= lowerRightY; l++){
                for(int w = upperLeftX; w<=lowerRightX; w++){
                    try {
                        sum = sum + image.getPixelValue( w, l );
                        //System.out.println( "NOW CALCULATING PIXEL AT X = "+ w + " Y = " + l + ". VALUE IS: " + image.getPixelValue( w, l ));
                        pixelAmt++;
                        //System.out.println( "SUM " + sum );
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
            meanResultLabel.setText( "Mean of Region: " + (sum/pixelAmt) );
        }
        
    }
}
