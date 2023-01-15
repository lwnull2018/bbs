/**
 * 机器人管理
 */
var pageCurr;
var form;
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#botList',
            url:'/botManager/list',
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
                ,{field:'token', title:'token',align:'center'}
                ,{field:'username', title:'机器人名称',align:'center'}
                ,{field:'status', title:'状态',align:'center', templet: function (d) {
                    if(d.status === true) {
                        res = "<span style='color:green'>启用</span>"
                    } else {
                        res = "<span style='color:darkred'>停用</span>"
                    }
                    return res;
                }}
                ,{field:'remarks', title:'备注',align:'center'}
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
        table.on('tool(botTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delBot(data,data.id,data.username);
            } else if(obj.event === 'edit'){
                //编辑
                openForm(data,"编辑");
            }
        });
    });

    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer;

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
    });

});

//提交表单
function formSubmit(obj){
    let status = $("#status").prop('checked');
    let formData = {
        "id": $("#id").val(),
        "token": $("#token").val(),
        "username": $("#username").val(),
        "status": status,
        "remarks": $("#remarks").val()
    }
    $.ajax({
        type: "POST",
        data: formData,
        url: "/botManager/saveOrUpdate",
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
function createBot() {
    openForm(null,"新建机器人");
}
function openForm(data,title) {
    if (data==null || data=="") {
        $("#id").val("");
    } else {
        $("#id").val(data.id);
        $("#token").val(data.token);
        $("#username").val(data.username);
        if (data.status) {
            $("#status").attr('checked', 'checked');
        } else {
            $("#status").attr("checked", false);
        }
        $("#remarks").val(data.remarks);

        form.render();
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
        content:$('#setBot'),
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

function delBot(obj,id,name) {
    layer.confirm('删除后不可恢复，您确定要删除'+name+'机器人吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/botManager/del",{"id":id},function(data){
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