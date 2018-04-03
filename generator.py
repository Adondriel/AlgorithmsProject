"""
# File structure needed:
#    Small:
#        - S_1 ... S_30
#        S (Sorted):
#            - SS_1 ... SS_30
#        RS (reverse sorted):
#            - SRS_1 ... SRS_30
#   Medium:
#        - M_1 ... M_30
#        S:
#            - MS_1 ... MS_30
#        RS:
#            - MRS_1 ... MRS_30
#   Large:
#        - L_1 ... L_30
#        S:
#            LS_1 ... LS_30
#        RS:
#            LRS_1 ... LRS_30
"""
import os
import random
import numpy as np

num_files = 30
main_folders = ['small', 'med', 'large']
sub_folders = ['unsorted', 'sorted', 'reverseSorted']

def generate_unsorted_number_list(size):
    arr = []
    number_of_ints = 0
    if size == main_folders[0]:
        number_of_ints = 10000
    elif size == main_folders[1]:
        number_of_ints = 100000
    elif size == main_folders[2]:
        number_of_ints = 1000000
    for _ in range(number_of_ints):
        rndNum = random.randint(0,99999)
        arr.append(rndNum)
    return arr    

def generate_sorted_number_list(size, reverse):
    arr = generate_unsorted_number_list(size)
    arr.sort(key=None, reverse=reverse)
    return arr

def print_num_list_to_file(array, filepath):
    # fo = open(filepath + '.csv', 'w')
    # for num in array:
    #     fo.write(str(num))
    #     if num != array[-1]:
    #         fo.write(',')
    # print(filepath)
    np.savez(filepath, array)

def generate():
    """ Generate all the files. """
    current_directory = os.getcwd()

    for folder in main_folders:
        final_directory = os.path.join(current_directory, folder)
        if not os.path.exists(final_directory):
            os.makedirs(final_directory)
        for sub_folder in sub_folders:
            sub_directory = os.path.join(final_directory, sub_folder)
            #print(sub_directory)
            if not os.path.exists(sub_directory):
                os.makedirs(sub_directory)
            for i in range(num_files):
                # If we are in the unsorted folder.
                if sub_folder == sub_folders[0]:
                    numArr = generate_unsorted_number_list(folder)
                    file_name = folder + '_' + sub_folder + '_' + str(i) 
                    print_num_list_to_file(numArr, os.path.join(sub_directory, file_name))
                if sub_folder == sub_folders[1]:
                    #sorted folder
                    numArr = generate_sorted_number_list(folder, False)
                    file_name = folder + '_' + sub_folder + '_' + str(i)
                    print_num_list_to_file(numArr, os.path.join(sub_directory, file_name))
                if sub_folder == sub_folders[2]:
                    #reverse sorted folder
                    numArr = generate_sorted_number_list(folder, True)
                    file_name = folder + '_' + sub_folder + '_' + str(i)
                    print_num_list_to_file(numArr, os.path.join(sub_directory, file_name))
generate()
