package com.est.common.ext.util.excelexport;

import java.util.List;

/**
 * 
 * @desc
 * @author hebo
 * @date Jul 23, 2010
 * @path com.est.common.ext.util.excelexport.ExcelItem
 * @corporation Enstrong S&T
 */
public class ExcelItem {

	private StringBuffer buf = new StringBuffer(200);

	public ExcelItem(String[] contents, String[] contentsStyle, String[] align,
			int[] colspan) {
		buf.append("<TR>");
		for (int i = 0; i < colspan.length; i++) {
			buf.append("<TD colspan=" + colspan[i] + " style="
					+ contentsStyle[i] + " align=" + align[i] + ">");
			buf.append(contents[i]);
			buf.append("</TD>");
		}
		buf.append("</TR>");
	};

	public ExcelItem(String title, List contents, int rowspan, int colspan) {
		for (int i = 0; i < contents.size(); i++) {

			if (i == 0) {
				buf.append("<td rowspan=" + rowspan + ">" + title + "</td>");
				buf.append(" <td  colspan=" + colspan + ">" + contents.get(i)
						+ "</td>");
				buf.append("</tr>");
			} else {
				buf.append("<tr>");
				buf.append("<td colspan="+colspan+">" + contents.get(i) + "</td>");
				buf.append("</tr>");
			}
		}
	}

	public String getItem() {
		return buf.toString();
	}

}
