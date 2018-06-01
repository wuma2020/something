package cn.kkcoder.jianzhi_offer;

import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * 题目：输入一个链表，从尾到头打印链表每个节点的值。
 *
 */
/**
 * Created by static-mkk on 5/4/2018.
 */
public class ListNode_01 {

	public static void main(String[] args) {
		ListNode node = new ListNode(2);
		node.next = new ListNode(4);
		node.next.next = new ListNode(3);
		printListFromTailToHead(node);
	}

	static ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
		ArrayList<Integer> arrayList = new ArrayList<>();
		while (listNode!=null) {
			arrayList.add(listNode.val);
			listNode = listNode.next;
		}

		//方法二
		Collections.reverse(arrayList);

		/*  方法一
		int arraySize = arrayList.size();
		Integer[] ojbArray = arrayList.toArray(new Integer[arraySize]);
		//这里开始倒叙放置元素
		for (int i=0;i<arraySize/2;i++){
			Integer temp = ojbArray[i];
			ojbArray[i]  = ojbArray[arraySize-i-1];
			ojbArray[arraySize-i-1] = temp;
		}
		ArrayList<Integer> list = new ArrayList<>();
		for (int i=0;i<arraySize;i++){
			list.add(ojbArray[i]);
		}
		System.out.println(list.toString());
		return list;
		*/

		System.out.println(arrayList.toString());
		return arrayList;


	}


		static class ListNode {
			int val;
			ListNode next = null;

			ListNode(int val) {
				this.val = val;
			}
		}

}


