package com.est.common.ext.util.classutil;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 *@desc Morphs a String to a Date. fix the defaultValue get/set method
 *@author jingpj
 *@date Feb 7, 2010
 *@path com.est.common.ext.util.classutil.DateMorpher
 *@corporation Enstrong S&T
 */
public final class DateMorpher extends AbstractObjectMorpher
{
   private Date defaultValue;
   private String[] formats;
   private boolean lenient;
   private Locale locale;

   /**
    * @param formats a list of formats this morpher supports.
    */
   public DateMorpher( String[] formats )
   {
      this( formats, Locale.getDefault(), false );
   }

   /**
    * @param formats a list of formats this morpher supports.
    * @param lenient if the parsing should be lenient or not.
    */
   public DateMorpher( String[] formats, boolean lenient )
   {
      this( formats, Locale.getDefault(), lenient );
   }

   /**
    * @param formats a list of formats this morpher supports.
    * @param defaultValue return value if the value to be morphed is null.
    */
   public DateMorpher( String[] formats, Date defaultValue )
   {
      this( formats, defaultValue, Locale.getDefault(), false );
   }

   /**
    * @param formats a list of formats this morpher supports.
    * @param defaultValue return value if the value to be morphed is null.
    * @param locale the Locale used to parse each format.
    * @param lenient if the parsing should be lenient or not.
    */
   public DateMorpher( String[] formats, Date defaultValue, Locale locale, boolean lenient )
   {
      super( true );
      if( formats == null || formats.length == 0 ){
         throw new MorphException( "invalid array of formats" );
      }
      // should use defensive copying ?
      this.formats = formats;

      if( locale == null ){
         this.locale = Locale.getDefault();
      }else{
         this.locale = locale;
      }

      this.lenient = lenient;
      setDefaultValue( defaultValue );
   }

   /**
    * @param formats a list of formats this morpher supports.
    * @param locale the Locale used to parse each format.
    */
   public DateMorpher( String[] formats, Locale locale )
   {
      this( formats, locale, false );
   }

   /**
    * @param formats a list of formats this morpher supports.
    * @param locale the Locale used to parse each format.
    * @param lenient if the parsing should be lenient or not.
    */
   public DateMorpher( String[] formats, Locale locale, boolean lenient )
   {
      if( formats == null || formats.length == 0 ){
         throw new MorphException( "invalid array of formats" );
      }
      // should use defensive copying ?
      this.formats = formats;

      if( locale == null ){
         this.locale = Locale.getDefault();
      }else{
         this.locale = locale;
      }

      this.lenient = lenient;
   }

   @Override
public boolean equals( Object obj )
   {
      if( this == obj ){
         return true;
      }
      if( obj == null ){
         return false;
      }

      if( !(obj instanceof DateMorpher) ){
         return false;
      }

      DateMorpher other = (DateMorpher) obj;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( formats, other.formats );
      builder.append( locale, other.locale );
      builder.append( lenient, other.lenient );
      if( isUseDefault() && other.isUseDefault() ){
         builder.append( getDefaultValue(), other.getDefaultValue() );
         return builder.isEquals();
      }else if( !isUseDefault() && !other.isUseDefault() ){
         return builder.isEquals();
      }else{
         return false;
      }
   }

   /**
    * Returns the default value for this Morpher.
    */
   public Date getDefaultValue()
   {
      if(defaultValue==null) {
    	  return null;
      } else {
    	  return (Date) defaultValue.clone();
      }
   }

   @Override
public int hashCode()
   {
      HashCodeBuilder builder = new HashCodeBuilder();
      builder.append( formats );
      builder.append( locale );
      builder.append( lenient );
      if( isUseDefault() ){
         builder.append( getDefaultValue() );
      }
      return builder.toHashCode();
   }

   @Override
public Object morph( Object value )
   {
      if( value == null ){
         return null;
      }

      if( Date.class.isAssignableFrom( value.getClass() ) ){
         return value;
      }

      if( !supports( value.getClass() ) ){
         throw new MorphException( value.getClass() + " is not supported" );
      }

      String strValue = (String) value;
      SimpleDateFormat dateParser = null;

      for( int i = 0; i < formats.length; i++ ){
         if( dateParser == null ){
            dateParser = new SimpleDateFormat( formats[i], locale );
         }else{
            dateParser.applyPattern( formats[i] );
         }
         dateParser.setLenient( lenient );
         try{
            return dateParser.parse( strValue.toLowerCase() );
         }
         catch( ParseException pe ){
            // ignore exception, try the next format
         }
      }

      // unable to parse the date
      if( isUseDefault() ){
         return defaultValue;
      }else{
         throw new MorphException( "Unable to parse the date " + value );
      }
   }

   @Override
public Class morphsTo()
   {
      return Date.class;
   }

   /**
    * Sets the defaultValue to use if the value to be morphed is null.
    *
    * @param defaultValue return value if the value to be morphed is null
    */
   public void setDefaultValue( Date defaultValue )
   {
	   if(defaultValue!=null) {
		   this.defaultValue = (Date) defaultValue.clone();
	   } else {
		   this.defaultValue = null;
	   }
   }

   @Override
public boolean supports( Class clazz )
   {
      return String.class.isAssignableFrom( clazz );
   }
}