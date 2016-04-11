package org.htz.core;

import java.util.List;

import com.lowagie.text.Image;
/**
 * 存取excel中图片
 * @author newapps
 * 2009-11-11
 */
public class SheetImage {
	/**图片*/
	private  List image=null;
	/**获得图片的集合*/
	public List getImage() {
		return image;
	}
	/**设置图片*/
	public void setImage(List image) {
		this.image = image;
	}


	
}
