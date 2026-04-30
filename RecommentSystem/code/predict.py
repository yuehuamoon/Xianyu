import torch
from model import TwoTowerModel
import pandas as pd

# ====================== 配置 ======================
data_path = '../data/actions.csv'

# ====================== 加载数据信息 ======================
df = pd.read_csv(data_path)
all_users = sorted(df["user_id"].unique())
all_items = sorted(df["item_id"].unique())

user2idx = {int(u): i for i, u in enumerate(all_users)}
item2idx = {int(i): idx for idx, i in enumerate(all_items)}
idx2item = {int(idx): i for idx, i in enumerate(all_items)}

num_users = len(all_users)
num_items = len(all_items)

# ====================== 加载模型 ======================
model = TwoTowerModel(num_users, num_items, embed_dim=16)
model.load_state_dict(torch.load("../model/two_tower.pth"))
model.eval()


def recommend(user_id, top_n=3):
    # 直接返回空列表，不返回字符串！
    if user_id not in user2idx:
        return []

    uid = user2idx[user_id]
    scores = []

    with torch.no_grad():
        for item_id in all_items:
            iid = item2idx[item_id]
            score = model(
                torch.tensor([uid], dtype=torch.long),
                torch.tensor([iid], dtype=torch.long)
            ).item()
            scores.append((item_id, score))

    scores.sort(key=lambda x: x[1], reverse=True)
    return scores[:top_n]

# ====================== 测试（安全遍历） ======================
if __name__ == "__main__":
    target_user = 12  # 你的用户ID 0~10

    recs = recommend(target_user, top_n=10)

    if not recs:
        print(f"用户 {target_user} 不存在或无推荐")
    else:
        print(f"✅ 用户 {target_user} 的推荐商品：")
        print("-" * 30)
        for good_id, score in recs:
            print(f"商品ID: {good_id:2d} | 推荐分数: {score:.3f}")