import sys

import torch
from RecommentSystem.utils.datasets import load_data, get_unique_users_items

from torch import nn, optim

from model import TwoTowerModel
import pandas as pd

data_path = '../data/actions_new.csv'


user_tensor, item_tensor, label_tensor = load_data(data_path)
all_users, all_items = get_unique_users_items()

print(item_tensor)
user2idx = {u: i for i, u in enumerate(all_users)}
item2idx = {i: idx for idx, i in enumerate(all_items)}

user_idx = torch.tensor([user2idx[u.item()] for u in user_tensor], dtype=torch.long)
item_idx = torch.tensor([item2idx[i.item()] for i in item_tensor], dtype=torch.long)


num_users = len(all_users)
num_items = len(all_items)

model = TwoTowerModel(num_users, num_items, embed_dim=16)
criterion = nn.BCELoss()
optimizer = optim.Adam(model.parameters(), lr=0.01)

print("开始训练...")
model.train()
for epoch in range(50):
    optimizer.zero_grad()
    outputs = model(user_idx, item_idx)
    loss = criterion(outputs, label_tensor)
    loss.backward()
    optimizer.step()

    if epoch % 10 == 0:
        print(f"Epoch {epoch:2d} | Loss: {loss.item():.4f}")


torch.save(model.state_dict(), "../model/two_tower.pth")