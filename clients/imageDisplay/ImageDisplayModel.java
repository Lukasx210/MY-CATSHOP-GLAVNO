package clients.imageDisplay;

import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReader;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the image display client
 * Displays images of stock items.
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class ImageDisplayModel extends Observable
{
    private StockReader theStock = null;

    /**
     * Set up initial connection to the stock reader
     * @param mf Factory to deliver stock reader objects
     */
    public ImageDisplayModel(MiddleFactory mf)
    {
        try
        {
            theStock = mf.makeStockReader();
        } catch (Exception e)
        {
            DEBUG.error("ImageDisplayModel: " + e.getMessage());
        }
        new Thread(() -> backgroundRun()).start();
    }

    /**
     * Run as a thread in the background to continually update the display
     */
        public void backgroundRun() {
        while (true) {
        try {
            Thread.sleep(2000);
            DEBUG.trace("ImageDisplayModel call view");

            // TODO: Replace this with the actual list of product numbers
            List<String> productNumbers = List.of("0001", "0005", "0003", "0006", "0007");
            
            setChanged();
            notifyObservers(productNumbers);
        } catch (InterruptedException e) {
            DEBUG.error("%s\n%s\n",
                    "ImageDisplayModel.run()",
                    e.getMessage());
        }
        }
    }
    
    public List<ImageIcon> getImages(List<String> productNumbers) {
        List<ImageIcon> imageIcons = new ArrayList<>();

        try {
        for (String pNum : productNumbers) {
            ImageIcon imageIcon = theStock.getImage(pNum);
            if (imageIcon != null) {
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                imageIcons.add(new ImageIcon(scaledImage));
            }
        }
        } catch (StockException e) {
        DEBUG.error("ImageDisplayModel.getImages(): " + e.getMessage());
        }

        return imageIcons;
        }

    // Will be called by the viewOfImageDisplay
    // when it is told that the view has changed
    
    }