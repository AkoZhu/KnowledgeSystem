<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a class="login-menu" 
      @click="showLoginModal" 
      v-if="!user.id"
    >
      <span> <LoginOutlined/> Login</span>
    </a>
    <a-popconfirm 
      title="Confirm to logout ?" 
      ok-text="Yes"
      cancel-text="No"
      @confirm="logout" 
    >
      <a class="login-menu" v-if="user.id">
        <span> <LogoutOutlined /> Logout</span>
      </a>
    </a-popconfirm>
    <a class="login-menu" v-if="user.id">
      <span>Welcome: <UserOutlined /> {{user.name}}</span>
    </a>

    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="/">
          <router-link to="/">Home</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/user">
          <router-link to="/admin/user">User Management</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/ebook">
          <router-link to="/admin/ebook">Ebooks Management</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/category">
          <router-link to="/admin/category">Category Management</router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">About</router-link>
        </a-menu-item>
    </a-menu>

    <a-modal title="Login" 
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
    >
      <a-form
        :model="loginUser"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="Login Name">
          <a-input v-model:value="loginUser.loginName"/>
        </a-form-item>
        <a-form-item label="Password">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>        
      </a-form>
    </a-modal>

  </a-layout-header>
</template>

<script lang="ts">
import axios from 'axios';
import { message } from 'ant-design-vue';
import { computed, defineComponent, ref } from 'vue';
import { UserOutlined, LogoutOutlined, LoginOutlined} from '@ant-design/icons-vue';
import store from '@/store';

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  components:{
    LoginOutlined,
    LogoutOutlined,
    UserOutlined
  },
  setup() {
    // Save after login.
    const user = computed(() => {return store.state.user});

    // Used for login.
    const loginUser = ref({
      loginName:"test",
      password:"test123"
    })
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);

    const showLoginModal = () =>{
      loginModalVisible.value = true;
    }

    // Log in 
    const login = () => {
      console.log("Start login.")
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) =>{
        loginModalLoading.value = false;
        const data = response.data;
        if(data.success){
          loginModalVisible.value = false;
          message.success("Log in successfully!");
          store.commit("setUser", data.content);
        }else{
          message.error(data.message);
        }
      });
    };

    // log out
    const logout = () => {
      console.log("Start logout.");
      axios.get("/user/logout/" + user.value.token).then((response) =>{
        const data = response.data;
        if(data.success){
          message.success("Log in successfully!");
          // remove user in sessionStorage.
          store.commit("setUser", {});
        }else{
          message.error(data.message);
        }
      });
    };
    
    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      user,

      logout,
    }

  }
});
</script>

<style>
  .login-menu {
    float: right !important;
    color:white;
    padding-left: 10px;
  }
</style>
