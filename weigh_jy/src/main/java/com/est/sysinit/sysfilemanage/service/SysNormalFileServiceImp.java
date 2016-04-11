package com.est.sysinit.sysfilemanage.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.NumberUtil;
import com.est.sysinit.sysfilemanage.dao.ISysNormalFileDao;
import com.est.sysinit.sysfilemanage.vo.SysNormalFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-17
 * Time: 上午12:49
 * To change this template use File | Settings | File Templates.
 */
public class SysNormalFileServiceImp implements ISysNormalFileService {
    private ISysNormalFileDao normalFileDao;
    public void setNormalFileDao(ISysNormalFileDao normalFileDao) {
        this.normalFileDao = normalFileDao;
    }

    public void savSysNormalFile(SysNormalFile entity) {
        if(entity.getFileid() == null || entity.getFileid() == 0) {
            entity.setFileid(null);
            entity.setFileversion("1.00");
            normalFileDao.save(entity);
            return;
        }
        SysNormalFile old = normalFileDao.findById(entity.getFileid());
        new File(old.getPath()).delete();
        old.setPath(entity.getPath());
        old.setFilename(entity.getFilename());
        old.setFileversion(NumberUtil.add(Double.parseDouble(old.getFileversion()), 0.01) + "");
        old.setRemark(entity.getRemark());
        normalFileDao.save(old);
    }

    public SysNormalFile getSysNormalFile(SearchCondition params) {
        String fileid = (String)params.get("fileid");
        return normalFileDao.findById(Long.parseLong(fileid));
    }

    public void delSysNormalFile(SysNormalFile entity) {
        SysNormalFile old = normalFileDao.findById(entity.getFileid());
        if (old != null) {
            new File(old.getPath()).delete();
            normalFileDao.del(old);
        }
    }

    public Result<SysNormalFile> getSysNormalFileList(SearchCondition params, Page page) {
        StringBuilder hql = new StringBuilder("from SysNormalFile where fileid is not null");
        List cond = new ArrayList();
        Result<SysNormalFile> rst = normalFileDao.findByPage(page, hql.toString(), cond.toArray());
        return rst;
    }

    public String checkversion(String filename, String version) {
        String NON = "NON";
        StringBuilder hql = new StringBuilder("select max(fileversion) from SysNormalFile where filename=?");
        List list = normalFileDao.findByHql(hql.toString(), filename);
        if (list.size() == 0) {
            return NON;
        }
        Double maxversion = Double.parseDouble((String)list.get(0));
        if (maxversion <= Double.parseDouble(version)) {
            return NON;
        }
        SysNormalFile snf = (SysNormalFile) normalFileDao.findUniqueByHql("from SysNormalFile where filename=? and fileversion=?", filename, (String)list.get(0));
        return snf.getFileid() + "&" + snf.getFileversion();
    }
}
