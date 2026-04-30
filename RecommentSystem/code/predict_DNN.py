import torch
import pandas as pd
from model import DNNRank

# ====================== 固定配置 ======================
MODEL_PATH = "../model/dnn_rank.pth"
DATA_PATH = "../data/user_item_info.csv"

# ====================== 全局初始化（只加载一次） ======================
# 加载数据 + 重建编码映射
df = pd.read_csv(DATA_PATH)


def encode_feature(series):
    unique = series.unique()
    mapping = {v: i for i, v in enumerate(unique)}
    return series.map(mapping), len(unique), mapping


# 重建所有映射
df["user_id_new"], num_users, user2id = encode_feature(df["user_id"])
df["item_id_new"], num_items, item2id = encode_feature(df["item_id"])
df["gender_new"], num_gender, gender2id = encode_feature(df["gender"])
df["role_new"], num_role, role2id = encode_feature(df["role"])
df["item_label_new"], num_label, label2id = encode_feature(df["item_label"])

# 反向映射：编码ID → 原始商品ID
idx2item = {v: k for k, v in item2id.items()}

# 加载模型（全局单例，避免重复加载）
model = DNNRank(num_users, num_items, num_gender, num_role, num_label)
model.load_state_dict(torch.load(MODEL_PATH, map_location=torch.device('cpu')))
model.eval()
torch.set_grad_enabled(False)


# ====================== 内部工具函数 ======================
def get_user_features(original_user_id):
    row = df[df["user_id"] == original_user_id].iloc[0]
    user_idx = user2id[original_user_id]
    gender_idx = gender2id[row["gender"]]
    role_idx = role2id[row["role"]]
    return user_idx, gender_idx, role_idx


def get_item_feature(original_item_id):
    row = df[df["item_id"] == original_item_id].iloc[0]
    item_idx = item2id[original_item_id]
    label_idx = label2id[row["item_label"]]
    return item_idx, label_idx


# ====================== 核心对外接口 ======================
def dnn_rank_items(user_id: int, candidate_item_ids: list) -> list:
    """
    对外暴露的精排接口：给一个用户 + 候选商品列表，返回排序后的结果

    Args:
        user_id: 原始用户ID（如 12）
        candidate_item_ids: 候选商品编码ID列表（如 [16,4,18,...]）

    Returns:
        排序后的列表，格式: [{"item_id": 原始商品ID, "score": 得分}, ...]
    """
    # 转换候选商品：编码ID → 原始商品ID
    try:
        CANDIDATES = [idx2item[i] for i in candidate_item_ids]
    except KeyError as e:
        raise ValueError(f"候选商品ID不存在于映射表: {e}")

    user_idx, gender_idx, role_idx = get_user_features(user_id)
    results = []

    for item_id in CANDIDATES:
        try:
            item_idx, label_idx = get_item_feature(item_id)
        except Exception:
            continue  # 跳过无效商品

        # 构造输入张量
        u = torch.tensor([user_idx])
        i = torch.tensor([item_idx])
        g = torch.tensor([gender_idx])
        r = torch.tensor([role_idx])
        l = torch.tensor([label_idx])

        # 预测分数
        score = model(u, i, g, r, l).item()
        results.append({
            "item_id": item_id,
            "score": round(score, 4)
        })

    # 按分数降序排序
    results = sorted(results, key=lambda x: x["score"], reverse=True)
    return results

def dict_to_list(di):
    result = []
    for i in di:
        temp = []
        for k, v in i.items():
            temp.append(v)
        result.append(temp)
    return result

def main(user_id, candidate_item_ids):
    """
    封装好的主函数，供外部包直接调用
    会打印结果，并返回排序后的商品列表
    """
    ranked_list = dnn_rank_items(user_id, candidate_item_ids)

    # 打印结果
    print(f"✅ 用户 {user_id} 精排完成：")
    for i, item in enumerate(ranked_list):
        print(f"Top{i + 1}  商品ID: {item['item_id']}  得分: {item['score']}")

    return dict_to_list(ranked_list)


# 本文件直接运行时的测试入口
if __name__ == '__main__':
    # 测试调用
    test_user_id = 12
    test_candidates = [16, 4, 18, 11, 8, 14, 17, 19, 20, 15]
    main(test_user_id, test_candidates)