"""
Implement the algorithms
"""
from numpy import genfromtxt
import os

num_files = 30
main_folders = ['small', 'med', 'large']
sub_folders = ['unsorted', 'sorted', 'reverseSorted']
quick_sort_times = []
merge_sort_times = []
heap_sort_times = []

#Referenced: https://en.wikipedia.org/wiki/Quicksort
def doQuickSort(array):
    lo = array[0]
    hi = array[-1]
    if lo < hi:
        part = partition(array, lo, hi)

def partition(array, lo, hi):
    # todo: manage this, based on array size.
    pivot = array[hi]

    return array

def pullDataFromFile(fileName):
    current_data = genfromtxt(fileName, delimiter=',', dtype=int)
    doQuickSort(current_data)

def main():
    current_directory = os.getcwd()
    for folder in main_folders:
        final_directory = os.path.join(current_directory, folder)
        for sub_folder in sub_folders:
            sub_directory = os.path.join(final_directory, sub_folder)
            for i in range(num_files):
                file_name = folder + '_' + sub_folder + '_' + str(i)
                file_path = os.path.join(sub_directory, file_name)
                pullDataFromFile(file_path)
main()
