package com.est.sysinit.sysfilemanage.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.businessutil.SerialNumGeneratorUtil;
import com.est.common.ext.util.fileutil.DirectoryCreateException;
import com.est.common.ext.util.fileutil.FileUtil;
import com.est.sysinit.sysfilemanage.dao.ISysFileInfoDao;
import com.est.sysinit.sysfilemanage.vo.SysDir;
import com.est.sysinit.sysfilemanage.vo.SysFileinfo;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;

/**
 * @desc 文件管理service
 * @author jingpj
 * @date Jul 29, 2009
 * @path com.est.sysinit.sysfilemanage.service.SysFileInfoServiceImp
 * @corporation Enstrong S&T
 */
@Service
@Transactional
public class SysFileInfoServiceImp implements ISysFileInfoService {
	
	/**
	 * 报表文件目录前缀：为区别报表文件和报表模板文件，保存时在报表模板路径前加上前缀
	 */
	public final static String REPORT_DIR_PREFIX = "report/";
	
	private ISysFileInfoDao sysFileInfoDao;

	private String fileStorePath;

	private ISysTemplateService sysTemplateService ;
	
	public void setSysFileInfoDao(ISysFileInfoDao sysFileInfoDao) {
		this.sysFileInfoDao = sysFileInfoDao;
	}


	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}
	
	public String getFileStorePath() {
		return fileStorePath;
	}


	public void setSysTemplateService(ISysTemplateService sysTemplateService) {
		this.sysTemplateService = sysTemplateService;
	}


	/**
	 * @desc 通过fileno来得到FileInfo
	 * @date Jul 30, 2009
	 * @author jingpj
	 * @param fileNo
	 * @return
	 * @see com.est.sysinit.sysfilemanage.service.ISysFileInfoService#getSysFileInfoByFileNo(java.lang.String)
	 */
	public SysFileinfo getSysFileInfoByFileNo(String fileNo) {
		String hql = "From SysFileinfo t where t.fileno = ?";
		return (SysFileinfo) sysFileInfoDao.findUniqueByHql(hql, fileNo);
	}

	/**
	 * @desc 通过id得到fileinfo
	 * @date Jul 30, 2009
	 * @author jingpj
	 * @param fileInfoId
	 * @return
	 * @see com.est.sysinit.sysfilemanage.service.ISysFileInfoService#getSysFileInfoById(java.lang.Long)
	 */
	public SysFileinfo getSysFileInfoById(Long fileInfoId) {
		return sysFileInfoDao.findById(fileInfoId);
	}

	/**
	 * @desc
	 * @date Jul 30, 2009根据条件查询fileinfo列表
	 * @author jingpj
	 * @param cond
	 * @see com.est.sysinit.sysfilemanage.service.ISysFileInfoService#getSysFileInfoList(com.est.common.ext.util.SearchCondition)
	 */
	public List<SysFileinfo> getSysFileInfoList(SearchCondition cond) {

		return null;
	}

	/**
	 * @desc 保存fileinfo
	 * @date Jul 30, 2009
	 * @author jingpj
	 * @param sysFileInfo
	 * @see com.est.sysinit.sysfilemanage.service.ISysFileInfoService#savSysFileInfo(com.est.sysinit.sysfilemanage.vo.SysFileinfo)
	 */
	public void savSysFileInfo(SysFileinfo sysFileInfo) {
		sysFileInfoDao.save(sysFileInfo);
	}

	/**
	 * @desc
	 * @date Jul 30, 2009
	 * @author jingpj
	 * @param fileNo 
	 * @return
	 * @see com.est.sysinit.sysfilemanage.service.ISysFileInfoService#copySysFileInfo(java.lang.String)
	 */
	public synchronized String savCopySysFileInfo(String fileNo,boolean isTemplateUpdate) {
		try {
			
			// 得到源fileinfo
			SysFileinfo srcFileInfo = getSysFileInfoByFileNo(fileNo); 
			
			//获得fileno序列号
			SerialNumGeneratorUtil numGen = new SerialNumGeneratorUtil();
			String serialNum = numGen.getDateSerialNum(sysFileInfoDao, "SysFileinfo", "fileno", "yyMMdd", 4);
			
			//新建目标Fileinfo,并从srcFileInfo拷贝需要的属性，设置序列号
			SysFileinfo destFileInfo = new SysFileinfo(srcFileInfo, serialNum);
			destFileInfo.setFileno(serialNum);
			
			/* 拼装保存地址*/
			StringBuilder sbDestUrl = new StringBuilder(200);
			{
				sbDestUrl.append(fileStorePath);  //文件存放配置路径
				if(!fileStorePath.endsWith("\\") && !fileStorePath.endsWith("/")){
					sbDestUrl.append("/");
				}
				
				SysTemplate tpl = sysTemplateService.getSysTemplateByFileNo(fileNo);
				if(tpl == null) {	
					//不是模板，加上前缀
					sbDestUrl.append(REPORT_DIR_PREFIX);
				}
				
				sbDestUrl.append(destFileInfo.getSysDir().getDirpath()); //sysDir配置路径
				sbDestUrl.append("/");
				sbDestUrl.append(serialNum);				//文件名为：序列号+扩展名
				sbDestUrl.append("."+destFileInfo.getFileextension());
			
			}
			
			destFileInfo.setUrl(sbDestUrl.toString());

			File dirPath = new File(destFileInfo.getUrl());
			dirPath = dirPath.getParentFile();
			if (dirPath != null) {
				FileUtil.chkDirExist(dirPath);
			} else {
				return null;
			}
			FileUtil.file2file(srcFileInfo.getUrl(), destFileInfo.getUrl());

			if (getSysFileInfoByFileNo(serialNum) == null) {
				SysTemplate tpl = destFileInfo.getSysTemplate();
				if(tpl!=null && isTemplateUpdate) {
					tpl.setUpdateflag("否");
				}
				sysFileInfoDao.save(destFileInfo);
				
				return destFileInfo.getFileno();
			} else {
				FileUtil.deleteFile(destFileInfo.getUrl());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @desc 上传文件
	 * @date Jul 30, 2009
	 * @author Administrator
	 * @param file
	 *            上传的文件
	 * @param dir
	 *            存放路径
	 * @param template
	 *            报表模板，如果没有则为null
	 * @param fileName
	 *            文件名
	 * @return
	 * @see com.est.sysinit.sysfilemanage.service.ISysFileInfoService#uploadFile(java.io.File,
	 *      com.est.sysinit.sysfilemanage.vo.SysDir,
	 *      com.est.sysinit.sysfilemanage.vo.SysTemplate, java.lang.String)
	 * @deprecated Use
	 *             {@link #savUploadFile(File,SysDir,SysTemplate,String,SysFileinfo)}
	 *             instead
	 */
	public SysFileinfo uploadFile(File file, SysDir dir, SysTemplate template,
			String fileName) {
		try {
			return savUploadFile(new FileInputStream(file), dir, template,
					fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

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
	public synchronized SysFileinfo savUploadFile(InputStream inputStream, SysDir dir,
			SysTemplate template, String fileName, SysFileinfo oldSysFileInfo) {
		if (oldSysFileInfo == null) {
			// 新增文件
			SysFileinfo newSysFileInfo = new SysFileinfo();
			if (fileName != null) {
				newSysFileInfo.setFileextension(fileName.substring(fileName.lastIndexOf(".") + 1));
			} else {
				newSysFileInfo.setFileextension("");
			}

			newSysFileInfo.setFilename(fileName);
			newSysFileInfo.setSysDir(dir);
			newSysFileInfo.setSysTemplate(template);

			try {
				SerialNumGeneratorUtil numGen = new SerialNumGeneratorUtil();

				String serialNum = numGen.getDateSerialNum(sysFileInfoDao, "SysFileinfo", "fileno", "yyMMdd", 4);

				newSysFileInfo.setFileno(serialNum);
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
			String saveUrl = fileStorePath + "/" + dir.getDirpath() + "/" + newSysFileInfo.getFileno();
			String fileextension = newSysFileInfo.getFileextension();
			if (fileextension != null && !"".equals(fileextension)) {
				saveUrl += "." + fileextension;
			}

			newSysFileInfo.setUrl(saveUrl);

			try {
				File file = new File(saveUrl);
				File filepath = file.getParentFile(); // 存放文件夹

				FileUtil.chkDirExist(filepath); // 检查文件夹是否存在，没有则创建

				FileUtil.stream2File(inputStream, saveUrl);
				sysFileInfoDao.save(newSysFileInfo);
				return newSysFileInfo;

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (DirectoryCreateException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			// 覆盖文件
			try {
				File file = new File(oldSysFileInfo .getUrl());
				File filepath = file.getParentFile(); // 存放文件夹
				FileUtil.chkDirExist(filepath); // 检查文件夹是否存在，没有则创建
				FileUtil.stream2File(inputStream, new File(oldSysFileInfo .getUrl()));
				return oldSysFileInfo;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (DirectoryCreateException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 *@desc 上传文件
	 *@date Sep 2, 2009
	 *@author jingpj 
	 *@param inputStream 
	 *@param filePath 文件保存路径
	 *@param fileno 关联对象的fileno，上传的文件序列号为关联对象的fileno+3位流水号
	 *@param fileName 文件名
	 *@param oldSysFileInfo 被覆盖fileinfo对象
	 *@return 关联对象的fileno
	 */
	public synchronized String savUploadFile(InputStream inputStream, String filePath, String fileno,String saveFileName,String fileName,SysFileinfo oldSysFileInfo) {
		if (oldSysFileInfo == null) {
			/**新增文件*/
			
			SysFileinfo newSysFileInfo = new SysFileinfo();
			
			//设置文件扩展名
			if (fileName != null) {
				newSysFileInfo.setFileextension(fileName.substring(fileName.lastIndexOf(".") + 1));
			} else {
				newSysFileInfo.setFileextension("");
			}

			newSysFileInfo.setFilename(saveFileName);
			newSysFileInfo.setSysDir(null);
			newSysFileInfo.setSysTemplate(null);
			
			//设置fileno
			String serialNum = null;
			try {
				SerialNumGeneratorUtil numGen = new SerialNumGeneratorUtil();

				if(fileno==null||"".equals(fileno)) {
					/** 没有保存过附件 */
					serialNum = numGen.getDateSerialNum(sysFileInfoDao, "SysFileinfo", "SUBSTR(fileno,0,10)", "yyMMdd", 4);
					serialNum += "001";
					newSysFileInfo.setFileno(serialNum);
				} else {
					/** 已经有附件 */
					serialNum = numGen.getSerialNum(sysFileInfoDao, "SysFileinfo", "fileno", fileno, 3);
					newSysFileInfo.setFileno(serialNum);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
			
			//设置文件上传url
			String saveUrl = fileStorePath + "/" + filePath + "/" + newSysFileInfo.getFileno();
			String fileextension = newSysFileInfo.getFileextension();
			if (fileextension != null && !"".equals(fileextension)) {
				saveUrl += "." + fileextension;
			}

			newSysFileInfo.setUrl(saveUrl);

			//上传文件
			try {
				File file = new File(saveUrl);
				File filepath = file.getParentFile(); // 存放文件夹

				FileUtil.chkDirExist(filepath); // 检查文件夹是否存在，没有则创建

				FileUtil.stream2File(inputStream, saveUrl);
				sysFileInfoDao.save(newSysFileInfo);
				return serialNum.substring(0,10);

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (DirectoryCreateException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			/** 覆盖文件 */
			try {
				File file = new File(oldSysFileInfo .getUrl());
				File filepath = file.getParentFile(); // 存放文件夹
				FileUtil.chkDirExist(filepath); // 检查文件夹是否存在，没有则创建
				FileUtil.stream2File(inputStream, new File(oldSysFileInfo .getUrl()));
				if (fileName != null) {
					oldSysFileInfo.setFilename(saveFileName);
					oldSysFileInfo.setFileextension(fileName.substring(fileName.lastIndexOf(".") + 1));
				} 
				return fileno.substring(0,10);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (DirectoryCreateException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 *@desc 根据fileno得到其关联的所有文件列表
	 *@date Sep 3, 2009
	 *@author jingpj
	 *@param fileNo
	 *@return
	 */
	public Result<SysFileinfo> getSysFileInfoListByFileNo(Page page,String fileNo) {
		String hql = "From SysFileinfo t where t.fileno like ? order by t.fileno";
		if(fileNo==null || "".equals(fileNo)){
			fileNo = "Empty";
		} else {
			fileNo += "%" ;
		}

		return  sysFileInfoDao.findByPage(page, hql, fileNo);
	}
	
	/**
	 *@desc 根据id删除sysfileinfo对象,和关联的文件
	 *@date Sep 5, 2009
	 *@author jingpj
	 *@param fileinfoId
	 */
	public void delFileinfo(Long fileinfoId) {
		SysFileinfo fileinfo = sysFileInfoDao.findById(fileinfoId);
		String url = fileinfo.getUrl();
		File file = new File(url);
		file.delete();
		sysFileInfoDao.delById(fileinfoId);
	}
	
}
