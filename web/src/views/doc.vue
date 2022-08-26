<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', 
        padding:'24px', 
        margin:0, 
        minHeight:'280px'}" 
    >
      <a-row>
        <a-col :span="8">
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
					</a-table>    
        </a-col>
        <a-col :span="16">
					<div id="content"></div>
				</a-col>
      </a-row>
        <div class="doc">
          <h1>Welcome to Doc page.</h1>
        </div>
      </a-layout-content>
  </a-layout>
</template>

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
    name: 'Doc',
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
					}
				];

			const level1 = ref();// Level 1 Doc tree. Children property is the second level.
      level1.value = [];

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


			const treeSelectData = ref();
      treeSelectData.value = [];
      const doc = ref();
      doc.value = {};
			let editor: E;


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

        doc,

        handleQuery,
        handleQuerySearch,

        treeSelectData,
      }
		}
})

</script>