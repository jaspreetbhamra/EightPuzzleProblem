package steepestAscent;

public class TileConfig {

    int tileConfig[][];
    int blankLocation[];
	private int cost;

	public TileConfig(int tileConfig[][], int blankLocation[]){
		this.tileConfig = tileConfig;
		this.blankLocation = blankLocation;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
