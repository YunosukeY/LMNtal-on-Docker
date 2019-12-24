package plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import jp.ac.waseda.info.ueda.unyo.customReallocator.Reallocator;

public class Grid extends Reallocator{
	
	static
	private HashSet inActiveSet_ = new HashSet();
	static
	private HashMap<Object,int []> posMap_ = new HashMap<Object, int []>();

	final static
	private double WIDTH_ = 80;
	
	static
	private HashSet connectedSet_ = new HashSet();
	
	static
	private boolean init_ = false;

	@Override
	public HashSet<Object> groupNode(Object node, Object observer) {
		connectedSet_ = getConnectedNodes(node, observer);
		setGroup(observer);
		return connectedSet_;
	}

	@Override
	public boolean isBaseNode(Object node, Object observer) {
		return true;
	}
	
	private void setGroup(Object observer){
		Iterator groupIter = connectedSet_.iterator();
		while (groupIter.hasNext()) {
			Object targetNode = (Object) groupIter.next();
			if(!getName(targetNode).equals("grid")){
				continue;
			}
			int [] posArray = new int[2];
			inActiveSet_.add(targetNode);
			Object nthNode2 = null;
			for(int i=0; i < getLinkNum(targetNode); i++){
				Object nthNode = getNthNode(targetNode, i, observer);
				nthNode2 = nthNode;
				int pos;
				try {
					pos = Integer.parseInt(getName(nthNode));
				} catch (NumberFormatException e) {
					continue;
				}
				posArray[i] = pos;
				inActiveSet_.add(nthNode);
			}
			posMap_.put(nthNode2, posArray);
		}
	}

	@Override
	public void reallocate(Object baseNode, Object observer) {
		int n = 0;
		HashSet reallocatedNodeSet = new HashSet();
		if(inActiveSet_.contains(baseNode)){
			return;
		}
		double baseX = getBounds(baseNode).getCenterX();
		double baseY = getBounds(baseNode).getCenterY();
		reallocatedNodeSet.add(baseNode);
		double baseNthX = baseX;
		double baseNthY = baseY;

		Iterator<Object> targetNodes = posMap_.keySet().iterator();
		while (targetNodes.hasNext()) {
			Object targetNode = (Object) targetNodes.next();
			if(reallocatedNodeSet.contains(targetNode)){
				continue;
			}
			if(inActiveSet_.contains(targetNode)){
				continue;
			}
			int[] posArray = posMap_.get(targetNode);

			int t0 = posArray[0];
			int t1 = posArray[1];
			double x = t0*WIDTH_ + baseX;
			double y = t1*WIDTH_ + baseY;
			move(targetNode, x - getBounds(targetNode).getCenterX(), y - getBounds(targetNode).getCenterY());
			reallocatedNodeSet.add(targetNode);
		}
		setInactiveNodes(inActiveSet_);
	}

	@Override
	public String typeName() {
		// TODO 自動生成されたメソッド・スタブ
		return "grid";
	}

	@Override
	public boolean useDefaultFource() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void draw(Object baseNode, Object observer) {
		// TODO Auto-generated method stub
		
	}

}
