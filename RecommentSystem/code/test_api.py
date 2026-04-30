import requests

res = requests.post('http://127.0.0.1:5000/recommend', json={'user_id': 12, 'top_n': 5})
print(res.json())
