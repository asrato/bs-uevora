import numpy as np
import pandas as pd

from sklearn.preprocessing import MinMaxScaler, QuantileTransformer, StandardScaler, RobustScaler, LabelEncoder, PowerTransformer
from sklearn.decomposition import PCA
from typing import List, Tuple, Any


def normalize(df: pd.DataFrame, columns: List[str], normalizer='identity') -> Tuple[pd.DataFrame, Any]:

    if normalizer == 'identity':

        return df.copy()

    scaled_df = df.copy()
    scalers = []

    for column in columns:

        if normalizer == 'z_score':
            scaler = StandardScaler()

        if normalizer == 'power':
            scaler = PowerTransformer('yeo-johnson')

        if normalizer == 'min_max':
            scaler = MinMaxScaler()

        if normalizer == 'robust':
            scaler = RobustScaler()

        if normalizer == 'quant':
            scaler = QuantileTransformer(random_state=42)

        #if scaled_df[column].dtype != int:

        scaled_df[column] = scaler.fit_transform(scaled_df[column].to_numpy().reshape(-1, 1))
        scalers.append(scaler)

    return scaled_df, scalers


def encode_categorical(df: pd.DataFrame, columns: List[str]):

    encoded_df = df.copy()
    encoder = LabelEncoder()

    for column in columns:
        encoded_df[column] = encoder.fit_transform(encoded_df[column])

    return encoded_df


def reduce(df: pd.DataFrame) -> np.array:

    pca = PCA(n_components=2)
    return pca.fit_transform(df.to_numpy())

