import requests
import json
import random

# 基础配置
LOGIN_URL = "http://localhost:8081/user/login"  # 登录接口
BASE_GOOD_URL = "http://localhost:8083/good"  # 商品接口前缀
HEADERS = {"Content-Type": "application/json"}

# 登录信息
LOGIN_DATA = {
    "phone": "13300133006",
    "password": "e10adc3949ba59abbe56e057f20f883e"  # 密码MD5
}


def get_jwt_token():
    """调用登录接口获取JWT Token"""
    try:
        print("=== 登录获取Token ===")
        response = requests.post(LOGIN_URL, headers=HEADERS, json=LOGIN_DATA)
        res_json = response.json()
        print(f"登录响应: {res_json}")

        # 适配常见的Token返回格式，根据实际情况调整
        if res_json.get("code") == 200 and res_json.get("data"):
            token = res_json["data"]
            print(f"Token获取成功: {token[:20]}...\n")  # 隐藏部分Token，避免泄露
            return token
        else:
            raise Exception(f"登录失败: {res_json.get('msg', '未知错误')}")
    except Exception as e:
        print(f"Token获取失败: {str(e)}\n")
        return None


def generate_test_good():
    """生成符合Good实体的测试数据"""
    return {
        "id": None,  # 新增时ID为空，由数据库自增
        "sallerId": 12,  # 卖家ID默认12（对应数据库默认值）
        "name": f"测试商品_{random.randint(1000, 9999)}",
        "content": "Python接口测试自动生成的商品描述",
        "unitPrice": random.randint(100, 9999),  # 单价（分），1-99.99元
        "count": 1,  # 默认1
        "imgUrl": "http://localhost:5000/img/0.jpg",  # 默认图
        "label": random.choice(["教材教辅", "毕业季闲置", "生活用品", "电子产品"]),
        "status": 0,  # 0-审核中（默认）
        "address": f"测试地址_{random.randint(1, 10)}号楼",
        "viewCount": 0,  # 默认0
        "favoriteCount": 0  # 默认0
    }


def test_info(token):
    """测试分页查询商品 /good/info"""
    url = f"{BASE_GOOD_URL}/info"
    headers = HEADERS.copy()
    headers["token"] = token
    data = {"page": 0}  # PageDTO参数

    try:
        response = requests.post(url, headers=headers, json=data)
        print("=== 测试分页查询商品 ===")
        print(f"请求参数: {data}")
        print(f"响应状态码: {response.status_code}")
        res_json = response.json()
        print(f"响应内容: {res_json}\n")
        return res_json
    except Exception as e:
        print(f"接口调用失败: {str(e)}\n")
        return None


def test_search(token):
    """测试按标签搜索 /good/search"""
    url = f"{BASE_GOOD_URL}/search"
    headers = HEADERS.copy()
    headers["token"] = token

    # 测试用例1：有效标签
    data1 = {"label": "教材教辅"}
    # 测试用例2：空标签（预期错误）
    data2 = {"label": ""}

    try:
        print("=== 测试按标签搜索（有效标签） ===")
        res1 = requests.post(url, headers=headers, json=data1)
        print(f"请求参数: {data1}")
        print(f"响应: {res1.json()}\n")


    except Exception as e:
        print(f"搜索接口失败: {str(e)}\n")


def test_insert(token):
    """测试新增商品 /good/insert"""
    url = f"{BASE_GOOD_URL}/insert"
    headers = HEADERS.copy()
    headers["token"] = token
    test_data = generate_test_good()

    try:
        response = requests.post(url, headers=headers, json=test_data)
        print("=== 测试新增商品 ===")
        print(f"请求参数: {test_data}")
        res_json = response.json()
        print(f"响应: {res_json}\n")

        # 返回新增的商品数据（若有ID）
        if res_json.get("data"):
            return res_json["data"]
        return None
    except Exception as e:
        print(f"新增商品失败: {str(e)}\n")
        return None


def test_delete_by_id(token):
    """测试删除商品 /good/deleteByID"""
    url = f"{BASE_GOOD_URL}/deleteByID"
    headers = HEADERS.copy()
    headers["token"] = token


    # 测试用例1：有效ID
    data1 = {"id": 12}
    # 测试用例2：空ID
    data2 = {"id": None}

    try:
        print("=== 测试删除商品（有效ID） ===")
        res1 = requests.post(url, headers=headers, json=data1)
        print(f"请求参数: {data1}")
        print(f"响应: {res1.json()}\n")

    except Exception as e:
        print(f"删除商品失败: {str(e)}\n")


def test_update(token):
    """测试更新商品 /good/update"""
    url = f"{BASE_GOOD_URL}/update"
    headers = HEADERS.copy()
    headers["token"] = token


    # 测试用例1：有效ID+更新字段
    update_data = {
        "id": 13,
        "name": f"_已更新",
        'content':"test",
        "address": "更新后的测试地址",
        "sellerId": 12,
    }
    # 测试用例2：空ID
    empty_id_data = {"id": None}

    try:
        print("=== 测试更新商品（有效ID） ===")
        res1 = requests.post(url, headers=headers, json=update_data)
        print(f"请求参数: {update_data}")
        print(f"响应: {res1.json()}\n")


    except Exception as e:
        print(f"更新商品失败: {str(e)}\n")


def test_review_confirm(token):
    """测试商品审核 /good/review/confirm"""
    url = f"{BASE_GOOD_URL}/review/confirm"
    headers = HEADERS.copy()
    headers["token"] = token



    # 测试用例1：有效Token+审核通过
    audit_data = {
        "id": 13,
        "status": 1,  # 审核通过，改为上架
        "sallerId": 12,
    }
    # 测试用例2：无效Token
    invalid_headers = HEADERS.copy()
    invalid_headers["token"] = "invalid_token_123456"

    try:
        print("=== 测试商品审核（有效Token） ===")
        res1 = requests.post(url, headers=headers, json=audit_data)
        print(f"请求参数: {audit_data}")
        print(f"响应: {res1.json()}\n")


    except Exception as e:
        print(f"审核接口失败: {str(e)}\n")


def test_review_info(token):
    """测试审核列表 /good/review/info"""
    url = f"{BASE_GOOD_URL}/review/info"
    headers = HEADERS.copy()
    headers["token"] = token
    data = {"page": 1}

    try:
        response = requests.post(url, headers=headers, json=data)
        print("=== 测试审核商品列表 ===")
        print(f"请求参数: {data}")
        print(f"响应: {response.json()}\n")
        return response.json()
    except Exception as e:
        print(f"审核列表接口失败: {str(e)}\n")
        return None


if __name__ == "__main__":
    # 1. 获取Token
    token = get_jwt_token()
    if not token:
        print("❌ 无有效Token，终止测试")
        exit(1)

    # 2. 执行所有测试
    print("========== 开始执行商品接口测试 ==========\n")
    test_info(token)
    test_search(token)
    test_insert(token)
    test_delete_by_id(token)
    test_update(token)
    test_review_confirm(token)
    test_review_info(token)
    print("========== 所有测试执行完成 ==========")