"""
Implement the algorithms
"""
import numpy as np
import pandas as pd
import os
import time
import sys
import math
sys.setrecursionlimit(5000)


def main():
    """ Referencing: https://www.geeksforgeeks.org/3-way-quicksort-dutch-national-flag/"""
    def doQuickSort(lo, hi):
        nonlocal i, j
        if (lo >= hi):
            return
        #should update i, j
        print(i, j)
        partition(lo, hi)
        print(i, j)
        doQuickSort(lo, i)
        doQuickSort(j, hi)

    def partition(lo, hi):
        nonlocal array, i, j
        print(array)
        if ((hi - lo) <= 1):
            if (array[hi] < array[lo]):
                # array.swap
                array[hi], array[lo] = array[lo], array[hi]
            i = lo
            j = hi
            return
        mid = lo
        pivot = array[hi]
        while(mid <= hi):
            if (array[mid] < pivot):
                lo += 1
                mid += 1
                array[lo], array[mid] = array[mid], array[lo]
            elif (array[mid] == pivot):
                mid += 1
            elif (array[mid] > pivot):
                hi -= 1
                array[mid], array[hi] = array[hi], array[mid]
        i = lo-1
        j = mid
        return

    def pullDataFromFile(fileName):
        nonlocal array, i, j
        with np.load(fileName) as data:
            array = data['arr_0']
            start = time.perf_counter()
            doQuickSort(0, len(array) - 1)
            end = time.perf_counter()
            quick_sort_times.append(end - start)
            print(end - start)

    num_files = 30
    main_folders = ['small', 'med', 'large']
    sub_folders = ['unsorted', 'sorted', 'reverseSorted']
    quick_sort_times = []
    merge_sort_times = []
    heap_sort_times = []
    array = []
    i = 0
    j = 0

    # Referenced: https://en.wikipedia.org/wiki/Quicksort

    current_directory = os.getcwd()
    for folder in main_folders:
        final_directory = os.path.join(current_directory, folder)
        for sub_folder in sub_folders:
            sub_directory = os.path.join(final_directory, sub_folder)
            for i in range(num_files):
                file_name = folder + '_' + sub_folder + '_' + str(i)
                file_path = os.path.join(sub_directory, file_name)
                print(file_path)
                pullDataFromFile(file_path + '.npz')

main()

   # def partition(lo, hi):
#     nonlocal array, i, j
#     if (hi-lo) >= 21:
#         piv_index = math.floor(lo + ((hi - lo) / 2))
#     else:
#         piv_index = lo
#     pivot = array[piv_index]
#     i = lo - 1
#     j = lo
#     while (j < hi):
#         if array[j] < pivot:
#             i = i + 1
#             array[i], array[j] = array[j], array[i]
#             # swap(array, i, j)
#         j = j + 1
#     array[i + 1], array[hi] = array[hi], array[i + 1]
#     # swap(array, i + 1, hi)
#     return i + 1

# def partition3(l, r):
#     nonlocal array, i, j
#     i = l-1
#     j = r
#     p = l - 1
#     q = r
#     v = array[r]
#     while(True):
#         while(array[i] < v):
#             i+=1
#         while (v < array[j]):
#             if j == l:
#                 break
#             j-=1
#         if (i >= j):
#             break
#         #Swap a[i] with a[j]
#         array[i], array[j] = array[j], array[i]
#         if array[i] == v:
#             p += 1
#             array[p], array[i] = array[i], array[p]
#         if array[j] == v:
#             q -= 1
#             array[j], array[q] = array[q], array[j]
#     array[i], array[r] = array[r], array[i]

#     j = i-1
#     k = r-1
#     while (k < p):
#         array[k], array[j] = array[j], array[k]
#         k += 1
#         j -= 1

#     i = i+1
#     k = r-1
#     while (k > q):
#         array[i], array[k] = array[k], array[i]
#         k -= 1
#         i += 1
#     print('test')
