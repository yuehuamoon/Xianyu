import numpy as np
import torch
import pandas as pd
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import Dataset, DataLoader


data_path = '../data/actions.csv'


def load_data(data_path):
    df = pd.read_csv(data_path)

    user_ids = df["user_id"].values
    item_ids = df["item_id"].values
    labels = df["label"].values

    # 转张量
    user_tensor = torch.tensor(user_ids, dtype=torch.long)
    item_tensor = torch.tensor(item_ids, dtype=torch.long)
    label_tensor = torch.tensor(labels, dtype=torch.float32)

    return user_tensor, item_tensor, label_tensor

# 获取全局唯一用户、商品 ID
def get_unique_users_items(data_path=data_path):
    df = pd.read_csv(data_path)
    unique_users = sorted(df["user_id"].unique())
    unique_items = sorted(df["item_id"].unique())
    return unique_users, unique_items


if __name__ == "__main__":
    user_tensor, item_tensor, label_tensor = load_data(data_path)
    unique_users, unique_items = get_unique_users_items(data_path)
    print(len(unique_users), len(unique_items))