package com.est.sysinit.sysfilemanage.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysfilemanage.common.NormalFileProvider;
import com.est.sysinit.sysfilemanage.service.ISysNormalFileService;
import com.est.sysinit.sysfilemanage.vo.SysNormalFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-17
 * Time: 上午12:27
 * To change this template use File | Settings | File Templates.
 */
public class NormalFileAction extends BaseAction {
    private SysNormalFile entity = new SysNormalFile();
    /** 文件名 */
    private String filename;

    /** 上传类型
     * REPORT_TEMPLATE 报表模板
     *
     * REPORT_FILE    报表文件
     */
    private String uploadType;

    private File file ;

    private String fileFileName;

    private String fileContentType;
    @Override
    public Object getModel() {
        return entity;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String fwdNormalFile() {
        return toJSP("normalfile");
    }

    public String savSysNormalFile() {
        String basePath = NormalFileProvider.getBasePath();
        ISysNormalFileService service = (ISysNormalFileService)this.getBean("sysNormalFileService");
        if (basePath == null) {
            return toSTR("{succes:false, '基础配置，保存路径读取失败!请查看filepath.properties文件'}");
        }
        File path = new File(basePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        entity.setFilename(req.getParameter("n_filename"));
        if (StringUtil.isEmpty(entity.getFilename())) {
            entity.setFilename(filename);
        }
        String[] name = file.getName().split(".");
        String absoultPath = basePath + "/" + UUID.randomUUID().toString().replaceAll("-","");
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new FileOutputStream(absoultPath);
            in = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read=in.read(buf)) != -1) {
                out.write(buf, 0 ,read);
            }
            out.flush();
            entity.setPath(absoultPath);
            try {
                service.savSysNormalFile(entity);
                return toSTR("{success:true}");
            } catch (Exception e) {
                e.printStackTrace();
                return toSTR("{success:false, error:'"+e.getMessage()+"'}");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return toSTR("{success:false, error:'未知错误!'}");
    }

    public String getSysNormalFile() {
        ISysNormalFileService service = (ISysNormalFileService)this.getBean("sysNormalFileService");
        try {
            entity = service.getSysNormalFile(this.params);
            return toJSON(entity, "{success:true,data:", "}");
        } catch (Exception e) {
            e.printStackTrace();
            return toSTR("{success:false, error:'" + e.getMessage() + "'}");
        }
    }

    public String delSysNormalFile() {
        ISysNormalFileService service = (ISysNormalFileService)this.getBean("sysNormalFileService");
        try {
            service.delSysNormalFile(entity);
            return toSTR("{success:true}");
        }catch(Exception ex) {
            return toSTR("{success:false,error:'删除文件失败'}");
        }
    }

    public String getSysNormalFileList() {
        ISysNormalFileService service = (ISysNormalFileService)this.getBean("sysNormalFileService");
        Result<SysNormalFile> rst = null;
        try {
            rst = service.getSysNormalFileList(this.params, this.getPage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toJSON(rst);
    }

    public String checkversion() {
        String filename = req.getParameter("filename");
        String version = req.getParameter("version");
        ISysNormalFileService service = (ISysNormalFileService)this.getBean("sysNormalFileService");
        try {
            String rtn = service.checkversion(filename, version);
            return toSTR(rtn);
        } catch (Exception e) {
            e.printStackTrace();
            return toSTR("NON");
        }
    }

    public String download() {
        try {
            String fileid = req.getParameter("fileid");
            ISysNormalFileService service = (ISysNormalFileService)this.getBean("sysNormalFileService");
            SearchCondition condition = new SearchCondition();
            condition.set("fileid", fileid);
            SysNormalFile snf = service.getSysNormalFile(condition);
            if (snf == null) {
                return toSTR("NON");
            }
            res.setHeader("Content-Type", "application/force-download");
            res.setHeader("Connection","close");
            res.setHeader("Content-Type","application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename="+ snf.getFilename());
            FileInputStream srcStream = new FileInputStream(snf.getPath());
            ServletOutputStream out = res.getOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = srcStream.read(buffer)) > 0){
                out.write(buffer,0,length);
            }
            srcStream.close();
            out.flush();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return toSTR("NON");
        }
    }
}
