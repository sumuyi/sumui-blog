<template>
  <div>
    <div class="type-bar s-card hover">
      <div class="all-type">
        <a class="type-item choose" @click="handleClick('/test1')">日常随笔</a>
        <a class="type-item" @click="handleClick('/test2')">技术分享</a>
        <a class="type-item" @click="handleClickYuque()">语雀</a>
      </div>
    </div>
    <div>
      <router-view />
    </div>
  </div>
</template>

<script>
import CardMe from '@/components/card/CardMe'
import CardArticle from '@/components/card/CardArticle'
import CardArchive from '@/components/card/CardArchive'
import CardTag from '@/components/card/CardTag'
import ArticleScrollPage from '@/views/common/ArticleScrollPage'

import { getArticles, getHotArtices, getNewArtices } from '@/api/article'
import { getHotTags } from '@/api/tag'
import { listArchives } from '@/api/article'
import axios from "axios";
export default {
  name: 'Index',
  created() {
    this.getHotArtices()
    this.getNewArtices()
    this.getHotTags()
    this.listArchives()
  },
  data() {
    return {
      allType: [
        { name: '日常随笔', path: '/test1' },
        { name: '技术分享', path: '/test2' },
        { name: '技术分享', path: '/test2' },
        { name: '技术分享', path: '/test2' },
        { name: '技术分享', path: '/test2' },
      ],
      hotTags: [],
      hotArticles: [],
      newArticles: [],
      archives: []
    }
  },
  created() {

  },
  methods: {
    handleClickYuque() {

      var config = {
        method: 'get',
        url: 'https://www.yuque.com/api/mine/common_used',
        headers: {
          'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
          'Accept': '*/*',
          'Host': 'www.yuque.com',
          'Connection': 'keep-alive'
        }
      };

      axios(config)
        .then(function (response) {
          console.log(JSON.stringify(response.data));
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    handleClick(path) {
      this.$router.push({ path: path })
    },
    getHotArtices() {
      let that = this
      getHotArtices().then(data => {
        that.hotArticles = data.data
      }).catch(error => {
        if (error !== 'error') {
          that.$message({ type: 'error', message: '最热文章加载失败!', showClose: true })
        }

      })

    },
    getNewArtices() {
      let that = this
      getNewArtices().then(data => {
        that.newArticles = data.data
      }).catch(error => {
        if (error !== 'error') {
          that.$message({ type: 'error', message: '最新文章加载失败!', showClose: true })
        }

      })

    },
    getHotTags() {
      let that = this
      getHotTags().then(data => {
        that.hotTags = data.data
      }).catch(error => {
        if (error !== 'error') {
          that.$message({ type: 'error', message: '最热标签加载失败!', showClose: true })
        }

      })
    },
    listArchives() {
      listArchives().then((data => {
        this.archives = data.data
      })).catch(error => {
        if (error !== 'error') {
          that.$message({ type: 'error', message: '文章归档加载失败!', showClose: true })
        }
      })
    }

  },
  components: {
    'card-me': CardMe,
    'card-article': CardArticle,
    'card-tag': CardTag,
    ArticleScrollPage,
    CardArchive
  }
}
</script>

<style scoped>
.s-card {
  width: 100%;
  padding: .8rem;
  border-radius: 16px;
  background-color: #fff;
  border: 1px solid #e3e8f7;
  box-shadow: 0 8px 16px -4px #2c2d300c;
  transition: all .3s;
  cursor: pointer;
}

.s-card:hover.hover {
  border-color: #425aef;
  box-shadow: 0 8px 16px -4px #4259ef0d;
}

.type-bar {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding: .6rem;
  font-weight: 700;
  animation: fade-up .6s .3s backwards
}

.type-bar .all-type {
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-right: 12px;
  overflow: hidden;
  mask: linear-gradient(90deg, #fff 0, #fff 90%, hsla(0, 0%, 100%, .6) 95%, hsla(0, 0%, 100%, 0) 100%)
}

.type-bar .all-type .type-item {
  display: flex;
  align-items: center;
  padding: .1rem .5rem;
  margin-right: 6px;
  font-weight: 700;
  border-radius: 8px;
  white-space: nowrap;
  height: 30px;
  cursor: pointer
}

.type-bar .all-type .type-item:hover {
  color: #fff;
  background-color: #425aef;
}

.type-bar .all-type .type-item .num {
  margin-left: 4px;
  font-weight: 400;
  padding: 2px 6px;
  font-size: .75rem;
  color: #363636;
  background-color: #e3e8f7;
  border-radius: 8px;
}

.type-bar .all-type .type-item.choose {
  color: #fff;
  background-color: #425aef;
}

.type-bar .all-type .type-item.choose .num {
  color: #425aef;
}

.type-bar .all-type .type-item.hidden {
  display: none;
}

.type-bar .all-type .type-item :hover {
  color: #fff;
  background-color: #425aef;
}

.type-bar .more-type {
  display: flex;
  flex-direction: row;
  align-items: center;
  white-space: nowrap;
  margin-right: 4px;
  margin-left: 8px;
}

.type-bar .more-type .iconfont {
  font-size: .9375rem;
  margin-right: 8px;
}

.type-bar .more-type:hover .iconfont {
  color: #425aef;
}
</style>
