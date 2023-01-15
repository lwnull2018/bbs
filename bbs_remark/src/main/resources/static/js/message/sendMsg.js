/**
 * 消息管理
 */
var pageCurr;
var form;
var file1;
$(function() {
    //搜索框
    layui.use(['form','laydate', 'upload'], function(){
        var form = layui.form ,layer = layui.layer
            ,upload = layui.upload;

        var choose_file_flag = false; // 是不否提交文件

        //绑定下拉框改变事件
        form.on('select(contentTypeSelect)', function (data){
            if (data.value==='Text') {
                $("#fileDiv").addClass("layui-hide");
                $("#textContentDiv").removeClass("layui-hide");
                $("#uploadFile").addClass("layui-hide");
                $("#formSubmit").removeClass("layui-hide");
            } else {
                $("#fileDiv").removeClass("layui-hide");
                $("#textContentDiv").addClass("layui-hide");
                $("#uploadFile").removeClass("layui-hide");
                $("#formSubmit").addClass("layui-hide");
            }
            console.log(data.value); //得到被选中的值
        });

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

        form.verify({
           isUpload: function (value, item) {
               console.log($("#contentTypeSelect").val());
               console.log(choose_file_flag);

               if ($("#contentTypeSelect").val() != 'Text' && choose_file_flag===false) {
                   return '请选择文件';
               }
           }
        });

        //选完文件后不自动上传
        upload.render({
            elem: '#fileContent'
            ,url: '/msgManage/uploadFile' //此处配置你自己的上传接口即可
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
                    "chatId":$("#chatId").val(),
                    "contentType": $("#contentTypeSelect").val(),
                    "appType": $("#appType").val(),
                    "textContent": $("#textContent").val()
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
function uploadSubmit(obj){
    console.log("uploadSubmit......");

    $.ajax({
        type: "POST",
        data: $("#msgForm").serialize(),
        url: "/msgManage/uploadFile",
        contentType: false,
        success: function (data) {
            console.log('data='+data);
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                });
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
    let formData = {
        "chatId":$("#chatId").val(),
        "contentType": $("#contentTypeSelect").val(),
        "appType": $("#appType").val(),
        "textContent": $("#textContent").val()
    };
    $.ajax({
        type: "POST",
        data: formData,
        url: "/msgManage/send",
        success: function (data) {
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load(obj);
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}

//开通用户
function addUser(){
    openUser(null,"开通用户");
}
function openUser(data,title){
    var roleId = null;
    if(data==null || data==""){
        $("#id").val("");
    }else{
        $("#id").val(data.id);
        $("#username").val(data.sysUserName);
        $("#mobile").val(data.userPhone);
        roleId = data.roleId;
    }
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);
    $.ajax({
        url:'/role/getRoles',
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                if(!roleId){
                    var option = new Option(item.roleName,item.id);
                }else {
                    var option = new Option(item.roleName,item.id);
                    // // 如果是之前的parentId则设置选中
                    if(item.id == roleId) {
                        option.setAttribute("selected",'true');
                    }
                }
                $('#roleId').append(option);//往下拉菜单里添加元素
                form.render('select'); //这个很重要
            })
        }
    });

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setUser'),
        end:function(){
            cleanUser();
        }
    });
}

function cleanUser(){
    $("#username").val("");
    $("#mobile").val("");
    $("#password").val("");
    $('#roleId').html("");
}
