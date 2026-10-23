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
import java.util.HashMap;
import java.util.Map;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.backend.PDFASBackend;
import at.gv.egiz.pdfas.lib.impl.backend.BackendLoader;
import at.gv.egiz.pdfas.lib.impl.configuration.ConfigurationImpl;
import at.gv.egiz.pdfas.lib.impl.configuration.GlobalConfiguration;
import at.gv.egiz.pdfas.lib.impl.configuration.PlaceholderConfiguration;
import at.gv.egiz.pdfas.lib.impl.configuration.SignatureProfileConfiguration;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class OperationStatus implements Serializable {

	private static final long serialVersionUID = -2985007198666388528L;

	private final SignParameter signParamter;
	@Setter
    @Getter
    private PDFObject pdfObject;

	private final ISettings configuration;

	@Getter(lazy = true)
	@Transient
	private final @NonNull PlaceholderConfiguration placeholderConfiguration = new PlaceholderConfiguration(this.configuration);

	@Getter(lazy = true)
	@Transient
	private final @NonNull GlobalConfiguration globalConfiguration = new GlobalConfiguration(this.configuration);

	@Transient
	private final Map<String, SignatureProfileConfiguration> signatureProfiles = new HashMap<>();
	@Setter
    @Getter
    private RequestedSignature requestedSignature;
	@Getter(lazy = true)
	@Transient
    private final PDFASBackend backend = BackendLoader.getPDFASBackend((ConfigurationImpl)this.configuration);
	@Getter
    private final Map<String, String> metaInformations = new HashMap<String, String>();

	public OperationStatus(ISettings configuration, SignParameter signParameter) throws PDFASError {
		this.configuration = configuration;
		this.signParamter = signParameter;
		if (configuration instanceof Configuration config) {
			backend = BackendLoader.getPDFASBackend(config);
		} else {
			backend = BackendLoader.getPDFASBackend();
		}
	}

	// ========================================================================
	
	public void clear() {
		if(pdfObject != null) {
			pdfObject.close();
		}
	}

	public @NonNull SignatureProfileConfiguration getSignatureProfileConfiguration(@NonNull String profileID) {
		return signatureProfiles.computeIfAbsent(profileID,
			key -> new SignatureProfileConfiguration(this.configuration, key)
		);
	}

	// ========================================================================

    public SignParameter getSignParameter() {
		return signParamter;
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
