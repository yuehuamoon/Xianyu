import torch
from torch import nn, optim
import pandas as pd
from model import DNNRank

# ====================== 1. 加载数据 ======================
df = pd.read_csv("/home/haha/codes/python/RecommentSystem/data/user_item_info.csv")

# ====================== 2. 所有特征重新编码（从0开始，解决越界） ======================
def encode_feature(series):
    unique = series.unique()
    mapping = {v: i for i, v in enumerate(unique)}
    return series.map(mapping), len(unique), mapping

# 编码所有分类特征
df["user_id"], num_users, user2id = encode_feature(df["user_id"])
df["item_id"], num_items, item2id = encode_feature(df["item_id"])
df["gender"], num_gender, gender2id = encode_feature(df["gender"])
df["role"], num_role, role2id = encode_feature(df["role"])
df["item_label"], num_label, label2id = encode_feature(df["item_label"])

# 标签转为浮点型
target = df["click_label"].astype(float)

# ====================== 3. 转换为张量 ======================
user_tensor = torch.tensor(df["user_id"].values, dtype=torch.long)
item_tensor = torch.tensor(df["item_id"].values, dtype=torch.long)
gender_tensor = torch.tensor(df["gender"].values, dtype=torch.long)
role_tensor = torch.tensor(df["role"].values, dtype=torch.long)
label_tensor = torch.tensor(df["item_label"].values, dtype=torch.long)
target_tensor = torch.tensor(target.values, dtype=torch.float32).view(-1, 1)

import random
random.seed(42)
# ====================== 4. 初始化模型 ======================
model = DNNRank(
    num_users=num_users,
    num_items=num_items,
    num_gender=num_gender,
    num_role=num_role,
    num_label=num_label
)

# 损失函数和优化器
criterion = nn.BCELoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)

# ====================== 5. 训练 ======================
print("开始训练精排模型...")
model.train()
for epoch in range(120):
    optimizer.zero_grad()
    outputs = model(user_tensor, item_tensor, gender_tensor, role_tensor, label_tensor)
    loss = criterion(outputs, target_tensor)
    loss.backward()
    optimizer.step()

    if epoch % 10 == 0:
        print(f"Epoch {epoch:2d} | Loss: {loss.item():.4f}")

# ====================== 6. 保存模型 ======================
torch.save(model.state_dict(), "../model/dnn_rank.pth")
print("✅ 训练完成！模型已保存")