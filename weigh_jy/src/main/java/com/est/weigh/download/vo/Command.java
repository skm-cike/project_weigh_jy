package com.est.weigh.download.vo;

public interface Command {
	String AGAINDOWN = "AGAINDOWN";  //完全重新下载
	String DOWNLOAD = "DOWNLOAD";    //下载新增的和修改的
	String DELLOAD = "DELLOAD";      //获取要删除的信息
	//String UPLOAD = "UPLOAD";        //上传
}
