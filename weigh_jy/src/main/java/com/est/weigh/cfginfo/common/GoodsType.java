package com.est.weigh.cfginfo.common;

import java.util.HashMap;
import java.util.Map;

public class GoodsType {
	public static final String fenmeihui = "fenmeihui";
	public static final String shigao = "shigao";
	public static final String huizha = "huizha";
	public static final String gaifen = "gaifen";
	public static final String suan = "suan";
	public static final String jian = "jian";
	public static final String yean = "yean";
	public static final String shihuishi = "shihuishi";
	private static Map<String, String> nameMap = new HashMap();
	static {
		nameMap.put(fenmeihui, "粉煤灰");
		nameMap.put(shigao, "石膏");
		nameMap.put(huizha, "灰渣");
		nameMap.put(gaifen, "钙粉");
		nameMap.put(suan, "酸");
		nameMap.put(jian, "碱");
		nameMap.put(yean, "液氨");
		nameMap.put(shihuishi, "石灰石");
	}
	
	public static String getName(String code) {
		return nameMap.get(code);
	}
}
