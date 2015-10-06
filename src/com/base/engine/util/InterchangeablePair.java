/**
 * I'm not putting a license with this file, it was too simple to code, etc.
 * I would rather this code be able to be used wherever wanted
 */

package com.base.engine.util;

public class InterchangeablePair<L, R> {
	
	private final L left;
	private final R right;
	
	public InterchangeablePair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public L getLeft() {
		return left;
	}
	
	public R getRight() {
		return right;
	}
	
	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof InterchangeablePair) {
			InterchangeablePair<?, ?> p = (InterchangeablePair<?, ?>) object;
			return (this.left.equals(p.getLeft()) && this.right.equals(p.getRight())) || (this.left.equals(p.getRight()) && this.right.equals(p.getLeft()));
		}
		return false;
	}
	
}
