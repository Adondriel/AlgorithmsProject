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
num_files = 30
main_folders = ['small', 'med', 'large']
sub_folders = ['unsorted', 'sorted', 'reverseSorted']
quick_sort_times = []
merge_sort_times = []
heap_sort_times = []
A = []
# i = 0
# j = 0
# p = 0
# q = 0
# v = 0
# n = len(A)


def main():
    # Really tried to get python to work, but had to use numpy's implementation of the algorithm, to actually get it to work,
    # since they actually do it using some weird C python, thing and I need the data, so I can do the rest of this project...
    # I spent too much time fiddling with the stupid quicksort, which just refused to ever work.
    def pullDataFromFile(fileName):
        global A
        with np.load(fileName) as data:
            A = data['arr_0'].tolist()
            # Quicksort
            print("quicksort")
            start = time.perf_counter()
            A = np.sort(A, kind='quicksort')
            end = time.perf_counter()
            if (all(A[x] <= A[x + 1] for x in range(len(A) - 1))):
                quick_sort_times.append(end - start)
            # MergeSort
            print("mergesort")
            start = time.perf_counter()
            A = np.sort(A, kind='mergesort')
            end = time.perf_counter()
            if (all(A[x] <= A[x + 1] for x in range(len(A) - 1))):
                merge_sort_times.append(end - start)
            # HeapSort
            print("heapsort")
            start = time.perf_counter()
            A = np.sort(A, kind='heapsort')
            end = time.perf_counter()
            if (all(A[x] <= A[x + 1] for x in range(len(A) - 1))):
                heap_sort_times.append(end - start)

    def print_num_list_to_file(array, filepath):
        fo = open(filepath + '.csv', 'w')
        for num in array:
            fo.write(str(num))
            if num != array[-1]:
                fo.write(',')
    # Referenced: https://en.wikipedia.org/wiki/Quicksort
    current_directory = os.getcwd()
    for folder in main_folders:
        final_directory = os.path.join(current_directory, folder)
        for sub_folder in sub_folders:
            sub_directory = os.path.join(final_directory, sub_folder)
            quick_sort_times = []
            merge_sort_times = []
            heap_sort_times = []
            for i in range(num_files):
                file_name = folder + '_' + sub_folder + '_' + str(i)
                file_path = os.path.join(sub_directory, file_name)
                print(file_path)
                pullDataFromFile(file_path + '.npz')
            print_num_list_to_file(
                quick_sort_times, 'quicksortTimes' + '_' + folder + '_' + sub_folder)
            print_num_list_to_file(
                merge_sort_times, 'mergesortTimes' + '_' + folder + '_' + sub_folder)
            print_num_list_to_file(
                heap_sort_times, 'heapsortTimes' + '_' + folder + '_' + sub_folder)


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

# def doQuickSort(l, h):
#     global A
#     stack = list(range(h - l + 1))
#     top = -1
#     top += 1
#     stack[top] = l
#     top += 1
#     stack[top] = h
#     while (top >= 0):
#         top -= 1
#         h = stack[top]
#         top -= 1
#         l = stack[top]
#         p = partition(l, h)

#         if (p - 1 > 1):
#             top += 1
#             stack[top] = l
#             top += 1
#             stack[top] = p - 1

#         if (p + 1 < h):
#             top += 1
#             stack[top] = p + 1
#             top += 1
#             stack[top] = h

# def partition(l, h):
#     global A
#     x = A[h]
#     i = (l - 1)
#     j = 1
#     while(j <= h - 1):
#         if (A[j] <= x):
#             i += 1
#             if (i < len(A)) and (j < len(A)):
#                 A[i], A[j] = A[j], A[i]
#             else:
#                 print("error")
#         j += 1
#     if (i + 1 < len(A)) and (h < len(A)):
#         A[i + 1], A[h] = A[h], A[i + 1]
#         return (i + 1)
#     return i
