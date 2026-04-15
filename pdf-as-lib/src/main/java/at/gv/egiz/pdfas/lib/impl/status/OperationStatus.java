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
package at.gv.egiz.pdfas.lib.impl.status;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.common.utils.TempFileHelper;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.backend.PDFASBackend;
import at.gv.egiz.pdfas.lib.impl.configuration.GlobalConfiguration;
import at.gv.egiz.pdfas.lib.impl.configuration.PlaceholderConfiguration;
import at.gv.egiz.pdfas.lib.impl.configuration.SignatureProfileConfiguration;
import at.gv.egiz.pdfas.lib.util.TimedFunction;
import lombok.Getter;
import lombok.Setter;

public class OperationStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2985007198666388528L;

	private SignParameter signParamter;
	@Setter
    @Getter
    private PDFObject pdfObject;

	private final ISettings configuration;
	private PlaceholderConfiguration placeholderConfiguration = null;
	private GlobalConfiguration globalConfiguration = null;
	private final Map<String, SignatureProfileConfiguration> signatureProfiles = new HashMap<String, SignatureProfileConfiguration>();
	private TempFileHelper helper;
	@Setter
    @Getter
    private RequestedSignature requestedSignature;
	@Setter
    @Getter
    private Calendar signingDate;
	@Getter
    private final PDFASBackend backend;
	@Getter
    private final Map<String, String> metaInformations = new HashMap<String, String>();
    @Getter
    private final TimedFunction.Context signTimer;

//	private HashMap<String, String> requestParameters = new HashMap<String, String>();

	public OperationStatus(ISettings configuration, SignParameter signParameter, PDFASBackend backend, TimedFunction.Context timer) {
		this.configuration = configuration;
		this.signParamter = signParameter;
		this.backend = backend;
        this.signTimer = timer;
		helper = new TempFileHelper(configuration);
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.helper != null) {
			try {
				this.helper.clear();
			} catch (Throwable ignored) {
			}
		}
		super.finalize();
	}

	// ========================================================================
	
	public void clear() {
		if (this.helper != null) {
			try {
				this.helper.clear();
			} catch (Throwable ignored) {
			}
		}
		if(pdfObject != null) {
			pdfObject.close();
		}
	}

    public PlaceholderConfiguration getPlaceholderConfiguration() {
      if (this.placeholderConfiguration == null) {
          this.placeholderConfiguration = new PlaceholderConfiguration(
                  this.configuration);
      }
      return this.placeholderConfiguration;
	}

	public GlobalConfiguration getGlobalConfiguration() {
		if (this.globalConfiguration == null) {
			this.globalConfiguration = new GlobalConfiguration(
					this.configuration);
		}
		return this.globalConfiguration;
	}

	public SignatureProfileConfiguration getSignatureProfileConfiguration(
			String profileID) {

		SignatureProfileConfiguration signatureProfileConfiguration = signatureProfiles
				.get(profileID);
		if (signatureProfileConfiguration == null) {
			signatureProfileConfiguration = new SignatureProfileConfiguration(
					this.configuration, profileID);
			signatureProfiles.put(profileID, signatureProfileConfiguration);
		}

		return signatureProfileConfiguration;
	}

	// ========================================================================

    public SignParameter getSignParameter() {
		return signParamter;
	}

	public TempFileHelper getTempFileHelper() {
		return this.helper;
	}

	public ISettings getSettings() {
		return this.configuration;
	}

    public String getTransactionId() {
      if(this.signParamter != null) {
          return this.signParamter.getTransactionId();
      }
      return null;
	}
}
