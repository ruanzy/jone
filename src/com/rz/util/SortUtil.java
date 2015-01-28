package com.rz.util;

public class SortUtil
{
	static int[] a = new int[] { 49, 38, 65, 97, 76, 13, 27, 49, 55, 4 };

	/**
	 * 原理：将数组分为无序区和有序区两个区，然后不断将无序区的第一个元素按大小顺序插入到有序区中去，最终将所有无序区元素都移动到有序区完成排序。
	 * 假设最初有序区只有一个元素a[0]
	 * 
	 * @param a
	 */
	public static void insertSort(int[] a)
	{
		for (int j = 1; j < a.length; j++)
		{
			int t = a[j];
			int i = j - 1;
			for (; i >= 0 && t < a[i]; i--)
			{
				a[i + 1] = a[i];
			}
			a[i + 1] = t;
		}
	}

	/**
	 * 原理：将数组分为无序区和有序区两个区，然后不断将无序区的元素两两比较，较大者后移，这样经过一轮，最大值移到了有序区中，
	 * 最终将所有无序区元素都移动到有序区完成排序。
	 * 
	 * @param a
	 */
	public static void bubbleSort(int[] a)
	{
		for (int i = 0; i < a.length; i++)
		{
			for (int j = 0; j < a.length - i - 1; j++)
			{
				if (a[j + 1] < a[j])
				{
					int t = a[j];
					a[j] = a[j + 1];
					a[j + 1] = t;
				}
			}
		}
	}

	/**
	 * 原理：将数组分为无序区和有序区两个区，寻找无序区中的最小值和无序区的首元素交换，有序区扩大一个,最终将所有无序区元素都移动到有序区完成排序。
	 * 
	 * 
	 */
	public static void selectSort(int[] a)
	{
		for (int i = 0; i < a.length; i++)
		{
			for (int j = i + 1; j < a.length; j++)
			{
				if (a[j] < a[i])
				{
					int t = a[i];
					a[i] = a[j];
					a[j] = t;
				}
			}
		}
	}

	/**
	 * 原理：将两个有序数组归并为一个数组。
	 * 
	 * 
	 * 
	 */
	public static int[] merge(int[] A, int[] B)
	{
		int[] C = new int[A.length + B.length];
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		while (p1 < A.length && p2 < B.length)
		{
			if (A[p1] <= B[p2])
			{
				C[p3] = A[p1];
				p1++;
				p3++;
			}
			else
			{
				C[p3] = B[p2];
				p2++;
				p3++;
			}
		}
		while (p1 < A.length)
		{

			C[p3] = A[p1];
			p1++;
			p3++;
		}
		while (p2 < B.length)
		{
			C[p3] = A[p2];
			p2++;
			p3++;
		}
		return C;
	}

	/**
	 * 原理：是对插入排序的改进，每次缩小增量。
	 * 
	 * @param arr
	 */
	public static void ShellSort(int[] arr)
	{
		int t;
		int j;
		int step = arr.length / 2;
		while (step >= 1)
		{
			for (int i = step; i < arr.length; i++)
			{
				if (arr[i] < arr[i - step])
				{
					t = arr[i];
					for (j = i - step; j >= 0 && arr[j] > t; j -= step)
					{
						arr[j + step] = arr[j];
					}
					arr[j + step] = t;
				}
			}
			step /= 2;
		}
	}

	public static void ShellSort2(int[] arr)
	{
		int j;
		int len = arr.length;
		for (int val = len >> 1; val > 0; val >>= 1)
		{
			for (int i = val; i < len; i++)
			{
				int temp = arr[i];
				for (j = i; j >= val && temp < arr[j - val]; j -= val)
				{
					arr[j] = arr[j - val];
				}
				arr[j] = temp;
			}
		}
	}

	private static int getKey(int[] arr, int low, int high)
	{
		int key = low;
		while (low < high)
		{
			while (low < high && arr[high] > arr[key])
			{
				high--;
			}
			int t = arr[high];
			arr[high] = arr[key];
			arr[key] = t;
			key = high;
			while (low < high && arr[low] < arr[key])
			{
				low++;
			}
			int t2 = arr[low];
			arr[low] = arr[key];
			arr[key] = t2;
			key = low;
		}
		return key;
	}

	public static void QuickSort(int[] arr, int low, int high)
	{
		if (low < high)
		{
			int key = getKey(arr, low, high);
			QuickSort(arr, low, key - 1);
			QuickSort(arr, key + 1, high);
		}
	}

	/**
	 * 二分法查找
	 * 
	 * @param arr
	 *            ,el
	 * 
	 */
	public static int BinarySearch(int[] arr, int el)
	{
		int low = 0;
		int high = arr.length - 1;
		int mid = 0;
		while (low < high)
		{
			mid = (high + low) / 2;
			if (arr[mid] > el)
			{
				high = mid - 1;
			}
			else if (arr[mid] < el)
			{
				low = low + 1;
			}
			else
			{
				return mid;
			}
		}
		return -1;
	}

	public static long Fbi(int n)
	{
		if (n < 2)
		{
			return n;
		}
		else
		{
			long a = 0;
			long b = 1;
			long c = 0;
			for (int i = 2; i <= n; i++)
			{
				c = a + b;
				a = b;
				b = c;
			}
			return c;
		}
	}

	public static void main(String[] args)
	{
		long t1 = System.currentTimeMillis();
		insertSort(a);
		// selectSort(a);

		// bubbleSort(a);
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
		for (int i = 0; i < a.length; i++)
		{
			System.out.println(a[i]);
		}
	}
}