import torch
from flask import Flask, request, jsonify
from predict_DNN import main
from model import TwoTowerModel, DNNRank
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

app = Flask(__name__)

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

def numpyInt_to_Int(data):
    temp = []
    for item in data:
        temp.append([int(item[0]), item[1]])
    return temp

@app.route('/recommend', methods=['POST'])
def api_recommend():
    print(1)
    user_id = request.get_json()['user_id']
    top_n = request.get_json()['top_n']

    if user_id is None:
        return jsonify({"code": 400, "msg": "请传入 user_id"}), 400

    data = recommend(user_id, top_n)

    ranked = main(user_id, [i[0] for i in numpyInt_to_Int(data)])

    return jsonify({
        "code": 200,
        "user_id": user_id,
        "data": numpyInt_to_Int(ranked),
    })

# ====================== 测试（安全遍历） ======================
if __name__ == "__main__":
    target_user = 12  # 你的用户ID 0~10

    recs = recommend(target_user, top_n=10)

    app.run(host='0.0.0.0', port=5000, debug=True)