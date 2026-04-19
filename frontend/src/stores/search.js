import { ref, computed } from 'vue'
import axios from 'axios'

const apiUrl = import.meta.env.VITE_API_URL

// 状态
const keyword = ref('')
const results = ref([])
const loading = ref(false)
const finished = ref(false)
const page = ref(1)

// 搜索函数
async function searchProducts(searchKeyword, pageNum = 1) {
  if (!searchKeyword || searchKeyword.trim() === '') {
    results.value = []
    return
  }

  loading.value = true
  const token = localStorage.getItem('token')

  try {
    const res = await axios.post(
      `/search/good`, // 搜索商品接口
      {
        page: pageNum,
        type: 'good',
        keyword: searchKeyword // 传递关键词参数
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
    )

    const list = res.data.data || []

    if (pageNum === 1) {
      results.value = list
    } else {
      results.value.push(...list)
    }

    if (list.length === 0) {
      finished.value = true
    } else {
      page.value = pageNum + 1
    }
  } catch (e) {
    console.error('搜索失败:', e)
    if (pageNum === 1) {
      results.value = []
    }
  } finally {
    loading.value = false
  }
}

// 重置搜索
function resetSearch() {
  keyword.value = ''
  results.value = []
  loading.value = false
  finished.value = false
  page.value = 1
}

export {
  keyword,
  results,
  loading,
  finished,
  page,
  searchProducts,
  resetSearch
}