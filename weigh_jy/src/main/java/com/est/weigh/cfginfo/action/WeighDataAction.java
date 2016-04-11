package com.est.weigh.cfginfo.action;

import com.est.common.ext.util.Page;
import com.est.weigh.report.service.WeighDetailExport;
import com.sun.corba.se.impl.logging.POASystemException;
import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Result;
import com.est.sysinit.sysuser.vo.SysUser;
import com.est.weigh.cfginfo.service.IWeighCompanyService;
import com.est.weigh.cfginfo.service.IWeighDataService;
import com.est.weigh.cfginfo.vo.WeighData;

public class WeighDataAction extends BaseAction {
	@Autowired
	private IWeighDataService dataService;
	@Autowired
	private IWeighCompanyService companyService;
	private WeighData entity = new WeighData();

	@Override
	public Object getModel() {
		return entity;
	}

	public String fwdWeighData() {
		return toJSP("weighData");
	}

    public String fwdWeighDataForUpdate() {
        return toJSP("weighDataUpdate");
    }
	
	public String fwdWeighDataQuery() {
		return toJSP("weighDataQuery");
	}

	/**
	 * @描述: 获取所有过磅数据
	 * @return
	 */
	public String getAllWeighDataList() {
		this.getPage().setRowPerPage(Integer.MAX_VALUE);
		return getWeighDataList();
	}

	/**
	 * @描述: 获取过磅数据
	 * @return
	 */
	public String getWeighDataList() {
		Result<WeighData> rst = dataService.getWeighDataList(this.params, this.getPage());
		return toJSON(rst);
	}

	/**
	 * @描述: 保存过磅数据
	 * @return
	 */
	public String savWeighData() {
		if ("".equals(entity.getId())) {
			entity.setId(null);
		}
		SysUser sysUser=getCurrentUser();
		entity.setRemark("由" + sysUser.getUsername() + "最后修改");
		entity.setType(req.getParameter("pz"));
		try {
			dataService.savWeighData(entity);
			return toJSON(entity, "{success:true,data:", "}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'保存错误，请联系管理员'}");
		}
	}

	/**
	 * @描述: 获得过磅数据
	 * @return
	 */
	public String getWeighData() {
		WeighData weighData = dataService.getWeighData(entity.getId());
		return toJSON(weighData, "{success:true,data:", "}");
	}

	/**
	 * @描述: 删除过磅数据
	 * @return
	 */
	public String delWeighData() {
		try {
			dataService.delWeighData(entity);
			return toJSON("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'保存错误，请联系管理员'}");
		}
	}

    public String exportWeighDetail() {
        Page page = new Page();
        page.setCurPage(0);
        page.setRowPerPage(Integer.MAX_VALUE);
        String companyName = (String) params.get("companyCombox");
        params.set("isexporder", "yes");
        Result<WeighData> rst = dataService.getWeighDataList(this.params, page);
        if (rst.getContent() == null || rst.getContent().size() == 0) {
            return null;
        }
        String v = rst.getContent().get(0).getType();
        String typename = null;
        if(v.equals("shigao"))
            typename = "石膏";
        else if(v.equals("fenmeihui"))
            typename = "粉煤灰";
        else if(v.equals("shihuishi"))
            typename = "石灰石";
        else if (v.equals("yean"))
            typename = "液氨";
        else if (v.equals("suan"))
            typename = "酸";
        else if (v.equals("jian"))
            typename = "碱";
        else if (v.equals("huizha"))
            typename = "灰渣";
        String[] needFields = null;
        String[] tableHead = null;
        if ("粉煤灰".equals(typename)) {
            needFields = new String[]{"grade", "vehicle_no", "net_weight", "jizu", "unit_price", "total_price", "gross_time", "weighman"};
            tableHead = new String[]{"品种等级", "车号","净重", "机组", "单价", "总价", "毛重时间", "毛重司磅人"};
        } else if ("石膏".equals(typename)) {
            needFields = new String[]{"grade", "vehicle_no", "net_weight", "unit_price", "total_price", "gross_time", "weighman"};
            tableHead = new String[]{"品种等级", "车号","净重", "单价", "总价", "毛重时间", "毛重司磅人"};
        } else if ("灰渣".equals(typename)) {
            needFields = new String[]{"jizu", "vehicle_no", "net_weight", "unit_price", "total_price", "gross_time", "weighman"};
            tableHead = new String[]{"机组", "车号","净重", "单价", "总价", "毛重时间", "毛重司磅人"};
        }
        WeighDetailExport wde = new WeighDetailExport(res, companyName + typename +"过磅明细", rst.getContent(), companyName + typename + "过磅明细", needFields, tableHead);
        wde.createExcel();
        return null;
    }

    public String batchSetWater() {
        try {
            dataService.batchSetWater(params);
            return toJSON("{success:true}");
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (e instanceof BaseBussinessException) {
                return toSTR("{success:false,error:'" + e.getMessage() + "'}");
            }
            return toSTR("{success:false,error:'保存错误，请联系管理员'}");
        }
    }
}
