/*******************************************************************************
 * <copyright> Copyright 2014 by E-Government Innovation Center EGIZ, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * 
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 ******************************************************************************/
/**
 * <copyright> Copyright 2006 by Know-Center, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 *
 * $Id: TablePos.java,v 1.1 2006/08/25 17:10:08 wprinz Exp $
 */
package at.knowcenter.wag.egov.egiz.pdf;

import java.io.Serializable;
import java.text.MessageFormat;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that holds the exact position where the table should be written to the
 * document.
 * 
 * @author wprinz
 * @author mruhmer
 */
@NoArgsConstructor
public class TablePos implements Serializable
{

  public enum PAGE_MODE {AUTO, LAST, EXACT, NEW}; 
  public enum POS_MODE {AUTO, EXACT};
  
  
  /**
   * SVUID.
   */
  private static final long serialVersionUID = -5299027706623518059L;

  /**
   * The page on which the block should be displayed.
   * 
   */
  @Getter
  private int page = 0;

  /**
   * The x position.
   */
  @Getter
  private float posX = 0.0f;

  /**
   * The y position.
   */
  @Getter
  private float posY = 0.0f;

  /**
   * The width of the block.
   */
  @Getter
  @Setter
  private float width = 0.0f;
  
  /**
   * The top y position of the footer line.
   */
  private float footerLine = 0.0f;
  
  /**
   * The rotation of the signature block
   */
  @Getter
  private float rotation = 0.0f;
  
     
  @Getter
  private PAGE_MODE pageMode = PAGE_MODE.AUTO;
  
  @Getter
  private POS_MODE xMode = POS_MODE.AUTO;
  
  @Getter
  private POS_MODE yMode = POS_MODE.AUTO;
  
  @Getter
  private POS_MODE wMode = POS_MODE.AUTO;
  
  public boolean isXauto() {
    return xMode.equals(POS_MODE.AUTO);
  
  }
  
  public boolean isYauto() {
    return yMode.equals(POS_MODE.AUTO);
	
  } 
  
  public boolean isWauto() {
    return wMode.equals(POS_MODE.AUTO);
  }
    
  public float getFooterLine() {
    //ignore if newpage and y is not auto
    return yMode.equals(POS_MODE.EXACT) || pageMode.equals(PAGE_MODE.NEW) ? 0.0f : footerLine;  
      
  } 
  
  /**
   * Constructor.
   * 
   * @param pos_string The pos instruction.
   *        format : [x:x_algo];[y:y_algo];[w:w_algo][p:p_algo];[f:f_algo];[r:r_algo]
   *        x_algo:='auto'     ... automatic positioning x
   *                floatvalue ... absolute x
   *        y_algo:='auto'     ... automatic positioning y
   *                floatvalue ... absolute y
   *        w_algo:='auto'     ... automatic width
   *                floatvalue ... absolute width    
   *        p_algo:='auto'     ... automatic last page
   *                'new'      ... new page
   *                'last'     ... force last page  
   *                intvalue   ... pagenumber
   *        f_algo  floatvalue ... consider footerline (only if y_algo is auto and p_algo is not 'new')
   *        r_algo  floatvalue ... rotate the table arround the lower left corner anti clockwise in degree
   * @throws PdfAsException
   */
  public TablePos(String pos_string) throws PdfAsException {
    parsePosString(pos_string);
    
  }
  
  /**
   * Constructor
   * @param pos_string The pos instruction.
   *    format : [x:x_algo];[y:y_algo];[w:w_algo][p:p_algo];[f:f_algo];[r:r_algo]
   *        x_algo:='auto'     ... automatic positioning x
   *                floatvalue ... absolute x
   *        y_algo:='auto'     ... automatic positioning y
   *                floatvalue ... absolute y
   *        w_algo:='auto'     ... automatic width
   *                floatvalue ... absolute width    
   *        p_algo:='auto'     ... automatic position
   *                'new'      ... new page 
   *                'last'     ... force last page 
   *                intvalue   ... pagenumber
   *        f_algo  floatvalue ... consider footerline (only if y_algo is auto and p_algo is not 'new')
   *        r_algo  floatvalue ... rotate the table arround the lower left corner anti clockwise in degree
   * @param basePosition The base Table Position these values are the base values
   * @throws PdfAsException
   */
  public TablePos(String pos_string, TablePos basePosition) throws PdfAsException {
    if(basePosition != null) {
      readFromPos(basePosition);
      
    }
    parsePosString(pos_string);
    
  }
  
