<template>
    <a-layout>
      <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <!-- Content -->
        <a-row :gutter="24">
          <a-col :span="8">
            <p>
              <a-form layout="inline" :model="param">
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
                    Add a Doc 
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-table
              v-if= "level1 && level1.length > 1"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
            >
              <template #name="{ text, record }">
                {{record.sort}} {{text}}
              </template>
              <template v-slot:action="{ text: record }">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    Edit
                  </a-button>
                  <a-popconfirm
                    title="Are you sure delete this Doc?"
                    ok-text="Yes"
                    cancel-text="No"
                    @confirm="handleDelete(record.id)"
                  >
                    <a-button type="danger" size="small">
                      Delete
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </a-table>    
          </a-col>
          <a-col :span="16">
            <p>
              <a-form layout="inline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="handleSave()">
                    Save
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-form 
              :model="doc" 
              :label-col="{ span: 6 }" 
              :wrapper-col="{ span: 18 }"
              layout="vertical"
            >
              <a-form-item >
                <a-input v-model:value="doc.name" placeholder="Name"/>
              </a-form-item>
              <a-form-item >
                  <a-tree-select
                    v-model:value="doc.parent"
                    show-search
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    placeholder="Please select a parent Doc"
                    allow-clear
                    tree-default-expand-all
                    :tree-data="treeSelectData"
                    :fieldNames="{label:'name', key:'id', value:'id'}"
                  >
                  </a-tree-select>
              </a-form-item>
              <a-form-item >
                <a-input v-model:value="doc.sort" placeholder="Order"/>
              </a-form-item>
              <a-form-item>
                <div id="content"></div>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-layout-content>
    </a-layout>
</template>
    <!-- <a-modal
      title="Docs form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModelOk"
    >
      <a-form 
        :model="doc" 
        :label-col="{ span: 6 }" 
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="Name">
          <a-input v-model:value="doc.name" />
        </a-form-item>
        <a-form-item label="Parent Doc">
            <a-tree-select
              v-model:value="doc.parent"
              show-search
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="Please select a parent Doc"
              allow-clear
              tree-default-expand-all
              :tree-data="treeSelectData"
              :fieldNames="{label:'name', key:'id', value:'id'}"
            >
            </a-tree-select>
        </a-form-item>
        <a-form-item label="Order">
          <a-input v-model:value="doc.sort" />
        </a-form-item>
        <a-form-item label="Content">
          <div id="content"></div>
        </a-form-item>
      </a-form>
    </a-modal>
</template> -->

