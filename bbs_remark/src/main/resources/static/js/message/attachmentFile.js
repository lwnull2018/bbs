/**
 * 附件管理
 */
var pageCurr;
var form;
var choose_file_flag = false; // 是否有提交文件
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#fileList',
            url:'/attachmentFileManage/fileList',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            page: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize
            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'totals', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type:'numbers'}
                ,{field:'id', title:'id',align:'center'}
                ,{field:'fileName', title:'文件名',align:'center'}
                ,{field:'createDate', title: '创建时间',align:'center', templet:"<div>{{layui.util.toDateString(d.createDate, 'yyyy-MM-dd HH:mm:ss')}}</div> "}
                ,{title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(fileTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delFile(data,data.id,data.fileName);
            } else if(obj.event === 'edit'){
                //编辑
                openFile(data,"编辑");
            }
        });

    });

    //搜索框
    layui.use(['form','laydate', 'upload'], function(){
        var form = layui.form ,layer = layui.layer
            ,upload = layui.upload;

        //监听提交
        /*form.on('submit(uploadFile)', function(data){
            uploadSubmit(data);
            return false;
        });*/

        //监听提交
        form.on('submit(formSubmit)', function(data){
            formSubmit(data);
            return false;
        });

        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });

        form.verify({
           isUpload: function (value, item) {
               if (choose_file_flag===false) {
                   return '请选择文件';
               }
           }
        });

        //选完文件后不自动上传
        upload.render({
            elem: '#fileContent'
            ,url: '/attachmentFileManage/uploadFile' //此处配置你自己的上传接口即可
            ,auto: false
            ,accept: 'file'
            //,multiple: true
            ,bindAction: '#uploadFile'
            ,choose: function (obj) {
                console.log('choose......');
                obj.preview(function (index, file, result) {
                    if(file.name.length > 0) {
                        choose_file_flag = true;
                    }
                });
            }
            ,before: function(obj){
                console.log('before......');
                obj.preview(function (index, file, result) {
                    if(file.name.length > 0) {
                        choose_file_flag = true;
                    }
                });
                layer.load(1, {
                    content: '添加中...',
                    success: function (layero) {
                        layero.find('.layui-layer-content').css({
                            'padding-top': '39px',
                            'width': '60px'
                        });
                    }
                });
                this.data={
                    "fileName":$("#fileName").val()
                };
            }
            ,done: function(res){
                console.log('done......');
                layer.closeAll('loading');
                layer.msg('上传成功');
                console.log(res)
            }
            ,error: function(res){
                console.log('error......');
                layer.closeAll('loading');
                console.log(res);
                //请求异常回调
                console.log('文件上传发生异常。。。。。。')
            }
        });

    });

});

//提交表单
function formSubmit(obj){
    let formData = {
        "id": $("#id").val(),
        "fileName": $("#fileName").val()
    }
    $.ajax({
        type: "POST",
        data: formData,
        url: "/attachmentFileManage/update",
        success: function (data) {
            console.log('data='+data);
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load();
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                load();
            });
        }
    });
}

//开通用户
function addFile(){
    openFile(null,"新建上传文件");
}
function openFile(data,title){
    if(data==null || data==""){
        $("#id").val("");

        $("#fileDiv").removeClass("layui-hide");
        $("#uploadFile").removeClass("layui-hide");
        $("#formSubmit").addClass("layui-hide");

        choose_file_flag = false;
    }else{
        $("#id").val(data.id);
        $("#fileName").val(data.fileName);

        $("#fileDiv").addClass("layui-hide");
        $("#uploadFile").addClass("layui-hide");
        $("#formSubmit").removeClass("layui-hide");

        choose_file_flag = true;
    }
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['700px'],
        content:$('#setFile'),
        end:function(){
            cleanFileForm();
        }
    });
}

function cleanFileForm(){
    $("#fileName").val("");
}

function load(obj){
    let queryData = null;
    if (obj && obj.field) {
        queryData = obj.field;
    }
    //重新加载table
    tableIns.reload({
        where: queryData
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function delFile(obj,id,name) {
    layer.confirm('删除后不可恢复，您确定要删除'+name+'文件吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/attachmentFileManage/del",{"id":id},function(data){
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load(obj);
                });
            } else {
                layer.alert(data.msg);
            }
        });
    }, function(){
        layer.closeAll();
    });
}