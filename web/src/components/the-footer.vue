<template>
    <a-layout-footer style="text-align: center">
      Wiki Ebooks! Welcome : {{user.name}};
    </a-layout-footer>
</template>

<script lang="ts">
import store from '@/store';
import { defineComponent, computed} from 'vue';

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => {return store.state.user} );
    return {
      user
    }
  }
});
</script>
