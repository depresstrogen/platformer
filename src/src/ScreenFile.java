package src;
import java.io.*;
import java.util.ArrayList;

/**
 * The file class, reads and writes an ArrayList using an a ObjectStream, which
 * saves every byte of the ArrayList as it is in RAM
 * 
 * @version January 14, 2021
 * @author Riley Power
 */

public class ScreenFile {
	/**
	 * Saves the array to a file using ObjectOutputStream, used mostly for the menu
	 * elements
	 * 
	 * @param array     The ArrayList that you would like to save to the directory
	 * @param directory The directory to save the ArrayList to
	 */
	public void writeArrayList(ArrayList<ScreenElement> array, String directory) {
		try {

			// ObjectOutputStream basically puts all of the raw data in one file
			FileOutputStream fileOut = new FileOutputStream(directory);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			// Writes the ArrayList to the directory specified
			objectOut.writeObject((ArrayList<ScreenElement>) array);
			// Memory leaks are not cool
			objectOut.close();
			fileOut.close();

			// Prints everything that saved for debugging purposes
			for (int i = 0; i < array.size(); i++) {
				ScreenElement element = (ScreenElement) array.get(i);
				System.out.print(element.getID() + "." + element.getX() + "." + element.getX() + ".");
//				if (element instanceof Button) {
//					System.out.println(((Button) element).getColor());
//				}
//				if (element instanceof Text) {
//					System.out.println(((Text) element).getFont());
//				}
//				if (element instanceof Picture) {
//					System.out.println(((Picture) element).getImage());
//				}
			}
			System.out.println("ArrayList saved to " + directory);
			// Everything has a catch
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}// writeArrayList

	/**
	 * Reads the array from a file using ObjectInputStream, used mostly for the menu
	 * elements
	 * 
	 * @param directory The directory of the file to read from
	 * @return The array contained in the file
	 */
	public ArrayList<ScreenElement> readArrayList(String directory) {
		ArrayList<ScreenElement> array;
		try {
			FileInputStream fileIn = new FileInputStream(directory);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			// Reads object from designated file
			System.out.print(23);
			ArrayList<ScreenElement> readObject = (ArrayList<ScreenElement>) objectIn.readObject();
			System.out.print(23);
			array = readObject;
			System.out.print(23);
			// Info to help debug more
			System.out.println("ArrayList read from " + directory + " successfully.");
			for (int i = 0; i < array.size(); i++) {
				ScreenElement element = (ScreenElement) array.get(i);
				System.out.print(element.getID() + "." + element.getX() + "." + element.getY() + ".");
//				if (element instanceof Button) {
//					System.out.println(((Button) element).getColor());
//				}
//				if (element instanceof Text) {
//					System.out.println(((Text) element).getFont());
//				}
//				if (element instanceof Picture) {
//					System.out.println(((Picture) element).getImage());
//				}
			}
			// Again memory leaks are not pog
			fileIn.close();
			objectIn.close();
		} catch (Exception e) {
			// Gotta make array equal something if its going to be returned
			array = new ArrayList<ScreenElement>();
			System.err.println("Exception: " + e.getMessage());
		}
		return array;
	}// readArrayList
}// ScreenFile
