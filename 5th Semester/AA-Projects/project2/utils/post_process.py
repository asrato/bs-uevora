import numpy as np
import pandas as pd

from typing import List

def convert_to_numeric(data_frame: pd.DataFrame, columns: List[str]):
    converted_data_frame = data_frame.copy()
    
    for column in columns:
        converted_data_frame[column] = pd.to_numeric(data_frame[column], downcast='integer')

    return converted_data_frame