<script lang="ts">
  import { defineComponent, onMounted, ref, createVNode, shallowRef} from 'vue';
  import axios from 'axios';
  import { message, Modal } from 'ant-design-vue';
  import { Tool } from '@/util/tool';
  import { useRoute } from 'vue-router';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
  import '@wangeditor/editor/dist/css/style.css'
  import E from 'wangeditor';



  export default defineComponent({
    name: 'AdminDoc',
    setup() {
      const route = useRoute();
      console.log("Route: ",route);
      const docs = ref();
      const loading = ref(false);

      const columns = [
        {
          title: 'Name',
          dataIndex: 'name',
          slots: { customRender: 'name' }
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];
      /**
       * Level 1 Doc tree, children property is the second level.
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
      const level1 = ref();// Level 1 Doc tree. Children property is the second level.
      level1.value = [];

      /**
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        axios.get("/doc/all"
          ).then((response) => {
            loading.value = false;
            const data = response.data;
            if(data.success){
              docs.value = data.content;
              console.log("Original Data:", doc.value);

              level1.value = [];
              level1.value = Tool.array2Tree(docs.value, 0);
              console.log("Tree Structure:", level1);
            }else{
              message.error(data.message);
            }
        });
      };

      /**
       *  QueryListSearch
       * 
      */
      const handleQuerySearch = (param: any) => {
        loading.value = true;
        axios.get("/doc/list", {
          params:{
            name:param.name
          }
        }).then((response) => {
            loading.value = false;
            const data = response.data;
            if(data.success){
              docs.value = data.content.list;

              level1.value = [];
              level1.value = Tool.array2Tree(docs.value, 0);
              console.log("Tree Structure:", level1);             
            }else{
              message.error(data.message);
            }
        });
      };



      // ----- Docs Form ------
      // Because the property state of the tree selection component 
      // will change (disabled) with the node of the currently edited book, 
      // declare a reactive variable separately. 
      const treeSelectData = ref();
      treeSelectData.value = [];
      const doc = ref();
      doc.value = {};
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      let editor: E;


      /**
       *  Value HTML
       * 
      */
    


      const handleSave = () => {
        modalLoading.value = true;
        doc.value.content = editor.txt.html();
        axios.post("/doc/save", doc.value).then((response) => {
          modalLoading.value = true;
          const data = response.data; // data = CommomResp
          if(data.success){
            // modalVisible.value = false;
            message.success("Save successfully！");
            // Reloading the list.
            handleQuery()
          }else{
            message.error(data.message);
          }
        })
      };

      /**
       *  Set a node and its children as disabled.
      */
     const setDisabled = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // Iterate all array
      for(let i = 0; i < treeSelectData.length; i++){
        const node = treeSelectData[i];
        if(node.id === id){
          // If the current node is the target node.
          console.log("disabled", node);
          node.disabled = true;

          // Iterate all its children. 
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j = 0; j < children.length; j++){
              setDisabled(children, children[j].id);
            }
          }
        }else {
            // If current node is a target, find other nodes. 
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              setDisabled(children, id);
            }
        }
      }
     }

      const deleteIds: Array<String> = [];
      const deleteNames: Array<string> = [];

      /**
       *  Check
      */
     const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // Iterate all array
      for(let i = 0; i < treeSelectData.length; i++){
        const node = treeSelectData[i];
        if(node.id === id){
          // If the current node is the target node.
          console.log("deleted:", node);
          // Put target id into idlist.
          deleteIds.push(id);
          deleteNames.push(node.name);

          // Iterate all its children. 
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j = 0; j < children.length; j++){
              getDeleteIds(children, children[j].id);
            }
          }
        }else {
            // If current node is a target, find other nodes. 
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              getDeleteIds(children, id);
            }
        }
      }
     }




      /**
       *  Query Content Search
       *
      */
      const handleQueryContent = () => {
        axios.get("/doc/file-content/" + doc.value.id).then((response) => {
            const data = response.data;
            if(data.success){
              // docs.value = data.content.list;
              console.log("data.content:", data.content);
              if(data.content != null){
                editor.txt.html(data.content);
              }else{
                editor.txt.html('');
              }
            }else{
              message.error(data.message);
            }
        });
      };

      /**
        *  Edit
      */

      const edit = (record: any) =>{
        // Remove editor rich text box.
        editor.txt.html('');
        modalVisible.value = true;
        // If you use
        // doc.value = record, it is a shallow copy. 
        // Changing doc causes changing of the record. 
        doc.value = Tool.copy(record);
        // Use doc.value.id
        console.log(doc.value);
        handleQueryContent();


        // Can't select the children of the current node as its parent.
        treeSelectData.value = Tool.copy(level1.value);
        setDisabled(treeSelectData.value, record.id);

        // Add the Null in tree;
        treeSelectData.value.unshift({id: 0, name: 'Null'});
      };

      /**
       *  Add 
       * 
      */

      const add = () =>{
        // Remove editor rich text box.
        editor.txt.html('');
        modalVisible.value = true;
        doc.value = {
          ebookId: route.query.ebookId
        };

        treeSelectData.value = Tool.copy(level1.value);

        // Add a Null as parent.
        treeSelectData.value.unshift({id: 0, name: 'Null'});
      };

      /**
       * Delete 
      */

      const handleDelete = (id: number) =>{
        // console.log("level1.value:", level1.value, id);
        getDeleteIds(level1.value, id);
        // console.log("ids: ", ids);
        
        // Clear arrays. Otherwise items in array will increase. 
        deleteIds.length = 0;
        deleteNames.length = 0;
        Modal.confirm({
          title: 'Do you want to delete these items?',
          icon: createVNode(ExclamationCircleOutlined),
          content: 'Will delete: [' + deleteNames.join(",") + ']. Do you really want to delete?',
          onOk() {
            axios.delete("doc/delete/" + deleteIds.join(",")).then((response)=>{
              const data = response.data; // data = commonResp
              if(data.success){
                // Reload the Doc list.
                handleQuery();
              }
            });
          },
        })
      };





      /**
       * Search bar
       * 
      */
      const param = ref();
      param.value = {};

      


      onMounted(() => {
        handleQuery();
        editor = new E('#content')
        editor.config.zIndex = 0;
        editor.create();
      });

      return {
        // docs,
        level1,
        columns,
        loading,

        edit,
        add,
        handleDelete,

        doc,
        modalVisible,
        modalLoading,
        handleSave,

        param,
        handleQuery,
        handleQuerySearch,

        treeSelectData,

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





