import java.util.*;
import java.io.*;
/**
 * @author Ikonija Bogojevic
 *
 */
public class Client
{
   private static String hostName;
   private static Thread thrd = null;
   
   // the list of threads is kept in a linked list
   private static LinkedList<Thread> list = new LinkedList<Thread>();
   private static ListIterator<Thread> li = null;

    public static void main(String[] args) throws IOException
    {
        int menuSelection = 0;
        // load hostName from the command line arguments
        hostName = args[0]; 

        // if no hostname is provided, quit
        if ((hostName.equals("")) || (hostName == null)) 
        { 
            System.out.println("User did not enter a host name. Client program exiting.");
            System.exit(1);
        }

        // until the user selects 7, the Exit option, keep looping and
        // offering the menu again after running the queries to the server
        else while (menuSelection != 7) {
        	// display the menu and get the user's choice
        	menuSelection = mainMenu();
        	
        	// if 7, exit program
        	if (menuSelection == 7) {
        		System.out.println("Quitting.");
        		System.exit(0);
        	}
        	
        	// otherwise, create threads in the linked list
        	// TO-DO: add method to ask user how many threads to make
        	// for now, just create 100
        	for (int i = 0; i < 30; i++) {
        		// make a new thread, tell it the hostname to connect to
        		// and the command to run
        		thrd = new Thread(new ClientThread(hostName, menuSelection));
        		thrd.start(); // start the thread
        		list.add(thrd); // add the thread to the end of the linked list
        		
        	}
        	
        	for (int i = 0; i < 30; i++) {
        		try {
					list.get(i).join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
                
        } // end while

    }//end main
//----------------------------------------------------------------------------
    public static int mainMenu()
    {
        int menuSelection = 0;
            System.out.println("The menu provides the following choices to the user: ");
            System.out.println("1. Host current Date and Time \n2. Host uptime\n"
                    + "3. Host memory use \n4. Host Netstat \n5. Host current users "
                    + "\n6. Host running processes \n7. Quit ");
            System.out.println("Please provide number corresponding to the action you want to be performed:");
            Scanner sc = new Scanner(System.in);
            menuSelection = sc.nextInt();
                System.out.println("Running command: " + menuSelection);
        return menuSelection;

    }//end mainMenu

}
