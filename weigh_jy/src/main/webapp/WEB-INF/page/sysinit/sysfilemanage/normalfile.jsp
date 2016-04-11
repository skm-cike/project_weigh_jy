<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.est.common.ext.util.classutil.DateUtil"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>车辆录入</title>
    <%@ include file="/include.jsp"%>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="文件上传下载">
    <!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->

</head>

<body>
<script type="text/javascript">
    Ext.onReady(function(){
        var colsType = {
            id:'fileid',
            cols:[
                {dataIndex: 'fileid', name:'fileid', hidden:true},
                {dataIndex: 'filename', name:'filename', header:'文件名',width:120},
                {dataIndex: 'fileversion', name:'fileversion', header:'文件版本'},
                {dataIndex: 'path', name:'path', header:'下载全路径',width:360},
                {dataIndex: 'remark', name:'remark', header:'备注',width:220}
            ]
        }

        var gridPanel = {
            xtype:'estlayout',
            region:'center',
            items:{
                id:'grid',
                xtype:'esteditgrid',
                storeurl:'<%=basePath%>est/sysinit/sysfilemanage/NormalFile/getSysNormalFileList',
                colstype : colsType,
                region:'center'
            }
        }


        var formcols = [
            {fieldset:'文件详细信息',id:'fileid',
                items: [
                    {fieldLabel:'文件名',name:'n_filename',allowBlank:false},
                    {fieldLabel:'备注',name:'remark'},
                    {fieldLabel:"上传文件", name:"file", cls:'file_field',inputType:"file"},
                    {name:'fileversion', hidden:true,hideLabel:true},   //文件版本
                    {name:'path', hidden:true,hideLabel:true}//文件绝对路径
                ]}];

        var formPanel = {
            xtype: 'estform',
            id: 'formPanel',
            region:'east',
            title:'详细信息',
            width:350,
            colnum:1,
            fileUpload : true,
            formurl: '<%=basePath%>est/sysinit/sysfilemanage/NormalFile',
            method: 'SysNormalFile',
            colstype: formcols,
            tbar:[
                {text:'重置/增加',iconCls:'page_add', handler:function(){
                    Ext.getCmp('formPanel').doReset();
                }},
                {text:'保存',iconCls:'page_save', handler: function() {
                    Ext.Msg.confirm('提示', '你确定要增加或修改吗?', function(btn) {
                        if (btn == 'yes') {
                            Ext.getCmp('formPanel').doSumbit({success:function(form, rep){
                                Ext.getCmp('grid').store.reload();
                            },failure:function() {error('错误','保存失败~!')}})
                        }
                    });
                }},
                {text:'删除',iconCls:'page_gear', handler: function() {
                    Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
                        if (btn == 'yes') {
                            Ext.getCmp('formPanel').doDelete({
                                success:function(form, rep) {
                                    Ext.getCmp('grid').store.reload();
                                }
                            });
                        }
                    });
                }}
            ]
        }

        var tpanel = {
            xtype : 'estlayout',
            layout:'border',
            region : 'center',
            items : [gridPanel, formPanel]
        }
        new Ext.Viewport({
            layout:'border',
            items: [menuPanel,tpanel]
        });
        Ext.getCmp('grid').on({
            'rowclick': function(t, i, e) {
                var fileid = t.getSelectionModel().getSelected().data['fileid'];
                Ext.getCmp('formPanel').doLoad({fileid:fileid});
            }
        });
    })

</script>
</body>
</html>
