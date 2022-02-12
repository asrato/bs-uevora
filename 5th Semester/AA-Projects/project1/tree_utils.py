import copy
import math

import numpy as np
import pandas as pd


def is_homogeneous(data):
    return data['classes'].nunique() == 1


def get_best_partition(data, impurity_function):
    attributes = data.columns[:-1].to_list()
    minimum_impurity = 1
    best_subsets = None
    best_attribute = None
    for attribute in attributes:
        sub_sets = get_subsets_by_attribute(data, attribute)
        impurity = calculate_impurity(impurity_function, sub_sets, data.shape[0])
        if impurity < minimum_impurity:
            minimum_impurity = impurity
            best_subsets = sub_sets
            best_attribute = attribute
    return best_attribute, best_subsets


def get_subsets_by_attribute(data, attribute):
    possible_values = data[attribute].unique()
    sub_sets = {}

    for value in possible_values:
        sub_set = data[data[attribute] == value]
        sub_sets[value] = sub_set

    return sub_sets


def calculate_impurity(impurity_function, sub_sets, total):
    summation = 0

    if impurity_function == 'gini':
        for sub_set in sub_sets.values():
            possible_classes = sub_set['classes'].value_counts().to_dict()
            weight = sub_set.shape[0] / total
            gini = np.sum([math.pow((class_instances / sub_set.shape[0]), 2) for class_instances in
                           possible_classes.values()])
            summation += weight * gini
        return 1 - summation
    elif impurity_function == 'entropy':
        for sub_set in sub_sets.values():
            possible_classes = sub_set['classes'].value_counts().to_dict()
            weight = sub_set.shape[0] / total
            entropy = np.sum([(class_instances / sub_set.shape[0]) * math.log2(class_instances /
                                                                               sub_set.shape[0]) for
                              class_instances in possible_classes.values()])
            summation += weight * entropy
        return -summation
    return


def children_are_leaves(node):
    for child in node.children:
        if not node.children[child].is_leaf():
            return False

    return True


def get_majority_class_of_children(children):
    maximum = -1
    child_value = ''

    for child in children:
        if children[child].number_of_instances > maximum:
            maximum = children[child].number_of_instances
            child_value = children[child].node_class

    return child_value


def get_pruned_trees(initial_tree):
    pruned_trees = []

    if not initial_tree.root.is_leaf():
        if children_are_leaves(initial_tree.root):
            new_tree = copy.deepcopy(initial_tree)
            new_tree.root.node_class = get_majority_class_of_children(new_tree.root.children)
            new_tree.root.children = {}
            pruned_trees.append(new_tree)
        else:
            for child in initial_tree.root.children:
                new_tree = copy.deepcopy(initial_tree)
                new_tree.root = initial_tree.root.children[child]
                pruned_trees += get_pruned_trees(new_tree)

    return pruned_trees


def pruning_score(tree, x_data, y_data):
    hits = 0

    predictions = tree.predict(x_data)

    for identifier in predictions.keys():
        if predictions[identifier] == y_data[identifier]:
            hits += 1

    return hits
