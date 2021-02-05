<template>
  <div id="articlepost">
    <div class="title">
      <el-input
        v-model="post.title"
        placeholder="文章标题(8-32个字符)"
        size="normal"
        clearable
        maxlength="32"
        minlength="8"
        show-word-limit
      ></el-input>
    </div>
    <div class="content">
      <mavon-editor
        v-model="post.content"
        boxShadowStyle="none"
        previewBackground="transparent"
        :toolbars="markdownOption"
        ref="md"
        @imgAdd="$imgAdd"
        :ishljs="true"
      />
    </div>
    <hr />
    <div class="edit">
      <div class="el-form-item category">
        <div class="el-form-item__label">分类</div>
        <div class="el-form-item__content">
          <el-select v-model="post.cid" clearable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="el-form-item desc">
        <div class="el-form-item__label">描述</div>
        <div class="el-form-item__content">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
            v-model="post.desc"
          >
          </el-input>
        </div>
      </div>
      <div class="el-form-item cover">
        <div class="el-form-item__label">封面</div>
        <div class="el-form-item__content">
          <el-upload
            class="avatar-uploader"
            action
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :http-request="uploadImage"
          >
            <img v-if="post.cover" :src="post.cover" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
      </div>
      <div class="el-form-item tags">
        <div class="el-form-item__label">标签</div>
        <div class="el-form-item__content">
          <el-tag
            :key="tag"
            v-for="tag in dynamicTags"
            closable
            :disable-transitions="false"
            @close="handleTagClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            maxlength="18"
            show-word-limit
            @keyup.enter.native="handleInputTagConfirm"
            @blur="handleInputTagConfirm"
          >
          </el-input>
          <el-button
            v-else
            class="button-new-tag"
            size="small"
            @click="showTagInput"
            >+ New Tag</el-button
          >
        </div>
      </div>
      <div class="control">
        <el-button type="primary" v-if="isedit === '0'" size="default" @click="savePost"
          >提交</el-button
        >
        <el-button type="primary" v-if="isedit === '1'" size="default" @click="updatePost"
          >保存修改</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import { getCategoryList } from '@/api/category';
import { addPost, getPostInfo, updatePost } from '@/api/post';
import { uploadImge } from '@/api/image';
export default {
  data() {
    return {
      isedit: this.$route.query.isedit || '0',
      pid: this.$route.query.pid || '',
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',

      options: [],
      type: 0,
      post: {
        title: '',
        cid: '',
        content: '',
        cover: '',
        desc: '',
        tags: '',
      },
      markdownOption: {
        header: true, // 标题
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
    };
  },
  created() {
    this.getCate();
    this.getPost();
  },
  methods: {
    getPost() {
      if (this.isedit === '1') {
        getPostInfo(this.pid).then(res => {
          console.log(res.data);
          const tags = res.data.tags
          this.post = res.data
          this.dynamicTags = tags.split("-")
          if (this.dynamicTags.length === 1 && this.dynamicTags[0] === "") {
            this.dynamicTags = []
          }
        })
      }
    },
    updatePost() {
      let tempTags = this.dynamicTags.join('-');
      this.post.tags = tempTags;
      console.log(this.post);
      const { title, content, cid, desc } = this.post;
      if (!title) {
        this.$message.info('标题不能为空');
        return;
      }
      if (!content) {
        this.$message.info('内容不能为空');
        return;
      }
      if (!cid) {
        this.$message.info('分类不能为空');
        return;
      }
      if (!desc) {
        this.$message.info('描述不能为空');
        return;
      }
      updatePost(this.post).then((res) => {
        console.log('success==> ', res);
        if (res.success === true) {
          this.$message.success('修改成功');
          this.$router.push('/center/content/articleContent');
        }
      });
    },
    handleTagClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
    showTagInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputTagConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.dynamicTags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
    $imgAdd(pos, $file) {
      const formdata = new FormData();
      formdata.append('file', $file);
      uploadImge(formdata).then((res) => {
        console.log(res);
        if (res.success) {
          this.$message.success(res.message);
          this.$refs.md.$img2Url(pos, res.data.url);
        }
      });
    },
    uploadImage(fileObj) {
      let formData = new FormData();
      formData.set('file', fileObj.file);
      uploadImge(formData).then((res) => {
        console.log(res);
        if (res.success) {
          this.post.cover = res.data.url;
          this.$message.success(res.message);
        }
      });
    },
    handleAvatarSuccess(res, file) {
      this.post.cover = URL.createObjectURL(file.raw);
    },
    savePost() {
      let tempTags = this.dynamicTags.join('-');
      this.post.tags = tempTags;
      console.log(this.post);
      const { title, content, cid, desc } = this.post;
      if (!title) {
        this.$message.info('标题不能为空');
        return;
      }
      if (!content) {
        this.$message.info('内容不能为空');
        return;
      }
      if (!cid) {
        this.$message.info('分类不能为空');
        return;
      }
      if (!desc) {
        this.$message.info('描述不能为空');
        return;
      }

      addPost(this.type, this.post).then((res) => {
        console.log('success==> ', res);
        if (res.success === true) {
          this.$message.success('添加成功');
          this.$router.push('/center/content/articleContent');
        }
      });
    },
    getCate() {
      getCategoryList().then((res) => {
        // console.log(res.data);
        const arr = [];
        res.data.forEach((item) => {
          if (item.status === '0') {
            arr.push({
              value: item.cid,
              label: item.title,
            });
          }
        });
        this.options = arr;
      });
    },
  },
};
</script>

<style lang="scss" scope>
#articlepost .title {
  position: relative;
  width: 100%;
  height: 58px;
  line-height: 58px;
  input {
    width: 100%;
    height: 100%;
    border: none;
    outline: none;
    color: #595959;
    font-size: 18px;
    padding-left: 20px;
    padding-right: 80px;
    font-weight: 400;
  }
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 268px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 268px;
  height: 178px;
  display: block;
}
</style>
