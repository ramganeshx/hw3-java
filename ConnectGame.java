package hw3;

import java.util.Random;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;

/**
 * 
 * @author ramganesh
 *
 */

public class ConnectGame {

	/*
	 * shows dialog when the upgrade results in a tile is greater than the current
	 * maximum
	 */
	private ShowDialogListener dialogListener;
	/*
	 * updates score
	 */
	private ScoreUpdateListener scoreListener;
	/*
	 * int variable that keeps track of minimum tile level
	 */
	private int min;
	/*
	 * int variable that keeps track of maximum tile level
	 */
	private int max;
	/*
	 * int variable that keeps track of grid height
	 */
	private int height;
	/*
	 * int variable that keeps track of grid width
	 */
	private int width;
	/*
	 * my current grid
	 */
	private Grid callGrid;
	/*
	 * boolean variable to check if the game has started or not
	 */
	private boolean gameStarted;
	/*
	 * keeps track of start to continue and continue to continue "click"
	 */
	private int click;
	/*
	 * Random variable that genereates a random number
	 */
	private Random rand;
	/*
	 * long variable that updates and keeps track of current player score
	 */
	private long playerScore;
	/*
	 * array list that keeps on last selected tile (all the selected on tiles)
	 */
	private ArrayList<Tile> selectedOnTiles;
	/*
	 * keeps track of when a random integer between min and max is generated
	 */
	private int randomTile;
	/*
	 * boolean that keeps track of if selected is adjacent or not
	 */
	private boolean adjacent;

	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {

		this.min = min;
		this.max = max;
		this.rand = rand;
		this.width = width;
		this.height = height;
		gameStarted = false; // game has not started so set equal to false
		click = 0; // click starts off at 0 because nothing has happened
		adjacent = false; // adjacent starts off at false because nothing has happened
		selectedOnTiles = new ArrayList<>();
		callGrid = new Grid(width, height);

	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {

		randomTile = rand.nextInt(min, max); // generates a random integer between min and max, and then creates a new
												// Tile object with that random value.
		Tile tileRandom = new Tile(randomTile);

		return tileRandom; // return the tile with random level between min and max

	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {

		for (int x = 0; x < width; x++) {

			for (int y = 0; y < height; y++) { // random tile goes through each box in grid, and each
												// box is assigned a random value of x, y

				callGrid.setTile(getRandomTile(), x, y); // if conditions are satisfied this regenerates the grid with
															// all random tiles produced by getRandomTile()
			}

		}

	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */

	public boolean isAdjacent(Tile t1, Tile t2) {

		int rowDistance = Math.abs(t1.getX() - t2.getX()); // checks horiztonal adjacentcy
		int columnDistance = Math.abs(t1.getY() - t2.getY()); // checks vertical adjacentcy
		int totalDistance = rowDistance + columnDistance; // total distance is the row distance plus the column distance

		if (totalDistance == 1) { // youre either going 1 horizontal or vertical

			adjacent = true; // if total distance is 1 that means it would be adjacent horiztonally or
								// vertically

		} else if (totalDistance == 2 && rowDistance == 1 && columnDistance == 1) { // diagnal adjacentcy

			adjacent = true; // if true than it is adjacent because of diagnol adjacentcy
		}

		return adjacent;

	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */

	public boolean tryFirstSelect(int x, int y) {

		boolean start = false;

		if (gameStarted == false) {

			start = true;
			gameStarted = true;

			click++; // you are starting it so you click the tile

			selectedOnTiles.add(callGrid.getTile(x, y)); // keeps track of most recent selected tile
			callGrid.getTile(x, y).setSelect(true); // we select a tile in gameStarted so we now take our grid
			// and get tile of (x to y) and set select that true since we select

		}

		return start;

	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {

		int currentTileLevel = callGrid.getTile(x, y).getValue(); // stores the current level/value of the Tile object
																	// located at position (x, y) on the game board.
		Tile currentTile = callGrid.getTile(x, y); // stores a reference to the Tile object located at position (x, y)
													// on the game board.

		int previousTileLevel;
		Tile previousTile;

		if (click != 0) {

			previousTileLevel = selectedOnTiles.get(selectedOnTiles.size() - 1).getValue();
			previousTile = selectedOnTiles.get(selectedOnTiles.size() - 1);

		} else {

			previousTileLevel = -1; // stores the level/value of the previously selected Tile object. This is set to
									// -1 if there was no previously selected Tile.
			previousTile = null; // stores a reference to the previously selected Tile object. This is set to
									// null if there was no previously selected Tile.
		}

		if (!gameStarted) { // we are checking if first select is called
			// so if not game started then we havent called first select

			return; // if game is not started just return. exit method

		}

		/*
		 * check for adjacency and same level keeping track of most recent select so we
		 * are trying to keep track of most recent selected tile, so we call our
		 * arrayList and get the array list size -1 to find our most recent tile we
		 * compare that to the current tile callGrid is current tile selectedOnTiles is
		 * last tile selected
		 * 
		 */
		if (!isAdjacent(currentTile, previousTile) && previousTile != null) {

			return; // we are exiting the method

		}

		else if (selectedOnTiles.contains(currentTile)) {

			unselect(previousTile.getX(), previousTile.getY());
			// unselect if previous tile selected contais the current tile, i unslect my
			// previous tile

		}

		/*
		 * when click is 1 it is the first tile selected if first one selected is
		 * current level equal to previous level selectedOnTiles.size()-1 gets most
		 * recent tile callGrid.getTile(x, y).getValue gets value of the current level
		 * checks if we are going from start to continue and if previous tile and
		 * current tile not the same level current level == previous level if its first
		 * 2 tiles that transition is between the first 2 tiles then the else if
		 * statement executes
		 * 
		 */
		else if (click == 1 && currentTileLevel == previousTileLevel) {

			selectedOnTiles.add(callGrid.getTile(x, y)); // if its true we add the current one to the array list
			click++; // so this method knows it is not the first method anymore
			callGrid.getTile(x, y).setSelect(true);
		}

		/*
		 * if click is greater than 1 it is not the first 2 tiles anymore, it is
		 * continue to continue not start continue if current level is equal to previous
		 * or current level is equal to previous level x 2 then current tile checks if
		 * it is continue to continue and if levels are the same or if its one above
		 */
		else if (click > 1 && (currentTileLevel == previousTileLevel || currentTileLevel == previousTileLevel * 2)) {

			callGrid.getTile(x, y).setSelect(true);
			// current tile and checks if it is continue to continue and if levels are the
			// same or if its above

			selectedOnTiles.add(callGrid.getTile(x, y));

			click++;

		}

	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {

		int selectedTileSize = selectedOnTiles.size();
		Tile currentTiles = callGrid.getTile(x, y);

		if (selectedTileSize == 1) {
			// if only one tile is selected, deselect all other tiles on the grid
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {

					callGrid.getTile(i, j).setSelect(false);

				}
			}

		} else if (selectedOnTiles.get(selectedTileSize - 1) != currentTiles) {
			// if the last tile selected is not the current tile being selected, return
			// false
			return false;

		} else if (selectedTileSize > 1) {
			// if more than one tile is selected update the score

			long updateScore = 0;

			for (int i = 0; i < selectedTileSize; i++) {
				// calculates the score obtained by selecting a group of tiles
				updateScore += selectedOnTiles.get(i).getValue();
				scoreListener.updateScore(updateScore);

			}

			upgradeLastSelectedTile();
			dropSelected();

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {

					callGrid.getTile(i, j).setSelect(false);

				}

			}

			selectedOnTiles.clear();
			setScore(updateScore);
			scoreListener.updateScore(updateScore);

		}

		gameStarted = false;
		click = 0;
		// reset gameStarted and click variables

		return true; // return true to indicate a successful selection

	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {

		Tile save = selectedOnTiles.get(selectedOnTiles.size() - 1);
		int lengthOfSelected = selectedOnTiles.size();
		int levelOfLastSelectedTile = selectedOnTiles.get(lengthOfSelected - 1).getLevel();
		int newLevelLastSelected = levelOfLastSelectedTile + 1;
		// calculate the new level of the last selected tile and set it

		selectedOnTiles.get(lengthOfSelected - 1).setLevel(newLevelLastSelected);

		unselect(save.getX(), save.getY());
		// unselect the last selected tile

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				if (newLevelLastSelected > max) {
					// if the upgrade results in a tile that is greater than the current maximum
					// tile level, both the minimum and maximum tile level are increased by 1.

					max++;
					min++;

					int highLevel = (int) Math.pow(2, max);
					int lowLevel = (int) Math.pow(2, min);

					dialogListener.showDialog("New block " + highLevel + ",removing blocks " + lowLevel);

				}

			}

		}

	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {

		Tile[] selected = new Tile[selectedOnTiles.size()];

		int selectedOnTileSize = selectedOnTiles.size();
		// copy the selected tiles from the list to the array

		for (int i = 0; i < selectedOnTileSize; i++) {

			selected[i] = selectedOnTiles.get(i);

		}

		return selected;

	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {

		/*
		 * essentially checking if it goes from box to box and checks if the level is
		 * equal to the level they want u to check if it is equal it removes that tile
		 * and drops everything by 1 and then replaces the top with a random tile
		 */
		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {
				// iterate over all tiles on the grid

				Tile currentTileSelect = callGrid.getTile(i, j);

				if (callGrid.getTile(i, j).getLevel() == level) {

					currentTileSelect.setLevel(level - 1);
					// decrease the level of the tile

					if (callGrid.getTile(i, j).getY() == 0) {
						// if the tile is at the top row of the grid, drop it down

						for (int x = 0; i < callGrid.getTile(i, j).getY(); x++) {

							int dropDown = callGrid.getTile(i, j).getY() - x;

							if (dropDown == 0) {
								// if the tile has dropped to the bottom, set a new random tile at its position

								callGrid.setTile(getRandomTile(), i, j);

							} else if (dropDown != 0) {
								// otherwise, move the tile down one row

								callGrid.setTile(currentTileSelect, x, dropDown);
							}

						}

					}

				}
			}
		}

	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {

		/*
		 * randomly replaces tiles that are currently selected with new tiles generated
		 * by the getRandomTile method. If the selected tile is in the top row (j == 0),
		 * then it will be replaced and a new tile will be generated to fill the empty
		 * space. If the selected tile is not in the top row (j >= 1), then the tile
		 * above it will be moved down to take its place.
		 * 
		 */
		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				if (selectedOnTiles.contains(callGrid.getTile(i, j))) {

					callGrid.setTile(getRandomTile(), i, j);

					if (j == 0) {

						callGrid.setTile(getRandomTile(), i, j);

					} else if (j >= 1) {

						callGrid.getTile(i, j - 1);

						callGrid.setTile(callGrid.getTile(i, j - 1), i, j);
					}

				}

			}

		}

	}

	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {

		/*
		 * unselects the tile at the specified (x,y) coordinates, removes it from the
		 * list of currently selected tiles, and adjusts the index of the remaining
		 * selected tiles based on the info
		 */
		int index = 0;
		callGrid.getTile(x, y).setSelect(false);

		for (int i = 0; i < selectedOnTiles.size(); i++) {

			if (selectedOnTiles.get(i).getX() == x && selectedOnTiles.get(i).getY() == y) {

				break;

			} else {

				index++;
			}

		}

		selectedOnTiles.remove(index);

	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		return playerScore;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {

		return callGrid;

	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {
		return min;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {
		return max;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {

		playerScore = score;

	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {

		callGrid = grid;

		height = grid.getHeight();

		width = grid.getWidth();

	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {

		min = minTileLevel;

	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {

		max = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 * @throws FileNotFoundException
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