  public String toString() {  
   String thatsme = "pos_x:"+this.posX+" pos_y:"+this.posY+" page:"+this.page+" width:"+this.width+" footer:"+this.footerLine+" rotation:"+this.rotation+"\n "+" autoX:"+xMode+" autoY:"+yMode+" autoW:"+wMode+" pageMode:"+pageMode; 
   return thatsme;
   
  }
  
  
  private float parseAndCheck(String commandval, int minValue, String errorMsg) 
      throws PdfAsException, NumberFormatException {    
    float value= Float.parseFloat(commandval);
    if (value < minValue) {
      throw new PdfAsException(MessageFormat.format(errorMsg, value));
          
    }       
    return value;
       
  }
  
  private void parsePosString(String pos_string) throws PdfAsException {
	//parse posstring and throw exception
		//[x:x_algo];[y:y_algo];[w:w_algo][p:p_algo];[f:f_algo]
		
		String[] strs = pos_string.split(";");
		try {
		  for (int cmds = 0;cmds<strs.length;cmds++) {			 
			 String cmd_kvstring = strs[cmds];
			 String[] cmd_kv = cmd_kvstring.split(":");
			 
			 if (cmd_kv.length != 2) {
				 throw new PdfAsException("Pos string (=" + pos_string + ") is invalid.");
				 
			 }
			 
			 String cmdstr =  cmd_kv[0];
			 if (cmdstr.length() != 1) {
				 throw new PdfAsException("Pos string (=" + pos_string + ") is invalid.");
				 
			 }
			 
			 char command = cmdstr.charAt(0);
		   String commandval= cmd_kv[1];
		   switch (command) {
		     	case 'x': {
		     		         if (!commandval.equalsIgnoreCase("auto")) {	     		          
		     		        	this.posX = parseAndCheck(commandval, 0, "Pos string (x:{0}) is invalid.");
		     		        	this.xMode = POS_MODE.EXACT;		     		        	
		     		         }
		     		         break;
		     			  }	
		     	case 'y': {
			         		if (!commandval.equalsIgnoreCase("auto")) {			         			
			         			this.posY = parseAndCheck(commandval, 0, "Pos string (y:{0}) is invalid.");
			         			this.yMode = POS_MODE.EXACT; 			         			
			         		}    		         
			         		break;
		     			  }		
		     	case 'w': { 
	         				if (!commandval.equalsIgnoreCase("auto")) {
	         					this.width = parseAndCheck(commandval, 1, "pos.width (w:{0}) must not be lower or equal 0.");
	         					this.wMode = POS_MODE.EXACT; 
	         				}      		         
	         				break;
	      				  }
		     	case 'p': {
	 						if (!commandval.equalsIgnoreCase("auto")) { 
	 							if (commandval.equalsIgnoreCase("new")) { 								
	 							 pageMode = PAGE_MODE.NEW;
	 							  
	 							} else if (commandval.equalsIgnoreCase("last")) {                 
	                 pageMode = PAGE_MODE.LAST;
	                  	 							 
	 							} else {
	 								int pval = Integer.parseInt(commandval);
	 								if (pval<1) {
	 									throw new PdfAsException("Page (p:" + pval + ") must not be lower than 1.");
	 									
	 								}
	 								this.page =pval;
	 								pageMode = PAGE_MODE.EXACT;
	 								
	 							}
	 						
	 						} else {
  	 						pageMode = PAGE_MODE.AUTO;
	 							
	 						}
	 						break;
	      				  }
		     	case 'f': {
		     	        footerLine = parseAndCheck(commandval, 0, "Footer pos string (f={0}) is invalid.");
		     	        break;
		     			  }
		     	case 'r': {
	 		        		float flval=Float.parseFloat(commandval);
	 		        		// reduce to 0 - 360 degrees
	 		        		flval = flval % 360;
	 		        		// we only support rotation in 90 degree steps
	 		        		if(flval % 90 != 0) {
	 		        			throw new PdfAsException("Pos string (=" + pos_string + ") is invalid. Rotation is only allowed to be a multiple of 90.");
	 		        		}    
	 		        		this.rotation = flval;
	 		        		break;
	 			  }
		     	default : {
			                throw new PdfAsException("Pos string (=" + pos_string + ") is invalid.");
		                  }
		     }
		  }

	    }
	    catch (NumberFormatException e)
	    {
	      throw new PdfAsException("Pos string (=" + pos_string + ") cannot be parsed.");
	    }
  }
  
  private void readFromPos(TablePos base) {
	  this.pageMode = base.getPageMode();
	  this.xMode = base.getXMode();
	  this.yMode = base.getYMode();	  
	  this.wMode = base.getWMode();	  	  	 
	  
	  this.footerLine = base.getFooterLine();
	  this.page = base.page;
	  this.posX = base.posX;
	  this.posY = base.posY;
	  this.rotation  = base.rotation;
	  this.width = base.width;
	  
  }
  
}
