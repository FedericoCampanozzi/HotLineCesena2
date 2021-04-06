package hotlinecesena.controller.generator;

import javafx.util.Pair;
import java.util.Map;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import hotlinecesena.model.dataccesslayer.JSONDataAccessLayer;
import hotlinecesena.model.dataccesslayer.SimbolsType;

public class QuadraticRoom extends AbstractRoom {
	final private  int w;
	private  int d;
	
	private QuadraticRoom(Map<Pair<Integer, Integer>, SimbolsType> map, Pair<Integer, Integer> center, int edge) {
		super();
		this.map = map;
		this.w = edge;
	}
	
	public QuadraticRoom(int edge, int nDoor) {
		super();
		
		if (edge % 2 == 0) {
			edge -= 1;
		}
		
		this.w = edge;
		this.d = nDoor;
		generate();
	}
	
	@Override
	public void generate() {
		
		final int width = (this.w - 1) / 2;
		Random rnd = new Random();
		rnd.setSeed(JSONDataAccessLayer.SEED);
		
		for (int y = -width; y <= width; y++) {
			for (int x = -width; x <= width; x++) {

				if (y == -width || x == -width || y == width || x == width) {
					map.put(new Pair<>(y, x), SimbolsType.WALL);
				} else {
					map.put(new Pair<>(y, x), SimbolsType.WALKABLE);
				}
			}
		}
		Set<Pair<Integer, Integer>> connections = new HashSet<>();
		while (connections.size() < this.d) {
			final int x = rnd.nextInt(this.w) - width;
			final int y = rnd.nextInt(this.w) - width;
			Pair<Integer, Integer> cPos = new Pair<>(y, x);

			if ((!cPos.equals(new Pair<Integer, Integer>(-width, -width))
					&& !cPos.equals(new Pair<Integer, Integer>(width, width))
					&& !cPos.equals(new Pair<Integer, Integer>(-width, width))
					&& !cPos.equals(new Pair<Integer, Integer>(width, -width))) && !connections.contains(cPos)
					&& this.map.get(cPos).equals(SimbolsType.WALL)) {
				this.map.put(cPos, SimbolsType.DOOR);
				connections.add(cPos);
			}
		}
	}

	@Override
	public Room deepCopy() {
		return new QuadraticRoom(this.map, this.center, this.w);
	}
	
	public int getWidth() {
		return this.w;
	}
}
