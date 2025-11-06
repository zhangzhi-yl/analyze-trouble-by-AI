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
	<link rel="stylesheet" href="../../../assets/fonts/fontawesome/css/fontawesome-all.min.css">
	<link rel="stylesheet" href="../../../assets/plugins/animation/css/animate.min.css">

	<!-- 日期插件 -->
	<link rel="stylesheet"
		  href="../../../assets/plugins/material-datetimepicker/css/bootstrap-material-datetimepicker.css">

	<!-- select插件 -->
	<link rel="stylesheet" href="../../../assets/plugins/select2/css/select2.min.css">
	<link rel="stylesheet" href="../../../assets/plugins/multi-select/css/multi-select.css">
	<!--引入 element-ui 的样式-->
	<link rel="stylesheet" type="text/css" href="../../../assets/element-ui/lib/theme-chalk/index.css">
	<!-- vue -->
	<script src="../../../assets/js-vue/vue.js"></script>
	<!-- 引入element 的组件库-->
	<script src="../../../assets/element-ui/lib/index.js"></script>
	<!--全局配置-->
	<script src="../../../assets/js-v/config.js"></script>

</head>
<body style="background-color: white">
<div class="pcoded-wrapper" id="app" v-loading.fullscreen.lock="loading">
	<template>
		<div class="pcoded-content">
			<div class="pcoded-inner-content">
				<div class="main-body">
					<div class="page-wrapper">
						<div class="row">
							<div style="width: 100%;padding-top:20px">
								<el-form ref="form" label-width="120px" id="showform">
									<#list fieldList as var>
										<#if var[3]=="是">
											<#if var[1]=='Date'>
												<el-row>
													<el-col :span="22">
														<el-form-item label="${var[2]}">
															<el-date-picker v-model="pd.${var[0]}" ref="${var[0]}"
																			id="${var[0]}" type="date" size="small"
																			placeholder="请选择${var[2]}">
															</el-date-picker>
														</el-form-item>
													</el-col>
												</el-row>
											<#elseif var[1]=='Integer'>
												<el-row>
													<el-col :span="22">
														<el-form-item label="${var[2]}">
															<el-input type="number" v-model="pd.${var[0]}" size="small"
																	  ref="${var[0]}" id="${var[0]}" clearable
																	  placeholder="请输入${var[2]}"></el-input>
														</el-form-item>
													</el-col>
												</el-row>
											<#elseif var[1]=='Double'>
												<el-row>
													<el-col :span="22">
														<el-form-item label="${var[2]}">
															<el-input type="number" v-model="pd.${var[0]}" size="small"
																	  ref="${var[0]}" id="${var[0]}" clearable
																	  placeholder="请输入${var[2]}"></el-input>
														</el-form-item>
													</el-col>
												</el-row>
											<#else>
												<#if var[7] !='null'>
													<el-row>
														<el-col :span="22">
															<el-form-item label="${var[2]}">
																<el-input v-model="pd.${var[0]}" size="small"
																		  ref="${var[0]}" id="${var[0]}"
																		  clearable
																		  placeholder="请输入${var[2]}">
																</el-input>
															</el-form-item>
														</el-col>
													</el-row>
												<#else>
													<el-row>
														<el-col :span="22">
															<el-form-item label="${var[2]}">
																<el-input v-model="pd.${var[0]}"
																		  ref="${var[0]}" id="${var[0]}"
																		  clearable size="small"
																		  placeholder="请输入${var[2]}">
																</el-input>
															</el-form-item>
														</el-col>
													</el-row>
												</#if>
											</#if>
										</#if>
									</#list>
								</el-form>
								<div class="input-group" style="margin-top:10px;display:flex;justify-content:center">
                                        <span style="width: 100%;text-align: center;">
                                            <el-button type="primary" @click="save"  size="small">保存</el-button>
                                            <el-button onclick="top.Dialog.close()"  size="small">取消</el-button>
                                        </span>
								</div>
							</div>

							<template v-if="msg == 'edit'">
								<div style="margin-top: 10px;width: 100%;">
									<iframe name="treeFrame" id="treeFrame" frameborder="0"
											v-bind:src="'../${objectNameLower}mx/${objectNameLower}mx_list.html?${objectNameUpper}_ID='+${objectNameUpper}_ID"
											style="margin:0 auto;width:100%;height:466px;;"></iframe>
								</div>
							</template>
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

<!-- 日期插件 -->
<script src="../../../assets/js/pages/moment-with-locales.min.js"></script>
<script src="../../../assets/plugins/material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>
<script src="../../../assets/js/pages/form-picker-custom.js"></script>

