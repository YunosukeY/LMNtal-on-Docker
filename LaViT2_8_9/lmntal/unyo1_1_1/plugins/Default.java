package plugins;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import jp.ac.waseda.info.ueda.unyo.customReallocator.Reallocator;

public class Default extends Reallocator{

	public HashSet<Object> groupNode(Object node, Object observer) {
		HashSet nthSet = getConnectedNodes(node, observer);
		return nthSet;	
	}

	public boolean isBaseNode(Object node, Object observer) {
		return true;
	}

	public void reallocate(Object baseNode, Object observer) {
		HashSet reallocatedNodeSet = new HashSet();

		LinkedHashMap nthNodeMap =
			new LinkedHashMap();
		nthNodeMap.put(baseNode, baseNode);
		reallocatedNodeSet.add(baseNode);
		while(!nthNodeMap.isEmpty()){
			LinkedHashMap nthNodeMap2 =
				new LinkedHashMap();

			Iterator targetNodes = nthNodeMap.keySet().iterator();
			while(targetNodes.hasNext()){
				Object targetNode = targetNodes.next();
				Object orgNode = nthNodeMap.get(targetNode);

				Rectangle orgRect = getBounds(orgNode);
				double cx = orgRect.getCenterX();
				double cy = orgRect.getCenterY();

				// ∫∆«€√÷
				Rectangle rect = getBounds(targetNode);
				double cx2 = rect.getCenterX();
				double cy2 = rect.getCenterY();

				double dx = cx2 - cx;
				double dy = cy2 - cy;

				if(dx == 0.0){ dx = 0.000000001; }
				double angle = Math.atan(dy / dx);
				if(dx < 0.0) angle += Math.PI;
				angle += Math.PI;

				for(int i = 0, j = 1; i < getLinkNum(targetNode); i++){
					Object nthNode = getNthNode(targetNode, i, observer);
					if(reallocatedNodeSet.contains(nthNode)){
						continue;
					}
					nthNodeMap2.put(nthNode, targetNode);
					reallocatedNodeSet.add(nthNode);
					double springLength = calcLinkLength(nthNode, targetNode);
					double theta = 2 * Math.PI * ((double)j / (double)getLinkNum(targetNode)) + angle;
					int x = 
						(int)(cx2 + springLength * Math.cos(theta));
					int y = 
						(int)(cy2 + springLength * Math.sin(theta));
					
					Rectangle nthRect = getBounds(nthNode);
					move(nthNode, x - nthRect.getCenterX(), y - nthRect.getCenterY());
//					nthNode.setActivity(ActiveNode.MOVE, targetPanel_);
					j++;
				}
			}
			nthNodeMap = nthNodeMap2;
		}

	}

	public String typeName() {
		return "isogon";
	}

	public boolean useDefaultFource() {
		return false;
	}

	static
	private double calcLinkLength(Object node1, Object node2){
		double linkLengthMargin = 50;
		Rectangle rect1 = getBounds(node1);
		Rectangle rect2 = getBounds(node2);

		double dx = rect1.getCenterX() - rect2.getCenterX();
		double dy = rect1.getCenterY() - rect2.getCenterY();

		double l = Math.sqrt(dx * dx + dy * dy);

		double dw = rect1.getWidth() + rect2.getWidth();
		double dh = rect1.getHeight() + rect2.getHeight();

		boolean side = ((Math.abs(dx) / dw) < (Math.abs(dy) / dh)) ? true : false;

		double value = 0;
		if(side){
			double h = (rect1.getHeight() + rect2.getHeight()) /2;
			double y = Math.abs(dy);
			value = (l * h) / y;
		} else {
			double w = (rect1.getWidth() + rect2.getWidth()) /2;
			double x = Math.abs(dx);
			value = (l * w) / x;
		}
		return value + linkLengthMargin;
	}

	public void draw(Object baseNode, Object observer) {
		// TODO Auto-generated method stub
		
	}
}
