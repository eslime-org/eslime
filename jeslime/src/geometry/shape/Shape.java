package geometry.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import structural.identifiers.Coordinate;
import geometry.lattice.Lattice;

public abstract class Shape {
	
	// The set of sites in this geometry, given the lattice type
	protected Coordinate[] canonicalSites;
	
	// A reverse lookup of coordinate to index into canonicalSites
	protected Map<Coordinate, Integer> coordMap;

	protected Lattice lattice;
	
	public Shape(Lattice lattice) {
		verify(lattice);
		this.lattice = lattice;

	}
	
	/**
	 * Initialize general data structures. Should be invoked by a Shape
	 * subclass after local variables have been assigned.
	 */
	protected void init() {
		canonicalSites = calcSites();
		buildCoordMap();
	}
	
	protected abstract void verify(Lattice lattice);

	
	/* PUBLIC METHODS */

	public Integer coordToIndex(Coordinate coord) {
		if (!coordMap.containsKey(coord)) {
			throw new IllegalArgumentException("Attempted to index non-canonical coordinate " + coord);
		}
		
		return coordMap.get(coord);
	}
	
	
	// Get a complete list of sites for this geometry.
	public Coordinate[] getCanonicalSites() {
		return canonicalSites.clone();
	}
	
	public abstract Coordinate getCenter();
	
	public abstract Coordinate[] getBoundaries();
	
	/**
	 * Returns two coordinates representing the limits of the geometry.
	 * 
	 * The coordinates are given as minimum and maximum displacements,
	 * in terms of the basis vectors, from the center coordinate.
	 * 
	 */
	//public abstract Coordinate[] getLimits();
	
	/**
	 * Returns a coordinate vector, in the basis of the geometry, of how far
	 * over the boundary a particular point is.
	 * 
	 * @param coord
	 * @return
	 */
	public abstract Coordinate getOverbounds(Coordinate coord);
	
	/* PROTECTED METHODS */
	protected abstract Coordinate[] calcSites();
	
	protected void include(Collection<Coordinate> list, Coordinate coordinate) {
		Coordinate adjusted = lattice.adjust(coordinate);
		list.add(adjusted);		
	}
	
	/* PRIVATE METHODS */
	
	private void buildCoordMap() {
		
		coordMap = new HashMap<Coordinate, Integer>();
		for (Integer i = 0; i < canonicalSites.length; i++) {
			coordMap.put(canonicalSites[i], i);
		}
	}

	public abstract int[] getDimensions();
}