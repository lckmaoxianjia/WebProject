<template>
  <div class="container centered-div">
    <el-form :model="book" label-width="80px">
      <el-form-item label="书名">
        <el-input v-model="book.book_name"></el-input>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="book.author"></el-input>
      </el-form-item>
      <el-form-item label="出版日期">
        <el-date-picker
          v-model="book.publish_date"
          type="date"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="页数">
        <el-input v-model="book.pages" type="number"></el-input>
      </el-form-item>
      <el-form-item label="价格">
        <el-input v-model="book.price" type="number"></el-input>
      </el-form-item>
      <!-- <el-form-item label="图片">
        <el-input v-model="book.picture"></el-input>
      </el-form-item> -->
        <!-- 上传图片的表单项 -->
      <el-form-item label="上传图片">
        <el-upload
          class="upload-demo"
          action="http://localhost:8081/api/upload" 
          :before-upload="handleBeforeUpload"
          :on-success="handleSuccess"
          :on-remove="handleRemove"
          :file-list="pictureList"
          list-type="picture">
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
        </el-upload>
      </el-form-item>

      <el-form-item label="内容简介">
        <el-input type="textarea" v-model="book.content"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveBook">添加图书</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addBook } from "@/network/api";

export default {
  data() {
    return {
      pictureList:[],
      book: {
        book_name: "",
        author: "",
        publish_date: new Date(),
        pages:'' ,
        price: '',
        picture: "",
        content: "",
      },
    };
  },
  mounted() {
    
  },
  methods: {
  async  saveBook() {
      // 在这里可以调用后台接口保存图书信息
      console.log("图书信息已保存", this.book);
      // 创建一个 FormData 对象
      const formData = new FormData();

//采用formdata好像会使用multipart提交,yes
      // 遍历对象的属性，将每个属性添加到 FormData 中

       // headers: {
        //   'Content-Type': 'multipart/form-data;', // 如果需要覆盖默认配置，可以在这里设置
        //   // 其他自定义请求头...
        // },
      // 这里可以添加保存成功后的提示或跳转逻辑
      try{
         const content = await addBook(this.book)
          this.$message.success(content.message)
      }catch(err){
        this.$message.error(err)
      }
    
     
    },
     handleBeforeUpload(file) {
      // 在上传之前的钩子，可以做一些验证操作
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 < 1*1024;

      if (!(isJPG || isPNG) || !isLt2M) {
        this.$message.error('上传图片只能是 JPG/PNG 格式且不超过 1M!');
        return false;
      }
      return true;
    },
    handleSuccess(response, file) {

      response =response.content
      console.log('照片上传成功',response.data);
      // 在上传成功的钩子，更新存储上传的图片信息
      if (this.pictureList.length > 0) {
        // 如果已经有照片，替换它
        this.pictureList.splice(0, 1, { url: response.data[0], name: file.name });
      } else {
        // 如果没有照片，添加新照片
        this.pictureList.push({ url: response.data[0], name: file.name });
      }
      //更新book中的picture
      this.book.picture = this.pictureList[0].url
    },
    handleRemove(file) {
      // 在移除文件的钩子，清空存储的图片信息
      this.book.pictureList = [];
    },
  },


};
</script>

<style scoped>
/* 在这里可以添加样式以美化页面 */
.centered-div {
  margin: 0 auto;
  width: 60%;
}

/* 设置 el-form-item 内元素左对齐 */
.el-form-item__label {
  text-align: left;
}

.el-form-item__content {
  text-align: left;
}

</style>
