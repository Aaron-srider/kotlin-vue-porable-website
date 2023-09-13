<template>
    <div class=''>
        <el-table :data="userList">
            <el-table-column label="id">
                <template #default="scope">
                    <span>{{ scope.row.id }}</span>
                </template>
            </el-table-column>
            <el-table-column label="username">
                <template #default="scope">
                    <span>{{ scope.row.username }}</span>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script lang='ts'>
import {Component, Vue} from 'vue-property-decorator';
import Client from "@/request/client";

@Component({})
export default class IndexView extends Vue {
    userList:Record<any, any>[] = []
    created() {
        Client.getUsers().then(resp => {
            this.userList = resp.data
        })
    }
}
</script>
<style lang='scss' scoped>
</style>
