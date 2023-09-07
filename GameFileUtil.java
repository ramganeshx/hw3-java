package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import api.Tile;

/**
 * @author ramganesh
 */


/**
 * Utility class with static methods for saving and loading game files.
 */
public class GameFileUtil {
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

			/*
			 * the method first gets the current grid (width and height), minimum and
			 * maximum tile level, and the current score from the game object. Then it
			 * constructs a string in a specific format that includes this information and
			 * the current state of the grid.
			 * 
			 */
			Grid saveGrid = game.getGrid();
			String readFile = "";

			readFile += "" + saveGrid.getWidth();
			readFile += " " + saveGrid.getHeight();
			readFile += " " + game.getMinTileLevel();
			readFile += " " + game.getMaxTileLevel();
			readFile += " " + game.getScore() + "\n";
			// write grid to file

			int width = saveGrid.getWidth();
			int height = saveGrid.getHeight();
			int i = 0;

			while (i < height) {
				for (int j = 0; j < width; j++) {
					if (j == 0) {
						readFile += saveGrid.getTile(j, i).getLevel();
					} else
						readFile += " " + saveGrid.getTile(j, i).getLevel();
				}
				if (i != height - 1) {
					readFile += "\n";
				}
				i++;
			}

			writer.write(readFile);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, ConnectGame game) {

		File file = new File(filePath);
		Scanner scanner;

		try {

			scanner = new Scanner(file);
			int width = scanner.nextInt();
			int height = scanner.nextInt();

			Grid save;
			save = new Grid(width, height); // format of the array is different than format of the game
			game.setGrid(save); // setting the grid width and height and index 0 and 1
			game.setMinTileLevel(scanner.nextInt()); // grid min tile level at index 2
			game.setMaxTileLevel(scanner.nextInt()); // grid max tile level at index 3
			game.setScore(scanner.nextInt()); // setting score is at index 4

			/*
			 * the scanner variable is the Scanner object that reads from the file. The code
			 * reads the grid from the file one row at a time and assigns the values to the
			 * save Grid object using the setTile method. Finally, the setGrid method of the
			 * game object is called to set the grid for the game.
			 * 
			 */
			while (scanner.hasNext()) {

				for (int i = 0; i < save.getHeight(); i++) {

					for (int j = 0; j < save.getWidth(); j++) {

						Tile readTiles = new Tile(scanner.nextInt());

						save.setTile(readTiles, j, i);

					}
				}

			}

			game.setGrid(save); // sets grid for the game

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
