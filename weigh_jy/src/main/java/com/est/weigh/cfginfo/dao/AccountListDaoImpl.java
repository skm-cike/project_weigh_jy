package com.est.weigh.cfginfo.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.est.common.base.BaseDaoImp;
import com.est.common.exception.BaseBussinessException;
import com.est.weigh.cfginfo.common.AccountType;
import com.est.weigh.cfginfo.vo.AccountList;
@Repository
public class AccountListDaoImpl extends BaseDaoImp<AccountList> implements IAccountListDao{
	/**
	 * @描述: 单价修改
	 */
	public int savDanjia(double money, String company, String breedtype, String grade, String remark) {
		String accountType = AccountType.danjia;
		//初始化记账数据实体类
		AccountList al = new AccountList();
		al.setAccountType(accountType);
		al.setBreedtype(breedtype);
		al.setCompany(company);
		al.setGrade(grade);
		al.setMoney(money);
		al.setOperDate(new Date());
		al.setRemark(remark);
		al.setWeighId(null);
		al.setId(null);
		this.save(al);
		return 1;
	}

	/**
	 * @描述: 付款冲销     暂未实现
	 */
	@Deprecated
	public int savFukuanchongxiao(double money) {
		String accountType = AccountType.fukuanchongxiao;
		return 0;
	}


	/**
	 * @描述: 结转金额　　暂时只针对买方结转
	 * @param company
	 * @param remark 记录月报的日期  yyyy-MM-dd
	 */
	public int savJiezhuanjine(String company, String breedtype, String remark, Double money) {
		String accountType = AccountType.jiezhuanjine;
		
		//初始化记账数据实体类
		Date date = new Date();
		AccountList newjzje = new AccountList();
		newjzje.setAccountType(accountType);
		newjzje.setBreedtype(breedtype);
		newjzje.setCompany(company);
		newjzje.setGrade("");
		newjzje.setMoney(money);
		newjzje.setOperDate(date);
		newjzje.setRemark(remark);								//这里用来记录结转月份  yyyy-MM
		newjzje.setWeighId("");
		newjzje.setId(null);
		
		this.save(newjzje);
		return 1;
	}
	
	
	/**
	 * @描述: 结转金额冲销   remark记录 结转月份  yyyy-MM
	 */
	public int savJiezhuanjineChongxiao(String company, String breedtype,
			String remark) {
		List<AccountList> jzList = this.findByHql("from AccountList where breedtype=? and company=? and remark=? and accountType in (?,?) and rownum=1 order by operDate desc"
															, breedtype, company, remark, AccountType.jiezhuanjine, AccountType.jiezhuanchongxiao);
		if ((jzList.size() != 0 && !AccountType.jiezhuanjine.equals(jzList.get(0).getAccountType())) || jzList.size() == 0) {
			throw new BaseBussinessException("没有结转数据,无法冲销!");
		}
		AccountList jzje = jzList.get(0);
		AccountList jzcx = new AccountList();
		jzcx.setAccountType(AccountType.jiezhuanchongxiao);
		jzcx.setBreedtype(breedtype);
		jzcx.setCompany(company);
		jzcx.setGrade("");
		jzcx.setMoney(jzje.getMoney());
		jzcx.setOperDate(jzje.getOperDate());
		jzcx.setRemark(remark);
		jzcx.setWeighId("");
		this.save(jzcx);
		return 1;
	}

	/**
	 * @描述: 扣款冲销
	 * @remark 这里记录机组信息
	 */
	public int savKoukuanchongxiao(String breedtype, Date operDate,  String weighId, String company, String grade, String remark) {
		String accountType = AccountType.koukuanchongxiao;
		AccountList koukuan = (AccountList)this.findUniqueByHql("from AccountList  where breedtype=? and operDate=?  and accountType=? and weighId=?",
														breedtype, operDate, AccountType.koukuan, weighId);
		if  (koukuan == null) {
			throw new BaseBussinessException("没有扣款数据，不能冲销!");
		}
		AccountList koukuanchongxiao = (AccountList)this.findUniqueByHql("from AccountList  where and breedtype=? and operDate=? and accountType=? and weighId=?",
				breedtype, operDate, AccountType.koukuanchongxiao, weighId);
		if (koukuanchongxiao != null) {
			throw new BaseBussinessException("已有冲销数据!");
		}
		koukuanchongxiao = new AccountList();
		koukuanchongxiao.setAccountType(accountType);
		koukuanchongxiao.setBreedtype(breedtype);
		koukuanchongxiao.setCompany(company);
		koukuanchongxiao.setGrade(grade);
		koukuanchongxiao.setMoney(koukuan.getMoney());
		koukuanchongxiao.setOperDate(operDate);
		koukuanchongxiao.setRemark(remark);
		koukuanchongxiao.setWeighId(weighId);
		koukuanchongxiao.setOperDate(new Date());
		this.save(koukuanchongxiao);
		return 1;
	}

	public int savKoukuan(String company, String breedtype, String grade, String remark, Date operDate, double money, String weighId) {
		String accountType = AccountType.koukuan;
		AccountList koukuan = (AccountList)this.findUniqueByHql("from AccountList  where company=? and breedtype=? and grade=? and remark=? and operDate=? and money=? and accountType=? and weighId=?",
				company, breedtype, grade, remark, operDate, money, AccountType.koukuan, weighId);
		if (koukuan != null) {
			throw new BaseBussinessException("已有该扣款数据!");
		}
		koukuan = new AccountList();
		koukuan.setAccountType(accountType);
		koukuan.setBreedtype(breedtype);
		koukuan.setCompany(company);
		koukuan.setGrade(grade);
		koukuan.setMoney(money);
		koukuan.setOperDate(operDate);
		koukuan.setRemark(remark);
		koukuan.setWeighId(weighId);
		koukuan.setOperDate(new Date());
		this.save(koukuan);
		return 1;
	}

	/**
	 * @描述: 预收货款冲销
	 * @operDate 预收货款的操作时间
	 */
	public int savYushouchongxiao(double money, String breedtype, Date operDate, String company, String remark) {
		String accountType = AccountType.yushouchongxiao;
		AccountList yushouchongxiao = new AccountList();
		yushouchongxiao.setAccountType(accountType);
		yushouchongxiao.setBreedtype(breedtype);
		yushouchongxiao.setCompany(company);
		yushouchongxiao.setGrade(null);
		yushouchongxiao.setMoney(money);
		yushouchongxiao.setOperDate(operDate);
		yushouchongxiao.setRemark(remark);
		yushouchongxiao.setWeighId(null);
		yushouchongxiao.setOperDate(new Date());
		this.save(yushouchongxiao);
		return 1;
	}

	/**
	 * @描述: 预收货款
	 */
	public int savYushou(double money, String breedtype, Date operDate, String company, String remark) {
		String accountType = AccountType.yushou;
		AccountList yushou = new AccountList();
		yushou.setAccountType(accountType);
		yushou.setBreedtype(breedtype);
		yushou.setCompany(company);
		yushou.setGrade(null);
		yushou.setMoney(money);
		yushou.setOperDate(operDate);
		yushou.setRemark(remark);
		yushou.setWeighId(null);
		yushou.setOperDate(new Date());
		this.save(yushou);
		return 1;
	}
	
	public int savYufu(double money, String company, String remark) {
		String accountType = AccountType.yufu;
		return 0;
	}

	public int savZhifu(double money, String guobangid, String company,
			String remark) {
		String accountType = AccountType.zhifu;
		return 0;
	}

	public int savYufuchongxiao(double money) {
		String accountType = AccountType.yufuchongxiao;
		return 0;
	}
}
