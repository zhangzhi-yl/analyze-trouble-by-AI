<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<title>MOM系统</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="author" content="YuanYes QQ356703572" />

	<link rel="icon" href="../../../assets/images/favicon.ico" type="image/x-icon">
	<link rel="stylesheet" href="../../../assets/fonts/fontawesome/css/fontawesome-all.min.css">
	<link rel="stylesheet" href="../../../assets/plugins/animation/css/animate.min.css">
	<link rel="stylesheet" href="../../../assets/css/style.css">

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
								<el-form ref="form" label-width="150px" id="showform">
									<#list fieldList as var>
										<#if var[3]=="是">
											<#if var[1]=='Date'>
												<el-row>
													<el-col :span="22">
														<el-form-item label="${var[2]}">
															<el-date-picker disabled v-model="pd.${var[0]}"
																			ref="${var[0]}" id="${var[0]}" type="date"
																			placeholder="请选择${var[2]}">
															</el-date-picker>
														</el-form-item>
													</el-col>
												</el-row>
											<#elseif var[1]=='Integer'>
												<el-row>
													<el-col :span="22">
														<el-form-item label="${var[2]}">
															<el-input disabled type="number"
																	  v-model="pd.${var[0]}" ref="${var[0]}"
																	  id="${var[0]}" clearable
																	  placeholder="请输入${var[2]}"></el-input>
														</el-form-item>
													</el-col>
												</el-row>
											<#elseif var[1]=='Double'>
												<el-row>
													<el-col :span="22">
														<el-form-item label="${var[2]}">
															<el-input disabled type="number"
																	  v-model="pd.${var[0]}" ref="${var[0]}"
																	  id="${var[0]}" clearable
																	  placeholder="请输入${var[2]}"></el-input>
														</el-form-item>
													</el-col>
												</el-row>
											<#else>
												<#if var[7] !='null'>
													<el-row>
														<el-col :span="22">
															<el-form-item label="${var[2]}">
																<el-input disabled
																		  v-model="pd.${var[0]}"
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
																		  clearable
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
								<div class="input-group" style="margin-top:10px;background-color: white">
                                        <span style="width: 100%;text-align: center;">
                                            <el-button onclick="top.Dialog.close()">关闭</el-button>
                                        </span>
								</div>
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
			${ faobject }_ID: '',			//父ID
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
					this.${ objectNameUpper } _ID = FID;
					this.getData();
				} else {
					this.${ faobject } _ID = this.getUrlKey('${faobject}_ID');
				}
				setTimeout(function () {
					vm.getDict();
				}, 200);
			},


			//根据主键ID获取数据
			getData: function() {
				//发送 post 请求
				this.loading = true
				$.ajax({
					xhrFields: {
						withCredentials: true
					},
					type: "POST",
					url: httpurl + '${objectNameLower}/goEdit',
					data: { ${ objectNameUpper }_ID: this.${ objectNameUpper }_ID, tm: new Date().getTime()},
					dataType: "json",
					success: (data) => {
						if ("success" == data.result) {
							vm.pd = data.pd;							//参数map
							this.loading = false
							<#list fieldList as var >
							<#if var[3] == "是" >
							<#if var[7] != 'null' >
							vm.${ var [0] } = data.pd.${ var [0] };
							<#elseif var[1] == 'Date' >
							$("#${var[0]}").val(data.pd.${ var[0] });
							</#if >
							</#if >
							</#list >
						} else if ("exception" == data.result) {
							showException("${TITLE}", data.exception);	//显示异常
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