package com.est.sysinit.sysfilemanage.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysfilemanage.vo.SysNormalFile;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-17
 * Time: 上午12:49
 * To change this template use File | Settings | File Templates.
 */
public interface ISysNormalFileService {
    void savSysNormalFile(SysNormalFile entity);

    SysNormalFile getSysNormalFile(SearchCondition params);

    void delSysNormalFile(SysNormalFile entity);

    Result<SysNormalFile> getSysNormalFileList(SearchCondition params, Page page);

    String checkversion(String filename, String version);
}
