package org.htz.core;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import jxl.Cell;
import jxl.CellType;
import jxl.Range;
import jxl.Sheet;
import jxl.format.Alignment;
import jxl.format.BoldStyle;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;

import org.htz.pdfevent.PageEvent;
import org.xhtmlrenderer.css.parser.property.BorderPropertyBuilders.Border;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;


public class Convert extends WriterPdf{
	/**pdf表格*/
	private Table table=null;
	/** 当 Excel 的 border 是 NONE 是，pdf 的 border 是否是 0 */
	private boolean noEmptyBorder = false;
	/**工作簿数组*/
	private Sheet[] sheets=null;
	/**工作簿*/
	private Excel excel=null;
	/**页眉*/
	private HeaderText header;
	/**最后一个工作簿图片*/
	private SheetImage sheetImage;
	
	int iRows = 0;
	int iTotalrows = 0;
	int iCount = 0;
	
	/**获得页面对象*/
	public HeaderText getHeader() {
		return header;
	}
	/**
	 * 读入一个excel文件获得一个pdf文件输出流
	 * @param filePath excel文件路径
	 * @param output pdf输出流
	 */
	public Convert(String filePath,OutputStream output) {
		super(output);
		readExcel(filePath);
	}
	/**
	 * 将指定路径一个excel文件转化为指定路径的pdf文件
	 * @param filePath excel文件路径
	 * @param destFilePath pdf文件路径
	 */
	public Convert(String filePath,String destFilePath){
		super(destFilePath);
		readExcel(filePath);
	}
	/**
	 * 从数据库中读入一个excel文件的输入转化为一个输出流
	 * @param input excel文件的输入流
	 * @param output excel文件输出流
	 */
	public Convert(InputStream input,OutputStream output){
		super(output);
		readExcel(input);
	}
	/**
	 * 读取excel的方法
	 * @param obj Object对象
	 */
	private void readExcel(Object obj){
		excel=new Excel();
		if(obj instanceof String){
			String filePath=(String)obj;
		    excel.readExcel(filePath);//从输入流中读入excel文件
		}else if(obj instanceof InputStream ){
			InputStream input=(InputStream)obj;
		    excel.readExcelFromDB(input);
		}
		sheets=excel.getSheets();//获得excel文件工作簿数
		for(int i=0;i<=sheets.length-1;i++)
		{
			int rows=sheets[i].getRows();
			if(rows != 0)
			{
				this.setITotalrows(rows);
			}
		}
		System.out.println("->sheet的个数为:"+sheets.length);
		int length=sheets.length-1;
		ExcelSheet image_Sheet=new ExcelSheet(sheets[length]);
		List imageList=image_Sheet.getImages();
		sheetImage=new SheetImage();
		sheetImage.setImage(imageList);
		try {
			header=readHeader(sheets[length]);
		} catch (Exception e) {
			System.err.println("->读取页眉出错");
			e.printStackTrace();
		}
	}
	/**
	 * 转化的核心方法
	 * @param pageEvent 页面事件
	 * @throws Exception
	 */
	public void convert(PageEvent pageEvent)throws Exception{
		noEmptyBorder = true;
		writer.setPageEvent(pageEvent);//设置页面事件
		document.setPageSize(pageSize);//设置pdf页面的大小
		document.open();//打开document对象
		System.out.println("->document已经打开");
		if(sheets==null||sheets.length<0){
			System.out.println("->excel文件中没有工作簿");
			return;
		}
		for(int i=0;i<=sheets.length-1;i++){
			int colswidth[]=colsWidth(sheets[i]);
			int rows=sheets[i].getRows();
			int cols=sheets[i].getColumns();
			Range[] range=sheets[i].getMergedCells();
			if(cols>0){
				CopyOnWriteArrayList<PdfCellUtil> cells=new CopyOnWriteArrayList();
				table=new Table(cols);//创建含有cols列的表格
				table.setWidths(colswidth);//设置每列的大小
				table.setPadding(2f);//设置填充间隔
				table.setSpacing(0.0f);//设置单元格之间距离
				table.setWidth(95.0f);//设置表格的宽度百分比
				table.setBorder(0);//设置表格的边框
				table.setOffset(30.0f);//设置表与表之间的偏移量
				Set<String> set = new HashSet();
				for (int j = 0; j < rows; j++) {                   //填充单元格
					for (int k = 0; k < cols; k++) {
						com.lowagie.text.Cell pcell=null;
						Phrase phrase = null;
						Cell jxlcell= sheets[i].getCell(k, j);
						CellFormat format = jxlcell.getCellFormat();//取得单元格的格式
						Font font = null;
						if(format != null && format.getFont() != null) {
							font = convertFont(format.getFont());// 调用convertFont()的方法转变字体
						}else{
							font = new Font(Font.COURIER, 10.0f, Font.NORMAL, Color.BLACK);
						}
						
						String content=jxlcell.getContents();
						phrase=new Phrase(content,font);
						pcell=new com.lowagie.text.Cell();
						pcell.addElement(phrase);
						transferFormat(pcell, jxlcell, j, k, set);//将jxl中的cell转化为pdf的cell
						table.addCell(pcell, j, k);
						PdfCellUtil pdfCell = new PdfCellUtil();
						pdfCell.row = j;
						pdfCell.col = k;
						pdfCell.cell = pcell;
						cells.add(pdfCell);
					}
				}
				
				for (Range r: range) {                    //合并单元格
					int s_row = r.getTopLeft().getRow();
					int s_col = r.getTopLeft().getColumn();
					int e_row = r.getBottomRight().getRow();
					int e_col = r.getBottomRight().getColumn();
					PdfCellUtil t = null;
					for (int j = 0; j < cells.size(); j++) {
						if (cells.get(j).row == s_row && cells.get(j).col == s_col) {
							t = cells.get(j);
						}
					}
					if (t == null) {
						continue;
					}
					t.cell.setRowspan(e_row + 1 - s_row);
					t.cell.setColspan(e_col + 1 - s_col);
				}
				
				document.add(table);//将table对象添加到文档对象中去
			}
		}
		System.out.println("执行了");
		CloseDocument();
		excel.closeWorkbook();
	}
	/**
	 * 获得工作簿中所有列宽的数组
	 * @param sheet 工作簿
	 * @return 列宽数组
	 */
	private int[] colsWidth(Sheet sheet){
		int width[]=new int[sheet.getColumns()];
		for(int i=0;i<width.length;i++){
			width[i]=sheet.getColumnView(i).getSize();
		}
		return width;
	}
	/**
	 * 将当前工作簿中的所有单元格添加到List集合中
	 * @param sheet 工作簿
	 * @param rows  工作簿行数
	 * @param cols  工作簿列数
	 */
	private List getSheetCell(Sheet sheet,int rows,int cols){
		List cells=new ArrayList();
		int i = 0;
		if(iRows != 0)
		{
			iRows++;
		}
		for(i = iRows;i<rows;i++){
			for(int j=0;j<cols;j++){
				System.out.println(sheet.getCell(j,i).getContents());
				cells.add(sheet.getCell(j,i));
			}
			iCount++;
			if(iCount % 3000 == 0)
			{
				iRows = i;
				break;
			}
		}
		return cells;
	}
	/**
	 * 查找已合并后单元格下标
	 * @param i 下标位置
	 * @param subs 合并单元格的下标集合
	 * @return 是否找到
	 */
	private boolean findIndex(int i,List subs){
		for(int n = 0; n < subs.size(); n++){
			if(n==i+1){
				return true;
			}
		}
		return false;
	}
	/**
	 * 转换字体
	 * @param f - 字体
	 * @return
	 */
	private com.lowagie.text.Font convertFont(jxl.format.Font f) {
		if (f == null || f.getName() == null)
			return FontFactory.getFont(FontFactory.COURIER, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		int fontStyle = convertFontStyle(f);
		com.lowagie.text.Font font = null;
		Color fontColor = convertColour(f.getColour(), Color.BLACK);
		if (ChineseFont.BASE_CHINESE_FONT != null) {
			font = new Font(ChineseFont.BASE_CHINESE_FONT, f.getPointSize(), fontStyle, fontColor);
		} else {
			String s = f.getName().toLowerCase();
			int fontFamily;
			if (s.indexOf("courier") >= 0) //"courier new".equals(s) || "courier".equals(s))
				fontFamily = Font.COURIER;
			else if (s.indexOf("times") >= 0)
				fontFamily = Font.TIMES_ROMAN;
			else
				fontFamily = Font.HELVETICA;
		
			font = new Font(fontFamily, f.getPointSize(), fontStyle, fontColor);

		}

		return font;
	}
	/**
	 * 颜色转换
	 * @param colour
	 * @param defaultColor
	 * @return
	 */
	private Color convertColour(Colour colour, Color defaultColor) {
		if (defaultColor == null)
			defaultColor = Color.WHITE;
		
		if (colour == null)
			return defaultColor;

        if (colour == Colour.AQUA) // Excel中的自动(前景色)
        	return Color.BLACK;
           // return new Color(Colour.AUTOMATIC.getDefaultRGB().getRed(),Colour.AUTOMATIC.getDefaultRGB().getGreen(),Colour.AUTOMATIC.getDefaultRGB().getBlue());
        else if (colour == Colour.DEFAULT_BACKGROUND) // Excel中的自动(底色)
        	return Color.white;
          //  return new Color(Colour.DEFAULT_BACKGROUND.getDefaultRGB().getRed(),Colour.DEFAULT_BACKGROUND.getDefaultRGB().getGreen(),Colour.DEFAULT_BACKGROUND.getDefaultRGB().getBlue());
        
		RGB rgb = RGB.getDefaultRGB();;;
		return new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}
	/**
	 * 转换字体样式
	 * @param font - 字体
	 * @return
	 */
	private int convertFontStyle(jxl.format.Font font) {

		int result = com.lowagie.text.Font.NORMAL;
		if (font.isItalic())
			result |= com.lowagie.text.Font.ITALIC;
//		if (font.isStruckout())
//			result |= com.lowagie.text.Font.STRIKETHRU;
		
		if (font.getBoldWeight() == BoldStyle.BOLD.getValue())
			result |= com.lowagie.text.Font.BOLD;
		
		if (font.getUnderlineStyle() != null) {
			// 下划线
			UnderlineStyle style = font.getUnderlineStyle();
			if (style.getValue() != UnderlineStyle.NO_UNDERLINE.getValue())
				result |= com.lowagie.text.Font.UNDERLINE;
		}
		return result;
	}
	/**
	 * 转换单元格的格式 PdfPCell
	 * @param pdfCell
	 * @param cell
	 * @param mergeRow
	 */
	private void transferFormat2(PdfPCell pcell,Cell cell){
        jxl.format.CellFormat format = cell.getCellFormat();
        if (format != null) {
            // 水平对齐
            pcell.setHorizontalAlignment(convertAlignment(format.getAlignment(), cell.getType()));
            // 垂直对齐
            pcell.setVerticalAlignment(convertVerticalAlignment(format.getVerticalAlignment()));
            // 背景
           // pcell.setBorderWidthBottom(1.0f);
    		//pcell.setBorderWidthRight(1.0f);

            // 处理 border
            BorderLineStyle lineStyle = null;

                lineStyle = format.getBorderLine(jxl.format.Border.BOTTOM);
                if (lineStyle.getValue() == BorderLineStyle.NONE.getValue()){
                    pcell.setBorderColorBottom(Color.WHITE);
                    pcell.setBorderWidthBottom(0.0f);
                }
                else{
                    pcell.setBorderColorBottom(convertColour(format.getBorderColour(jxl.format.Border.BOTTOM), Color.BLACK));
                    pcell.setBorderWidthBottom(convertBorderStyle(lineStyle));
                }
            lineStyle = format.getBorderLine(jxl.format.Border.TOP);
           
            if (lineStyle.getValue() == BorderLineStyle.NONE.getValue()){
                pcell.setBorderColorTop(Color.WHITE);
                pcell.setBorderWidthTop(0.0f);
            }
            else{
                pcell.setBorderColorTop(convertColour(format.getBorderColour(jxl.format.Border.TOP), Color.BLACK));
                pcell.setBorderWidthTop(convertBorderStyle(lineStyle));//
            }
            lineStyle = format.getBorderLine(jxl.format.Border.LEFT);
            //convertBorderStyle(lineStyle)
            if (lineStyle.getValue() == BorderLineStyle.NONE.getValue()){
                pcell.setBorderColorLeft(Color.WHITE);
                pcell.setBorderWidthLeft(0.0f);
            }
            else{
                pcell.setBorderColorLeft(convertColour(format.getBorderColour(jxl.format.Border.LEFT), Color.BLACK));
                pcell.setBorderWidthLeft(convertBorderStyle(lineStyle));
            }
            lineStyle = format.getBorderLine(jxl.format.Border.RIGHT);
           
            if (lineStyle.getValue() == BorderLineStyle.NONE.getValue()){
                pcell.setBorderColorRight(Color.WHITE);
                pcell.setBorderWidthRight(0.0f);
            }
            else{
                pcell.setBorderColorRight(convertColour(format.getBorderColour(jxl.format.Border.RIGHT), Color.BLACK));
                pcell.setBorderWidthRight(convertBorderStyle(lineStyle));
            }
            if (format.getBackgroundColour().getValue() != Colour.DEFAULT_BACKGROUND.getValue()) {
                pcell.setBackgroundColor(convertColour(format.getBackgroundColour(), Color.WHITE));
              }
        }else{
        	pcell.setBorder(0);
        }

	}
	/**
	 * 转换单元格的格式 com.lowagie.text.Cell
	 * @param pdfCell
	 * @param cell
	 * @param mergeRow
	 */
	/**
	 * @param pcell
	 * @param cell
	 * @param r
	 * @param c
	 * @param rows
	 * @param cols
	 * @param range
	 * @param lineNum
	 */
	private void transferFormat(com.lowagie.text.Cell pcell, Cell cell,int r,int c, Set<String> lineNum) {
        jxl.format.CellFormat format = cell.getCellFormat();
        if (format != null) {
            // 水平对齐
            pcell.setHorizontalAlignment(convertAlignment(format.getAlignment(), cell.getType()));
            // 垂直对齐
            pcell.setVerticalAlignment(convertVerticalAlignment(format.getVerticalAlignment()));
            // 背景
            if (format.getBackgroundColour().getValue() != Colour.DEFAULT_BACKGROUND.getValue()) {
                pcell.setBackgroundColor(convertColour(format.getBackgroundColour(), Color.WHITE));
            }
            
            // 处理 border
            pcell.setBorderWidthBottom(0);
            pcell.setBorderWidthTop(0);
            pcell.setBorderWidthLeft(0);
            pcell.setBorderWidthRight(0);
            pcell.setBorderColorBottom(Color.WHITE);
            pcell.setBorderColorTop(Color.WHITE);
            pcell.setBorderColorLeft(Color.WHITE);
            pcell.setBorderColorRight(Color.WHITE);
            
            BorderLineStyle b_lineStyle = format.getBorderLine(jxl.format.Border.BOTTOM);
            BorderLineStyle t_lineStyle = format.getBorderLine(jxl.format.Border.TOP);
            BorderLineStyle l_lineStyle = format.getBorderLine(jxl.format.Border.LEFT);
            BorderLineStyle r_lineStyle = format.getBorderLine(jxl.format.Border.RIGHT);
            
            Colour b_colour = format.getBorderColour(jxl.format.Border.BOTTOM);
            Colour t_colour = format.getBorderColour(jxl.format.Border.TOP);
            Colour l_colour = format.getBorderColour(jxl.format.Border.LEFT);
            Colour r_colour = format.getBorderColour(jxl.format.Border.RIGHT);
            
            String topLine = "(" + c  + "," + r + ")-(" + (c + 1) + "," + r + ")";
            String bottomLine = "(" + c  + "," + (r + 1) + ")-(" + (c + 1) + "," + (r + 1) + ")";
            String leftLine = "(" + c  + "," + r + ")-(" + c + "," + (r + 1) + ")";
            String rightLine = "(" + (c + 1)  + "," + r + ")-(" + (c + 1) + "," + (r + 1) + ")";
            
            if (lineNum.contains(topLine)) {
            	t_lineStyle = null;
    		}
    		if (lineNum.contains(leftLine)) {
    			l_lineStyle = null;
    		}
    		
    		if (lineNum.contains(bottomLine)) {
    			b_lineStyle = null;
    		}
    		
    		if (lineNum.contains(rightLine)) {
    			r_lineStyle = null;
    		}
            
            if (b_lineStyle != null && b_lineStyle.getValue() != BorderLineStyle.NONE.getValue() && !Colour.WHITE.equals(b_colour)) {
                pcell.setBorderColorBottom(new Color(b_colour.getDefaultRed(), b_colour.getDefaultGreen(), b_colour.getDefaultBlue()));
                pcell.setBorderWidthBottom(b_lineStyle.getValue());
                lineNum.add(bottomLine);
            }
            
            if (t_lineStyle != null && t_lineStyle.getValue() != BorderLineStyle.NONE.getValue() && !Colour.WHITE.equals(t_colour)) {
                pcell.setBorderColorTop(new Color(t_colour.getDefaultRed(), t_colour.getDefaultGreen(), t_colour.getDefaultBlue()));
                pcell.setBorderWidthTop(t_lineStyle.getValue());
                lineNum.add(topLine);
            }
            
            if (l_lineStyle != null && l_lineStyle.getValue() != BorderLineStyle.NONE.getValue() && !Colour.WHITE.equals(l_colour)) {
                pcell.setBorderColorLeft(new Color(l_colour.getDefaultRed(), l_colour.getDefaultGreen(), l_colour.getDefaultBlue()));
                pcell.setBorderWidthLeft(l_lineStyle.getValue());
                lineNum.add(leftLine);
            }
            
            if (r_lineStyle != null && r_lineStyle.getValue() != BorderLineStyle.NONE.getValue() && !Colour.WHITE.equals(r_colour)) {
                pcell.setBorderColorRight(new Color(r_colour.getDefaultRed(), r_colour.getDefaultGreen(), r_colour.getDefaultBlue()));
                pcell.setBorderWidthRight(r_lineStyle.getValue());
                lineNum.add(rightLine);
            }
        }else{
        	pcell.setBorder(0);
        }
       }
    
    
	/**
	 * 转换边框样式
	 * @param style
	 * @return
	 */
	private float convertBorderStyle(BorderLineStyle style) {
		if (style == null) return 0.0f;
		
		float w = 0.0f;
		if(BorderLineStyle.HAIR.getValue()==style.getValue()){
			w=0.0f;
		}else if(BorderLineStyle.NONE.getValue() == style.getValue()) {
			// 默认全部使用边框，边框大小 0.5f
			if (noEmptyBorder){
				w = 0.0f;
			}
		}else if(BorderLineStyle.THIN.getValue() == style.getValue()){
			w = 1.0f;
		}else if (BorderLineStyle.THICK.getValue() == style.getValue()) {
			w = 1.5f;
        }else if (BorderLineStyle.MEDIUM.getValue() == style.getValue()) {
			w = 1.0f;
        }else if(BorderLineStyle.DOUBLE.getValue()==style.getValue()){
        	w = 1.5f;
        }else {
			w = 0.0f;
		}
		return w;
	}
	/**
	 * 转换对齐方式
	 * @param align
	 * @param cellType
	 * @return int 
	 */
	private int convertAlignment(Alignment align, CellType cellType) {
		if (align == null)
			return Element.ALIGN_UNDEFINED;
		
		if (Alignment.CENTRE.getValue() == align.getValue())
			return  Element.ALIGN_CENTER;
		
		if (Alignment.LEFT.getValue() == align.getValue())
			return Element.ALIGN_LEFT;
		
		if (Alignment.RIGHT.getValue() == align.getValue())
			return Element.ALIGN_RIGHT;
		
		if (Alignment.JUSTIFY.getValue() == align.getValue())
			return Element.ALIGN_JUSTIFIED;
		
		if (Alignment.GENERAL.getValue() == align.getValue()) {
			// 所有未明确设置对齐方式的元素，都属于 Alignment.GENERAL 类型
			if (cellType == CellType.NUMBER || cellType == CellType.NUMBER_FORMULA)
				return Element.ALIGN_RIGHT;   // 数字右对齐
			if (cellType == CellType.DATE || cellType == CellType.DATE_FORMULA)
				return Element.ALIGN_RIGHT;   // 日期右对齐
		}
		return Element.ALIGN_UNDEFINED;
	}
	/**
	 * 转换垂直对齐方式
	 * @param align - jxl 的对齐方式
	 * @return int
	 */
	private int convertVerticalAlignment(VerticalAlignment align) {
		if (align == null)
			return Element.ALIGN_UNDEFINED;
		
		if (VerticalAlignment.BOTTOM.getValue() == align.getValue())
			return Element.ALIGN_BOTTOM;
		
		if (VerticalAlignment.CENTRE.getValue() == align.getValue())
			return Element.ALIGN_MIDDLE;
		
		if (VerticalAlignment.TOP.getValue() == align.getValue())
			return Element.ALIGN_TOP;
		
		if (VerticalAlignment.JUSTIFY.getValue() == align.getValue())
			return Element.ALIGN_JUSTIFIED;

		return Element.ALIGN_UNDEFINED;
	}
	/**
	 * 读取页眉信息
	 * @param sheet
	 * @return HeaderText
	 */
	private HeaderText readHeader(Sheet sheet){
		int addNum=0,imageNums=0;
		if(sheetImage.getImage()!=null){
			imageNums=sheetImage.getImage().size();
			System.out.println("最后一个sheet 含有图片的数量："+imageNums);
		}
		HeaderText text=new HeaderText();
		PdfPCell pdfCell = null;
		Phrase content = null;
		if(sheet!=null&&sheet.getColumns()>0){
			Cell cell1=sheet.getCell(0,1);
		  if(cell1!=null&& cell1.getContents().equalsIgnoreCase("t")){
			  Cell cell = sheet.getCell(0,0);//取得每一行的单元格
				jxl.format.CellFormat format = cell.getCellFormat();//取得单元格的格式
				Font font = null;
				if (format != null && format.getFont() != null) {
					font = convertFont(format.getFont());// 调用convertFont()的方法转变字体
				} else {
					font = new Font(Font.COURIER, 10.0f, Font.NORMAL, Color.BLACK);
					//font = ChineseFont.createChineseFont(10,Font.NORMAL,Color.BLACK);
				}
				if(cell.getContents()==null){
					  text.setHeaderText1(new PdfPCell(new Phrase(" ")));
				}else{
				content = new Phrase(cell.getContents(), font);
				pdfCell = new com.lowagie.text.pdf.PdfPCell(content);
				transferFormat2(pdfCell, cell);
			    text.setHeaderText1(pdfCell);
				}
		  }else if(cell1!=null&&cell1.getContents().equalsIgnoreCase("i")){
			  if(sheetImage.getImage()!=null&&sheetImage.getImage().size()>0){
			  pdfCell=new PdfPCell((Image)sheetImage.getImage().get(0));
			  pdfCell.setBorder(0);
			  text.setHeaderText1(pdfCell);
			  addNum++;
			  }else{
				  text.setHeaderText1(new PdfPCell(new Phrase(" ")));
			  }
		  }
		  Cell cell2=sheet.getCell(1,1);
		  if(cell2!=null&& cell2.getContents().equalsIgnoreCase("t")){
				  Cell cell = sheet.getCell(1,0);//取得每一行的单元格
					jxl.format.CellFormat format = cell.getCellFormat();//取得单元格的格式
					Font font = null;
					if (format != null && format.getFont() != null) {
						font = convertFont(format.getFont());// 调用convertFont()的方法转变字体
					} else {
						font = new Font(Font.COURIER, 10.0f, Font.NORMAL, Color.BLACK);
						//font = ChineseFont.createChineseFont(10,Font.NORMAL,Color.BLACK);
					}if(cell.getContents()==null){
						text.setHeaderText2(new PdfPCell(new Phrase(" ")));
					}else{
					content = new Phrase(cell.getContents(), font);
					pdfCell = new com.lowagie.text.pdf.PdfPCell(content);
					transferFormat2(pdfCell, cell);
			    	text.setHeaderText2(pdfCell);
					}
			  }else if(cell2!=null&&cell2.getContents().equalsIgnoreCase("i")){
				  if(sheetImage.getImage()!=null&&sheetImage.getImage().size()>0){
				  if(addNum==0){
					  Image image=(Image) sheetImage.getImage().get(0);
					  pdfCell=new PdfPCell(image);
					  pdfCell.setBorder(0);
					  text.setHeaderText2(pdfCell);
				  }else if(addNum==1&&addNum<imageNums){
					  pdfCell=new PdfPCell((Image)sheetImage.getImage().get(1));
					  pdfCell.setBorder(0);
					  text.setHeaderText2(pdfCell);
				  }
				  addNum++;
				  }else{
					  text.setHeaderText2(new PdfPCell(new Phrase(" "))); 
				  }
			  }
		  Cell cell3=sheet.getCell(2,1);
		  if(cell3!=null&& cell3.getContents().equalsIgnoreCase("t")){
				//text.setHeaderText3(sheet.getCell(2,0).getContents());
			  Cell cell = sheet.getCell(2,0);//取得每一行的单元格
				jxl.format.CellFormat format = cell.getCellFormat();//取得单元格的格式
				Font font = null;
				if (format != null && format.getFont() != null) {
					font = convertFont(format.getFont());// 调用convertFont()的方法转变字体
				} else {
					font = new Font(Font.COURIER, 10.0f, Font.NORMAL, Color.BLACK);
					//font = ChineseFont.createChineseFont(10,Font.NORMAL,Color.BLACK);
				}if(cell.getContents()==null){
					text.setHeaderText3(new PdfPCell(new Phrase(" ")));
				}else{
				content = new Phrase(cell.getContents(), font);
				pdfCell = new com.lowagie.text.pdf.PdfPCell(content);
				transferFormat2(pdfCell, cell);
			    text.setHeaderText3(pdfCell);
				}
			  }else if(cell3!=null&&cell3.getContents().equalsIgnoreCase("i")){
				  if(sheetImage.getImage()!=null&&sheetImage.getImage().size()>0){
				  if(addNum==0){
					  pdfCell=new PdfPCell((Image)sheetImage.getImage().get(0));
					  pdfCell.setBorder(0);
					  text.setHeaderText3(pdfCell);
					  //text.setHeaderText3(sheetImage.getImage().get(0));
				  }else if(addNum==1&&addNum<imageNums){
					  pdfCell=new PdfPCell((Image)sheetImage.getImage().get(1));
					  pdfCell.setBorder(0);
					  text.setHeaderText3(pdfCell);
					  //text.setHeaderText3(sheetImage.getImage().get(0+1));
				  }else if(addNum==2&&addNum<imageNums){
					  pdfCell=new PdfPCell((Image)sheetImage.getImage().get(2));
					  pdfCell.setBorder(0);
					  text.setHeaderText3(pdfCell);
				 // text.setHeaderText3(sheetImage.getImage().get(0+2));
				  }
				  }else{
					  text.setHeaderText3(new PdfPCell(new Phrase(" ")));
				  }
			  }
		}
	    return text;
	    }
	/***
	 * 判断下边框是否是默认边框
	 * @param cell
	 * @return
	 */
	private boolean cellBottomBorderLineStyle(Cell cell){
		boolean b=false;
		jxl.format.CellFormat format = cell.getCellFormat();
		if(format!=null){
			BorderLineStyle lineStyle = null;
            lineStyle = format.getBorderLine(jxl.format.Border.BOTTOM);
            if (lineStyle.getValue() == BorderLineStyle.NONE.getValue()){
              b=true;
            }
		}
		return b;
	}
	public int getIRows() {
		return iRows;
	}
	public void setIRows(int rows) {
		iRows = rows;
	}
	public int getITotalrows() {
		return iTotalrows;
	}
	public void setITotalrows(int totalrows) {
		iTotalrows = totalrows;
	}
	/**
	 * 判断左边框是否是默认边框
	 * @param cell
	 * @return
	 */
	private boolean cellLeftBorderLineStyle(Cell cell){
		boolean b=false;
		jxl.format.CellFormat format = cell.getCellFormat();
		if(format!=null){
			BorderLineStyle lineStyle = null;
            lineStyle = format.getBorderLine(jxl.format.Border.RIGHT);
            if (lineStyle.getValue() == BorderLineStyle.NONE.getValue()){
              b=true;
            }
		}
		return b;
	}
	private boolean cellRightBorderLineStyle(Cell cell){
		boolean b=false;
		jxl.format.CellFormat format = cell.getCellFormat();
		if(format!=null){
			BorderLineStyle lineStyle = null;
            lineStyle = format.getBorderLine(jxl.format.Border.LEFT);
            if (lineStyle.getValue()!= BorderLineStyle.NONE.getValue()){
              b=true;
            }
		}
		return b;
	}
	
	private class PdfCellUtil {
		private int row;
		private int col;
		private com.lowagie.text.Cell cell;
	}
}

