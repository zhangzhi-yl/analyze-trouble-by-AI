<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<title>MOM系统</title>
	<!-- HTML5 Shim and Respond.js IE10 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 10]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
	<!-- Meta -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="author" content="YuanYes QQ356703572" />

	<link rel="icon" href="../../../assets/images/favicon.ico" type="image/x-icon">
	<link rel="stylesheet" href="../../../assets/fonts/material/css/materialdesignicons.min.css">
	<link rel="stylesheet" href="../../../assets/fonts/fontawesome/css/fontawesome-all.min.css">
	<link rel="stylesheet" href="../../../assets/plugins/animation/css/animate.min.css">
	<link rel="stylesheet" href="../../../assets/css/style.css">
	<!--引入 element-ui 的样式-->
	<link rel="stylesheet" type="text/css" href="../../../assets/element-ui/lib/theme-chalk/index.css">
	<!-- vue -->
	<script src="../../../assets/js-vue/vue.js"></script>
	<!-- 引入element 的组件库-->
	<script src="../../../assets/element-ui/lib/index.js"></script>
	<!--全局配置-->
	<script src="../../../assets/js-v/config.js"></script>
	<style>
	  .el-form-item{
	    margin-bottom:0
	  }
	</style>
</head>

<body>
<div class="pcoded-wrapper" id="app" v-loading.fullscreen.lock="loading">
	<template>
		<div class="pcoded-content">
			<div class="pcoded-inner-content">
				<div class="main-body">
					<div class="page-wrapper">
						<div class="row">
							<div class="col-xl-12">
								<el-form ref="form" label-width="80px" id="showform">
									<div class="card">
										<div style="padding-left: 20px;padding-top: 15px;">
											<el-row>
												<el-col :span="5">
													<el-form-item label="关键词">
														<el-input size="small" @keyup.enter.native="getList"
																  style="width: 200px; margin-right: 10px"
																  class="filter-item" placeholder="这里输入关键词"
																  v-model="KEYWORDS">
														</el-input>
													</el-form-item>
												</el-col>
												<el-col :span="6" style="padding-top: 3px;">
													<el-button size="small" class="filter-item" type="primary"
															   icon="el-icon-search" @click="getList">搜索</el-button>
													<el-button size="small" class="filter-item" type="info"
															   icon="el-icon-download" @click="goExcel">导出</el-button>
													<el-button size="small" class="filter-item" type="warning"
															   icon="search" @click="handleEmpty">清空</el-button>
													<el-button size="small" v-show="add" class="filter-item" type="success"
															   icon="edit" @click="goAdd">添加</el-button>
												</el-col>
											</el-row>
										</div>
										<!-- 检索  -->
										<div class="card-block table-border-style" style="margin-top: 10px">
											<el-table :key="tableKey" :data="varList" v-loading.body="loading"
													  size="small" stripe max-height="600" border highlight-current-row
													  style="width: 100%">
												<#list fieldList as var>
													<el-table-column align="center" label="${var[2]}">
														<template slot-scope="scope">
															<#if var[1]=='String' && var[7] !='null'>
																<span>{{ scope.row.DNAME${var_index+1 }}</span>
															<#else>
																<span>{{scope.row.${var[0]}}}</span>
															</#if>
														</template>
													</el-table-column>
												</#list>
												<el-table-column fixed="right" width="300px" align="center"
																 label="操作">
													<template slot-scope="scope">
														<el-button size="mini" type="primary"
																   @click="goSee(scope.row.${objectNameUpper}_ID)">查看
														</el-button>
														<el-button v-show="edit" size="mini" type="success"
																   @click="goEdit(scope.row.${objectNameUpper}_ID)">编辑
														</el-button>
														<el-button v-show="del" size="mini" type="danger"
																   @click="goDel(scope.row.${objectNameUpper}_ID)">删除
														</el-button>
													</template>
												</el-table-column>
											</el-table>
											<div class="page">
												<div v-show="!loading" class="pagination-container">
													<el-pagination
															style="float: right;padding-top: 10px;margin-top: 0px;"
															@size-change="changeCount" @current-change="toTZ"
															:current-page="1" background :page-sizes="[10, 20, 30, 50]"
															:page-size="page.showCount"
															layout="total, prev, pager, next, sizes, jumper"
															:total="page.totalResult">
													</el-pagination>
												</div>
											</div>
										</div>
									</div>
								</el-form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</template>
</div>

<script type="text/javascript" src="../../../assets/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="../../../assets/js/pre-loader.js"></script>
<script src="../../../assets/plugins/sweetalert/js/sweetalert.min.js"></script>
<!-- 表单验证提示 -->
<script src="../../../assets/js/jquery.tips.js"></script>

<script type="text/javascript">

	var vm = new Vue({
		el: '#app',

		data: {
			varList: [],	//list
			tableKey: 0,
			page: [],		//分页类
			KEYWORDS: '',	//检索条件,关键词
			showCount: -1,	//每页显示条数(这个是系统设置里面配置的，初始为-1即可，固定此写法)
			currentPage: 1,	//当前页码
			add: false,		//增
			del: false,		//删
			edit: false,		//改
			toExcel: false,	//导出到excel权限
			loading: false	//加载状态
		},

		methods: {

			//初始执行
			init() {
				//复选框控制全选,全不选
				$('#zcheckbox').click(function () {
					if ($(this).is(':checked')) {
						$("input[name='ids']").click();
					} else {
						$("input[name='ids']").attr("checked", false);
					}
				});
				this.getList();
			},

			//获取列表
			getList: function () {
				this.loading = true;
				$.ajax({
					xhrFields: {
						withCredentials: true
					},
					type: "POST",
					url: httpurl + '${objectNameLower}/list?showCount=' + this.showCount + '&currentPage=' + this.currentPage,
					data: { KEYWORDS: this.KEYWORDS, tm: new Date().getTime() },
					dataType: "json",
					success: (data) => {
						if ("success" == data.result) {
							vm.varList = data.varList;
							vm.page = data.page;
							vm.hasButton();
							vm.loading = false;
							$("input[name='ids']").attr("checked", false);
						} else if ("exception" == data.result) {
							showException("${TITLE}", data.exception);//显示异常
							vm.loading = false;
						}
					}
				}).done().fail(function () {
					swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
					setTimeout(function () {
						window.location.href = "../../login.html";
					}, 2000);
				});
			},

			//清空
			handleEmpty: function () {
				this.KEYWORDS = undefined;
				this.getList();
			},

			//新增
			goAdd: function () {
				var diag = new top.Dialog();
				diag.Drag = false;
				diag.Title = "新增";
				diag.URL = '../../${packageName}/${objectNameLower}/${objectNameLower}_edit.html';
				diag.Width = 1000;
				diag.Height = 800;
				diag.Modal = true;				//有无遮罩窗口
				diag.ShowMaxButton = false;	//最大化按钮
				diag.ShowMinButton = false;		//最小化按钮
				diag.CancelEvent = function () { //关闭事件
					var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
					if (varSon != null && varSon.style.display == 'none') {
						vm.getList();
					}
					diag.close();
				};
				diag.show();
			},

			//修改
			goEdit: function (id) {
				var diag = new top.Dialog();
				diag.Drag = false;
				diag.Title = "编辑";
				diag.URL = '../../${packageName}/${objectNameLower}/${objectNameLower}_edit.html?FID=' + id;
				diag.Width = 1000;
				diag.Height = 800;
				diag.Modal = true;				//有无遮罩窗口
				diag.ShowMaxButton = false;	//最大化按钮
				diag.ShowMinButton = false;		//最小化按钮
				diag.CancelEvent = function () { //关闭事件
					var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
					if (varSon != null && varSon.style.display == 'none') {
						vm.getList();
					}
					diag.close();
				};
				diag.show();
			},

			//查看
			goSee: function (id) {
				var diag = new top.Dialog();
				diag.Drag = false;
				diag.Title = "编辑";
				diag.URL = '../../${packageName}/${objectNameLower}/${objectNameLower}_look.html?FID=' + id;
				diag.Width = 1000;
				diag.Height = 800;
				diag.Modal = true;				//有无遮罩窗口
				diag.ShowMaxButton = false;	//最大化按钮
				diag.ShowMinButton = false;		//最小化按钮
				diag.CancelEvent = function () { //关闭事件
					var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
					if (varSon != null && varSon.style.display == 'none') {
						vm.getList();
					}
					diag.close();
				};
				diag.show();
			},

			//删除
			goDel: function (id) {
				swal({
					title: '',
					text: "确定要删除吗?",
					icon: "warning",
					buttons: true,
					dangerMode: true,
				}).then((willDelete) => {
					if (willDelete) {
						this.loading = true;
						$.ajax({
							xhrFields: {
								withCredentials: true
							},
							type: "POST",
							url: httpurl + '${objectNameLower}/delete',
							data: { ${ objectNameUpper }_ID: id, tm: new Date().getTime()},
							dataType: 'json',
							success: (data) => {
								if ("success" == data.result) {
									this.loading = false
									this.$message.success("删除成功");
								}
								vm.getList();
							}
						});
					}
				});
			},

			//批量操作
			makeAll: function (msg) {
				swal({
					title: '',
					text: msg,
					icon: "warning",
					buttons: true,
					dangerMode: true,
				}).then((willDelete) => {
					if (willDelete) {
						var str = '';
						for (var i = 0; i < document.getElementsByName('ids').length; i++) {
							if (document.getElementsByName('ids')[i].checked) {
								if (str == '') str += document.getElementsByName('ids')[i].value;
								else str += ',' + document.getElementsByName('ids')[i].value;
							}
						}
						if (str == '') {
							$("#cts").tips({
								side: 2,
								msg: '点这里全选',
								bg: '#AE81FF',
								time: 3
							});
							swal("", "您没有选择任何内容!", "warning");
							return;
						} else {
							if (msg == '确定要删除选中的数据吗?') {
								this.loading = true;
								$.ajax({
									xhrFields: {
										withCredentials: true
									},
									type: "POST",
									url: httpurl + '${objectNameLower}/deleteAll?tm=' + new Date().getTime(),
									data: { DATA_IDS: str },
									dataType: 'json',
									success: (data) => {
										if ("success" == data.result) {
											this.loading = false
											this.$message.success("删除成功");
										}
										vm.getList();
									}
								});
							}
						}
					}
				});
			},

			//判断按钮权限，用于是否显示按钮
			hasButton: function() {
				var keys = '${objectNameLower}:add,${objectNameLower}:del,${objectNameLower}:edit,toExcel';
				$.ajax({
					xhrFields: {
						withCredentials: true
					},
					type: "POST",
					url: httpurl + 'head/hasButton',
					data: { keys: keys, tm: new Date().getTime() },
					dataType: "json",
					success: function (data) {
						if ("success" == data.result) {
							vm.add = data.${ objectNameLower }yysystemadd;		//新增权限
							vm.del = data.${ objectNameLower }yysystemdel;		//删除权限
							vm.edit = data.${ objectNameLower }yysystemedit;	//修改权限
							vm.toExcel = data.toExcel;						//导出到excel权限
						} else if ("exception" == data.result) {
							showException("按钮权限", data.exception);		//显示异常
						}
					}
				})
			},

			//导出excel
			goExcel: function () {
				swal({
					title: '',
					text: '确定要导出到excel吗?',
					icon: "warning",
					buttons: true,
					dangerMode: true,
				}).then((willDelete) => {
					if (willDelete) {
						window.location.href = httpurl + '${objectNameLower}/excel';
					}
				});
			},

			//-----分页必用----start
			nextPage: function(page) {
				this.currentPage = page;
				this.getList();
			},
			changeCount: function(value) {
				console.log(value)
				// return;
				this.showCount = value;
				this.getList();
			},
			toTZ: function(value) {
				console.log(value)
				this.nextPage(value);
			}
			//-----分页必用----end

		},

		mounted(){
			this.init();
		}
	})

</script>

</body>

</html>