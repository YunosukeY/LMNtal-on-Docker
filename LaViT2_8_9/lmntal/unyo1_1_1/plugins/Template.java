package plugins;
/*
 * ���Ѥ���plugin��plugins�ѥå�������ľ�����֤����� 
 * package plugins;
 */
import java.awt.Rectangle;
import java.util.HashSet;

/* Reallocator��import */
import jp.ac.waseda.info.ueda.unyo.customReallocator.Reallocator;

/**
 * ������������֤Υƥ�ץ졼��
 * @author wakako
 *
 */

/* 
 * �Ѱդ���Ƥ���᥽�å�
 * HashSet getConnectedNodes(node, observer) // node�ȥ�󥯤ǷҤ��ä�Node���������
 * Object getNthNode(node, i, observer) // i���ܤΥ�����Node���������
 * Rectangle getBounds(node) // Node���礭�����������
 * int getLinkNum(node) // ��󥯿����������
 * String getName(node) // Node��̾�����������
 * boolean isAtom(node) // Node�����ȥफ�ɤ���
 * move(node, dx, dy) //dx, dy ʬ����Node���ư������
 */

public class Template extends Reallocator{

	/**
	 * �����֤�Ŭ���ϰϤ���ꤹ�롥
	 * ���Ȥʤ�node��link�ǷҤ��ä�node�ΰ�����Ŭ���ϰϤȤ����֤���
	 * �⤷����node���Ƥ��֤���
	 */
	@Override
	public HashSet<Object> groupNode(Object node, Object observer) {
		/*
		 * getConnectedNodes�᥽�åɤ�Node�ȥ�󥯤ǷҤ��ä�
		 * Node������Ǥ��롥(��ʬ���Ȥϴޤޤʤ�)
		 * �ʲ��ϡ���󥯤ǷҤ��ä�Node���Ƥ�Ŭ���ϰϤȤ����硥
		 */
//		HashSet nodeSet = new HashSet();
//		HashSet nthSet = getConnectedNodes(node, observer);
//		return nthSet;
		return null;
	}
	
	/**
	 * ���ꤵ�줿node�����Ȥ�����node�Ȱۤʤ��硤false���֤���
	 */
	@Override
	public boolean isBaseNode(Object node, Object observer) {
		return true;
	}

	/**
	 * baseNode���˺����֤�Ԥ���
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
	 * typeName���֤�
	 */
	@Override
	public String typeName() {
//		return "typeName";
		return null;
	}

	/**
	 * �̾���ϳؤ�Ȥ���硤true���֤���
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
