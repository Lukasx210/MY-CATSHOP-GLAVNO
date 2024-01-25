package clients.customer;

import catalogue.Basket;
import catalogue.Product;                    
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerModel extends Observable
{
  private Product     theProduct = null;          // Current product
  private Basket      theBasket  = null;          // Bought items

  private String      pn = "";                    // Product being processed

  private StockReader     theStock     = null;
  private OrderProcessing theOrder     = null;
  private ImageIcon       thePic       = null;
  private Map<String, String> productNameToNumber = new HashMap<>();

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          // 
    {  
      theStock = mf.makeStockReader();           // Database access
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theBasket = makeBasket();                    // Initial Basket
    initializeProductNameToNumberMap();  // Initialize the product name to number map
  }
  
  /**
     * Initialize the map from product names to product numbers manually
     */
    private void initializeProductNameToNumberMap() {
      productNameToNumber.put("40 inch led hd tv", "0001");
      productNameToNumber.put("dab radio", "0002");
      productNameToNumber.put("toaster", "0003");
      productNameToNumber.put("watch", "0004");
      productNameToNumber.put("digital camera", "0005");
      productNameToNumber.put("mp3 player", "0006");
      productNameToNumber.put("32gb usb2 drive", "0007");
    }
  
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket()
  {
    return theBasket;
  }
  
    public void doSearchByName(String productName) {
    productName = productName.trim().toLowerCase();
    String productNumber = productNameToNumber.get(productName);

    if (productNumber != null) {
        doCheck(productNumber);  // Call the existing doCheck method with product number
    } else {
        setChanged();
        notifyObservers("Product not found");
    }
    }

      /**
     * Check if the product is in Stock, search by product number or name
     * @param input The user input (product number or name)
     */
      public void doCheck(String input) {
      theBasket.clear(); // Clear shopping basket
      String theAction = "";
      input = input.trim(); // Trim input

      try {
        if (input.matches("\\d+")) {
          // If the input is a number, assume it's a product number
          handleProductNumberInput(input);
        } else {
          // If the input is not a number, assume it's a product name
          handleProductNameInput(input);
        }
      } catch (StockException e) {
        DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
        theAction = "Error checking product";
      }

      setChanged();
      notifyObservers(theAction);
    }

     private void handleProductNumberInput(String productNum) throws StockException {
        Product pr = theStock.getDetails(productNum);
      handleProductDetails(pr, productNum);
        }

    private void handleProductNameInput(String productName) throws StockException {
      // Use the map to find the product number corresponding to the given product name
      String productNum = productNameToNumber.get(productName.toLowerCase());

      if (productNum != null) {
        // If the product number is found, retrieve product details
        Product pr = theStock.getDetails(productNum);
        handleProductDetails(pr, productNum);
      } else {
        setChanged();
        notifyObservers("Product not found");
      }
        }

    private void handleProductDetails(Product pr, String productNum) {
      String theAction;
      pn = productNum.trim();  // Use productNum to get the product number
      int amount = 1; // Quantity

      try {
        if (pr.getQuantity() >= amount) {
      // Product is in stock
          theAction = String.format("%s : %7.2f (%2d) ",
          pr.getDescription(),
          pr.getPrice(),
          pr.getQuantity());
          pr.setQuantity(amount);
          theBasket.add(pr);
          thePic = theStock.getImage(pn); // Use pn here
        } else {
      // Product is not in stock
      theAction = pr.getDescription() + " not in stock";
        }
      } catch (StockException e) {
        DEBUG.error("CustomerModel.handleProductDetails()\n%s", e.getMessage());
        theAction = "Error loading image";
        thePic = null; // Set thePic to null to handle the error
      }

      // Set theAction outside the try-catch block
      setChanged();
      notifyObservers(theAction);
    }
    
  /**
   * Clear the products from the basket
   */
  public void doClear()
  {
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Name or Number";       // Set display
    thePic = null;                            // No picture
    setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */ 
  public ImageIcon getPicture()
  {
    return thePic;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
}

