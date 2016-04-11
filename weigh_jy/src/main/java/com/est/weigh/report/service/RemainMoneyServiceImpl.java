package com.est.weigh.report.service;

import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.NumberUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.dao.IWeighCompanyDao;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.report.vo.RemainMoney;
import com.est.weigh.transaction.dao.IReceiveMoneyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.*;

/**
 * Created by Administrator on 2015-11-09.
 */
@Service
public class RemainMoneyServiceImpl implements  IRemainMoneyService {
    @Autowired
    private IReceiveMoneyDao moneyDao;
    @Autowired
    private IWeighCompanyDao companyDao;
    public List<RemainMoney> getRemainMoneyCompanys(SearchCondition params) {
        String jizu = (String)params.get("jizu");
        String pz = (String)params.get("pz");
        boolean hasJizu = false;
        if ("fenmeihui".equals(pz) || "huizha".equals(pz)) {
            hasJizu = true;
        }

        //获取结转金额 和　结转日期
        String sql = "select hkye, companycode, jizu,t.breedtype,t.yearmonth,t.enddate " +
                    "  from monthreport t " +
                    " where yearmonth = (select max(yearmonth) " +
                    "                      from monthreport " +
                    "                     where companycode = t.companycode " +
                    "                       " + (hasJizu?" and jizu = t.jizu":"") + " and breedtype=t.breedtype)";

        if (!StringUtil.isEmpty(pz)) {
            sql += " and breedtype='"+pz+"'";
        }
        if (!StringUtil.isEmpty(jizu)) {
            sql += " and jizu='"+jizu+"'";
        }
        List<Object[]> rst = moneyDao.sqlQuery(sql);
        Map<String, Double> jzjeMap = new HashMap();
        for (Object[] objs: rst) {
            String key = (String)objs[1] + "_" + (String)objs[2];
            Double val = ((BigDecimal)(objs[0]==null?0d:objs[0])).doubleValue();
            jzjeMap.put(key, val);
        }

        String _jizu = null;
        if (!StringUtil.isEmpty(jizu)) {
            String[] arr_jizu = jizu.split("_");
            for (int i = 0; i < arr_jizu.length; i++) {
                _jizu += "'" + arr_jizu[i] + "',";
            }
            if (_jizu != null) {
                _jizu = _jizu.substring(0, _jizu.length() - 1);
            }
        }

        //计算已售金额
        if ("fenmeihui".equals(pz) || "huizha".equals(pz)) {
            sql = "select sum(nvl(t1.total_price,0)), t1.companycode, t1.jizu,t1.type " +
                    "  from weigh_data t1  " +
                    " where t1.jizu is not null ";
            if (!StringUtil.isEmpty(pz)) {
                sql += " and t1.type='"+pz+"'";
            }
            if (!StringUtil.isEmpty(jizu)) {
                sql += " and t1.jizu in ("+_jizu+")";
            }
            sql += "  and exists (select 1 " +
                    "  from monthreport t " +
                    " where yearmonth = (select max(yearmonth) " +
                    "                      from monthreport " +
                    "                     where companycode = t.companycode " + (hasJizu?" and jizu = t.jizu":"") +
                    "                       and breedtype = t.breedtype) " +
                    "           and t1.companycode = t.companycode " +
                    "           and instr(t.jizu,t1.jizu)!=0 " +
                    "           and t1.type = t.breedtype " +
                    "           and to_char(t1.gross_time, 'yyyy-mm-dd') > t.enddate) " +
                    " group by t1.companycode, t1.jizu,t1.type";
        } else {
            sql = "select sum(nvl(t1.total_price,0)), t1.companycode, t1.jizu,t1.type " +
                    "  from weigh_data t1  " +
                    " where t1.jizu is null";
            if (!StringUtil.isEmpty(pz)) {
                sql += " and t1.type='"+pz+"'";
            }
            sql += "  and exists (select 1 " +
                    "  from monthreport t " +
                    " where yearmonth = (select max(yearmonth) " +
                    "                      from monthreport " +
                    "                     where companycode = t.companycode " +
                    "                       and breedtype = t.breedtype) " +
                    "           and t1.companycode = t.companycode " +
                    "           and t1.type = t.breedtype " +
                    "           and to_char(t1.gross_time, 'yyyy-mm-dd') > t.enddate) " +
                    " group by t1.companycode, t1.jizu,t1.type";
        }
        Map<String, Double> ysjeMap = new HashMap();
        rst = moneyDao.sqlQuery(sql);
        for (Object[] objs: rst) {
            String t_jizu = null;
            if ((String)objs[2] != null) {
                if (((String)objs[2]).contains("31#") || ((String)objs[2]).contains("32#")) {
                    t_jizu = "31#_32#";
                }
                if (((String)objs[2]).contains("33#") || ((String)objs[2]).contains("34#")) {
                    t_jizu = "33#_34#";
                }
            }
            String key = (String)objs[1] + "_" + t_jizu;
            Double val = ((BigDecimal)(objs[0]==null?0d:objs[0])).doubleValue();
            ysjeMap.put(key, val);
        }
        //获取预收金额
        if ("fenmeihui".equals(pz) || "huizha".equals(pz)) {
            sql = " select sum(t1.money), t1.companycode, t1.jizu, t1.breedtype " +
                    "   from WEIGH_RECEIVEMONEY t1 " +
                    "  where t1.jizu is not null ";
            if (!StringUtil.isEmpty(pz)) {
                sql += " and t1.breedtype='"+pz+"'";
            }
            if (!StringUtil.isEmpty(jizu)) {
                sql += " and t1.jizu in ("+_jizu+")";
            }
            sql +=  "    and exists " +
                    "  (select hkye, companycode, jizu, t.breedtype, t.yearmonth, t.enddate " +
                    "           from monthreport t " +
                    "          where yearmonth = (select max(yearmonth) " +
                    "                               from monthreport " +
                    "                              where companycode = t.companycode " + (hasJizu?" and jizu = t.jizu":"") +
                    "                                and breedtype = t.breedtype) " +
                    "            and t1.companycode = t.companycode " +
                    "            and instr(t.jizu, t1.jizu) != 0 " +
                    "            and t1.breedtype = t.breedtype " +
                    "            and to_char(t1.receivedate, 'yyyy-mm-dd') > t.enddate) " +
                    "  group by t1.companycode, t1.jizu, t1.breedtype";
        } else {
            sql = " select sum(t1.money), t1.companycode, t1.jizu, t1.breedtype " +
                    "   from WEIGH_RECEIVEMONEY t1 " +
                    "  where t1.jizu is null ";
            if (!StringUtil.isEmpty(pz)) {
                sql += " and t1.breedtype='"+pz+"'";
            }
            sql +=  "    and exists " +
                    "  (select hkye, companycode, jizu, t.breedtype, t.yearmonth, t.enddate " +
                    "           from monthreport t " +
                    "          where yearmonth = (select max(yearmonth) " +
                    "                               from monthreport " +
                    "                              where companycode = t.companycode " +
                    "                                and breedtype = t.breedtype) " +
                    "            and t1.companycode = t.companycode " +
                    "            and t1.breedtype = t.breedtype " +
                    "            and to_char(t1.receivedate, 'yyyy-mm-dd') > t.enddate) " +
                    "  group by t1.companycode, t1.jizu, t1.breedtype";
        }
        Map<String, Double> yshkMap = new HashMap();
        rst = moneyDao.sqlQuery(sql);
        for (Object[] objs: rst) {
            String key = (String)objs[1] + "_" + (String)objs[2];
            Double val = ((BigDecimal)(objs[0]==null?0d:objs[0])).doubleValue();
            yshkMap.put(key, val);
        }
        List<RemainMoney> list = new ArrayList(ysjeMap.size());
        Map<String, RemainMoney> total = new HashMap();
        for (String key: jzjeMap.keySet()) {
            if (total.get(key) == null) {
                total.put(key, new RemainMoney());
            }
        }
        for (String key: ysjeMap.keySet()) {
            if (total.get(key) == null) {
                total.put(key, new RemainMoney());
            }
        }
        for (String key: yshkMap.keySet()) {
            if (total.get(key) == null) {
                total.put(key, new RemainMoney());
            }
        }
        for (String key: total.keySet()) {
            RemainMoney money = total.get(key);
            money.setCompanycode(key.split("_")[0]);
            money.setJizu(key.replaceFirst(key.split("_")[0] + "_", ""));
            money.setJzje(jzjeMap.get(key)==null?0d:jzjeMap.get(key));
            money.setRemainMoney(NumberUtil.sub(NumberUtil.add(money.getJzje(), yshkMap.get(key)==null?0d:yshkMap.get(key)), ysjeMap.get(key)==null?0d:ysjeMap.get(key)));
            list.add(money);
        }

        //获取单位
        List<WeighCompany> companies = companyDao.findByHql("from WeighCompany where type='"+pz+"'");
        Map<String, String> companynames = new HashMap();
        for (WeighCompany c: companies) {
            companynames.put(c.getCompanycode(), c.getCompanyname());
        }
        for (RemainMoney m: list) {
            m.setCompanyname(companynames.get(m.getCompanycode()));
        }
        final Collator collator = Collator.getInstance(Locale.CHINESE);
        Collections.sort(list, new Comparator<RemainMoney>() {
            public int compare(RemainMoney o1, RemainMoney o2) {
                return collator.compare(o1.getCompanyname(), o2.getCompanyname());
            }
        });
        return list;
    }
}
