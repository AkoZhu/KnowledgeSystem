<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', 
        padding:'24px', 
        margin:0, 
        minHeight:'280px'}" 
    >
			<h3 v-if="level1.length === 0"> Can't find given document!</h3>
      <a-row>
        <a-col :span="6">
					<a-tree
						v-if= "level1 && level1.length > 0"
						@select="onSelect"
						:tree-data="level1"
						:defaultExpandAll="true"
						:fieldNames="{title:'name', key:'id', value:'id'}"
						:defaultSelectedKeys="defaultSelectedKeys"
					>
					</a-tree>    
        </a-col>
        <a-col :span="18">
					<div class='wangeditor'
						:innerHTML="html"
					>
					</div>
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
			const defaultSelectedKeys = ref();
			defaultSelectedKeys.value = [];
	
			const columns = [
					{
						title: 'Name',
						dataIndex: 'name',
						slots: { customRender: 'name' }
					}
				];

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
							if(Tool.isNotEmpty(level1)){
								// Set this node as selected.
								defaultSelectedKeys.value = [level1.value[0].id];
								// Search the content of this selected node.
								handleQueryContent(level1.value[0].id)
							}
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
				defaultSelectedKeys
      }
		}
})

</script>

<style>
  /* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
  /* table 样式 */
  .wangeditor table {
    border-top: 1px solid #ccc;
    border-left: 1px solid #ccc;
  }
  .wangeditor table td,
  .wangeditor table th {
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
    padding: 3px 5px;
  }
  .wangeditor table th {
    border-bottom: 2px solid #ccc;
    text-align: center;
  }

  /* blockquote 样式 */
  .wangeditor blockquote {
    display: block;
    border-left: 8px solid #d0e5f2;
    padding: 5px 10px;
    margin: 10px 0;
    line-height: 1.4;
    font-size: 100%;
    background-color: #f1f1f1;
  }

  /* code 样式 */
  .wangeditor code {
    display: inline-block;
    *display: inline;
    *zoom: 1;
    background-color: #f1f1f1;
    border-radius: 3px;
    padding: 3px 5px;
    margin: 0 3px;
  }
  .wangeditor pre code {
    display: block;
  }

  /* ul ol 样式 */
  .wangeditor ul, ol {
    margin: 10px 0 10px 20px;
  }

	/* 和antdv p冲突，覆盖掉 */
  .wangeditor blockquote p {
    font-family:"YouYuan";
    margin: 20px 10px !important;
    font-size: 16px !important;
    font-weight:600;
  }
</style>