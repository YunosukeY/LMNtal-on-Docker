package plugins;
/*
 * 使用するpluginはpluginsパッケージの直下に置くこと 
 * package plugins;
 */
import java.awt.Rectangle;
import java.util.HashSet;

/* Reallocatorをimport */
import jp.ac.waseda.info.ueda.unyo.customReallocator.Reallocator;

/**
 * カスタム再配置のテンプレート
 * @author wakako
 *
 */

/* 
 * 用意されているメソッド
 * HashSet getConnectedNodes(node, observer) // nodeとリンクで繋がったNodeを取得する
 * Object getNthNode(node, i, observer) // i番目のリンク先のNodeを取得する
 * Rectangle getBounds(node) // Nodeの大きさを取得する
 * int getLinkNum(node) // リンク数を取得する
 * String getName(node) // Nodeの名前を取得する
 * boolean isAtom(node) // Nodeがアトムかどうか
 * move(node, dx, dy) //dx, dy 分だけNodeを移動させる
 */

public class Template extends Reallocator{

	/**
	 * 再配置の適用範囲を指定する．
	 * 基準となるnodeとlinkで繋がったnodeの一部を適用範囲とし，返す．
	 * もしくはnode全てを返す．
	 */
	@Override
	public HashSet<Object> groupNode(Object node, Object observer) {
		/*
		 * getConnectedNodesメソッドでNodeとリンクで繋がった
		 * Nodeを取得できる．(自分自身は含まない)
		 * 以下は，リンクで繋がったNode全てを適用範囲とする場合．
		 */
//		HashSet nodeSet = new HashSet();
//		HashSet nthSet = getConnectedNodes(node, observer);
//		return nthSet;
		return null;
	}
	
	/**
	 * 指定されたnodeが基準としたいnodeと異なる場合，falseを返す．
	 */
	@Override
	public boolean isBaseNode(Object node, Object observer) {
		return true;
	}

	/**
	 * baseNodeを基に再配置を行う．
	 */
	@Override
	public void reallocate(Object baseNode, Object observer) {

//		Object nthNode = getNthNode(baseNode, 0, observer);
//		Rectangle rect = getBounds(baseNode);
//		double x = rect.getCenterX();
//		double y = rect.getCenterY();
//
//		Rectangle nthRect = getBounds(nthNode);
//		move(nthNode, x - nthRect.getCenterX(), y - nthRect.getCenterY());

	}
	
	/**
	 * typeNameを返す
	 */
	@Override
	public String typeName() {
//		return "typeName";
		return null;
	}

	/**
	 * 通常の力学も使う場合，trueを返す．
	 */
	@Override
	public boolean useDefaultFource() {
		return false;
	}

	@Override
	public void draw(Object baseNode, Object observer) {
		// TODO Auto-generated method stub
		
	}

}
