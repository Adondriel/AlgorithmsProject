import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class algorithm
{
	private static int numFiles = 30;
	private static ArrayList<String> mainFolders = new ArrayList<String>();
	private static ArrayList<String> subFolders = new ArrayList<String>();

	private static int[] array;

	public static void main(String[] args) throws FileNotFoundException
	{
		mainFolders.add("small");
		mainFolders.add("med");
		mainFolders.add("large");
		subFolders.add("unsorted");
		subFolders.add("sorted");
		subFolders.add("reverseSorted");

		for (String folder : mainFolders)
		{
			for (String subFolder : subFolders)
			{
				for (int i = 0; i < numFiles; i++)
				{
					String fileName = folder + "/" + subFolder + "/" + folder + "_" + subFolder + "_" + i + ".csv";
					int arraySize = 0;
					switch (folder)
					{
					case "small":
						arraySize = 10000;
					case "med":
						arraySize = 100000;
					case "large":
						arraySize = 1000000;
					}
					System.out.println(fileName);
					readFileIntoArray(fileName, arraySize);
				}

			}
		}

	}

	public static void readFileIntoArray(String fileName, int arraySize) throws FileNotFoundException
	{
		array = new int[arraySize];
		BufferedReader br = null;
		String line = "";
		try
		{
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null)
			{

				// use comma as separator
				String[] strArr = line.split(",");
				for (int i = 0; i < strArr.length; i++)
				{
					// trying to parse String value as int
					try
					{
						// worked, assigning to respective int[] array position
						array[i] = Integer.parseInt(strArr[i]);
					}
					// didn't work, moving over next String value
					// at that position int will have default value 0
					catch (NumberFormatException nfe)
					{
						continue;
					}
				}
			}
			quickSort(0, array.length - 1);
			boolean isSorted = true;
			for (int f = 0; f < array.length - 1; f++)
			{
				if (array[f] > array[f + 1])
				{
					isSorted = false;
				}

			}
			System.out.println(isSorted);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	// Referencing https://algs4.cs.princeton.edu/23quicksort/QuickX.java.html
	public static void quickSort(int lo, int hi)
	{
		if (hi <= lo)
			return;

		int n = hi - lo + 1;
		// if (n <= 21) {
		// Insertion.sort(a, lo, hi + 1);
		// return;
		// }

		int j = partition(lo, hi);
		quickSort(lo, j - 1);
		quickSort(j + 1, hi);
	}

	public static int partition(int lo, int hi)
	{
		int n = hi - lo + 1;
		int m = median3(lo, lo + n / 2, hi);
		swap(m, lo);

		int i = lo;
		int j = hi + 1;
		int v = array[lo];

		// a[lo] is unique largest element
		while (less(array[++i], v))
		{
			if (i == hi)
			{
				swap(lo, hi);
				return hi;
			}
		}

		// a[lo] is unique smallest element
		while (less(v, array[--j]))
		{
			if (j == lo + 1)
				return lo;
		}

		// the main loop
		while (i < j)
		{
			swap(i, j);
			while (less(array[++i], v))
				;
			while (less(v, array[--j]))
				;
		}

		// put partitioning item v at a[j]
		swap(lo, j);

		// now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
		return j;
	}

	// return the index of the median element among a[i], a[j], and a[k]
	private static int median3(int i, int j, int k)
	{
		return (less(array[i], array[j]) ? (less(array[j], array[k]) ? j : less(array[i], array[k]) ? k : i) : (less(array[k], array[j]) ? j : less(array[k], array[i]) ? k : i));
	}

	private static boolean less(int v, int w)
	{
		return v < w;
	}

	private static void swap(int i, int j)
	{
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

}
