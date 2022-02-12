import numpy as np
import copy

from tree_utils import get_pruned_trees, is_homogeneous, get_best_partition, pruning_score


class TreeNode:
    def __init__(self, attribute, node_class, number_of_instances, depth):
        self.attribute = attribute
        self.node_class = node_class
        self.number_of_instances = number_of_instances
        self.depth = depth
        self.children = {}

    def is_leaf(self):
        return self.children == {}

    def add_child(self, child, attribute_value):
        self.children[attribute_value] = child


class DecisionTree:
    def __init__(self, impurity_function='gini', max_depth=np.Inf, min_number_of_instances_per_node=1,
                 post_pruning=False):
        self.impurity_function = impurity_function
        self.max_depth = max_depth
        self.min_number_of_instances_per_node = min_number_of_instances_per_node
        self.post_pruning = post_pruning
        self.root = TreeNode(None, None, 0, 0)

    def fit(self, x_data, y_data):
        data = x_data.copy()
        data.insert(len(x_data.columns), 'classes', y_data)

        self.root.number_of_instances = data.shape[0]

        self.grow_tree(data, self.root)

    def grow_tree(self, data, root):
        if root.depth == self.max_depth or root.number_of_instances < self.min_number_of_instances_per_node:
            return

        if is_homogeneous(data):
            root.node_class = data['classes'].iloc[0]
            return

        root.attribute, best_partition = get_best_partition(data, self.impurity_function)

        if best_partition is None:
            return

        for sub_set in best_partition.keys():
            if not best_partition[sub_set].empty:
                child_node = TreeNode(None, None, best_partition[sub_set].shape[0], root.depth + 1)
                root.add_child(child_node, sub_set)
                self.grow_tree(best_partition[sub_set], child_node)

    def reduced_error_pruning(self, x_data, y_data):
        trees_to_prune = get_pruned_trees(self)
        tree_scores = []

        for tree in trees_to_prune:
            tree_scores.append(pruning_score(tree, x_data, y_data))

        initial_score = pruning_score(self, x_data, y_data)

        if len(tree_scores) > 0 and initial_score - max(tree_scores) <= 0:
            self.root = trees_to_prune[tree_scores.index(max(tree_scores))].root
            self.reduced_error_pruning(x_data, y_data)

    def predict(self, x_data):
        predictions = {}
        for data in x_data.iterrows():
            instance = data[1].to_dict()
            node = self.root

            while not node.is_leaf():
                instance_value_on_attribute = instance[node.attribute]
                node = node.children[instance_value_on_attribute]

            instance_class = node.node_class
            predictions[data[0]] = instance_class
        return predictions

    def score(self, y_data, predictions, number_of_classes):
        summation = 0
        reals = y_data.to_dict()
        classes = y_data.unique()

        for positive_class in classes:
            true_positives = 0
            true_negatives = 0

            for identifier in predictions.keys():
                if predictions[identifier] == positive_class and reals[identifier] == positive_class:
                    true_positives += 1
                elif predictions[identifier] != positive_class and reals[identifier] != positive_class:
                    true_negatives += 1

            accuracy = (true_positives + true_negatives) / y_data.shape[0]
            summation += accuracy

        return summation/number_of_classes
