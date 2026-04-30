import torch
import torch.nn as nn



class TwoTowerModel(nn.Module):
    def __init__(self, num_users, num_items, embed_dim=16):
        super().__init__()
        # 用户塔
        self.user_embedding = nn.Embedding(num_users, embed_dim)
        # 商品塔
        self.item_embedding = nn.Embedding(num_items, embed_dim)

    def forward(self, user_ids, item_ids):
        # 得到用户向量 & 商品向量
        u_emb = self.user_embedding(user_ids)
        i_emb = self.item_embedding(item_ids)

        # 内积表示相似度
        dot = (u_emb * i_emb).sum(dim=1)
        return torch.sigmoid(dot)



# model_rank.py  精排模型
import torch
import torch.nn as nn

class DNNRank(nn.Module):
    def __init__(
        self,
        num_users, num_items,
        num_gender=2, num_role=3, num_label=20,
        embed_dim=16
    ):
        super().__init__()

        # 基础ID
        self.user_emb = nn.Embedding(num_users, embed_dim)
        self.item_emb = nn.Embedding(num_items, embed_dim)

        # 用户侧特征
        self.gender_emb = nn.Embedding(num_gender, embed_dim)
        self.role_emb = nn.Embedding(num_role, embed_dim)

        # 物品侧特征
        self.label_emb = nn.Embedding(num_label, embed_dim)

        # 精排核心：MLP
        self.mlp = nn.Sequential(
            nn.Linear(embed_dim * 5, 32),
            nn.ReLU(),
            nn.Linear(32, 16),
            nn.ReLU(),
            nn.Linear(16, 1)
        )

    def forward(self, user_id, item_id, gender, role, label):
        u = self.user_emb(user_id)
        i = self.item_emb(item_id)
        g = self.gender_emb(gender)
        r = self.role_emb(role)
        l = self.label_emb(label)

        # 精排必须拼接，不能内积！
        feat = torch.cat([u, i, g, r, l], dim=-1)
        score = self.mlp(feat)
        return torch.sigmoid(score)