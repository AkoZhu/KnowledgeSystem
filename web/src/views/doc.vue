<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', 
        padding:'24px', 
        margin:0, 
        minHeight:'280px'}" 
    >
      <a-row>
        <a-col :span="6">
					<a-tree
						v-if= "level1 && level1.length > 0"
						@select="onSelect"
						:tree-data="level1"
						:defaultExpandAll="true"
						:fieldNames="{title:'name', key:'id', value:'id'}"
					>
					</a-tree>    
        </a-col>
        <a-col :span="18">
					<div :innerHTML="html"></div>
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
			const html = ref();
	
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
        axios.get("/doc/all/" + route.query.ebookId
          ).then((response) => {
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
      // const handleQuerySearch = (param: any) => {
      //   axios.get("/doc/list", {
      //     params:{
      //       name:param.name
      //     }
      //   }).then((response) => {

      //       const data = response.data;
      //       if(data.success){
      //         docs.value = data.content.list;

      //         level1.value = [];
      //         level1.value = Tool.array2Tree(docs.value, 0);
      //         console.log("Tree Structure:", level1);             
      //       }else{
      //         message.error(data.message);
      //       }
      //   });
      // };



			const treeSelectData = ref();
      treeSelectData.value = [];
      const doc = ref();
      doc.value = {};



			/**
       *  Query Content Search
       *
      */
      const handleQueryContent = (id: number) => {
        axios.get("/doc/file-content/" + id).then((response) => {
            const data = response.data;
            if(data.success){
              // docs.value = data.content.list;
              console.log("data.content:", data.content);
              html.value = data.content;
            }else{
              message.error(data.message);
            }
        });
      };

			const onSelect = (selectedKeys: any, info: any) =>{
				console.log('selected', selectedKeys, info);
				if(Tool.isNotEmpty(selectedKeys)){
					// load content
					handleQueryContent(selectedKeys[0]);
				}
			};

		onMounted(() => {
        handleQuery();
    });

		return {
        // docs,
        level1,
				html,
				onSelect,
      }
		}
})

</script>