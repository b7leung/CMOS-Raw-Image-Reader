package rawReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;

public class Main extends JFrame implements ActionListener{
    
    private File rawFile;
    private int xPixel;
    private int yPixel;
    private JButton findButton;
    private JTextField xText;
    private JTextField yText;
    private ProcessRaw image;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private boolean fileChosen = false;
    private JLabel lblFileName;
    private JLabel lblHeight;
    private JLabel lblWidth;
    private JLabel lblPixelValue;
    private JPanel panel_1;
    private JButton btnChange;
    private JMenuItem mntmSave;
    private JMenu mnMode;
    private JMenuItem mntmRegionOfInterest;
    private JMenuItem mntmPixelValueEditor;

    public Main(){
        
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            System.err.println( "Could not set correct GUI parameters. Aborting program." );
            e1.printStackTrace();
            System.exit( 1 );
        }
        
        JFrame frame = new JFrame(".raw Reader");
        frame.setSize( 548,370);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic( KeyEvent.VK_F );
        menuBar.add( fileMenu );
        
        openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener( this );
        fileMenu.add( openMenuItem );
        
        mntmSave = new JMenuItem("Save As Bitmap");
        fileMenu.add(mntmSave);
        
        frame.setJMenuBar( menuBar );
        
        mnMode = new JMenu("Tools");
        menuBar.add(mnMode);
        
        mntmRegionOfInterest = new JMenuItem("Calculate Mean of Region");
        mnMode.add(mntmRegionOfInterest);
        
        mntmPixelValueEditor = new JMenuItem("Pixel Value Editor");
        mnMode.add(mntmPixelValueEditor);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        mntmPixelValueEditor.addActionListener( this );
        
        JPanel panel_2 = new JPanel();
        frame.getContentPane().add(panel_2, BorderLayout.NORTH);
        panel_2.setLayout(new GridLayout(0, 1, 0, 0));
        
        lblFileName = new JLabel("File Name:");
        panel_2.add(lblFileName);
        
        lblHeight = new JLabel("Height: ");
        panel_2.add(lblHeight);
        
        lblWidth = new JLabel("Width:");
        panel_2.add(lblWidth);
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JLabel xLabel = new JLabel("x");
        panel.add( xLabel );
        
        xText = new JTextField(5);
        panel.add( xText );
        
        JLabel yLabel = new JLabel("y");
        panel.add( yLabel );
        
        yText = new JTextField(5);
        panel.add( yText );
        
        findButton = new JButton("Find");
        findButton.addActionListener( this );
        panel.add( findButton );
        
        panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
        
        lblPixelValue = new JLabel("Pixel Value:");
        panel_1.add(lblPixelValue);
        
        btnChange = new JButton("Change");
        panel_1.add(btnChange);
        
        frame.setVisible( true );
    }
    
    public void actionPerformed(ActionEvent e){
        if( e.getSource() == openMenuItem ){
            JFileChooser fileChooser = new JFileChooser();
            int fileOpenReturn = fileChooser.showOpenDialog(null);
            if(fileOpenReturn == JFileChooser.APPROVE_OPTION){
                rawFile = fileChooser.getSelectedFile();
                fileChosen = true;
                try {
                    image = new ProcessRaw( rawFile );
                    lblFileName.setText( "File Name: " + rawFile.getName());
                    lblHeight.setText( "Height: "+image.getHeight() + " pixels");
                    lblWidth.setText( "Width: "+image.getWidth() + " pixels");
                } catch (IOException error) {
                    System.err.println( "File invalid" );
                    // TODO Auto-generated catch block
                    error.printStackTrace();
                }
            }
        } else if(e.getSource() == findButton && fileChosen ){
            
                xPixel = Integer.parseInt( xText.getText() );
                yPixel = Integer.parseInt(  yText.getText() );
                boolean valid = true;
                if(xPixel >= image.getWidth() ){
                    System.out.println( "Error: " + xPixel + " is too big for the image width of " + image.getWidth() );
                    valid = false;
                }
                if (xPixel < 0){
                    System.out.println( "Error: pixel's x-coordinate cannot be negative" ); 
                    valid = false;
                }
                if ( yPixel >= image.getWidth() ){
                    System.out.println( "Error: " + yPixel + " is too big for the image height of " + image.getHeight() );
                    valid = false;
                }
                if ( yPixel < 0 ){
                    System.out.println( "Error: pixel's y-coordinate cannot be negative" ); 
                    valid = false;
                } 
                if(valid){
                    try {
                        System.out.println( "The pixel data at ( " + xPixel + ", " + yPixel + " ) is "
                        + image.getPixelValue( xPixel, yPixel ) );
                    } catch (IOException e1) {
                        System.err.println( "Pixel Data could not be read." );
                    }
                }
        }else if (e.getSource() == mntmPixelValueEditor){
            PixelValueEditor pixelEditFrame = new PixelValueEditor(image);
            pixelEditFrame.make();
        }
    }
    
    public static void main(String args[]){
        
        Main gui = new Main();
    }
}






