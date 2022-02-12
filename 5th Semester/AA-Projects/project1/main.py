import pandas as pd
from sklearn.model_selection import train_test_split

from decision_tree import DecisionTree

if __name__ == '__main__':
    file_name = 'weather-nominal.csv'
    data_set = pd.read_csv(f'files/{file_name}')

    x_data = data_set.copy()
    x_data.drop(data_set.columns[len(data_set.columns) - 1], inplace=True, axis=1)
    y_data = data_set[data_set.columns[len(data_set.columns) - 1]]

    x_sub_data, x_test, y_sub_data, y_test = train_test_split(x_data, y_data, random_state=2)

    tree = DecisionTree('gini', post_pruning=False)

    if tree.post_pruning:
        x_train, x_pruning, y_train, y_pruning = train_test_split(x_sub_data, y_sub_data, random_state=2)
    else:
        x_train = x_sub_data.copy()
        y_train = y_sub_data.copy()

    tree.fit(x_train, y_train)

    if tree.post_pruning:
        tree.reduced_error_pruning(x_pruning, y_pruning)

    predictions = tree.predict(x_test)

    score = tree.score(y_test, predictions, y_data.nunique())

    print(f'Algorithm score: {score}')

