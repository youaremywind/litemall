<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="generateContract">生成合同</el-button>
    </div>

    <div style="text-align:left;margin:15px 0 15px 10px">
      <span style="display:inline-block;width:70px">姓名：</span>
      <el-input v-model="name" size="mini" style="width:200px" clearable />
      <span style="display:inline-block;margin-left:20px;width:70px">电话：</span>
      <el-input v-model="telephone" size="mini" style="width:200px" clearable @input="xxtelephone()" />
      <span>{{ telMessage }}</span>
    </div>
    <div class="header">
      <span style="width:200px">课程名字</span>
      <span style="width:50px">单价</span>
      <span style="width:160px">数量</span>
      <span style="width:100px">金额</span>
      <span style="width:100px">操作</span>
    </div>
    <div class="content">
      <div v-for="(item,index) in tableData" :key="index" class="item">
        <span style="width:200px">{{ item.courseName }}</span>
        <span style="width:50px">{{ item.coursePrices }}元</span>
        <span style="width:160px">
          <el-input-number v-model.number="item.courseAmounts" :min="1" size="mini" style="width:100px" clearable />课时
        </span>
        <span style="width:100px">{{ item.coursePrices * item.courseAmounts }}元</span>
        <span style="width:100px" @click="del(index)">删除</span>
      </div>
      <div v-if="tableData.length == 0" style="height:48px;line-height:48px">暂无添加课程</div>
      <div class="item">
        <span style="width:200px">常规赠送</span>
        <span style="width:50px">0元</span>
        <span style="width:160px">
          <el-input-number v-model.number="addAmounts" size="mini" style="width:100px" clearable />课时
        </span>
        <span style="width:100px">0元</span>
        <span style="width:100px" />
      </div>
      <div class="item">
        <span style="width:200px">合计</span>
        <span style="width:50px">{{ averagePrice }}元</span>
        <span style="width:160px">{{ allAmounts }}课时</span>
        <span style="width:100px">{{ total }}元</span>
        <span style="width:100px" />
      </div>
    </div>
    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="modal_add" :rules="rules" :model="modal_add" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="课程名称" prop="courseName">
          <el-select v-model="modal_add.courseName" @change="changeCourseName">
            <el-option v-for="item in catL1" :key="item.courseName" :label="item.courseName" :value="item.courseName" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程单价" prop="coursePrice">
          <el-input v-model="modal_add.coursePrice" :value="modal_add.coursePrice" :disabled="true" />
        </el-form-item>
        <el-form-item label="课程数量" prop="courseAmounts">
          <el-input-number v-model="modal_add.courseAmounts" :min="1" :value="modal_add.courseAmounts" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <!-- <el-button v-else type="primary" @click="updateData">确定</el-button> -->
      </div>
    </el-dialog>

  </div>
</template>

<style scoped>
/* .filter-item{
  margin-left: 100px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 145px;
  height: 145px;
  display: block;
} */

