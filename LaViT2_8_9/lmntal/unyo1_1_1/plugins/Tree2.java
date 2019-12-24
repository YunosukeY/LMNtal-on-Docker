package plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import jp.ac.waseda.info.ueda.unyo.customReallocator.*;

public class Tree2 extends Reallocator{

	final static
	private double CHILD_WIDTH = 30.0;

	final static
	private double CHILD_HEIGHT = 12.0;
	
	static
	private Object baseNode_ = null;
	static
	private HashMap<Object, Double> widthMap_;
	static
	private HashMap<Object, Integer> levelMap_;
	static
	private HashMap<Object, Object> parentMap_;

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
			if(nodeSet.contains(targetNode)|| targetNode.equals(node)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isBaseNode(Object node, Object observer) {
		baseNode_ = node;
		return true;
	}
	
	private void calcu(Object baseNode, Object observer){
		HashMap<Object, Integer> levelMap = new HashMap<Object, Integer>();
		HashMap parentMap = new HashMap();		
		HashMap<Object, Double> widthMap = new HashMap<Object, Double>();
		HashSet<Object> childNodes = new HashSet<Object>();
		HashMap<Object, HashSet> childrenMap = new HashMap<Object, HashSet>();
		LinkedList<Object> preorderSet = new LinkedList<Object>();

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
		preorder(baseNode,preorderSet,parentMap,observer);
		divideAndConquer(preorderSet, levelMap, widthMap, parentMap, observer);
		widthMap_ = widthMap;
		levelMap_ = levelMap;
		parentMap_ = parentMap;
	}

	private int levelOrder(Object node,
			HashMap<Object, Integer> levelMap,
			int depth,
			HashMap parentMap,
			int level,
			Object observer){
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

	private void preorder(Object node,
			LinkedList<Object> preorderSet,
			HashMap parentMap,
			Object observer){
		int n = 0;
		if(!preorderSet.contains(node)){
			preorderSet.add(node);
		}
		for(int i = 0; i < getLinkNum(node); i++){
			Object targetNode = getNthNode(node, i, observer);
			if(parentMap.get(node).equals(targetNode) || preorderSet.contains(targetNode)){
				continue;
			}
			n++;
			if(getLinkNum(targetNode) != 1){
				preorder(targetNode, preorderSet, parentMap, observer);
				continue;
			}
			if(!preorderSet.contains(targetNode)){
				preorderSet.add(targetNode);
			}
		}

	}
	
	private void setSubtree(Object node,
			LinkedList subtree,
			HashMap parentMap,
			HashMap<Object, Double> widthMap,
			Double maxWidth,
			Double minWidth,
			Object observer){
		for(int i = 0; i < getLinkNum(node); i++){
			Object nthNode = getNthNode(node, i, observer);
			if(parentMap.get(node) == null || subtree.contains(node)){
				continue;
			}
			if(parentMap.get(node).equals(nthNode)){
				continue;
			}
			double x = widthMap.get(nthNode);
			maxWidth = Math.max(x, maxWidth);
			minWidth = Math.min(x, minWidth);
			subtree.add(nthNode);
			if(getLinkNum(nthNode) != 1){
				setSubtree(nthNode, subtree, parentMap, widthMap, maxWidth, minWidth, observer);
			}
		}
	}
	
	private void removeWidth(Object parentNode, HashMap parentMap, HashMap<Object, Double> widthMap, Object observer){
		double preMax = 0.0;
		for(int i = 0; i < getLinkNum(parentNode); i++){
			Object nthNode = getNthNode(parentNode, i, observer);
			LinkedList subtree = new LinkedList();
			double maxWidth = Double.MIN_VALUE;
			double minWidth = Double.MAX_VALUE;
			setSubtree(nthNode, subtree, parentMap, widthMap, maxWidth, minWidth, observer);
			if(subtree.isEmpty()){
				maxWidth = widthMap.get(nthNode);
				minWidth = widthMap.get(nthNode);
			}
			if(i==0){
				preMax = maxWidth;
				continue;
			}
			for (ListIterator nodes = subtree.listIterator(); nodes.hasNext();) {
				Object node = (Object) nodes.next();
				double x = widthMap.get(node);
				widthMap.remove(node);
				widthMap.put(nthNode, x + CHILD_WIDTH + preMax - minWidth);
			}
			preMax = maxWidth;
		}
		parentNode = parentMap.get(parentNode);
		if(parentNode == null){
			return;
		}
		if(!parentNode.equals(baseNode_)){
			removeWidth(parentNode, parentMap, widthMap, observer);
		}
	}
	
	private void divideAndConquer(LinkedList<Object> preorderSet,
			HashMap<Object, Integer> levelMap,
			HashMap<Object, Double> widthMap,
			HashMap<Object, Object> parentMap,
			Object observer){
		double x = 0;
		//divide
		for (ListIterator nodes = preorderSet.listIterator(); nodes.hasNext();) {
			Object node = (Object) nodes.next();
			x += CHILD_WIDTH*1.5;
			widthMap.put(node, x);
		}
		//conquer
		//親ノードを子ノードの中心に配置
		ListIterator nodes = preorderSet.listIterator(preorderSet.size());
		while(nodes.hasPrevious()) {
			Object node = (Object) nodes.previous();
			if(getLinkNum(node) == 1){
				continue;
			}
			double prex = 0, max = 0, min = 0;
			boolean b = true;
			for(int i=0; i < getLinkNum(node); i++){
				Object childNode = getNthNode(node, i, observer);
				if(parentMap.get(node).equals(childNode)){
					continue;
				}
				x = widthMap.get(childNode);
				if(b){
					max = x;
					min = x;
				}else{
					max = Math.max(x, prex);
					min = Math.min(x, prex);
				}
				prex = x;
				b = false;
			}

			double parentx = (max - min)/2 + min;
			widthMap.remove(node);
			widthMap.put(node, parentx);
		}
		//reduce extra width
		int prelevel = Integer.MAX_VALUE, level = 0;
		boolean b = true;
		for(int i = 0; i < getLinkNum(baseNode_); i++){
			Object nthNode = getNthNode(baseNode_, i, observer);
			LinkedList subtree = new LinkedList();
			setSubtree(nthNode, subtree, parentMap, widthMap, Double.MIN_VALUE, Double.MAX_VALUE, observer);
			for (nodes = subtree.listIterator(); nodes.hasNext();) {
				Object node = (Object) nodes.next();
				level = levelMap.get(node);
				if(b){
					prelevel = level;
					continue;
				}
				if(level < prelevel){
					Object parentNode = parentMap.get(node);
					removeWidth(parentNode, parentMap, widthMap, observer);
					break;
				}
			}
		}
//		double preMax = 0;
//		HashMap<Integer, Double> levelWidthMap = new HashMap<Integer, Double>();
//		HashMap<Integer, Double> levelWidthMap2 = new HashMap<Integer, Double>();
//		for(int i = 0; i < getLinkNum(baseNode_); i++){
//			Object nthNode = getNthNode(baseNode_, i, observer);
//			LinkedList subtree = new LinkedList();
//			setSubtree(nthNode, subtree, parentMap, observer);
//			if(widthMap.get(nthNode) == null){
//				continue;
//			}
//			double prex = widthMap.get(nthNode), max = prex, min = prex;
//			if(subtree.isEmpty()){
//				if(i != 0){
//					x = widthMap.get(nthNode);
//					widthMap.remove(nthNode);
//					widthMap.put(nthNode, x - CHILD_WIDTH + preMax - min);
//				}
//				preMax = max;
//				continue;
//			}
////			int clevel = 0, plevel = 0, depth = 0, preDepth = 0;
////			double levelMax = 0.0;
////			boolean b = true;
////			nodes = subtree.listIterator(subtree.size());
////			while(nodes.hasPrevious()) {
////				Object node = (Object) nodes.previous();
////				clevel = levelMap.get(node);
////				x = widthMap.get(node);
////				if(b){
////					plevel = clevel;
////					levelMax = x;
////					depth = clevel;
////				}
////				max = Math.max(x, prex);
////				min = Math.min(x, prex);
////				prex = x;
////				if(plevel == clevel){
////					levelMax = Math.max(x, levelMax);
////				}else{
////					levelMax = x;
////				}
////				if(levelMap.containsKey(clevel)){
////					levelMap.remove(clevel);
////				}
////				levelWidthMap.put(clevel, levelMax);
////				depth = Math.max(clevel, depth);
////				plevel = clevel;
////			}
//			Iterator subtreeNodes = subtree.iterator();
//			while (subtreeNodes.hasNext()) {
//				Object node = (Object) subtreeNodes.next();
//				x = widthMap.get(node);
//				max = Math.max(x, prex);
//				min = Math.min(x, prex);
//				prex = x;
//			}
//			if(i == 0){
////				levelWidthMap2.putAll(levelWidthMap);
//				preMax = max;
////				preDepth = depth;
//				continue;
//			}
////			double depthMax = preMax;
////			if(!levelWidthMap2.containsKey(depth) || depth <= preDepth){
////				int thisLevel = 0;
////				Object preNode = null;
////				nodes = subtree.listIterator();
////				b = true;
////				while(nodes.hasNext()){
////					Object node = (Object) nodes.next();
////					thisLevel = levelMap.get(node);
////					if( thisLevel == 3 && !b){
////						min = levelWidthMap.get(preNode);
////						break;
////					}
////					if(levelMap.get(node) == preDepth){
////						min = levelWidthMap2.get(preDepth);
////						break;
////					}
////					preNode = node;
////					b = false;
////				}
////			}else{
////				depthMax = levelWidthMap2.get(depth);
////			}
//			x = widthMap.get(nthNode);
//			widthMap.remove(nthNode);
//			widthMap.put(nthNode, x - CHILD_WIDTH + preMax - min);
////			nodes = subtree.listIterator(subtree.size());
////			while(nodes.hasPrevious()) {
////				Object node = (Object) nodes.previous();
////				x = widthMap.get(node);
////				widthMap.remove(node);
////				widthMap.put(node, x + CHILD_WIDTH + depthMax - min);
////			}
//			subtreeNodes = subtree.iterator();
//			while (subtreeNodes.hasNext()) {
//				Object node = (Object) subtreeNodes.next();
//				x = widthMap.get(node);
//				widthMap.remove(node);
////				if(nosubtree){
////					widthMap.put(node, x - CHILD_WIDTH + preMax - parentNodex*1.0);
////				}else{
//					widthMap.put(node, x - CHILD_WIDTH + preMax - min*1.0);
////				}
//			}
////			levelWidthMap2.putAll(levelWidthMap);
////			preMax = max + CHILD_WIDTH + depthMax - min;
////			preDepth = depth;
//		}
		double prex = 0, max = 0, min = 0;
		for(int i = 0; i < getLinkNum(baseNode_); i++){
			Object nthNode = getNthNode(baseNode_, i, observer);
			if(widthMap.get(nthNode) == null){
				continue;
			}
			x = widthMap.get(nthNode);
			if(i == 0){
				max = x;
				min = x;
			}else{
				max = Math.max(x, prex);
				min = Math.min(x, prex);
			}
			prex = x;
		}
		widthMap.remove(baseNode_);
		widthMap.put(baseNode_, (max - min)/2 + min);
	}

	@Override
	public void reallocate(Object baseNode, Object observer) {
		int baseLinkNum = getLinkNum(baseNode);
		if(baseLinkNum == 0){
			return;
		}
		calcu(baseNode, observer);
		HashSet reallocatedNodeSet = new HashSet();
		if(widthMap_.size() == 0){
			return;
		}
		reallocatedNodeSet.add(baseNode);
		double baseNodeX = widthMap_.get(baseNode);
		HashSet connectedNodeSet = getConnectedNodes(baseNode, observer);
		Iterator connectedNodes = connectedNodeSet.iterator();
		while (connectedNodes.hasNext()) {
			Object connectedNode = (Object) connectedNodes.next();
			if(reallocatedNodeSet.contains(connectedNode)){
				continue;
			}
			int targetLevel = levelMap_.get(connectedNode);
			Object parentNode = parentMap_.get(connectedNode);
			double cx = getBounds(baseNode_).getCenterX();
			double cy = getBounds(parentNode).getCenterY();
			double x = cx + widthMap_.get(connectedNode) - baseNodeX;
			double y = cy+ CHILD_HEIGHT * (targetLevel + 1);
			move(connectedNode,
					x - getBounds(connectedNode).getCenterX(),
					y - getBounds(connectedNode).getCenterY());
			reallocatedNodeSet.add(connectedNode);
		}
	}

	@Override
	public String typeName() {
		return "binary tree";
	}

	@Override
	public boolean useDefaultFource() {
		return false;
	}

	@Override
	public void draw(Object baseNode, Object observer) {
		
	}

}
