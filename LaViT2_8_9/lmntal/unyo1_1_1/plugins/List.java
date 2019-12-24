package plugins;

import jp.ac.waseda.info.ueda.unyo.customReallocator.*;

import java.awt.Rectangle;
import java.util.HashSet;

public class List extends Reallocator{

	@Override
	public boolean useDefaultFource(){
		return false;
	}
	
	@Override
	public void reallocate(Object baseNode, Object observer) {

		Object nthNode = getNthNode(baseNode, 0, observer);
		Rectangle rect = getBounds(baseNode);
//		非Active Node は飛ばす
//		while(nthNode != null && nthNode == null
//		&& getName(nthNode).equals(".")
//		&& getLinkNum(nthNode) == 3){
//		nthNode = getNthNode(nthNode, 2, observer);
//		}
		double cx = rect.getCenterX();
		double cy = rect.getCenterY();
		double springLength = 1000;
		if(baseNode != null && nthNode != null){
			springLength = calcLinkLength(baseNode, nthNode);
		}
		double x = cx - springLength;
		double y = cy;

		if(nthNode != null){
			Rectangle nthRect = getBounds(nthNode);
			move(nthNode, x - nthRect.getCenterX(), y - nthRect.getCenterY());
		}
		listAngle(nthNode, x, y,observer);

	}

	private void listAngle(Object node, double cx,double cy,Object observer){
		if(!((getName(node).equals("."))
				&& (getLinkNum(node) == 3))){
			return;
		}
		Object nthNode0 = getNthNode(node, 0, observer);
		Object nthNode2 = getNthNode(node, 2, observer);
		if(nthNode0 == null || nthNode2 == null || node == null){
			return;
		}
//		非ActiveNode は飛ばす
//		while(nthNode2 != null && nthNode2 == null
//		&& getName(nthNode2).equals(".")
//		&& getLinkNum(nthNode2) == 3){
//		nthNode2 = getNthNode(nthNode2, 2, observer);
//		}

//		double cx = getBounds(node).getCenterX();
//		double cy = getBounds(node).getCenterY();
//		double cx = baseRect.getCenterX();
//		double cy = baseRect.getCenterY();
		double springLength0 = 1000;
		double springLength2 = 1000;
		if(nthNode0 != null && node != null){
			springLength0 = calcLinkLength(nthNode0, node);
		}
		if(nthNode2 != null && node != null){
			springLength2 = calcLinkLength(nthNode2, node);
		}
		double x = cx - springLength2;
		double y = cy + springLength0;

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
			listAngle(nthNode2, x, cy, observer);	
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
		if(!(getName(node).equals("[]") && (getLinkNum(node) == 1))){
			return false;
		}
		
		return true;
	}

	@Override
	public String typeName() {
		return "list";
	}

	@Override
	public HashSet<Object> groupNode(Object node, Object observer) {
		if(!isBaseNode(node,observer)){
			return null;
		}
		Object nthNode = getNthNode(node, 0, observer);
		if(getLinkNum(nthNode) != 3 
				&& !getName(nthNode).equals(".")){
			return null;
		}
		Object opstNode = getNthNode(nthNode, 1, observer);
		if(node != opstNode){
			return null;
		}
		HashSet<Object> nodeSet = new HashSet<Object>();
		nodeSet.add(node);
		nodeSet.add(nthNode);		
		list(nthNode, observer, nodeSet);
		return nodeSet;	
	}

	private void list(Object node, Object observer, HashSet<Object> nodeSet){
		if(getLinkNum(node) != 3 || true){
			return;
		}
		Object nthNode0 = getNthNode(node, 0, observer);
		Object nthNode2 = getNthNode(node, 2, observer);
		if(nthNode0 == null || nthNode2 == null){
			return;
		}
		nodeSet.add(nthNode0);	
		nodeSet.add(nthNode2);

		if(getName(nthNode2).equals(".")){
			list(nthNode2, observer, nodeSet);
		}

	}

	@Override
	public void draw(Object baseNode, Object observer) {
		// TODO Auto-generated method stub
		
	}

}

