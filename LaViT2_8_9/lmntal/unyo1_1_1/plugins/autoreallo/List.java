package plugins.autoreallo;

import jp.ac.waseda.info.ueda.unyo.customReallocator.*;

import java.awt.Rectangle;
import java.util.HashSet;


public class List extends Reallocator_AutoLoading{
	static
	private boolean reallocated_ = false;
	static
	private boolean turn_ = true;
	
	@Override
	public void reallocate(Object baseNode, Object observer) {

		Object nthNode = getNthNode(baseNode, 0, observer);
		Rectangle rect = getBounds(baseNode);
		double cx = rect.getCenterX();
		double cy = rect.getCenterY();
		if(reallocated_){
			return;
		}
		int[] screenSize = getScreen(observer);
		if(screenSize[0] < cx || 0 > cx){
			turn_ = false;
		}else{
			turn_ = true;
		}
		double springLength = 1000;
		if(baseNode != null && nthNode != null){
			springLength = calcLinkLength(baseNode, nthNode);
		}
		double x = cx + springLength;
		double y = cy;
//		if(cx <= 0){
//			cx = cx + rect.getWidth();
//			move(baseNode, cx - nthRect.getCenterX(), y - nthRect.getCenterY());
//		}else if(cx >= screenSize[0]){
//			
//		}

		if(nthNode != null){
			Rectangle nthRect = getBounds(nthNode);
			move(nthNode, x - nthRect.getCenterX(), y - nthRect.getCenterY());
		}
		listAngle(nthNode, screenSize[0], 1, x,y,observer);
		reallocated_ = false;

	}

	private void listAngle(Object node, int screenWidth, int count,double cx, double cy,Object observer){
		if(!((getName(node).equals("."))
				&& (getLinkNum(node) == 3))){
			return;
		}
		Object nthNode0 = getNthNode(node, 0, observer);
		Object nthNode2 = getNthNode(node, 1, observer);
		if(nthNode0 == null || nthNode2 == null || node == null){
			return;
		}
		double springLength0 = 1000;
		double springLength2 = 1000;
		if(nthNode0 != null && node != null){
			springLength0 = calcLinkLength(nthNode0, node);
		}
		if(nthNode2 != null && node != null){
			springLength2 = calcLinkLength(nthNode2, node);
		}
		if(!turn_) count = 1;
		double i = count % 2.0;
		double x = cx + (2.0*i-1.0)*springLength2;
		double y = cy + springLength0;
		if((x <= 0 || x >= screenWidth - 100) && turn_){
			count++;
			i=count % 2.0;
			x = cx + (2.0*i-1.0)*springLength2;
			cy += springLength0*2.0;
		}

		if(nthNode0 != null){
			Rectangle nthRect = getBounds(nthNode0);
			move(nthNode0, cx - nthRect.getCenterX(), y - nthRect.getCenterY());
		}
		if(nthNode2 != null){
			Rectangle nthRect = getBounds(nthNode2);
			move(nthNode2, x - nthRect.getCenterX(), cy - nthRect.getCenterY());
		}
		if((getLinkNum(nthNode2) == 3) 
				&& (getName(nthNode2).equals("."))){
			listAngle(nthNode2, screenWidth, count, x, cy, observer);	
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
//
//	@Override
//	public HashSet<Object> groupNode(Object node, Object observer) {
//		if(!isBaseNode(node,observer)){
//			return null;
//		}
//		Object nthNode = getNthNode(node, 0, observer);
//		if(getLinkNum(nthNode) != 3 
//				&& !getName(nthNode).equals(".")){
//			return null;
//		}
//		Object opstNode = getNthNode(nthNode, 2, observer);
//		if(node != opstNode){
//			return null;
//		}
//		HashSet<Object> nodeSet = new HashSet<Object>();
//		nodeSet.add(node);
//		nodeSet.add(nthNode);		
//		list(nthNode, observer, nodeSet);
//		return nodeSet;	
//	}

	private void list(Object node, Object observer, HashSet<Object> nodeSet){
		if(getLinkNum(node) != 3 || true){
			return;
		}
		Object nthNode0 = getNthNode(node, 0, observer);
		Object nthNode2 = getNthNode(node, 1, observer);
		if(nthNode0 == null || nthNode2 == null){
			return;
		}
		nodeSet.add(nthNode0);	
		nodeSet.add(nthNode2);

		if(getName(nthNode2).equals(".")){
			list(nthNode2, observer, nodeSet);
		}

	}

}

