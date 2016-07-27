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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

public class Main extends JFrame implements ActionListener{
    
	// constant string messages
    private static final String UNOPENED_FILE_ERROR = new String("A .raw file must be opened first, under File > Open.");
    private static final String INVALID_FILE_ERROR = new String("Invalid .raw file. "
    		+ "Check that file is a .raw file, permissions to read/write are granted, and that no other processes are accessing the file.");
    private static final String TRANSFORMATION_ERROR = new String("Could not perform requested transformation.");
    
    // elements relating to the image chosen
    private File rawFile;
    private int xPixel;
    private int yPixel;
    private ProcessRaw image;
    private boolean fileChosen = false;
    private EditPictureUtils imageEditor;
    private static String filename;

    // Menu Bar elements
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenuItem openSubmenu;
    private final JMenuItem saveAsSubmenu;
    private final JMenu toolsMenu;
    private final JMenuItem regionOfInterestSubmenu;
    private final JMenuItem pixelValueEditorSubmenu;
    private final JMenuItem rotateRightSubmenu;
    private final JMenuItem rotateLeftSubmenu;
    private final JMenuItem flipVerticallySubmenu;
    private final JMenuItem flipHorizontallySubmenu;
    private final JMenu editMenu;
    private ArrayList<JMenuItem> fileDependent = new ArrayList<JMenuItem>();
    
    // GUI Elements
    private final JFrame frame;
    private final JLabel FileNameLabel;
    private final JLabel HeightLabel;
    private final JLabel WidthLabel;
    private final JLabel historyLabel;
    private final JPanel historyPanel;
    private static JTextArea historyTextArea;
    private final JPanel historyLabelPanel;

    public Main(){
    	
        // setting up GUI window
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            System.err.println( "Could not set correct GUI parameters. Aborting program." );
            e1.printStackTrace();
            System.exit( 1 );
        }
        
        frame = new JFrame(".raw Reader");
        frame.setSize( 548,370);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        // configuring menu bar elements
        menuBar = new JMenuBar();      
        fileMenu = new JMenu("File");
        menuBar.add( fileMenu );
        
        openSubmenu = new JMenuItem("Open");
        openSubmenu.addActionListener( this );
        fileMenu.add( openSubmenu );
        
        saveAsSubmenu = new JMenuItem("Save As Bitmap");
        saveAsSubmenu.setEnabled(false);
        fileDependent.add(saveAsSubmenu);
        fileMenu.add(saveAsSubmenu);
        saveAsSubmenu.addActionListener( this );
        
        frame.setJMenuBar( menuBar );
        
        toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);
        
        regionOfInterestSubmenu = new JMenuItem("Calculate Mean of Region");
        regionOfInterestSubmenu.setEnabled(false);
        fileDependent.add(regionOfInterestSubmenu);
        toolsMenu.add(regionOfInterestSubmenu);
        regionOfInterestSubmenu.addActionListener( this );
        
        pixelValueEditorSubmenu = new JMenuItem("Pixel Value Editor");
        pixelValueEditorSubmenu.setEnabled(false);
        fileDependent.add(pixelValueEditorSubmenu);
        toolsMenu.add(pixelValueEditorSubmenu);
        
        editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        
        rotateRightSubmenu = new JMenuItem("Rotate Right");
        rotateRightSubmenu.setEnabled(false);
        fileDependent.add(rotateRightSubmenu);
        editMenu.add(rotateRightSubmenu);
        rotateRightSubmenu.addActionListener(this);
        
        rotateLeftSubmenu = new JMenuItem("Rotate Left");
        rotateLeftSubmenu.setEnabled(false);
        fileDependent.add(rotateLeftSubmenu);
        editMenu.add(rotateLeftSubmenu);
        rotateLeftSubmenu.addActionListener(this);
        
        flipHorizontallySubmenu = new JMenuItem("Flip Horizontally");
        flipHorizontallySubmenu.setEnabled(false);
        fileDependent.add(flipHorizontallySubmenu);
        editMenu.add(flipHorizontallySubmenu);
        flipHorizontallySubmenu.addActionListener(this);
        
        flipVerticallySubmenu = new JMenuItem("Flip Vertically");
        flipVerticallySubmenu.setEnabled(false);
        fileDependent.add(flipVerticallySubmenu);
        editMenu.add(flipVerticallySubmenu);
        flipVerticallySubmenu.addActionListener(this);
        
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        pixelValueEditorSubmenu.addActionListener( this );
        
        // creating initial GUI graphic elements
        JPanel imageInfoPane = new JPanel();
        frame.getContentPane().add(imageInfoPane, BorderLayout.NORTH);
        imageInfoPane.setLayout(new GridLayout(0, 1, 0, 0));
        
        FileNameLabel = new JLabel("File Name:");
        FileNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imageInfoPane.add(FileNameLabel);
        
        HeightLabel = new JLabel("Height: ");
        HeightLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imageInfoPane.add(HeightLabel);
        
        WidthLabel = new JLabel("Width:");
        WidthLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imageInfoPane.add(WidthLabel);
        
        historyPanel = new JPanel();
        frame.getContentPane().add(historyPanel, BorderLayout.SOUTH);
        
        historyTextArea = new JTextArea(10, 60);
        historyTextArea.setLineWrap(false);
        historyTextArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(historyTextArea);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        
        historyPanel.add(scroll);
        
        historyLabelPanel = new JPanel();
        frame.getContentPane().add(historyLabelPanel, BorderLayout.CENTER);
        
        historyLabel = new JLabel("History:");
        historyLabelPanel.add(historyLabel);
        
        frame.pack();
        frame.setVisible( true );
    }
    
    public void actionPerformed(ActionEvent e){
        
        // actions for selecting and opening .raw file
        if( e.getSource() == openSubmenu ){
        	
        	// opening file browser
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter( "RAW files", "raw");
            fileChooser.setFileFilter( filter );
            int fileOpenReturn = fileChooser.showOpenDialog(null);
            
            // checking if file chosen was a valid .raw file
            if(fileOpenReturn == JFileChooser.APPROVE_OPTION){
                String filenameBuffer = new String(fileChooser.getSelectedFile()+"");
                try{
                    String fileExtension = filenameBuffer.substring( filenameBuffer.lastIndexOf( "." ) +1); 
                    if(fileExtension.equals("raw")){
                    	
                    	// if the file is valid, then name/height/width values are initialized
                        rawFile = fileChooser.getSelectedFile();
                        fileChosen = true;
                        image = new ProcessRaw( rawFile );
                        filename = new String(rawFile.getName());
                        FileNameLabel.setText( "File Name: " + rawFile.getName());
                        HeightLabel.setText( "Height: "+image.getHeight() + " pixels");
                        WidthLabel.setText( "Width: "+image.getWidth() + " pixels");
                        for(int i = 0; i < fileDependent.size(); i++){
                        	fileDependent.get(i).setEnabled(true);
                        }
                        imageEditor = new EditPictureUtils(image);
                        editHistory("Added image.");
                    }else{
                        throw new IllegalArgumentException();
                    }
                }catch(Exception fileNameError){
                    JOptionPane.showMessageDialog( frame, INVALID_FILE_ERROR,
                    "Error", JOptionPane.PLAIN_MESSAGE);
                }
            }
        // actions for opening pixel value editor
        } else if (e.getSource() == pixelValueEditorSubmenu){
        	
                PixelValueEditor pixelEditFrame = new PixelValueEditor(image);
                pixelEditFrame.make();
                
        } else if (e.getSource() == regionOfInterestSubmenu){
        	
                MeanOfRegion meanOfRegionFrame = new MeanOfRegion(image);
                meanOfRegionFrame.make();
                
        } else if (e.getSource() == saveAsSubmenu){

        } else if (e.getSource() == rotateRightSubmenu){
        	
        	image = imageEditor.rotateRight();
        	
        } else if (e.getSource() == rotateLeftSubmenu){
        	
        	image = imageEditor.rotateLeft();
        	
        } else if (e.getSource() == flipHorizontallySubmenu){
        	
        	image = imageEditor.flipHorizontally();
        	
        } else if (e.getSource() == flipVerticallySubmenu){
        	
        	try{
        		image = imageEditor.flipVertically();
        	}catch(Exception exception){
                JOptionPane.showMessageDialog( frame, TRANSFORMATION_ERROR,
                "Error", JOptionPane.PLAIN_MESSAGE);
        	}
        } 
    }
    
    public static void main(String args[]){
        Main gui = new Main();
    }
    
    public static void editHistory(String text){
       historyTextArea.append(filename + " -- "+text+"\n"); 
    }

}






