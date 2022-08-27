<template>
    <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <!-- Content -->
        <p>
          <a-form layout="inline" :model="param">
            <a-form-item>
              <a-input v-model:value="param.loginName" placeholder="Name"></a-input>
            </a-form-item>
            <a-form-item>
              <a-button
                type="primary"
                @click="handleQuery({page: 1, size: pagination.pageSize})"
              >
                Search
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                Add a User 
              </a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
        >
          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                Edit
              </a-button>
              <a-button type="primary" @click="edit(record)">
                New Password
              </a-button>  
              <a-popconfirm
                title="Are you sure delete this User?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="danger">
                  Delete
                </a-button>
              </a-popconfirm>            
            </a-space>
          </template>
        </a-table>      
      </a-layout-content>
    </a-layout>

    <a-modal
      title="Users form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModelOk"
    >
      <a-form 
        :model="user" 
        :label-col="{ span: 6 }" 
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="Login Name" >
          <a-input v-model:value= "user.loginName" :disabled="!!user.id"/>
        </a-form-item>
        <a-form-item label="Name">
          <a-input v-model:value="user.name" />
        </a-form-item>
        <a-form-item label="Password" v-show="!user.id">
          <a-input v-model:value="user.password"/>
        </a-form-item>
      </a-form>
    </a-modal>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import { Tool } from '@/util/tool';

  declare let hexMd5: any;
  declare let KEY: any;


  export default defineComponent({
    name: 'AdminUser',
    setup() {
      const users = ref();
      const pagination = ref({
        current: 1,
        pageSize: 10,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: 'Login Name',
          dataIndex: 'loginName',
          slots: { customRender: 'loginName' }
        },
        {
          title: 'Name',
          dataIndex: 'name'
        },
        {
          title: 'Password',
          dataIndex: 'password'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];

      /**
       * 数据查询
       **/
      const handleQuery = (params: any) => {
        loading.value = true;
        console.log("param.value.login: ",param.value.login);
        axios.get("/user/list", {
            params: {
              page: params.page,
              size: params.size,
              loginName: param.value.loginName,
            }
          }).then((response) => {
            loading.value = false;
            const data = response.data;
            if(data.success){
              users.value = data.content.list;
              // 重置分页按钮
              pagination.value.current = params.page;
              pagination.value.total = data.content.total
            }else{
              message.error(data.message);
            }
        });
      };

      /**
       * 表格点击页码时触发
       */
      const handleTableChange = (pagination: any) => {
        console.log("看看自带的分页参数都有啥：" + pagination);
        handleQuery({
          page: pagination.current,
          size: pagination.pageSize
        });
      };

      // ----- Users Form ------
      /**
       * Array, [100, 101] represents Frontend Development/ Vue.
       * 
      */
      const user = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModelOk = () => {
        modalLoading.value = true;

        user.value.password = hexMd5(user.value.password + KEY);

        axios.post("/user/save", user.value).then((response) => {
          modalLoading.value = false;
          const data = response.data; // data = CommomResp
          if(data.success){
            modalVisible.value = false;

            // Reloading the list.
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize,
            })
          }else{
            message.error(data.message);
          }
        })
      };

      /**
       *  Edit 
      */
     
      const edit = (record: any) =>{
        modalVisible.value = true;
        user.value = Tool.copy(record);
      };
      /**
       *  Add 
       * 
      */
      const add = () =>{
        modalVisible.value = true;
        user.value = {}
      };

      /**
       * Delete 
      */
      const handleDelete = (id: number) =>{
        axios.delete("user/delete/" + id).then((response)=>{
          const data = response.data; // data = commonResp
          if(data.success){
            // Reload the User list.
            handleQuery({
              page:pagination.value.current,
              size:pagination.value.pageSize,
            })
          }
        });
      };

      const level1 =  ref();



      /**
       * Search bar
       * 
      */
      const param = ref();
      param.value = {};



      onMounted(() => {
        handleQuery({
          page: 1,
          size: pagination.value.pageSize,
        });
      });

      return {
        users,
        pagination,
        columns,
        loading,
        handleTableChange,

        edit,
        add,
        handleDelete,

        user,
        modalVisible,
        modalLoading,
        handleModelOk,
        level1,

        param,
        handleQuery,
      }
    }
  });
</script>

<style scoped>
  img {
    width: 50px;
    height: 50px;
  }
</style>


