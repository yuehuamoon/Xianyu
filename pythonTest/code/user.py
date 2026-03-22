import requests
import json

# 基础配置
BASE_URL = "http://localhost:8081/user"
HEADERS = {"Content-Type": "application/json"}


def test_login():
    """测试登录接口：正确/错误场景"""
    print("=== 测试登录接口 ===")
    # 1. 正确场景：手机号和密码匹配
    login_success_data = {
        "phone": "13700137002",
        "password": "e10adc3949ba59abbe56e057f20f883e"
    }
    res_success = requests.post(
        f"{BASE_URL}/login",
        json=login_success_data,
        headers=HEADERS
    )
    print("✅ 正确登录响应：")
    print(json.dumps(res_success.json(), ensure_ascii=False, indent=2))

    # 2. 错误场景：密码错误
    login_error_data = {
        "phone": "13700137002",
        "password": "wrong_password"
    }
    res_error = requests.post(
        f"{BASE_URL}/login",
        json=login_error_data,
        headers=HEADERS
    )
    print("❌ 密码错误响应：")
    print(json.dumps(res_error.json(), ensure_ascii=False, indent=2))

    # 3. 错误场景：手机号不存在
    login_none_data = {
        "phone": "13800000000",
        "password": "e10adc3949ba59abbe56e057f20f883e"
    }
    res_none = requests.post(
        f"{BASE_URL}/login",
        json=login_none_data,
        headers=HEADERS
    )
    print("❌ 手机号不存在响应：")
    print(json.dumps(res_none.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


def test_register():
    """测试注册接口：正确/错误场景"""
    print("=== 测试注册接口 ===")
    # 1. 正确场景：完整参数注册
    register_success_data = {
        "realName": "测试用户",
        "nickName": "test_user",
        "password": "123456",
        "phone": "13900139000",
        "address": "北京市朝阳区",
        "gender": 0,
        "avatarUrl": "https://test.com/avatar.jpg",
        "certNo": "110101199001011234"
    }
    res_success = requests.post(
        f"{BASE_URL}/register",
        json=register_success_data,
        headers=HEADERS
    )
    print("✅ 正确注册响应：")
    print(json.dumps(res_success.json(), ensure_ascii=False, indent=2))

    # 2. 错误场景：必填参数缺失（手机号为空）
    register_error_data = {
        "realName": "测试用户",
        "password": "123456"
        # 缺少phone参数
    }
    res_error = requests.post(
        f"{BASE_URL}/register",
        json=register_error_data,
        headers=HEADERS
    )
    print("❌ 缺少手机号响应：")
    print(json.dumps(res_error.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


def test_delete():
    """测试删除用户接口：正确/错误场景"""
    print("=== 测试删除用户接口 ===")
    # 1. 正确场景：手机号存在
    delete_success_data = {
        "phone": "13900139000"
    }
    res_success = requests.post(
        f"{BASE_URL}/delete",
        json=delete_success_data,
        headers=HEADERS
    )
    print("✅ 正确删除响应：")
    print(json.dumps(res_success.json(), ensure_ascii=False, indent=2))

    # 2. 错误场景：手机号为空
    delete_error_data = {
        "realName": "测试用户"
        # 缺少phone参数
    }
    res_error = requests.post(
        f"{BASE_URL}/delete",
        json=delete_error_data,
        headers=HEADERS
    )
    print("❌ 手机号为空响应：")
    print(json.dumps(res_error.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


def test_update():
    """测试更新用户接口：正确/错误场景"""
    print("=== 测试更新用户接口 ===")
    # 1. 正确场景：更新昵称和地址
    update_success_data = {
        "phone": "13700137002",
        "nickName": "updated_nick",
        "address": "上海市浦东新区"
    }
    res_success = requests.post(
        f"{BASE_URL}/update",
        json=update_success_data,
        headers=HEADERS
    )
    print("✅ 正确更新响应：")
    print(json.dumps(res_success.json(), ensure_ascii=False, indent=2))

    # 2. 错误场景：手机号为空
    update_error_data = {
        "nickName": "wrong_update"
        # 缺少phone参数
    }
    res_error = requests.post(
        f"{BASE_URL}/update",
        json=update_error_data,
        headers=HEADERS
    )
    print("❌ 手机号为空响应：")
    print(json.dumps(res_error.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


def test_identify():
    """测试身份审核接口：正确/错误场景"""
    print("=== 测试身份审核接口 ===")
    # 1. 正确场景：审核通过（status=1）
    identify_success_data = {
        "phone": "13700137002",
        "status": 1
    }
    res_success = requests.post(
        f"{BASE_URL}/identify",
        json=identify_success_data,
        headers=HEADERS
    )
    print("✅ 正确审核响应：")
    print(json.dumps(res_success.json(), ensure_ascii=False, indent=2))

    # 2. 错误场景：手机号为空
    identify_error_data = {
        "status": 0
        # 缺少phone参数
    }
    res_error = requests.post(
        f"{BASE_URL}/identify",
        json=identify_error_data,
        headers=HEADERS
    )
    print("❌ 手机号为空响应：")
    print(json.dumps(res_error.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


def test_info():
    """测试获取用户信息接口：正确/错误场景"""
    print("=== 测试获取用户信息接口 ===")
    # 1. 正确场景：手机号存在（GET请求，参数拼URL）
    info_success_params = {
        "phone": "13700137002"
    }
    res_success = requests.get(
        f"{BASE_URL}/info",
        params=info_success_params,
        headers=HEADERS
    )
    print("✅ 正确获取信息响应：")
    print(json.dumps(res_success.json(), ensure_ascii=False, indent=2))

    # 2. 错误场景：手机号为空
    info_error_params = {
        "phone": ""
    }
    res_error = requests.get(
        f"{BASE_URL}/info",
        params=info_error_params,
        headers=HEADERS
    )
    print("❌ 手机号为空响应：")
    print(json.dumps(res_error.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


def test_get_all():
    """测试获取所有用户接口（仅正确场景）"""
    print("=== 测试获取所有用户接口 ===")
    res = requests.get(f"{BASE_URL}/get", headers=HEADERS)
    print("✅ 获取所有用户响应：")
    print(json.dumps(res.json(), ensure_ascii=False, indent=2))
    print("-" * 50)


# 执行所有测试
if __name__ == "__main__":
    test_login()
    test_register()
    test_delete()
    test_update()
    test_identify()
    test_info()
    test_get_all()
    print("🎉 所有接口测试完成！")