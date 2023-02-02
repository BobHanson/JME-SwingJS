package jme.canvas;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jme.util.Box;

public class Graphical2DObjectGroup<T extends Graphical2DObject> implements Graphical2DObject {

	public ArrayList<T> group;

	public Graphical2DObjectGroup() {
		this.group = new ArrayList<T>();
	}

	public Graphical2DObjectGroup(ArrayList<T> initGroup) {
		this.group = new ArrayList<T>(initGroup);
	}

	@Override
	public void draw(PreciseGraphicsAWT og) {
		for (T go : this.group) {
			go.draw(og);
		}

	}

	@Override
	public void moveXY(double movex, double movey) {
		for (T go : this.group) {
			go.moveXY(movex, movey);
		}
	}

	/**
	 *@return null if nothing here
	 */
	@Override
	public Rectangle2D.Double computeBoundingBoxWithAtomLabels(Rectangle2D.Double union) {
		for (T go : group) {
			union = go.computeBoundingBoxWithAtomLabels(union);
		}
		return union;
	}

	@Override
	public double centerX() {
		double center = 0;
		for (T go : this.group) {
			center += go.centerX();
		}

		if (this.group.size() > 0) {
			center /= group.size();
		}
		return center;
	}

	@Override
	public double centerY() {
		double center = 0;
		for (T go : this.group) {
			center += go.centerY();
		}

		if (this.group.size() > 0) {
			center /= group.size();
		}
		return center;
	}

//	@Override
//	public void XY(double x, double y) {
//		for (Graphical2DObject go : this.group) {
//			go.moveXY(x, y);
//		}
//
//	}

	/**
	 * Align all my objects based on their respective centers
	 * 
	 * @param xOrY
	 */
	public void alignCenter(Box.Axis xOrY) {
		double groupCenter = Graphical2DObject.center(this, xOrY);

		for (Graphical2DObject go : this.group) {
			double delta = groupCenter - Graphical2DObject.center(go, xOrY);
			Graphical2DObject.move(go, xOrY, delta);
		}

	}

	@Override
	public double closestDistance(double x, double y) {
		double min = Double.MAX_VALUE;
		for (T go : this.group) {
			min = Math.min(min, go.closestDistance(x, y));
		}
		return min;
	}

	public void add(T element) {
		this.group.add(element);

	}

	public void add(int pos, T element) {
		this.group.add(pos, element);

	}

	public int size() {
		return this.group.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	public void distributePositions(final Box.Axis xOrY, double margin) {
		distributePositions(xOrY, margin, true);
	}

	/**
	 * keepXorYorder: should be true most of the time such that the object on the
	 * left stays on left, otherwise the first object on the left would be the first
	 * object in my group
	 * 
	 * @param xOrY
	 * @param margin
	 * @param keepXorYorder
	 */
	public void distributePositions(final Box.Axis xOrY, double margin, boolean keepXorYorder) {

		if (this.size() <= 1) {
			return;
		}

		Graphical2DObjectGroup<T> sorted = new Graphical2DObjectGroup<T>();
		sorted.addAll(this);
		sorted.removeNoSizeObjects(); // otherwise boundingBox is null
		if (sorted.size() <= 1) {
			return;
		}
		double beforeAlignCenter = Graphical2DObject.center(sorted, xOrY);
		if (keepXorYorder) {
			Collections.sort(sorted.group, new Comparator<T>() {
				@Override
				public int compare(T m1, T m2) {
					// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
					double x2 = Box.get(m1.computeBoundingBoxWithAtomLabels(null), xOrY);
					double x1 = Box.get(m2.computeBoundingBoxWithAtomLabels(null), xOrY);
					return x1 > x2 ? -1 : (x2 < x1) ? 1 : 0;
				}
			});
		}
		double sumMove = 0;
		for (T mol : sorted.group) {
			Rectangle2D.Double moleculeBox = mol.computeBoundingBoxWithAtomLabels(null);
			double move = sumMove - Box.get(moleculeBox, xOrY);
			Graphical2DObject.move(mol, xOrY, move);
			sumMove += Box.getDim(moleculeBox, xOrY) + margin;
		}

		// the algorithm above move the objects.
		// apply correction
		double afterAlignCenter = Graphical2DObject.center(sorted, xOrY);
		Graphical2DObject.move(sorted, xOrY, beforeAlignCenter - afterAlignCenter);
		assert (Math.abs(beforeAlignCenter - Graphical2DObject.center(sorted, xOrY)) < 0.001);

	}

	/**
	 * @return
	 * 
	 */
	boolean removeNoSizeObjects() {

		Graphical2DObjectGroup<T> emptyList = new Graphical2DObjectGroup<T>();
		for (T mol : this.group) {
			Rectangle2D.Double moleculeBox = mol.computeBoundingBoxWithAtomLabels(null);
			if (moleculeBox == null || moleculeBox.isEmpty()) {
				emptyList.add(mol);
			}
		}

		for (T mol : emptyList.group) {
			this.group.remove(mol);
		}

		return emptyList.size() > 0;

	}

	public void addAll(Graphical2DObjectGroup<T> graphical2dObjectGroup) {
		this.addAll(graphical2dObjectGroup.group);

	}

	public void addAll(ArrayList<T> graphical2dObjectList) {
		this.group.addAll(graphical2dObjectList);

	}

}