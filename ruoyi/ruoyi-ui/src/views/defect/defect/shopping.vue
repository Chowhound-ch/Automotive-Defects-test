<template>
  <div class="app-container">
    <iframe :src="'http://127.0.0.1:8080/mall_page.do?token=' + getTokenStr()" class="shopping-frame">
    </iframe>
  </div>
</template>
<script>
import {getToken} from "@/utils/auth";

export default {
  name: "shopping",
  data() {
    return {
      activeName: '1'
    };
  },
  mounted() {
    // 主站登录成功后发送消息给iframe
    window.frames['iframeId'].postMessage({ authToken: userAuthToken }, 'https://child-domain.com');

// iframe监听message事件并处理登录
    window.addEventListener('message', function(event) {
      if (event.origin === 'https://main-domain.com') {
        const authToken = event.data.authToken;
        // 使用authToken向服务器验证身份
      }
    });

  }
  ,
  methods: {
    getTokenStr(){
      return getToken()
    }
  }
};
</script>


<style scoped lang="scss">
.app-container{
  width: 100%;
  height: 1280px;
}
.shopping-frame{
  width: 100%;
  height: 100%;
}

.collapse-title{
  font-size: 40px;
}
.collapse-title-1{
  font-size: 35px;
}
.collapse-title-2{
  font-size: 28px;
}
.collapse-title-3{
  font-size: 20px;
}

</style>
