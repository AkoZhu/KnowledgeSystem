<template>
    <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <!-- Content -->
        <p>
          <a-form layout="inline" :model="param">
            <a-form-item>
              <a-input v-model:value="param.name" placeholder="Name"></a-input>
            </a-form-item>
            <a-form-item>
              <a-button
                type="primary"
                @click="handleQuerySearch(param)"
              >
                Search
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                Add a Category 
              </a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar" />
          </template>
          <template v-slot:action="{ text: record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                Edit
              </a-button>
              <a-popconfirm
                title="Are you sure delete this Category?"
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
      title="Categorys form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModelOk"
    >
      <a-form 
        :model="category" 
        :label-col="{ span: 6 }" 
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="Name">
          <a-input v-model:value="category.name" />
        </a-form-item>
        <a-form-item label="Parent Category">
          <a-input v-model:value="category.parent" />
        </a-form-item>
        <a-form-item label="Order">
          <a-input v-model:value="category.sort" />
        </a-form-item>
      </a-form>
    </a-modal>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import { Tool } from '@/util/tool';


  export default defineComponent({
    name: 'AdminCategory',
    setup() {
      const categorys = ref();
      const loading = ref(false);

      const columns = [
        {
          title: 'Name',
          dataIndex: 'name'
        },
        {
          title: 'Parent Category',
          key: 'parent',
          dataIndex: 'parent'
        },
        {
          title: 'Order',
          dataIndex: 'sort'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];
      /**
       * Level 1 Category tree, children property is the second level.
       * [{
       *    id: "",
       *    name: "",
       *    children: [{
       *        id: "",
       *        name: "",
       *        children:[]
       *         }]
       * }]
       * 
      */
      const level1 = ref();// Level 1 Category tree. Children property is the second level.


      /**
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        axios.get("/category/all"
          ).then((response) => {
            loading.value = false;
            const data = response.data;
            if(data.success){
              categorys.value = data.content;
              console.log("Original Data:", category.value);

              level1.value = [];
              level1.value = Tool.array2Tree(categorys.value, 0);
              console.log("Tree Structure:", level1);
            }else{
              message.error(data.message);
            }
        });
      };

      /**
       *  QuerySearch
       * 
      */
      const handleQuerySearch = (param: any) => {
        loading.value = true;
        axios.get("/category/list", {
          params:{
            name:param.name
          }
        }).then((response) => {
            loading.value = false;
            const data = response.data;
            if(data.success){
              categorys.value = data.content.list;

              level1.value = [];
              level1.value = Tool.array2Tree(categorys.value, 0);
              console.log("Tree Structure:", level1);             
            }else{
              message.error(data.message);
            }
        });
      };


      // ----- Categorys Form ------
      const category = ref({});
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModelOk = () => {
        modalLoading.value = true;

        axios.post("/category/save", category.value).then((response) => {
          modalLoading.value = false;
          const data = response.data; // data = CommomResp
          if(data.success){
            modalVisible.value = false;

            // Reloading the list.
            handleQuery()
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
        // If you use
        // category.value = record, it is a shallow copy. 
        // Changing category causes changing of the record. 
        category.value = Tool.copy(record);
      };
      /**
       *  Add 
       * 
      */
      const add = () =>{
        modalVisible.value = true;
        category.value = {}
      };

      /**
       * Delete 
      */
      const handleDelete = (id: number) =>{
        axios.delete("category/delete/" + id).then((response)=>{
          const data = response.data; // data = commonResp
          if(data.success){
            // Reload the Category list.
            handleQuery()
          }
        });
      };

      /**
       * Search bar
       * 
      */
      const param = ref();
      param.value = {};



      onMounted(() => {
        handleQuery();
      });

      return {
        // categorys,
        level1,
        columns,
        loading,

        edit,
        add,
        handleDelete,

        category,
        modalVisible,
        modalLoading,
        handleModelOk,

        param,
        handleQuery,
        handleQuerySearch
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


