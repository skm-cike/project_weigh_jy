package com.est.sysinit.sysfilemanage.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysfilemanage.vo.SysDir;
import com.est.sysinit.sysfilemanage.vo.SysFileinfo;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;

/**
 * 
 *@desc 文件管理service接口
 *@author jingpj
 *@date Jul 29, 2009
 *@path com.est.sysinit.sysfilemanage.service.ISysFileInfoService
 *@corporation Enstrong S&T
 */
public interface ISysFileInfoService {

	public SysFileinfo getSysFileInfoById(Long fileInfoId);
	
	public SysFileinfo getSysFileInfoByFileNo(String fileNo);
	
	public List<SysFileinfo> getSysFileInfoList(SearchCondition cond);
	
	public void savSysFileInfo(SysFileinfo sysFileInfo);
	
	public String savCopySysFileInfo(String fileNo,boolean isTemplateUpdate);
	
	/**
	 * @deprecated Use {@link #savUploadFile(File,SysDir,SysTemplate,String,SysFileinfo)} instead
	 */
	public SysFileinfo uploadFile(File file,SysDir dir,SysTemplate template,String fileName);


	/**
	 * @desc 上传模板文件
	 * @date Jul 30, 2009
	 * @author jingpj
	 * @param inputStream
	 *            上传的文件
	 * @param dir
	 *            存放路径
	 * @param template
	 *            报表模板，如果没有则为null
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public SysFileinfo savUploadFile(InputStream inputStream,SysDir dir,SysTemplate template,String fileName, SysFileinfo oldSysFileInfo);
	
	/**
	 *@desc 上传文件
	 *@date Sep 2, 2009
	 *@author jingpj 
	 *@param inputStream 上传的文件
	 *@param filePath 文件保存路径
	 *@param fileno 关联对象的fileno，上传的文件序列号为关联对象的fileno+3位流水号
	 *@param fileName 文件名
	 *@param oldSysFileInfo 被覆盖fileinfo对象
	 *@return 关联对象的fileno
	 */
	public String savUploadFile(InputStream inputStream, String filePath, String fileno,String saveFileName,String fileName,SysFileinfo oldSysFileInfo) ;
	
	/**
	 *@desc 根据fileno得到其关联的所有文件列表
	 *@date Sep 3, 2009
	 *@author jingpj
	 *@param fileNo
	 *@return
	 */
	public Result<SysFileinfo> getSysFileInfoListByFileNo(Page page,String fileNo);
	
	
	/**
	 *@desc 根据id删除sysfileinfo对象
	 *@date Sep 5, 2009
	 *@author jingpj
	 *@param fileinfoId
	 */
	public void delFileinfo(Long fileinfoId) ;
	
	
	
}
