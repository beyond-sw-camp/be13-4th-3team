<template>
  <div id="wapper" class="container-sm">
      <Header @toggle-sidebar="toggleSidebar"></Header>
      <!-- <div class="row">
          <div v-if="route.name !== 'main'" class="col-md-3" id="sidebar">
              <Sidebar />
          </div>

          <div class="col-md-9">
              <RouterView />
          </div> 
        </div>-->
        <div class="content">
          <Sidebar id="side-bar" :class="{ open: isSidebarVisible }" @toggle-sidebar="toggleSidebar"></Sidebar>

        <div id="main-content">
      <RouterView></RouterView>
    </div>
  </div>

      <Footer></Footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { RouterView, useRoute } from 'vue-router';
import Footer from '@/components/common/Footer.vue';
import Header from '@/components/common/Header.vue';
import Sidebar from '@/components/common/Sidebar.vue';

const isSidebarVisible = ref(false);
const route = useRoute();


const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};
</script>

<style scoped>
#wapper{
  min-height: 100%;
  position: relative;
}
Footer{
  bottom: 0px;
  height: 162px;
  position : absolute;
}

.content {
  display: flex;
  transition: transform 0.3s ease-in-out;
}

/* 📌 사이드바 스타일 */
#side-bar {
  position: fixed;
  top: 0px;
  left: -280px; /* 기본적으로 숨김 */
  width: 280px;
  height: 100vh;
  background-color: white;
  z-index: 1000;
  transition: transform 0.3s ease-in-out;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}


#side-bar.open {
  transform: translateX(280px);
}


#main-content {
  flex: 1;
  transition: transform 0.3s ease-in-out;
}

#main-content.shifted {
  transform: translateX(280px);
}

</style>