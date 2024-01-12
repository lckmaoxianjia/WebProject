<template>
  <div class="bookListContainer">
    <div class="input-header book-add-container container">
      
          <el-row type="flex" justify="start">
          <el-col :sapn="10" >
            <el-input
              placeholder="请输入内容"
              v-model="searchResult"
              class="input-with-select"
            >
              <el-select style="width:130px"  v-model="value" slot="prepend" placeholder="请选择">
                <!-- <el-option label="书名" value="1"></el-option>
                <el-option label="作者" value="2"></el-option>
                <el-option label="ID" value="3"></el-option> -->
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="parseInt(item.value)">
                </el-option>
              </el-select>
              <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
            </el-input>
          
        </el-col>
        <el-col :span="6">
          <div class="button">
            <el-button type="primary" plain @click="addBook">增加图书</el-button>
          </div>
        </el-col>
      </el-row>
    
    </div>

    <div class="tableDiv">
      <el-table :data="tableData" stripe class="table">
        <el-table-column prop="id" label="ID" width="120"> </el-table-column>
        <el-table-column prop="book_name" label="书名"> </el-table-column>
        <el-table-column prop="author" label="作者"> </el-table-column>
        <el-table-column prop="publish_date" label="出版日期">
        </el-table-column>
        <el-table-column prop="pages" label="页数" width="120">
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120">
        </el-table-column>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              round
              @click="showDetails(scope.row.id)"
            >
              查看详情
            </el-button>
              <el-button
              size="mini"
              type="danger"
              round
              @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
                  <el-button
              size="mini"
              type="green"
              round
              @click="modifyBook(scope.row.id)"
            >
              修改
            </el-button>

          </template>

        </el-table-column>
      </el-table>
    </div>

    <div class="pagination">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageInfo.pageNow"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageInfo.totalBooks"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { getBookList,deletBookById } from '@/network/api';

export default {
  name: "BookList",
  data() {
    return {
      value:1,
      options:[
        {
          value:1,
          label:'书名'
        },
         {
          value:2,
          label:'作者'
        },
         {
          value:3,
          label:'ID'
        },
      ],
      pageInfo: {
        pageNow: 1, //当前页数
        pageSize: 10, //每页显示的数据量
        totalBooks: 0, //总数据量
      },
      tableData: [], //用来存储后端传来的书籍信息，并填入表格
      searchResult: "", //搜索内容
      
    };
  },
  mounted() {
    //页面加载时发送请求数据
    // this.getBookList();
    this.getList();
  
  },
  methods: {

    addBook(){
      this.$router.push('/bookAdd')
    },
    async getList() {
      try{
        const content=  await getBookList({
            pageNow: this.pageInfo.pageNow,
            pageSize: this.pageInfo.pageSize,
            info: this.searchResult,
            select: this.value,  
        })
         //成功获取数据，更新表格数据和页码数据
         this.tableData = content.message.books;
          this.pageInfo.totalBooks = content.message.bookCount;
      }catch(error){ 
          return this.$message.error("获取图书列表失败：" + error);
      };
    },
    handleSizeChange(val) {
      //设置每页显示条数时，更新pageSize，重新获取图书列表
      this.pageInfo.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      //翻页或跳转特定页码时，更新当前页码pageNow，重新获取图书列表
      this.pageInfo.pageNow = val;
      this.getList();
    },
    showDetails(id) {
      //点击查看详情按钮，路由带参跳转
      this.$router.push(`book/${id}`);
    },

    async handleDelete(id) {
      //删除
      if (!id) {
        this.$message.error("删除失败");
        return;
      }

      try{
        await  this.$confirm("确认删除这条数据吗?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              })
        await deleteBookById(id) 
               // 删除成功后，更新 tableData
        this.tableData = this.tableData.filter(item => item.id !== id);
        return  this.$message.success('delete successfully')
      }catch(err){
        return this.$message.error(err);
      }
      
    },
    // deleteBook(id) {
    //   console.log('delete book id = '+id);
    //   deleteBookById(id).then((res) => {
    //       console.log('delete res '+res);
    //       if (res.data.statusCode == 200) {
    //         //成功删除数据，更新表格数据和页码数据
    //         this.getList();
    //       } else if (res.data.statusCode == 500) {
    //         //数据获取失败，弹出提示信息
    //         return this.$message.error(res.data.statusMessage);
    //       }
    //     })
    //     .catch((error) => {
    //       return this.$message.error("获取图书列表失败：" + error);
    //     });
    // },

    search(){
     this.getList()

    },

    modifyBook(id) {

      console.log(id);
      //跳转修改页面,传输id过去，在modify页面使用钩子函数获取
      this.$router.push(`bookModify/${id}`);
    },
  },
};
</script>

<style scoped>
.bookListContainer {
  height: 100%;
}
.input-header{
  width:80%;
  margin: 0,auto;
}
.input-with-select{
  width: 60%;
}
.tableDiv {
  width: 80%;
  text-align: center;
  margin: auto;
  background: #fff;
  padding: 20px;
  box-shadow: 0 10px 10px rgba(0, 0, 0, 0.15);
  vertical-align: middle;
}
.table {
  line-height: 40px;
}
.pagination {
  margin-top: 40px;
}
</style>