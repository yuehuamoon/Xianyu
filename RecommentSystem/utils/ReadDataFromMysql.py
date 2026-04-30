import pymysql
import pandas as pd

# ----------------------
# 数据库配置（改成你自己的）
# ----------------------
DB_CONFIG = {
    "host": "localhost",
    "user": "root",          # 你的数据库用户名
    "password": "123456",    # 你的密码
    "database": "YHtrade",   # 数据库名
    "charset": "utf8mb4"
}

# ----------------------
# 从 MySQL 读取点赞数据
# ----------------------
def read_likes_from_db():
    conn = pymysql.connect(**DB_CONFIG)
    # 只读取 商品点赞(type=2)，导出 user_id, item_id, label
    sql = """
        SELECT *
        FROM view_train_data
    """
    df = pd.read_sql(sql, conn)
    conn.close()
    return df

# ----------------------
# 保存为 CSV
# ----------------------
def save_to_csv(df, csv_path="../data/user_item_info.csv"):
    df.to_csv(csv_path, index=False, encoding="utf-8")
    print(f"✅ 成功导出 CSV：{csv_path}")
    print(f"📊 总数据条数：{len(df)}")

# ----------------------
# 执行
# ----------------------
if __name__ == "__main__":
    df = read_likes_from_db()
    print(df.head(10))  # 打印前10条看看
    save_to_csv(df)