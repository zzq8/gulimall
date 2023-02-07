<template>
  <div>
    <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>
    <el-tree :data="menus" :props="defaultProps" :expand-on-click-node="false" show-checkbox node-key="catId"
      :default-expanded-keys="expandedKey" :draggable="draggable" :allow-drop="allowDrop" @node-drop="handleDrop"
      ref="menuTree">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button v-if="node.level <= 2" type="text" size="mini" @click="append(data)">
            Append
          </el-button>
          <el-button type="text" size="mini" @click="edit(data)">
            Edit
          </el-button>
          <el-button v-if="node.childNodes.length==0" type="text" size="mini" @click="() => remove(node, data)">
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog :title="category.catId==null?'添加':'修改'" :visible.sync="dialogFormVisible" :close-on-click-modal=false>
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input v-model="category.productUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="category.catId==null?addCategory():update()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        maxLevel: 0,
        draggable: false,
        pCid: [],
        updateNodes: [],
        category: {
          catId: null,
          name: "",
          parentCid: 0,
          catLevel: 0,
          showStatus: 1,
          sort: 0,
          icon: null,
          productUnit: null
        },
        dialogFormVisible: false,
        menus: [],
        expandedKey: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      };
    },
    methods: {

      batchDelete() {
        let catIds = [];
        let checkedNodes = this.$refs.menuTree.getCheckedNodes();
        console.log(checkedNodes);
        for (let i = 0; i < checkedNodes.length; i++) {
          catIds.push(checkedNodes[i].catId);
        }
        this.$confirm(`是否删除【${catIds}】菜单?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/product/category/delete'),
            method: 'post',
            data: this.$http.adornData(catIds, false)
          }).then(({
            data
          }) => {
            this.$message({
              type: 'success',
              message: '菜单批量删除成功!'
            });

            this.getMenus();
          });
        }).catch(() => {

        });

      },

      allowDrop(draggingNode, dropNode, type) {
        //1、被拖动的当前节点以及所在的父节点总层数不能大于3

        //1）、被拖动的当前节点总层数
        console.log("allowDrop:", draggingNode, dropNode, type);
        //
        this.countNodeLevel(draggingNode);
        //当前正在拖动的节点+父节点所在的深度不大于3即可
        let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
        //当前拖的这个的有几层深度
        console.log("深度：", deep);

        //   this.maxLevel
        if (type == "inner") {
          // console.log(
          //   `this.maxLevel：${this.maxLevel}；draggingNode.data.catLevel：${draggingNode.data.catLevel}；dropNode.level：${dropNode.level}`
          // );
          return deep + dropNode.level <= 3;
        } else {
          return deep + dropNode.parent.level <= 3;
        }
      },
      countNodeLevel(node) {
        //找到所有子节点，求出最大深度
        if (node.childNodes != null && node.childNodes.length > 0) {
          for (let i = 0; i < node.childNodes.length; i++) {
            if (node.childNodes[i].level > this.maxLevel) {
              this.maxLevel = node.childNodes[i].level;
            }
            this.countNodeLevel(node.childNodes[i]);
          }
        }
      },

      handleDrop(draggingNode, dropNode, dropType, ev) {
        console.log("handleDrop: ", draggingNode, dropNode, dropType);
        //1、当前节点最新的父节点id
        let pCid = 0;
        let siblings = null;
        if (dropType == "before" || dropType == "after") {
          pCid =
            dropNode.parent.data.catId == undefined ?
            0 :
            dropNode.parent.data.catId;
          siblings = dropNode.parent.childNodes;
        } else {
          pCid = dropNode.data.catId;
          siblings = dropNode.childNodes;
        }
        this.pCid.push(pCid);

        //2、当前拖拽节点的最新顺序，
        for (let i = 0; i < siblings.length; i++) {
          if (siblings[i].data.catId == draggingNode.data.catId) {
            //如果遍历的是当前正在拖拽的节点
            let catLevel = draggingNode.level;
            if (siblings[i].level != draggingNode.level) {
              //当前节点的层级发生变化
              catLevel = siblings[i].level;
              //修改他子节点的层级
              this.updateChildNodeLevel(siblings[i]);
            }
            this.updateNodes.push({
              catId: siblings[i].data.catId,
              sort: i,
              parentCid: pCid,
              catLevel: catLevel
            });
          } else {
            this.updateNodes.push({
              catId: siblings[i].data.catId,
              sort: i
            });
          }
        }

        //3、当前拖拽节点的最新层级
        console.log("updateNodes", this.updateNodes);
      },

      batchSave() {
        this.$http({
          url: this.$http.adornUrl('/product/category/update/sort'),
          method: 'post',
          data: this.$http.adornData(this.updateNodes, false)
        }).then(({
          data
        }) => {
          this.$message({
            type: 'success',
            message: '菜单顺序等修改成功!'
          });
          //刷新菜单
          this.getMenus();
          this.expandedKey = this.pCid;

          this.updateNodes = [];
        });
      },

      updateChildNodeLevel(node) {
        if (node.childNodes.length > 0) {
          for (let i = 0; i < node.childNodes.length; i++) {
            var cNode = node.childNodes[i].data;
            this.updateNodes.push({
              catId: cNode.catId,
              catLevel: node.childNodes[i].level
            });
            this.updateChildNodeLevel(node.childNodes[i]);
          }
        }
      },

      edit(data) {
        //拉取要修改的这一条的最新数据，回显
        this.$http({
          url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
          method: 'get',
        }).then(({
          data
        }) => {
          console.log("要回显的数据", data);
          this.dialogFormVisible = true;
          this.category.name = data.category.name;
          this.category.catId = data.category.catId;
          this.category.icon = data.category.icon;
          this.category.productUnit = data.category.productUnit;
          this.category.parentCid = data.category.parentCid;
        })
      },

      update() {
        //修改
        let {
          catId,
          name,
          icon,
          productUnit
        } = this.category;
        this.$http({
          url: this.$http.adornUrl('/product/category/update'),
          method: 'post',
          data: this.$http.adornData({
            catId,
            name,
            icon,
            productUnit
          }, false)
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false;
          this.getMenus();
          this.expandedKey = [this.category.parentCid];
        });
      },

      getMenus() {
        this.$http({
          url: this.$http.adornUrl('/product/category/list/tree'),
          method: 'get',
        }).then(
          ({
            data
          }) => {
            console.log("成功获取到菜单数据...", data.page);
            this.menus = data.data;
          }
        )
      },

      //   getMenusByVueAxios() {
      //     this.axios.get('/product/category/list/tree').then(
      //       ({
      //         data
      //       }) => {
      //         console.log("成功获取到菜单数据...", data.page);
      //         this.menus = data.page;
      //       }
      //     )
      //   },
      append(data) {
        this.dialogFormVisible = true;
        this.category.parentCid = data.catId;
        this.category.catLevel = data.catLevel + 1;
        this.category.catId = null;
        this.category.name = "";
        this.category.icon = "";
        this.category.productUnit = "";

        console.log(data);
      },

      //添加三级分类
      addCategory() {
        console.log("提交的三级分类数据", this.category);
        this.$http({
          url: this.$http.adornUrl('/product/category/save'),
          method: 'post',
          data: this.$http.adornData(this.category, false)
        }).then(({
          data
        }) => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });

          this.dialogFormVisible = false;
          this.getMenus();
          this.expandedKey = [this.category.parentCid];
        });
      },

      remove(node, data) {
        console.log(node, data);
        var ids = [data.catId];
        this.$confirm(`是否删除【${data.name}】菜单?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })

          .then(() => {
            this.$http({
              url: this.$http.adornUrl('/product/category/delete'),
              method: 'post',
              data: this.$http.adornData(ids, false)
            }).then(({
              data
            }) => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              //刷新的原因：如果多个人操作了这个页面呢，不再请求一次页面数据怎么是最新的？
              this.getMenus();
              this.expandedKey = [node.parent.data.catId];

            });
          })

          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });

      },

    },
    created() {
      this.getMenus();
      // this.getMenusByVueAxios();  //没有成功。。因该是两者this.$http 和 axios 不能同时存在
    },
  };

</script>
<style>

</style>
