package plugins;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import jp.ac.waseda.info.ueda.unyo.customReallocator.*;

public class Tree extends Reallocator{

	final static
	private double CHILD_WIDTH = 30.0;

	final static
	private double CHILD_HEIGHT = 15.0;

	@Override
	public HashSet<Object> groupNode(Object baseNode, Object observer) {
		int baseLinkNum = getLinkNum(baseNode);
		if(baseLinkNum == 0){
			return null;
		}
		HashSet nodeSet = new HashSet();

		if(!isTree(baseNode, nodeSet, observer)){
			return null;
		}

		HashSet nthSet = getConnectedNodes(baseNode, observer);
		return nthSet;
	}

	private boolean isTree(Object node, HashSet nodeSet, Object observer){
		for(int i = 0; i < getLinkNum(node); i++){
			Object targetNode = getNthNode(node, i, observer);
			if(nodeSet.contains(targetNode) || targetNode.equals(node)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isBaseNode(Object node, Object observer) {
		return true;
	}

	private int levelOrder(Object node,
			HashMap<Object, Integer> levelMap,
			int depth,
			HashMap parentMap, int level, Object observer){
		int linkNum = getLinkNum(node);
//		if(linkNum <= 1){
//		return;
//		}
		level++;
		for(int i=0; i < linkNum; i++){
			Object targetNode = getNthNode(node, i, observer);
			if(!parentMap.containsValue(targetNode)){
				levelMap.put(targetNode, level);
				parentMap.put(targetNode, node);
				if(depth <= level)	depth = level;
				if(getLinkNum(targetNode) > 1){
					depth =
						levelOrder(targetNode,
								levelMap,
								depth,
								parentMap,
								level,
								observer);
				}
			}
		}
		return depth;
	}
	private void widthOrder(Object node,
			HashMap<Object, Integer> levelMap,
			HashMap parentMap, HashMap<Object, Double> widthMap,
			int level, Object observer){
		double chw = 0;
		Object parentNode = parentMap.get(node);
		for(int i = 0; i < getLinkNum(parentNode); i++){
			Object targetNode = getNthNode(parentNode, i, observer);
			if(!parentMap.get(targetNode).equals(parentNode)){
				continue;
			}
			if(widthMap.containsKey(targetNode)){
				chw += widthMap.get(targetNode) - CHILD_WIDTH*2;
//				chw += CHILD_WIDTH;
			}
			chw += (i != 0) ? getBounds(targetNode).getWidth() + CHILD_WIDTH
					: getBounds(targetNode).getWidth();
			widthMap.put(targetNode, chw);
			levelMap.remove(targetNode);
		}
		widthMap.put(parentNode, chw);
		if(levelMap.isEmpty()){
			return;
		}
		while(!levelMap.containsValue(level) && level > 1){
			level--;				
		}
		Iterator targetNodes = levelMap.keySet().iterator();
		while (targetNodes.hasNext()) {
			Object targetNode = (Object) targetNodes.next();
			int targetLevel = levelMap.get(targetNode);
			if(targetLevel == level && level > 1){
				widthOrder(targetNode, levelMap, parentMap, widthMap, level, observer);
				break;
			}
		}		
	}

	@Override
	public void reallocate(Object baseNode, Object observer) {
		int baseLinkNum = getLinkNum(baseNode);
		if(baseLinkNum == 0){
			return;
		}
		HashMap<Object, Integer> levelMap = new HashMap<Object, Integer>();
		HashMap<Object, Integer> levelMap2 = new HashMap<Object, Integer>();
		HashSet reallocatedNodeSet = new HashSet();
		HashMap parentMap = new HashMap();		
		HashMap<Object, Double> widthMap = new HashMap<Object, Double>();

		int depth = 0;
		int level = 1;
		levelMap.put(baseNode, level);
		parentMap.put(baseNode, baseNode);
		depth = 
			levelOrder(baseNode,
					levelMap,
					depth,
					parentMap,
					level,
					observer);
		
		levelMap2.putAll(levelMap);
		
		Object chileNode = null;
		Iterator nodes = levelMap.keySet().iterator();
		while (nodes.hasNext()) {
			Object node = (Object) nodes.next();
			int targetLevel = levelMap.get(node);
			if(targetLevel == depth){
				chileNode = node;
				widthOrder(chileNode, levelMap2, parentMap, widthMap, depth, observer);
				break;
			}
		}
		
		if(widthMap.size() == 0){
			return;
		}
		
		reallocatedNodeSet.add(baseNode);
		
		boolean isBase = false;
		Iterator orgNodes = parentMap.values().iterator();
		while (orgNodes.hasNext()) {
			Object orgNode = (Object) orgNodes.next();
			if(orgNode.equals(baseNode)) isBase = true;
			double orgWidth = widthMap.get(orgNode);
			Rectangle orgRect = getBounds(orgNode);
			double cx = orgRect.getCenterX();
			double cy = orgRect.getCenterY();
			double pointx = cx;
			int linkNum = getLinkNum(orgNode);
			for(int i = 0, j = 0; i < linkNum; i++){
				Object targetNode = getNthNode(orgNode, i, observer);			
				if(reallocatedNodeSet.contains(targetNode)
						|| !parentMap.get(targetNode).equals(orgNode)){						
					continue;
				}

				double targetWidth = widthMap.get(targetNode);
				int targetLevel = levelMap.get(targetNode);
				if(linkNum % 2 == ((isBase)? 0 : 1))
					pointx += targetWidth * (2*(j%2) - 1);
				double x = pointx;
				double y = cy + CHILD_HEIGHT * (targetLevel + 1);
				move(targetNode, x - getBounds(targetNode).getCenterX(),
						y - getBounds(targetNode).getCenterY());
				reallocatedNodeSet.add(targetNode);
				if(linkNum % 2 == ((isBase)? 1 : 0))
					pointx += -targetWidth * (2*(j%2) - 1);
				j++;
			}
			isBase = false;
		}
	}

	@Override
	public String typeName() {
		return "tree";
	}

	@Override
	public boolean useDefaultFource() {
		return false;
	}

	@Override
	public void draw(Object baseNode, Object observer) {
		// TODO Auto-generated method stub
		
	}

}
