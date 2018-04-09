import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class algorithm {
	private static int numFiles = 30;
	private static ArrayList<String> mainFolders = new ArrayList<String>();
	private static ArrayList<String> subFolders = new ArrayList<String>();
	private static ArrayList<Long> quicksortTimes = new ArrayList<Long>();
	private static ArrayList<Long> mergesortTimes = new ArrayList<Long>();
	private static ArrayList<Long> heapsortTimes = new ArrayList<Long>();
	private static int[] array;
	private static int[] workArray;

	public static void main(String[] args) throws IOException {
		mainFolders.add("small");
		mainFolders.add("med");
		mainFolders.add("large");
		subFolders.add("unsorted");
		subFolders.add("sorted");
		subFolders.add("reverseSorted");
		String fileName = "error";
		for (String folder : mainFolders) {
			for (String subFolder : subFolders) {
				// reset the time arrays, for the size.
				quicksortTimes = new ArrayList<Long>();
				mergesortTimes = new ArrayList<Long>();
				heapsortTimes = new ArrayList<Long>();
				for (int i = 0; i < numFiles; i++) {
					fileName = folder + "/" + subFolder + "/" + folder + "_" + subFolder + "_" + i + ".csv";
					int arraySize = 0;
					switch (folder) {
					case "small":
						arraySize = 10000;
					case "med":
						arraySize = 100000;
					case "large":
						arraySize = 1000000;
					}
					System.out.println(fileName);
					readFileIntoArray(fileName, arraySize, "quick");
					readFileIntoArray(fileName, arraySize, "merge");
					readFileIntoArray(fileName, arraySize, "heap");

				}
				fileName = "results/" + folder + "/" + folder + "_" + subFolder + "_quicksort" + ".csv";
				writeResultsToFile(quicksortTimes, fileName);
				fileName = "results/" + folder + "/" + folder + "_" + subFolder + "_mergesort" + ".csv";
				writeResultsToFile(mergesortTimes, fileName);
				fileName = "results/" + folder + "/" + folder + "_" + subFolder + "_heapsort" + ".csv";
				writeResultsToFile(heapsortTimes, fileName);
			}
		}
	}

	public static void writeResultsToFile(ArrayList<Long> array, String fileName) throws IOException {
		ArrayList<String> strArr = new ArrayList<String>();
		for (Long item : array) {
			strArr.add(item.toString());
		}
		File f = new File(fileName);
		f.getParentFile().mkdirs();

		Path file = Paths.get(fileName);
		Files.write(file, strArr, StandardOpenOption.CREATE);
	}

	public static void readFileIntoArray(String fileName, int arraySize, String sortType) throws FileNotFoundException {
		array = new int[arraySize];
		workArray = new int[arraySize];
		BufferedReader br = null;
		String line = "";
		try {
			// read in the data
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				String[] strArr = line.split(",");
				for (int i = 0; i < strArr.length; i++) {
					try {
						// parse the string array into the proper array, as an integer.
						array[i] = Integer.parseInt(strArr[i]);
					}
					// end of file has an extra comma so it ends up getting a value outside the
					// range of
					// a normal int.
					catch (NumberFormatException nfe) {
						continue;
					}
				}
			}
			// start executing the sorting algorithms.
			Instant start, end;
			switch (sortType) {
			case "quick":
				start = Instant.now();
				quickSort(0, array.length - 1);
				end = Instant.now();
				if (!isSorted()) {
					System.err.println("ERROR: THE ARRAY HAS NOT BEEN SORTED!");
				}
				quicksortTimes.add(Duration.between(start, end).toMillis());
				break;
			case "merge":
				start = Instant.now();
				BottomUpMergeSort(array, workArray, arraySize);
				end = Instant.now();
				if (!isSorted()) {
					System.err.println("ERROR: THE ARRAY HAS NOT BEEN SORTED!");
				}
				mergesortTimes.add(Duration.between(start, end).toMillis());
				break;

			case "heap":
				start = Instant.now();
				heapsort(array, arraySize);
				end = Instant.now();
				if (!isSorted()) {
					System.err.println("ERROR: THE ARRAY HAS NOT BEEN SORTED!");
				}
				heapsortTimes.add(Duration.between(start, end).toMillis());
				break;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// **
	// QUICKSORT SECTION
	// **

	// Referencing https://algs4.cs.princeton.edu/23quicksort/QuickX.java.html
	// Modified to fit my dataformat, and change some of the logic to make it
	// simpler.
	public static void quickSort(int lo, int hi) {
		if (hi <= lo)
			return;
		// partition the next section
		int j = partition(lo, hi);
		quickSort(lo, j - 1);
		quickSort(j + 1, hi);
	}

	/**
	 * partition a sub array, from lo index to hi index.
	 * 
	 * @param lo
	 * @param hi
	 * @return
	 */
	public static int partition(int lo, int hi) {
		// the length of the section we are working on.
		int n = hi - lo + 1;
		// adding this actually made the quicksort much slower.
		// int m = lo;
		// if (n >= 21) {
		// m = median3(lo, lo + n / 2, hi);
		// }
		int m = median3(lo, lo + n / 2, hi);
		swap(m, lo);

		int i = lo;
		int j = hi + 1;
		int v = array[lo];

		// a[lo] is unique largest element
		while (less(array[++i], v)) {
			if (i == hi) {
				swap(lo, hi);
				return hi;
			}
		}

		// a[lo] is unique smallest element
		while (less(v, array[--j])) {
			if (j == lo + 1)
				return lo;
		}

		// the main loop
		while (i < j) {
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

	/**
	 * return the index of the median element among a[i], a[j], and a[k]
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	private static int median3(int i, int j, int k) {
				// is a[i] < a[j]?
		return (less(array[i], array[j]) ? 
				//if so, is a[j] < a[k] ? if so, return j, if not, check if a[i] < a[k], if so, return k, otherwise return i
				(less(array[j], array[k]) ? j : less(array[i], array[k]) ? k : i)
				//if a[i] > a[j], check if a[k] < a[j], if so, return j, otherwise check a[k] < a[i], if so, return k, otherwise return i
				: (less(array[k], array[j]) ? j : less(array[k], array[i]) ? k : i));
	}
	/**
	 * check if the global array "array" is sorted or not
	 * @return true if it is, false if not.
	 */
	public static boolean isSorted() {
		boolean isSorted = true;
		for (int f = 0; f < array.length - 1; f++) {
			if (array[f] > array[f + 1]) {
				isSorted = false;
			}

		}
		return isSorted;
	}

	/**
	 * Utility method, for use in quicksort, and especially in our median3 method,
	 * which uses ternary conditionals, which would get really ugly if we didn't
	 * have this.
	 * 
	 * @param v
	 * @param w
	 * @return
	 */
	private static boolean less(int v, int w) {
		return v < w;
	}
	/**
	 * swap values in an array, by the index of those values.
	 * @param i first item's index
	 * @param j second item's index.
	 */
	private static void swap(int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	// **
	// MERGESORT SECTION (bottom up mergesort)
	// References: https://en.wikipedia.org/wiki/Merge_sort
	// **
	/**
	 * Do mergesort, from the bottom up
	 * @param A the array that holds all the values
	 * @param B an array that we can do our work in.
	 * @param n the size of the array. 
	 */
	private static void BottomUpMergeSort(int[] A, int[] B, int n) {
		// Each 1-element run in A is already "sorted".
		// Make successively longer sorted runs of length 2, 4, 8, 16... until whole
		// array is sorted.
		for (int width = 1; width < n; width = 2 * width) {
			// Array A is full of runs of length width.
			for (int i = 0; i < n; i = i + 2 * width) {
				// Merge two runs: A[i:i+width-1] and A[i+width:i+2*width-1] to B[]
				// or copy A[i:n-1] to B[] ( if(i+width >= n) )
				BottomUpMerge(A, i, Math.min(i + width, n), Math.min(i + 2 * width, n), B);
			}
			// Now work array B is full of runs of length 2*width.
			// Copy array B to array A for next iteration.
			// A more efficient implementation would swap the roles of A and B.
			CopyArray(B, A, n);
			// Now array A is full of runs of length 2*width.
		}
	}


	/**
	 * actually do the merging of items.
	 * Left run is A[iLeft : iRight-1 ].
	 * Right run is A[iRight : iEnd-1 ].
	 * @param A array with all our values
	 * @param iLeft left index
	 * @param iRight right index
	 * @param iEnd end index
	 * @param B working array
	 */
	private static void BottomUpMerge(int[] A, int iLeft, int iRight, int iEnd, int[] B) {
		int i = iLeft;
		int j = iRight;
		for (int k = iLeft; k < iEnd; k++) {
			if (i < iRight && (j >= iEnd || A[i] <= A[j])) {
				B[k] = A[i];
				i = i + 1;
			} else {
				B[k] = A[j];
				j = j + 1;
			}
		}
	}
	
	/**
	 * copy an array from B into A
	 * Used at the end of our merge sort, to move our changes from our working array, back over to the actual array.
	 * @param B our working array
	 * @param A the original, unsorted array
	 * @param n the amount of items in the arrays.
	 */
	private static void CopyArray(int[] B, int[] A, int n) {
		for (int i = 0; i < n; i++) {
			A[i] = B[i];
		}
	}

	// **
	// HEAPSORT SECTION (bottom-up heapsort because it is apparently faster for sets
	// of data with >16000 items)
	// References: https://en.wikipedia.org/wiki/Heapsort
	// **
	private static void heapsort(int[] A, int count) {
		heapify(A, count);
		int end = count - 1;
		while (end > 0) {
			// a[0] is the root, and largest value, swap moves it in front of the sorted
			// items.
			swap(end, 0);
			// The heap size needs to be reduced.
			end--;
			// swap ruined heap property, so we gotta fix that.
			siftDown(A, 0, end);
		}
	}

	private static void heapify(int[] A, int count) {
		// init start to the index of the last parent node.
		int start = iParent(count - 1);
		while (start >= 0) {
			siftDown(A, start, count - 1);
			// next parent node
			start--;
		}
	}

	private static void siftDown(int[] A, int i, int end) {
		int j = leafSearch(A, i, end);
		while (A[i] > A[j]) {
			j = iParent(j);
		}
		int x = A[j];
		A[j] = A[i];
		while (j > i) {
			// swap(x, A[iParent(j)]);
			// swap by value, not by reference.
			int iParentJ = iParent(j);
			// x = x+ A[iParentJ];
			// A[iParentJ] = x -A[iParentJ];
			// x = x -A[iParentJ];
			x = x ^ A[iParentJ];
			A[iParentJ] = x ^ A[iParentJ];
			x = x ^ A[iParentJ];
			// end value based swap
			j = iParent(j);
		}
	}

	private static int leafSearch(int[] A, int i, int end) {
		int j = i;
		while (iLeftChild(j) <= end) {
			int rChild = iRightChild(j);
			int lChild = iLeftChild(j);
			if (rChild <= end && (A[rChild] > A[lChild])) {
				j = rChild;
			} else {
				j = lChild;
			}
		}
		return j;

	}

	/**
	 * Find the parent index of the specified child index.
	 * 
	 * @param child
	 *            index
	 * @return parent index
	 */
	private static int iParent(int iChild) {
		return (int) Math.floor((iChild - 1) / 2);
	}

	/**
	 * get the index of the left child, given a parent index.
	 * 
	 * @param iParent
	 * @return child index
	 */
	private static int iLeftChild(int iParent) {
		return 2 * iParent + 1;
	}

	/**
	 * get the index of the right child, given a parent index.
	 * 
	 * @param iParent
	 * @return child index
	 */
	private static int iRightChild(int iParent) {
		return 2 * iParent + 2;
	}
}
