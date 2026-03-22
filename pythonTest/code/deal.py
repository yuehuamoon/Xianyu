import requests
import json
import random

# ===================== 基础配置 =====================
LOGIN_URL = "http://localhost:8081/user/login"
BASE_DEAL_URL = "http://localhost:8084/deal"
HEADERS = {"Content-Type": "application/json"}
LOGIN_DATA = {
    "phone": "13300133006",
    "password": "e10adc3949ba59abbe56e057f20f883e"
}

# ===================== 通用函数 =====================
def get_jwt_token():
    try:
        print("=== 登录获取Token ===")
        response = requests.post(LOGIN_URL, headers=HEADERS, json=LOGIN_DATA)
        res_json = response.json()
        print(f"登录响应: {res_json}")
        if res_json.get("code") == 200 and res_json.get("data"):
            token = res_json["data"]
            print(f"Token获取成功: {token[:20]}...\n")
            return token
        else:
            raise Exception(f"登录失败: {res_json.get('msg', '未知错误')}")
    except Exception as e:
        print(f"Token获取失败: {str(e)}\n")
        return None

# 关键：去掉了createTime/updateTime，数据库自动生成！
def generate_test_deal():
    unit_price = str(round(random.uniform(10, 999), 2))
    return {
        "id": None,
        "sellerId": 12,
        "buyerId": random.randint(1000, 9999),
        "goodId": random.randint(1, 50),
        "unitPrice": unit_price,
        "count": random.randint(1, 5),
        "status": 0
        # 删掉时间字段！数据库自动生成，不用前端传！
    }

# ===================== 接口测试函数 =====================
def test_deal_info(token):
    url = f"{BASE_DEAL_URL}/info"
    headers = HEADERS.copy()
    headers["token"] = token
    test_cases = [{"page": 0}, {"page": 1}]
    try:
        print("=== 测试分页查询订单 ===")
        for data in test_cases:
            response = requests.post(url, headers=headers, json=data)
            print(f"请求参数: {data}")
            print(f"响应状态码: {response.status_code}")
            res_json = response.json()
            print(f"响应内容: {res_json}\n")
        return res_json
    except Exception as e:
        print(f"分页查询订单失败: {str(e)}\n")
        return None

def test_deal_insert(token):
    url = f"{BASE_DEAL_URL}/insert"
    headers = HEADERS.copy()
    headers["token"] = token
    test_data = generate_test_deal()
    try:
        print("=== 测试新增订单 ===")
        print(f"请求参数: {test_data}")
        response = requests.post(url, headers=headers, json=test_data)
        res_json = response.json()
        print(f"响应内容: {res_json}\n")
        return res_json.get("data")
    except Exception as e:
        print(f"新增订单失败: {str(e)}\n")
        return None

def test_deal_delete_by_id(token):
    url = f"{BASE_DEAL_URL}/deleteByID"
    headers = HEADERS.copy()
    headers["token"] = token
    data1 = {"id": 4}
    data3 = {"id": None, "status": 0}
    try:
        print("=== 测试删除订单 ===")
        for idx, data in enumerate([data1, data3], 1):
            print(f"测试用例{idx} - 请求参数: {data}")
            response = requests.post(url, headers=headers, json=data)
            res_json = response.json()
            print(f"响应内容: {res_json}\n")
        return res_json
    except Exception as e:
        print(f"删除订单失败: {str(e)}\n")
        return None

def test_deal_update(token):
    url = f"{BASE_DEAL_URL}/update"
    headers = HEADERS.copy()
    headers["token"] = token
    data1 = {"id": 1, "sellerId": 12, "buyerId": 9999, "status": 5}
    data2 = {"id": 1, "sellerId": None, "status": 3}
    try:
        print("=== 测试更新订单 ===")
        for idx, data in enumerate([data1, data2], 1):
            print(f"测试用例{idx} - 请求参数: {data}")
            response = requests.post(url, headers=headers, json=data)
            res_json = response.json()
            print(f"响应内容: {res_json}\n")
        return res_json
    except Exception as e:
        print(f"更新订单失败: {str(e)}\n")
        return None

def test_deal_create(token):
    url = f"{BASE_DEAL_URL}/create"
    headers = HEADERS.copy()
    headers["token"] = token
    data1 = generate_test_deal()
    data2 = generate_test_deal()
    data2["sellerId"] = None
    try:
        print("=== 测试创建订单（含支付） ===")
        for idx, data in enumerate([data1, data2], 1):
            print(f"测试用例{idx} - 请求参数: {data}")
            response = requests.post(url, headers=headers, json=data)
            res_json = response.json()
            print(f"响应内容: {res_json}\n")
        return res_json
    except Exception as e:
        print(f"创建订单失败: {str(e)}\n")
        return None

# ===================== 主函数 =====================
if __name__ == "__main__":
    token = get_jwt_token()
    if not token:
        print("❌ 无有效Token，终止测试")
        exit(1)
    print("========== 开始执行交易订单接口测试（端口8084） ==========\n")
    test_deal_info(token)
    test_deal_delete_by_id(token)
    test_deal_update(token)
    print("========== 所有交易订单接口测试执行完成 ==========")