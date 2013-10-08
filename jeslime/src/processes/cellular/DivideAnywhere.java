package processes.cellular;

import io.project.ProcessLoader;

import java.util.HashSet;
import java.util.Random;

import processes.StepState;


import cells.Cell;

import structural.Flags;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

import geometries.Geometry;

public class DivideAnywhere extends BulkDivisionProcess {

	public DivideAnywhere(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, lattice, id, geom, p);
	}

	public void iterate(StepState state) throws HaltCondition {

		// Choose a random active cell.
		HashSet<Coordinate> candSet = lattice.getDivisibleSites();
		Coordinate[] candidates = candSet.toArray(new Coordinate[0]);
		
		execute(state, candidates);
	}

}