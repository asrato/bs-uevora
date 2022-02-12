import matplotlib.pyplot as plt
import matplotlib.cm as cm

import seaborn as sns
import pandas as pd

from typing import List


def plot_dataframe_hist(df: pd.DataFrame, columns: List[str], n_cols: int = 3, n_rows: int = 3):

    fig, axes = plt.subplots(nrows=n_rows, ncols=n_cols, figsize=(20, 20))

    for i, column in enumerate(columns):
        sns.histplot(df[column], ax=axes[i // n_cols, i % n_cols])


def plot_dataframe_boxplot(df: pd.DataFrame, columns: List[str], n_cols: int = 3, n_rows: int = 3):

    fig, axes = plt.subplots(nrows=n_rows, ncols=n_cols, figsize=(20, 20))

    for i, column in enumerate(columns):
        sns.boxplot(data=df[column], ax=axes[i // n_cols, i % n_cols])


def plot_cluster(vectors, colors):

    if colors is not None:

        scatter = plt.scatter(vectors[:, 0], vectors[:, 1], c=colors, cmap=cm.turbo)
        plt.legend(*scatter.legend_elements(),
                   loc="lower left", title="Classes")
    else:
        plt.scatter(vectors[:, 0], vectors[:, 1])


def component_analysis_plot(score,coeff, columns, colors=None, x_limits=(-1, 1), y_limits=(-1, 1)):

    xs = score[:,0]
    ys = score[:,1]
    n = coeff.shape[0]
    scalex = 1.0/(xs.max() - xs.min())
    scaley = 1.0/(ys.max() - ys.min())
    plt.scatter(xs * scalex,ys * scaley, c = colors)
    
    for i in range(n):
        plt.arrow(0, 0, coeff[i,0], coeff[i,1],color = 'r',alpha = 0.5)
        if colors is None:
            plt.text(coeff[i,0]* 1.15, coeff[i,1] * 1.15, "Var"+columns[i], color = 'g', ha = 'center', va = 'center')
        else:
            plt.text(coeff[i,0]* 1.15, coeff[i,1] * 1.15, columns[i], color = 'g', ha = 'center', va = 'center')

    plt.xlim(x_limits[0], x_limits[1])
    plt.ylim(y_limits[0], y_limits[1])