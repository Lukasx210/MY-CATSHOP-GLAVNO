package clients.imageDisplay;

import middle.MiddleFactory;
import java.awt.geom.GeneralPath;
import clients.customer.CustomerModel;
import clients.imageDisplay.ImageDisplayModel;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import middle.StockReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.stream.Collectors;

/**
 * The visual display seen by customers (Change to graphical version)
 * Displays images of stock items.
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class ImageDisplayView extends Canvas implements Observer
{
    private static final long serialVersionUID = 1L;
    private Image theImage = null;
    private ImageDisplayController cont = null;
    private int H = 300;         // Height of window 
      private int W = 400;         // Width  of window 

    /**
     * Construct the view
     * @param rpc Window in which to construct
     * @param mf  Factor to deliver image reader objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public ImageDisplayView(RootPaneContainer rpc, MiddleFactory mf, int x, int y)
    {
        Container cp = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc;         // Root Window
        cp.setLayout(new BorderLayout());             // Border N E S W CENTER
        rootWindow.setSize(W, H);                     // Size of Window
        rootWindow.setLocation(x, y);                 // Position on screen
        rootWindow.add(this, BorderLayout.CENTER);    //  Add to root window

        rootWindow.setVisible(true);                  // Make visible
    }

    public void setController(ImageDisplayController c)
    {
        cont = c;
    }

    /**
     * Called to update the display in the shop
     */
    @Override
    public void update(Observable aModelOfImageDisplay, Object arg) {
    List<ImageIcon> imageIcons = ((ImageDisplayModel) aModelOfImageDisplay).getImages((List<String>) arg);

        if (imageIcons != null && !imageIcons.isEmpty()) {
        // Assuming you want to display all images in the list
        List<Image> images = imageIcons.stream().map(ImageIcon::getImage).collect(Collectors.toList());
        theImage = createCombinedImage(images);
        repaint(); // Draw graphically
    }
    }
    
    private Image createCombinedImage(List<Image> images) {
        int totalWidth = 80 * images.size(); // Assuming each image has a width of 80
        int totalHeight = 80; // Assuming each image has a height of 80

        BufferedImage combinedImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();

        int currentX = 0;
        for (Image image : images) {
        g.drawImage(image, currentX, 0, null);
        currentX += 80; // Assuming each image has a width of 80
        }

        g.dispose();
        return combinedImage;
    }

    @Override
    public void update(Graphics g)        // Called by repaint
    {
        drawScreen((Graphics2D) g);         // Draw information on screen
    }

    /**
     * Redraw the screen double buffered
     * @param g Graphics context
     */
    @Override
    public void paint(Graphics g)         // When 'Window' is first
    {
        drawScreen((Graphics2D) g);         // Draw information on screen
    }

    /**
     * Redraw the screen
     * @param g Graphics context
     */
    public void drawScreen(Graphics2D g) {
        Dimension d = getSize(); // Size of the image
        g.setPaint(Color.lightGray); // Paint Colour

        g.fill(new Rectangle(0, 0, d.width, d.height));

        // Draw the arrow icon
        drawArrow(g, d.width / 2, d.height / 2);

        // Draw the message
        String message = "Here are some of our lovely items, get them now!";
        Font font = new Font("Arial", Font.BOLD, 16);
        g.setFont(font);
        g.setColor(Color.BLACK);

        FontMetrics metrics = g.getFontMetrics(font);
        int messageWidth = metrics.stringWidth(message);
        int messageHeight = metrics.getHeight();

        int messageX = (d.width - messageWidth) / 2;
        int messageY = d.height / 2 + messageHeight / 2 + 60; // Adjust Y position based on font metrics and spacing

        g.drawString(message, messageX, messageY);
        
        // Underline the message
        int underlineY = messageY + 5; // Adjust the position of the underline based on your preference
        g.drawLine(messageX, underlineY, messageX + messageWidth, underlineY);
        
        // Draw the image
        if (theImage != null) {
        g.drawImage(theImage, 0, 0, this); // Draw the image
        }
    }

    /**
     * Draw an arrow icon at the specified position
     * @param g Graphics context
     * @param x X-coordinate of the arrow's center
    * @param y Y-coordinate of the arrow's center
     */
    private void drawArrow(Graphics2D g, int x, int y) {
        int size = 45; // Size of the arrow
        int halfSize = size / 2;

        GeneralPath arrow = new GeneralPath();
        arrow.moveTo(x - halfSize, y + halfSize);
        arrow.lineTo(x + halfSize, y + halfSize);
        arrow.lineTo(x, y - halfSize);
        arrow.lineTo(x - halfSize, y + halfSize);

        g.setColor(Color.RED);
        g.fill(arrow);
    }
    
}
