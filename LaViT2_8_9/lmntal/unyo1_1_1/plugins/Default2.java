package plugins;

import jp.ac.waseda.info.ueda.unyo.customReallocator.*;

import java.awt.Rectangle;
import java.util.*;

public class Default2 extends Reallocator{

	@Override
	public boolean useDefaultFource(){
		return false;
	}
	
	@Override
	public void reallocate(Object baseNode, Object observer) {
		boolean isBase = true;
		HashSet reallocatedNodeSet = new HashSet();

		LinkedHashMap nthNodeMap =
			new LinkedHashMap();
		nthNodeMap.put(baseNode, baseNode);
		reallocatedNodeSet.add(baseNode);

		Rectangle baseRect = getBounds(baseNode);
		double basex = baseRect.getCenterX();
		double basey = baseRect.getCenterY();

		while(!nthNodeMap.isEmpty()){
			LinkedHashMap nthNodeMap2 =
				new LinkedHashMap();

			Iterator targetNodes = nthNodeMap.keySet().iterator();
			while(targetNodes.hasNext()){
				Object node = targetNodes.next();
				Object orgNode = nthNodeMap.get(node);

				Rectangle orgRect = getBounds(orgNode);
				double cx = orgRect.getCenterX();
				double cy = orgRect.getCenterY();

				// ∫∆«€√÷
				Rectangle rect = getBounds(node);
				double cx2 = rect.getCenterX();
				double cy2 = rect.getCenterY();

				double dx = cx2 - basex;
				double dy = cy2 - basey;

				if(dx == 0.0){ dx = 0.000000001; }
				double angle = Math.atan(dy / dx);
				if(dx < 0.0) angle += Math.PI;
				if(isBase)
					angle += Math.PI;
				else
					angle -= Math.PI / 2;


				for(int i = 0, j = 1; i < getLinkNum(node); i++){
					Object nthNode = getNthNode(node, i, observer);
					if(reallocatedNodeSet.contains(nthNode)){
						continue;
					}
					nthNodeMap2.put(nthNode, node);
					reallocatedNodeSet.add(nthNode);

					double springLength = calcLinkLength(nthNode, node) * 2;
//					double springLength = calcLinkLength(nthNode, node) * ((double)(i + getLinkNum(nthNode)) / 5);
					double r = Math.PI * ((double)j / (double)getLinkNum(node));
					double theta = (!isBase) ? r  + angle :
						2 * Math.PI * ((double)j / (double)getLinkNum(node)) + angle;
					int x = 
						(int)(cx2 + springLength * Math.cos(theta));
					int y = 
						(int)(cy2 + springLength * Math.sin(theta));

					Rectangle nthRect = getBounds(nthNode);

					move(nthNode, x - nthRect.getCenterX(), y - nthRect.getCenterY());
					j++;
				}
			}
			isBase = false;
			nthNodeMap = nthNodeMap2;
		}
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

	@Override
	public boolean isBaseNode(Object node, Object observer){
		return true;
	}

	@Override
	public String typeName() {
		return "fan";
	}

	@Override
	public HashSet<Object> groupNode(Object node, Object observer) {
		HashSet nthSet = getConnectedNodes(node, observer);
		return nthSet;	
	}

	@Override
	public void draw(Object baseNode, Object observer) {
		// TODO Auto-generated method stub
		
	}

}

