package plugins;

import java.util.HashSet;
import java.util.Iterator;

import jp.ac.waseda.info.ueda.unyo.customReallocator.Reallocator;

public class Othello extends Reallocator{
	
	static
	private HashSet groupNode_ = new HashSet();

	final static
	private double WIDTH_ = 80;
	
	static
	private HashSet connectedSet_ = new HashSet();

	@Override
	public HashSet<Object> groupNode(Object node, Object observer) {
		connectedSet_ = getConnectedNodes(node, observer);
		Iterator groupIter = connectedSet_.iterator();
		while (groupIter.hasNext()) {
			Object targetNode = (Object) groupIter.next();
			if(getName(targetNode).equals("black") ||
					getName(targetNode).equals("white") ||
					getName(targetNode).equals("empty")){
				groupNode_.add(targetNode);
			}
		}
		return connectedSet_;
	}

	@Override
	public boolean isBaseNode(Object node, Object observer) {
		return true;
	}
	public boolean isRealBaseNode(Object node, Object observer) {
		if(!getName(node).equals("white") ||
				!getName(node).equals("black") ||
				!getName(node).equals("empty")){
			return false;
		}
		if(getLinkNum(node) < 2){
			return false;
		}
		Object node0 = getNthNode(node, 0, observer); 
		Object node1 = getNthNode(node, 1, observer);
		int t0 = Integer.parseInt(getName(node0));
		int t1 = Integer.parseInt(getName(node1));
		if(t0 == 1 && t1 == 1){
			return true;
		}
		return false;
	}

	@Override
	public void reallocate(Object baseNode, Object observer) {
		int n = 0;
		HashSet reallocatedNodeSet = new HashSet();
		HashSet inactiveNodeSet = new HashSet();
		if(!groupNode_.contains(baseNode)){
			return;
		}
		double baseX = getBounds(baseNode).getCenterX();
		double baseY = getBounds(baseNode).getCenterY();
		reallocatedNodeSet.add(baseNode);
		double baseNthX = baseX;
		double baseNthY = baseY;
//		for(int i = 0; i < getLinkNum(baseNode); i++){
//			Object nthNode = getNthNode(baseNode, i, observer);
//			if(groupNode_.contains(nthNode) || reallocatedNodeSet.contains(nthNode)){
//				continue;
//			}
//			baseNthX -= WIDTH_/5;
//			baseNthY -= WIDTH_/5;
//			move(nthNode, baseNthX - getBounds(nthNode).getCenterX(), baseNthY - getBounds(nthNode).getCenterY());
//			reallocatedNodeSet.add(nthNode);
//		}
		Iterator<Object> targetNodes = connectedSet_.iterator();
		while (targetNodes.hasNext()) {
			Object targetNode = (Object) targetNodes.next();
			if(reallocatedNodeSet.contains(targetNode)){
				continue;
			}
			if(!groupNode_.contains(targetNode)){
				inactiveNodeSet.add(targetNode);
//				move(targetNode, baseX - WIDTH_ - getBounds(targetNode).getCenterX(), baseY - WIDTH_ - getBounds(targetNode).getCenterY());
				continue;
			}
			String node0 = getNthNodeName(targetNode, 0, observer); 
			String node1 = getNthNodeName(targetNode, 1, observer);
			int t0 = Integer.parseInt(node0);
			int t1 = Integer.parseInt(node1);
			double x = (t0 - 1)*WIDTH_ + baseX;
			double y = (t1 - 1)*WIDTH_ + baseY;
			move(targetNode, x - getBounds(targetNode).getCenterX(), y - getBounds(targetNode).getCenterY());
			reallocatedNodeSet.add(targetNode);
//			for(int i = 0; i < getLinkNum(targetNode); i++){
//				Object nthNode = getNthNode(targetNode, i, observer);
//				if(groupNode_.contains(nthNode) || reallocatedNodeSet.contains(nthNode)){
//					continue;
//				}
//				x -= WIDTH_/5;
//				y -= WIDTH_/5;
//				move(nthNode, x - getBounds(nthNode).getCenterX(), y - getBounds(nthNode).getCenterY());
//				reallocatedNodeSet.add(nthNode);
//			}
		}
		setInactiveNodes(inactiveNodeSet);
	}

	@Override
	public String typeName() {
		// TODO 自動生成されたメソッド・スタブ
		return "othello2";
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
