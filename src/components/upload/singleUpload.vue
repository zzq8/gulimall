<template> 
  <div>
    <el-upload
      action="http://up-z2.qiniup.com"
      :data="QiniuData"
      list-type="picture"
      :multiple="false" :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog>
  </div>
</template>
<script>
   import {policy} from './policy'
   import { getUUID } from '@/utils'

  export default {
    name: 'singleUpload',
    props: {
      value: String
    },
    computed: {
      imageUrl() {
        console.log("imageUrl",this.value);
        return this.value;
      },
      imageName() {
        if (this.value != null && this.value !== '') {
          return this.value.substr(this.value.lastIndexOf("/") + 1);
        } else {
          return null;
        }
      },
      fileList() {
        return [{
          name: this.imageName,
          url: this.imageUrl
        }]
      },
      showFileList: {
        get: function () {
          return this.value !== null && this.value !== ''&& this.value!==undefined;
        },
        set: function (newValue) {
        }
      }
    },
    data() {
      return {
        QiniuData: {    //这里是直接绑定data的值
          key: "", //图片名字处理
          token: "", //七牛云token
          dir: "img/",
          host: ""
        },
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: '',
          host: '',
          // callback:'',
        },
        dialogVisible: false
      };
    },
    methods: {
      emitInput(val) {
        this.$emit('input', val)
      },
      handleRemove(file, fileList) {
        this.emitInput('');
      },
      handlePreview(file) {
        this.dialogVisible = true;
      },
      beforeUpload(file) {
        let _self = this;
        return new Promise((resolve, reject) => {
          policy().then(response => {
            console.log("mydata",response.data);
            console.log(this.name);
            this.QiniuData.token = response.data.token;
            this.QiniuData.key = "img/"+getUUID() + response.data.key;
            this.QiniuData.host = response.data.host;
            // _self.dataObj.policy = response.data.policy;
            // _self.dataObj.signature = response.data.signature;
            // _self.dataObj.ossaccessKeyId = response.data.accessid;
            // _self.dataObj.key = response.data.dir + '/'+getUUID()+'_${filename}';
            // _self.dataObj.dir = response.data.dir;
            // _self.dataObj.host = response.data.host;
            resolve(true)
          }).catch(err => {
            reject(false)
          })
        })
      },
      handleUploadSuccess(res, file) {
        console.log("上传成功...")
        this.showFileList = true;
        this.fileList.pop();
        this.fileList.push({name: file.name, url: this.QiniuData.host+this.QiniuData.key });
        console.log("fileList",this.fileList);
        this.emitInput(this.fileList[0].url);
      }
    }
  }
</script>
<style>

</style>


