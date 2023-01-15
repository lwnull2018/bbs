/**
 * 消息模板管理
 */
var pageCurr;
var form;
var choose_file_flag = false; // 是否有提交文件
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#messageTemplateList',
            url:'/messageTemplateManage/messageTemplateList',
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
                ,{field:'id', title:'id',align:'center', width:'100'}
                ,{field:'beginChatId', title:'开始用户ID',align:'center', width: '180'}
                ,{field:'endChatId', title:'结束用户ID',align:'center', width: '180'}
                ,{field:'templateContent', title:'模板内容',align:'center', width: '450'}
                ,{field:'createDate', title: '创建时间',align:'center', width: '200'}
                ,{title:'操作',align:'center', toolbar:'#optBar', width: '200'}
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
        table.on('tool(messageTemplateTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delMessage(data,data.id);
            } else if(obj.event === 'edit'){
                //编辑
                openMessage(data,"编辑");
            } else if(obj.event === 'send'){
                //发送
                sendMessage(data,data.id);
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

        for (let i=1;i<5;i++){
            form.on('select(contentTypeSelect-'+i+')', function (data){
                if (data.value==='Text') {
                    $("#fileDiv-"+i).addClass("layui-hide");
                    $("#textContentDiv-"+i).removeClass("layui-hide");
                } else {
                    $("#fileDiv-"+i).removeClass("layui-hide");
                    $("#textContentDiv-"+i).addClass("layui-hide");
                }
                console.log(data.value); //得到被选中的值
            });
        }

        //监听提交
        form.on('submit(formSubmit)', function(data){
            formSubmit(data);
            return false;
        });

        //监听提交
        form.on('submit(sendSubmit)', function(data){
            sendSubmit(data);
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

        loadFileList(form);
    });

});

/**
 * 加载附件列表
 * @param form
 */
function loadFileList(form) {
    $.ajax({
        type: "POST",
        data: null,
        url: "/attachmentFileManage/files",
        success: function (data) {
            if (data.code == 1) {
                attachmentFileList = data.data;

                let optionHtml = '<option value="">请选择</option>';
                $.each(attachmentFileList, function (index, item) {
                    optionHtml += '<option value = "' + item.id + '">' + item.fileName + '</option>'
                    console.log('id='+item.id+',fileName='+item.fileName);
                });

                $("#fileContent-1").html(optionHtml);
                $("#fileContent-2").html(optionHtml);
                $("#fileContent-3").html(optionHtml);
                $("#fileContent-4").html(optionHtml);
                form.render('select');//需要渲染一下
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "POST",
        data: $("#msgForm").serialize(),
        url: "/messageTemplateManage/save",
        success: function (data) {
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
function createMessageTemplate(){
    openMessage(null,"新建消息模板");
}

function openMessage(data,title){
    if(data==null || data=="") {
        $("#id").val("0");
    } else {
        $("#id").val(data.id);
    }

    for (let i=1;i<5;i++){
        form.on('select(contentTypeSelect-'+i+')', function (data){
            if (data.value==='Text') {
                $("#fileDiv-"+i).addClass("layui-hide");
                $("#textContentDiv-"+i).removeClass("layui-hide");
            } else {
                $("#fileDiv-"+i).removeClass("layui-hide");
                $("#textContentDiv-"+i).addClass("layui-hide");
            }
            console.log(data.value); //得到被选中的值
        });
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
        content:$('#setMessage'),
        end:function(){
            cleanForm();
        }
    });
}

function cleanForm(){
    $("#beginChatId").val("");
    $("#endChatId").val("");

    for (let i=1;i<5;i++){
        // $("#contentTypeSelect-"+i).val("");
        $("#textContent-"+i).val("");
        $("#fileContent-"+i).val("");
    }
    form.render("select");
    console.log('表单清空完毕');
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

function sendMessage(obj,id) {
    /*layer.confirm('您确定要使用该消息模板发消息吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/messageTemplateManage/send",{"id":id},function(data){
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
    */
    openSend(obj, '发送消息');
}

function openSend(data,title){
    if(data==null || data=="") {
        $("#templateId").val("0");
    } else {
        $("#templateId").val(data.id);
    }

    $.post("/botManager/avaiableList",{},function(data) {
        if (data.code == 1) {
            console.log('data = ' + data.data);
            let botList = data.data;

            let optionHtml = '<option value="">请选择</option>';
            $.each(botList, function (index, item) {
                optionHtml += '<option value = "' + item.id + '">' + item.username + '</option>'
                console.log('id='+item.id+',username='+item.username);
            });

            $("#botList").html(optionHtml);
            form.render('select');//需要渲染一下
        } else {
            layer.alert(data.msg);
        }
    });

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['500px', '320px'],
        content:$('#sendDialog'),
        end:function(){
            //cleanForm();
        }
    });
}

//提交表单
function sendSubmit(obj){

    console.log('templateId = ' + $("#templateId").val());
    console.log('botList = ' + $("#botList").val());

    let formData = {
        'templateId': $("#templateId").val(),
        'botId': $("#botList").val()
    }

    $.ajax({
        type: "POST",
        data: formData,
        url: "/messageTemplateManage/sendMessage",
        success: function (data) {
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

function delMessage(obj,id) {
    layer.confirm('删除后不可恢复，您确定要删除该消息模板吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/messageTemplateManage/del",{"id":id},function(data){
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