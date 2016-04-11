package com.est.sysinit.sysfilemanage.vo;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-17
 * Time: 上午12:33
 * To change this template use File | Settings | File Templates.
 */
public class SysNormalFile {
    private Long fileid;
    private String filename;
    private String path;
    private String fileversion;
    private String remark;
    private String n_filename;
    public Long getFileid() {
        return fileid;
    }
    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    public String getN_filename() {
        return n_filename;
    }

    public void setN_filename(String n_filename) {
        this.filename = n_filename;
        this.n_filename = n_filename;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.n_filename = filename;
        this.filename = filename;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getFileversion() {
        return fileversion;
    }
    public void setFileversion(String fileversion) {
        this.fileversion = fileversion;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
