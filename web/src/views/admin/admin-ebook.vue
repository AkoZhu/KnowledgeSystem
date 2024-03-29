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
                @click="handleQuery({page: 1, size: pagination.pageSize})"
              >
                Search
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()">
                Add a Ebook 
              </a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar" />
          </template>
          <template v-slot:category="{text, record}">
            <span>{{getCategoryName(record.category1Id)}}/{{getCategoryName(record.category2Id)}}</span>
          </template>

          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <router-link :to="'/admin/doc?ebookId=' + record.id">
                <a-button type="primary">
                  Doc Management
                </a-button>
              </router-link>
              <a-button type="primary" @click="edit(record)">
                Edit
              </a-button>
              <a-popconfirm
                title="Are you sure delete this Ebook?"
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
      title="Ebooks form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModelOk"
    >
      <a-form 
        :model="ebook" 
        :label-col="{ span: 6 }" 
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="Cover">
          <a-input v-model:value= "ebook.cover" />
        </a-form-item>
        <a-form-item label="Name">
          <a-input v-model:value="ebook.name" />
        </a-form-item>
        <a-form-item label="Category">
          <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
          />
        </a-form-item>
        <a-form-item label="Description">
          <a-input v-model:value="ebook.description" type="textarea" />
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
    name: 'AdminEbook',
    setup() {
      const ebooks = ref();
      const pagination = ref({
        current: 1,
        pageSize: 10,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: 'Cover',
          dataIndex: 'cover',
          slots: { customRender: 'cover' }
        },
        {
          title: 'Name',
          dataIndex: 'name'
        },
        {
          title: 'Category',
          slots: { customRender: 'category' }
        },
        {
          title: 'Document Number',
          dataIndex: 'docCount'
        },
        {
          title: 'View Count',
          dataIndex: 'viewCount'
        },
        {
          title: 'Vote Count',
          dataIndex: 'voteCount'
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
        axios.get("/ebook/list", {
            params: {
              page: params.page,
              size: params.size,
              name: param.value.name,
            }
          }).then((response) => {
            loading.value = false;
            const data = response.data;
            if(data.success){
              ebooks.value = data.content.list;
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

      // ----- Ebooks Form ------
      /**
       * Array, [100, 101] represents Frontend Development/ Vue.
       * 
      */
      const categoryIds = ref();
      const ebook = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModelOk = () => {
        modalLoading.value = true;
        ebook.value.category1Id = categoryIds.value[0];
        ebook.value.category2Id = categoryIds.value[1];

        axios.post("/ebook/save", ebook.value).then((response) => {
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
        // If you use
        // ebook.value = record, it is a shallow copy. 
        // Changing ebook causes changing of the record. 
        ebook.value = Tool.copy(record);
        categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
      };
      /**
       *  Add 
       * 
      */
      const add = () =>{
        modalVisible.value = true;
        ebook.value = {}
      };

      /**
       * Delete 
      */
      const handleDelete = (id: number) =>{
        axios.delete("ebook/delete/" + id).then((response)=>{
          const data = response.data; // data = commonResp
          if(data.success){
            // Reload the Ebook list.
            handleQuery({
              page:pagination.value.current,
              size:pagination.value.pageSize,
            })
          }
        });
      };

      const level1 =  ref();
      let categorys: any;
      /**
       * 查询所有分类
       **/
      const handleQueryCategory = () => {
        loading.value = true;
        axios.get("/category/all").then((response) => {
          loading.value = false;
          const data = response.data;
          if (data.success) {
            categorys = data.content;
            console.log("原始数组：", categorys);

            level1.value = [];
            level1.value = Tool.array2Tree(categorys, 0);
            console.log("树形结构：", level1.value);

            // After loading the Category, we load ebooks. Otherwise if the category
            // loads slowly, ebooks render would get error. 
            handleQuery({
              page: 1,
              size: pagination.value.pageSize
            });
          } else {
            message.error(data.message);
          }
        });
      };

      const getCategoryName = (cid: number) => {
        let result = "";
        categorys.forEach((item: any) => {
          if(item.id === cid){
            result = item.name;
          }
        });
        return result;
      }

      /**
       * Search bar
       * 
      */
      const param = ref();
      param.value = {};



      onMounted(() => {
        handleQueryCategory();
      });

      return {
        ebooks,
        pagination,
        columns,
        loading,
        handleTableChange,

        edit,
        add,
        handleDelete,

        ebook,
        modalVisible,
        modalLoading,
        handleModelOk,
        categoryIds,
        level1,

        param,
        handleQuery,
        getCategoryName
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


