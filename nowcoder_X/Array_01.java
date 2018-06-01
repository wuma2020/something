package cn.kkcoder.jianzhi_offer;

import java.util.Arrays;

/**
 * 题目：   在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列
 *         都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数
 *         组和一个整数，判断数组中是否含有该整数。
 *
 *
 * 题目看错了，以为要自己吧无序的数组排序完成之后再进行比较。  题目给的数组就是有序的，所以排序的步骤是可以省去的.
 * 自己做麻烦了。
 */


/**
 * Created by static-mkk on 3/4/2018.
 */
public class Array_01 {

	public static void main(String[] args) {
		int[][] number = {{2, 3, 1}, {5, 4, 7}};
		boolean b = find(3, number);
		System.out.println(b);
	}


	private static boolean find(int target, int[][] number) {

		boolean result = false;

		//假设该数组为对称数组
		int rows = number.length;//二维数组的length属性为行数

		int columns = number[0].length;//第一行列数


		int[] newintArray = new int[rows * columns];

		int p = 0;
		//把所有值放到新数组中
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				newintArray[p] = number[i][j];
				if (number[i][j] == target) {
					result = true;
				}
				p++;
			}

		}

		Arrays.sort(newintArray);

		//把排序号的数组放回去
		for (int i = rows - 1; i >= 0; i--) {
			for (int j = columns - 1; j >= 0; j--) {
				number[i][j] = newintArray[p - 1];
				p--;
			}

		}
		System.out.println(Arrays.toString(newintArray));
		return result;
	}


}