.header {
  height: 48px;
  overflow: hidden;
  width: 100%;
  font-size: 14px;
  color: #606266;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgba(#909399, 0.2);
}
.header .span {
  display: inline-block;
    padding-left : 10px ;
    padding-right: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: left;
    white-space: normal;
    word-break: break-all;
    line-height: 48px;
    color : #909399;
    font-weight: bold;
    cursor: default;
}
.content .item{
    height: 48px;
    overflow: hidden;
    width: 100%;
    font-size: 14px;
    color: #606266;
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid rgba(#909399,0.2);
  }
  /* .content .item :hover{
    background-color: #f5f7fa;
    } */
  .content .span{
      display: inline-block;
      padding-left: 10px;
      padding-right: 10px;
      overflow: hidden;
      text-overflow: ellipsis;
      text-align: left;
      white-space: normal;
      word-break: break-all;
      line-height: 48px;
      cursor: default;
    }

</style>

<script>
import { publishContracts } from '@/api/contract'
import { MessageBox } from 'element-ui'
import { getToken } from '@/utils/auth'

export default {
  name: 'Contracrt',
  data() {
    return {
      // uploadPath,
      // list: [],
      // listLoading: true,
      catL1: [
        {
          courseName: '高二常规英语',
          coursePrice: '297.5'
        },
        {
          courseName: '高二常规数学',
          coursePrice: '297.5'
        },
        {
          courseName: '高二常规语文',
          coursePrice: '297.5'
        },
        {
          courseName: '高一常规英语',
          coursePrice: '280.5'
        },
        {
          courseName: '高一常规数学',
          coursePrice: '280.5'
        },
        {
          courseName: '高一常规语文',
          coursePrice: '280.5'
        },
        {
          courseName: '小三常规英语',
          coursePrice: '170	'
        },
        {
          courseName: '小三常规数学',
          coursePrice: '170	'
        },
        {
          courseName: '小三常规语文',
          coursePrice: '170	'
        },
        {
          courseName: '小四常规英语',
          coursePrice: '170	'
        },
        {
          courseName: '小四常规数学',
          coursePrice: '170	'
        },
        {
          courseName: '小四常规语文',
          coursePrice: '170	'
        },
        {
          courseName: '小五常规英语',
          coursePrice: '178.5'
        },
        {
          courseName: '小五常规数学',
          coursePrice: '178.5'
        },
        {
          courseName: '小五常规语文',
          coursePrice: '178.5'
        },
        {
          courseName: '小六常规英语',
          coursePrice: '187	'
        },
        {
          courseName: '小六常规数学',
          coursePrice: '187	'
        },
        {
          courseName: '小六常规语文',
          coursePrice: '187	'
        },
        {
          courseName: '初三常规科学',
          coursePrice: '212.5'
        },
        {
          courseName: '初三常规英语',
          coursePrice: '212.5'
        },
        {
          courseName: '初三常规语文',
          coursePrice: '212.5'
        },
        {
          courseName: '初三常规数学',
          coursePrice: '212.5'
        },
        {
          courseName: '初二常规英语',
          coursePrice: '204	'
        },
        {
          courseName: '初二常规科学',
          coursePrice: '204	'
        },
        {
          courseName: '初二常规语文',
          coursePrice: '204	'
        },
        {
          courseName: '初二常规数学',
          coursePrice: '204	'
        },
        {
          courseName: '初一常规英语',
          coursePrice: '195.5'
        },
        {
          courseName: '初一常规科学',
          coursePrice: '195.5'
        },
        {
          courseName: '初一常规语文',
          coursePrice: '195.5'
        },
        {
          courseName: '初一常规数学',
          coursePrice: '195.5'
        }
      ],
      telMessage: null,
      name: null,
      telephone: null,
      tableData: [
      ],
      addAmounts: 0,
      modal_add: {
        courseName: null,
        coursePrices: null,
        courseAmounts: null
      },
      // dataForm: {
      //   id: undefined,
      //   name: '',
      //   keywords: '',
      //   level: 'L2',
      //   pid: 0,
      //   desc: '',
      //   iconUrl: '',
      //   picUrl: ''
      // },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        courseAmounts: [
          { required: true, message: '数量不能为空', trigger: 'blur' },
          { pattern: /^\+?[1-9][0-9]*$/, message: '必须大于零' }
        ],
        courseName: [
          { required: true, message: '课程名称不能为空', trigger: 'blur' }
        ],
        coursePrice: [
          { required: true, message: '课程单价不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '名字不能为空', trigger: 'blur' }
        ],
        telephone: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    },
    averagePrice() {
      if (this.tableData.length === 0) return 0
      var sum = 0
      var freeNum = 0
      this.tableData.forEach(item => {
        sum += item.coursePrices
        if (item.coursePrices === 0) {
          freeNum++
        }
      })
      return Math.round(sum / (this.tableData.length - freeNum) * 10) / 10
    },
    allAmounts() {
      var sum = 0
      this.tableData.forEach(item => {
        if (!item.courseAmounts) item.courseAmounts = 0
        sum += item.courseAmounts
      })
      sum += this.addAmounts
      return sum
    },
    total() {
      var sum = 0
      this.tableData.forEach(item => {
        sum += item.coursePrices * item.courseAmounts
      })
      return sum
    }
  },
  created() {
    // this.getList()
    // this.getCatL1()
    console.log('loading......')
    this.modal_add.courseName = this.catL1[0].courseName
    this.modal_add.coursePrice = this.catL1[0].coursePrice
    console.log(this.modal_add.courseName)
    console.log(this.modal_add.coursePrice)
    // this.modal_add = {
    //   courseName: this.catL1[0].courseName,
    //   coursePrice: this.catL1[0].coursePrice
    // }
    console.log(this.modal_add)
  },
  methods: {
    changeCourseName: function(courseName) {
      console.log('change')
      console.log(courseName)
      this.catL1.forEach(item => {
        if (courseName === item.courseName) {
          this.modal_add.coursePrice = item.coursePrice
        }
      })
    },
    del(index) {
      this.tableData.splice(index, 1)
    },
    // getList() {
    //   this.listLoading = true
    //   listCategory()
    //     .then(response => {
    //       this.list = response.data.data.list
    //       this.listLoading = false
    //     })
    //     .catch(() => {
    //       this.list = []
    //       this.listLoading = false
    //     })
    // },
    // getCatL1() {
    //   listCatL1().then(response => {
    //     this.catL1 = response.data.data.list
    //     console.log(this.catL1)
    //   })
    // },
    resetForm() {
      this.modal_add = {
        courseName: null,
        coursePrices: null,
        courseAmounts: null
      }
    },
    // onLevelChange: function(value) {
    //   if (value === 'L1') {
    //     this.dataForm.pid = 0
    //   }
    // },
    handleCreate() {
      this.modal_add.courseName = this.catL1[0].courseName
      this.modal_add.coursePrice = this.catL1[0].coursePrice
      // this.modal_add = {
      //   courseName: this.catL1[0].courseName,
      //   coursePrice: this.catL1[0].coursePrice
      // }
      // this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['modal_add'].clearValidate()
      })
    },
    generateContract: function() {
      this.tableData.forEach(item => {
        item.courseTotal = item.coursePrices * item.courseAmounts
      })
      const contracts = {
        name: this.name,
        telephone: this.telephone,
        addAmounts: this.addAmounts,
        averagePrice: this.averagePrice,
        allAmounts: this.allAmounts,
        total: this.total,
        courses: this.tableData
      }
      publishContracts(contracts).then(response => {
        // this.$notify.success({
        //   title: '成功',
        //   message: '创建成功'
        // })
        window.location.href = 'https://litemall.hziee.top/files/out_example_payment_hack.docx'
        // this.$store.dispatch('tagsView/delView', this.$route)
        // this.$router.push({ path: '/goods/list' })
      }).catch(response => {
        MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
          confirmButtonText: '确定',
          type: 'error'
        })
      })
    },
    // uploadIconUrl: function(response) {
    //   this.dataForm.iconUrl = response.data.url
    // },
    // uploadPicUrl: function(response) {
    //   this.dataForm.picUrl = response.data.url
    // },
    createData() {
      this.$refs['modal_add'].validate(valid => {
        console.log(valid)
        if (valid) {
          this.tableData.push({
            courseName: this.modal_add.courseName,
            coursePrices: parseFloat(this.modal_add.coursePrice),
            courseAmounts: parseInt(this.modal_add.courseAmounts)
          })
          this.modal_add = {
            // courseName: null,
            // coursePrices: null,
            courseAmounts: null
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
      // console.log('點擊提交')
      // if (!this.modal_add.courseName) {
      //   MessageBox.alert('请选择课程', '错误', {
      //     confirmButtonText: '确定',
      //     type: 'error' })
      // }
      // console.log('请填写数量')
      // console.log(!this.modal_add.courseAmounts)
      // if (!this.modal_add.courseAmounts) {
      //   MessageBox.alert('请填写数量', '错误', {
      //     confirmButtonText: '确定',
      //     type: 'error' })
      //   this.xxAmout(this.modal_add.courseAmounts)
      // }
      // console.log('courseName')
      // console.log(this.modal_add.courseName)
      // console.log('coursePrice')
      // console.log(this.modal_add.coursePrice)
      // console.log('courseAmounts')
      // console.log(this.modal_add.courseAmounts)
      // console.log('addtableData')
      // console.log(this.tableData)
    },
    xxtelephone() {
      console.log(this.telephone)
      var reg = /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/
      if (!reg.test(this.telephone)) {
        this.telMessage = '请输入正确的手机号'
      } else {
        this.telMessage = ''
      }
    },
    xxAmout(x) {
      console.log('检验')
      console.log(x)
      var reg = /^\+?[1-9][0-9]*$/
      console.log(reg.test(x))
      if (!reg.test(x)) {
        MessageBox.alert('课程数请输入数字', '错误', {
          confirmButtonText: '确定',
          type: 'error' })
        this.addAmounts = 0
      }
    },
    xxAmout2(x, index) {
      console.log('检验')
      console.log(x)
      var reg = /^\+?[1-9][0-9]*$/
      console.log(reg.test(x))
      if (!reg.test(x)) {
        MessageBox.alert('课程数请输入数字', '错误', {
          confirmButtonText: '确定',
          type: 'error' })
        this.tableData[index].courseAmounts = 0
      }
    }
  }
}
</script>