<!-- select插件 -->
<script src="../../../assets/plugins/select2/js/select2.full.min.js"></script>
<script src="../../../assets/plugins/multi-select/js/jquery.quicksearch.js"></script>
<script src="../../../assets/plugins/multi-select/js/jquery.multi-select.js"></script>
<script src="../../../assets/js/pages/form-select-custom.js"></script>

<!-- 表单验证提示 -->
<script src="../../../assets/js/jquery.tips.js"></script>

<script type="text/javascript">

	var vm = new Vue({
		el: '#app',

		data: {
			${ objectNameUpper }_ID: '',	//主键ID
			pd: [],						//存放字段参数
			<#list fieldList as var>
			<#if var[3] == "是">
			<#if var[7] != 'null'>
			${var[0]}: '',
			</#if>
			</#if >
			</#list >
			msg: 'add',
			loading: false
		},

		methods: {

			//初始执行
			init() {
				var FID = this.getUrlKey('FID');	//当接收过来的FID不为null时,表示此页面是修改进来的
				if (null != FID) {
					this.msg = 'edit';
					this.${ objectNameUpper }_ID = FID;
					this.getData();
				}
				setTimeout(function () {
					vm.getDict();
				}, 200);
			},

			//去保存
			save: function () {
				$("#showform").hide();
				this.loading = true
				//发送 post 请求提交保存
				$.ajax({
					xhrFields: {
						withCredentials: true
					},
					type: "POST",
					url: httpurl + '${objectNameLower}/' + this.msg,
					data: { ${ objectNameUpper }_ID: this.${ objectNameUpper }_ID,
						<#list fieldList as var>
						<#if var[3] == "是">
						<#if var[7] != 'null'>
						${var[0]}:this.${var[0]},
						<#else>
						${var[0]}:this.pd.${var[0]},
						</#if>
						</#if>
						</#list >
						tm: new Date().getTime()},
					dataType: "json",
					success: (data) => {
						if ("success" == data.result) {
							this.$message.success(" 保存成功")
							setTimeout(function () {
								this.loading = false
								top.Dialog.close();//关闭弹窗
							}, 1000);
						} else if ("exception" == data.result) {
							showException("${TITLE}", data.exception);//显示异常
							$("#showform").show();
							this.loading = false
						}
					}
				}).done().fail(function () {
					swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
					$("#showform").show();
					$("#jiazai").hide();
				});
			},

			//根据主键ID获取数据
			getData: function() {
				//发送 post 请求
				$.ajax({
					xhrFields: {
						withCredentials: true
					},
					type: "POST",
					url: httpurl + '${objectNameLower}/goEdit',
					data: { ${ objectNameUpper }_ID: this.${ objectNameUpper }_ID, tm: new Date().getTime()},
					dataType: "json",
					success: function(data) {
						if ("success" == data.result) {
							vm.pd = data.pd;							//参数map
							<#list fieldList as var>
							<#if var[3] == "是">
							<#if var[7] != 'null'>
							vm.${var[0]} = data.pd.${var[0]};
							<#elseif var[1] == 'Date'>
							$("#${var[0]}").val(data.pd.${var[0]});
							</#if>
							</#if >
							</#list >
						} else if ("exception" == data.result) {
							showException("${TITLE}", data.exception);	//显示异常
							$("#showform").show();
							$("#jiazai").hide();
						}
					}
				}).done().fail(function () {
					swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
					$("#showform").show();
					$("#jiazai").hide();
				});
			},

			//获取数据字典数据
			getDict: function () {
				<#list fieldList as var>
				<#if var[3] == "是">
				<#if var[1] == 'String'>
				<#if var[7] != 'null'>
				$.ajax({
					xhrFields: {
						withCredentials: true
					},
					type: "POST",
					url: httpurl+'dictionaries/getLevels?tm='+new Date().getTime(),
					data: {DICTIONARIES_ID:'${var[7]}'},
					dataType:'json',
					success: function(data){
						$("#${var[0]}").append("<option value=''>请选择${var[2]}</option>");
						$.each(data.list, function(i, dvar){
							if(vm.${var[0]} == dvar.BIANMA){
								$("#${var[0]}").append("<option value=" + dvar.BIANMA + " selected>" + dvar.NAME + "</option>");
							}else{
								$("#${var[0]}").append("<option value=" + dvar.BIANMA + ">" + dvar.NAME + "</option>");
							}
						});
					}
				});
				</#if>
				</#if >
				</#if >
				</#list >
			},

			//根据url参数名称获取参数值
			getUrlKey: function (name) {
				return decodeURIComponent(
						(new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null;
			}

		},

		mounted(){
			this.init();
		}
	})

</script>

</body>

</html>