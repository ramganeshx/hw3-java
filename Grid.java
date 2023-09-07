package hw3;

import api.Tile;


/**
 * @author ramganesh
 */




/**
 * Represents the game's grid.
 */
public class Grid {

	/*
	 * instance variable for grid width
	 */
	private int width; 
	/*
	 * instance variable for grid height
	 */
	private int height; 
	/*
	 * 2d array that stores tiles called "storeTiles"
	 */
	private Tile[][] storeTiles; 

	/**
	 * Creates a new grid.
	 * 
	 * @param width  number of columns
	 * @param height number of rows
	 */
	public Grid(int width, int height) {

		this.width = width;
		this.height = height;

		storeTiles = new Tile[width][height];

	}

	/**
	 * Get the grid's width.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the grid's height.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the tile for the given column and row.
	 * 
	 * @param x the column
	 * @param y the row
	 * @return
	 */
	public Tile getTile(int x, int y) {

		return storeTiles[x][y];

	}

	/**
	 * Sets the tile for the given column and row. Calls tile.setLocation().
	 * 
	 * @param tile the tile to set
	 * @param x    the column
	 * @param y    the row
	 */
	public void setTile(Tile tile, int x, int y) {

		tile.setLocation(x, y);
		storeTiles[x][y] = tile;

	}

	@Override
	public String toString() {
		String str = "";
		for (int y = 0; y < getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			str += "[";
			for (int x = 0; x < getWidth(); x++) {
				if (x > 0) {
					str += ",";
				}
				str += getTile(x, y);
			}
			str += "]";
		}
		return str;
	}
}


