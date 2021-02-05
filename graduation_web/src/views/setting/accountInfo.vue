<template>
  <div id="account">
    <div class="title"><span>账号信息</span></div>
    <div class="content">
      <div class="user-info">
        <el-row :gutter="20">
          <el-col :span="4" :offset="0">
            <div @click="editAvatar">
              <el-tooltip class="bottom" effect="dark" content="修改头像">
                <el-avatar
                  size="large"
                  shape="circle"
                  :src="userinfo.avatar"
                  fit="fill"
                ></el-avatar>
              </el-tooltip>
            </div>
          </el-col>
          <el-col :span="10" :offset="0">
            <div class="nick-name" style="">{{ userinfo.name }}
              <span><span :lvl="userOtherInfo.rate | lavelFilter" class="m-level"></span></span>
            </div>
            <div class="sign">{{ userinfo.sign }}</div>
          </el-col>
          <el-col :span="8" :offset="0">
            <el-button type="primary" size="default" @click="editUserInfo">
              修改信息
            </el-button>
          </el-col>
        </el-row>
      </div>
      <div class="work-info">
        <div class="item-container">
          <el-row :gutter="20">
            <el-col :span="2" :offset="0">
              <div class="title">账号</div>
            </el-col>
            <el-col :span="12" :offset="0">
              <div>{{ userinfo.account }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="2" :offset="0">
              <div class="title">PY币</div>
            </el-col>
            <el-col :span="12" :offset="0">
              <div>{{ userOtherInfo.pyb }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="2" :offset="0">
              <div class="title">积分</div>
            </el-col>
            <el-col :span="12" :offset="0">
              <div>{{ userOtherInfo.integral }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="2" :offset="0">
              <div class="title">密码</div>
            </el-col>
            <el-col :span="12" :offset="0">
              <div>
                <el-button type="primary" size="default" @click="showChangePassword"> 修改密码 </el-button>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <el-dialog
      title="用户信息"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <el-form :model="form">
        <el-form-item label="昵称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户签名">
          <el-input v-model="form.sign" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUserInfo">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="上传头像"
      :visible.sync="avatarFrom"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <el-form :model="form" id="avatar">
        <el-form-item label="">
          <el-upload
            class="avatar-uploader"
            action
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :http-request="uploadImage"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="avatarFrom = false">取 消</el-button>
        <el-button type="primary" @click="updateAvatar">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="修改密码"
      :visible.sync="changePasswordFrom"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <el-form :model="formPass" id="avatar">
        <el-form-item label="密码">
          <el-input v-model="formPass.password" type="password"  autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="formPass.repassword" type="password" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changePassCancel">取 消</el-button>
        <el-button type="primary" @click="changePassClick">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { changePass, editUser, getInfo, getUserInfo } from '@/api/user';
import { uploadImge } from '@/api/image';
export default {
  filters: {
    lavelFilter(target){
      return +target
    }
  },
  data() {
    return {
      userinfo: {},
      changePasswordFrom: false,
      dialogFormVisible: false,
      avatarFrom: false,
      form: {
        uid: '',
        name: '',
        sign: '',
        avatar: '',
      },
      formPass: {
        uid: '',
        password: '',
        repassword: '',
      },
      userOtherInfo: {}
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    changePassCancel() {
      this.formPass.password = ''
      this.formPass.repassword = ''
      this.changePasswordFrom = false
    },
    changePassClick() {
      if (this.formPass.password.length < 6) {
        this.$message.info("密码不能少于6个字符")
        return
      }
      if (this.formPass.password != this.formPass.repassword) {
        this.$message.info("两次输入的密码不一致")
        return
      }
      changePass(this.formPass).then(async res => {
        if (res.success) {
          this.$message.info("修改成功，请重新登录")
          await this.$store.dispatch('user/logout')
          this.$router.push(`/login?redirect=${this.$route.fullPath}`)
          this.changePasswordFrom = false
        }
      })
    },
    showChangePassword() {
      this.formPass.uid = this.userinfo.uid
      this.changePasswordFrom = true
    },
    uploadImage(fileObj) {
      let formData = new FormData();
      formData.set('file', fileObj.file);
      uploadImge(formData).then((res) => {
        console.log(res);
        if (res.success) {
          this.form.avatar = res.data.url;
          this.$message.success(res.message);
        }
      });
    },
    handleAvatarSuccess(res, file) {
      this.form.avatar = URL.createObjectURL(file.raw);
    },
    updateAvatar() {
      console.log(this.form);
      const { avatar } = this.form;
      if (!avatar) {
        this.$message.warning('头像不能为空');
        return;
      }
      if (avatar === this.userinfo.avatar) {
        this.$message.warning('未改变内容');
        return;
      }
      editUser(this.form).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.avatarFrom = false;
          this.getUser();
        }
      });
    },
    editAvatar() {
      this.form.uid = this.userinfo.uid;
      this.form.name = this.userinfo.name;
      this.form.sign = this.userinfo.sign;
      this.form.avatar = this.userinfo.avatar;
      this.avatarFrom = true;
    },
    getUser() {
      getInfo().then((res) => {
        this.userinfo = res.data;
      });
      getUserInfo().then(res => {
         this.userOtherInfo = res.data;
      })
    },
    editUserInfo() {
      console.log('lod....');
      this.form.uid = this.userinfo.uid;
      this.form.name = this.userinfo.name;
      this.form.sign = this.userinfo.sign;
      this.form.avatar = this.userinfo.avatar;
      this.dialogFormVisible = true;
    },
    updateUserInfo() {
      console.log(this.form);
      const { name, sign } = this.form;
      if (!name) {
        this.$message.warning('昵称不能为空');
        return;
      }
      if (!sign) {
        this.$message.warning('签名不能为空');
        return;
      }
      if (name === this.userinfo.name && sign === this.userinfo.sign) {
        this.$message.warning('未改变内容');
        return;
      }
      editUser(this.form).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.dialogFormVisible = false;
          this.getUser();
        }
      });
    },
  },
};
</script>

<style lang="scss" scope>

#account {
  .title {
    margin-bottom: 2rem;
    span {
      font-weight: 400;
      font-size: 20px;
      line-height: 30px;
      text-align: center;
    }
  }
  .content .work-info .el-row {
    border-top: 1px dashed #ccc;
    line-height: 50px;
  }
  .content .work-info {
    margin-bottom: 30px;
  }
  .content .title {
    font-size: 16px;
    font-weight: 600;
  }
  .content .user-info {
    position: relative;
    border-bottom: #d9ecff 1px dashed;
    padding-bottom: 30px;
  }
  .content .user-info .nick-name {
    margin-top: 10px;
    font-size: 30px;
  }
  .content .user-info .sign {
    margin-top: 18px;
    font-size: 16px;
  }
  .content .user-info .el-avatar {
    width: 100px;
    border-radius: 50%;
    cursor: pointer;
    height: 100px;
  }
}
#avatar {
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
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
}
</style>
