<%@page import="at.gv.egiz.pdfas.web.config.WebConfiguration"%>
<%@page import="at.gv.egiz.pdfas.web.helper.PdfAsHelper"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>PDF-Signatur</title>
     <link rel="stylesheet" href="assets/css/style.css">    
    
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.min.js"></script>
	<script src="assets/js/pdf.js/build/pdf.js"></script>
	<script src="assets/js/dragNdrop.js"></script>
	
	<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <script src="assets/bootstrap/js/bootstrap.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-nav2/dist/bootstrap-nav-wizard.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
</head>
<body>
 <header>
</header>

	<!-- Old sidebar -->
	<!-- <div id="sidebar">
		 <div id="fileSelector" class="container">
			<h3 class="center">File Selector</h3>
			<div id="dropzone" class="center">
				Drag and Drop your Document here ...
			</div>
			  <div id="traditionalUpload">
				<p>... or select File here</p>
				<input type="file" name="pdf-file" id="pdf-file" accept="application/pdf">
			</div>
			
		</div>  

		<div id="signMethod" class="container">
			<h3 class="center">Sign Method</h3>
			<fieldset>
				<table>
					<%
					if (WebConfiguration.getHandyBKUURL() != null) {
					%>
					<tr>
						<td><input type="radio" id="mobileBKU" name="connector" value="mobilebku" checked></td>
						<td><label for="mobileBKU">Handy</td>
						<td><label for="mobileBKU"><img src="assets/img/mobileBKU.png" alt="Sign via mobile BKU"/></td>
						
					</tr>
					<%
						}
					%>
					<%
					if (WebConfiguration.getLocalBKUURL() != null) {
					%>
					<tr>
						<td><input type="radio" id="localBKU" name="connector" value="bku"></td>
						<td><label for="localBKU">Lokale BKU</label></td>
						<td><label for="localBKU"><img src="assets/img/onlineBKU.png" alt="Sign via local BKU" /></label></td>
						
					</tr>
					<%
						}
					%>
					<%
					if (WebConfiguration.getOnlineBKUURL() != null) {
					%>
					<tr>
						<td><input type="radio" id="onlineBKU" name="connector" value="onlinebku"></td>
						<td><label for="onlineBKU">Online BKU</label></td>
						<td><label for="onlineBKU"><img src="assets/img/onlineBKU.png" alt="Sign via online BKU"/></label></td>
						
					</tr>
					<%
						}
					%>
					<%
						if(WebConfiguration.getKeystoreDefaultEnabled()) {
					%>
					<tr>
						<td><input type="radio" id="jks" name="connector" value="jks"></td>
						<td><label for="jks">Server Keystore</label></td>
						<td><label for="jks"><img src="assets/img/onlineBKU.png" alt="Sign via Server Keystore"/></label></td>
					</tr>
					<%
						}
					%> 
				</table>
			</fieldset>
		</div>
		
		<div id="language" class="container">
			<h3 class="center">Language</h3>
				<fieldset>
				  <input type="radio" id="EN" name="locale" value="EN" checked><label for="EN">Englisch</label><br>
				  <input type="radio" id="DE" name="locale" value="DE"><label for="DE">Deutsch</label><br> 
				</fieldset>
		</div>
		
		<div id="btnSign">
			<h3 class="center">Sign Document</h3>
		</div>
	</div>
	-->
	
<div class="container">
	<!-- Header -->
<div class="center">
	  <div class="center" id="navBar">
	  <ul class="nav nav-wizard">
	    <li class="active" id="UploadStepButton"><a href="#"><span class="glyphicon glyphicon-open-file" aria-hidden="true"></span><span id="uploadNavText"> Upload</span></a></li>
	    <li id="PlaceStepButton" style="pointer-events:none;"><a href="#"><span class="glyphicon glyphicon-move" aria-hidden="true"></span><span id="placeNavText" style="display:none;"> Place</span></a></li>
	    <li id="SignStepButton" style="pointer-events:none;"><a href="#"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span><span id="signNavText" style="display:none;"> Sign</span></a></li>
	    <li id="FinishStepButton" style="pointer-events:none;"><a href="#"><span class="glyphicon glyphicon-save-file" aria-hidden="true"></span><span id="downloadNavText" style="display:none;"> Download</span></a></li>
	  </ul>
	  </div>
 </div>

	
<!-- Main Switch Frame -->
<div class="row">

	<div class="col-md-12">
	
		<div id="DropContainer">
			<h3 class="center">Upload your Document</h3>
			<div class="center">
				<div id="borderBox" class="center">
					<div>
						<h5 class="center"> Drop your file here</h5>
						<h5 class="center">... or select File here</h5>
							<div id="FormDefine" class="form-group has-feedback">
					            <div class="input-group">
					                <span class="input-group-btn">
					                    <span class="btn btn-primary btn-file">
					                     Browse... <input type="file" name="pdf-file" id="pdf-file" accept="application/pdf">
					                    </span>
					                </span>
					                <input id= "FileNamePreview" type="text" class="form-control" readonly>
					                <span id="BadFeedback" class="glyphicon glyphicon-remove form-control-feedback" style="display: none;" aria-hidden="true"></span>
					                <span id="GoodFeedback" class="glyphicon glyphicon-ok form-control-feedback" style="display: none;" aria-hidden="true"></span>
					            </div>
					            <div id="fileTypeErrorMessage" class="pull-right">
					            <p id="noPdfMessage" style="display:none;"><span class="glyphicon glyphicon-alert" aria-hidden="true"></span> The file type must be PDF!</p>
					            </div>

				            </div>	
				            
				            <div class="col-md-12 center">
				            <button id="uploadContinue" class="btn btn-primary btn-lg btn-block" disabled="disabled" >Continue</button>
				            </div>				        
					</div>
				</div>	
			</div>		
		</div>

		<div id="ViewContainer" style="display: none;">
			<h3 class="center">Place your Signature</h3>
			<h6 class="center" id="noSignatureWarning">(If no Signature is placed, one will be appended at the end of the Document)</h6>
			<div class="center">
				<div id="ViewerExternToolbar" class="center">
				<div id="placeSignatureExtern">
				<img src="assets/img/federohne.svg" title="Place Signature on current Page"><br>Add
				</div>
	            <div id="placeContinue">
	            <img src="assets/img/federohne.svg" title="Continue"><br>Continue
	            </div>
                </div>
             </div>
			<div id="content" class="center">
			Loading your PDF, please wait...
			</div>
		</div>
		
		<div id="SignContainer" style="display: none;">
			<h3 class="center">Choose your Sign Method</h3>
				<fieldset>
				<div class="center">
					<div id="methodContainer" class="row center">
						<%
						if (WebConfiguration.getHandyBKUURL() != null) {
						%>
						
						<div class="methodChooseContainer">
							<div id="ImageBox"><img src="assets/img/mobileBKU.png" alt="Sign via mobile BKU"/></div>
							<div><button class="btn btn-primary">Mobile<br>Phone</button></div>
							<div style="display:none;"><input type="radio" id="mobileBKU" name="connector" value="mobilebku" checked></div>
						</div>
						<%
							}
						%>
						<%
						if (WebConfiguration.getLocalBKUURL() != null) {
						%>
						<div class="methodChooseContainer">
							<div id="ImageBox"><img class="BKUImage" src="assets/img/onlineBKU.png" alt="Sign via local BKU" /></div>
							<div><button class="btn btn-primary">Lokale<br>BKU</button></div>
							<div style="display:none;"><input type="radio" id="localBKU" name="connector" value="bku"></div>
						</div>
						<%
							}
						%>
						<%
						if (WebConfiguration.getOnlineBKUURL() != null) {
						%>
						<div class="methodChooseContainer">
							<div id="ImageBox"><img class="BKUImage" src="assets/img/onlineBKU.png" alt="Sign via online BKU"/></div>
							<div><button class="btn btn-primary">Online<br>BKU</button></div>
							<div style="display:none;"><input type="radio" id="onlineBKU" name="connector" value="onlinebku"></div>
						</div>
						<%
							}
						%>
						<!--<%
							if(WebConfiguration.getKeystoreDefaultEnabled()) {
						%>
							<div id="ImageBox"><img class="BKUImage" src="assets/img/onlineBKU.png" alt="Sign via Server Keystore"/></div>
						    <div><button class="btn btn-primary">Server<br>Keystore</button></div>
							<div style="display:none;"><input type="radio" id="jks" name="connector" value="jks"></div>
						</div> -->
						<%
							}
						%>  
					</div>
				</div>
				</fieldset>
		</div>
		<div id="DownloadContainer" style="display: none;">
			<h3 class="center">Download your Document</h3>
			
			<div id="btnSign">
				<h3 class="center">Sign Document</h3>
			</div>
		
		</div>
				
	</div>
</div>

</div>
	
	
	
	
	<!--<form action="Sign" method="POST"
		enctype="multipart/form-data">
		<input type="hidden" name="source" id="source" value="internal" /> 
		 <input type="file" name="pdf-file" id="pdf-file" accept="application/pdf"> 
		<%
			if (request.getAttribute("FILEERR") != null) {
		%>
		<p>Bitte die zu signierende PDF Datei angeben.</p>
		<%
			}
		%>


		<%
			if (WebConfiguration.getLocalBKUURL() != null) {
		%>
		<img src="assets/img/onlineBKU.png" alt="Sign via local BKU" /> <button type="submit"
			value="bku" name="connector" id="bku">Lokale BKU
		</button>
		<%
			}
		%>
		<%
			if (WebConfiguration.getOnlineBKUURL() != null) {
		%>
		<img src="assets/img/onlineBKU.png" alt="Sign via online BKU"/>
		<button type="submit" value="onlinebku" name="connector"
			id="onlinebku">Online BKU</button>
		<%
			}
		%>
		<%
			if (WebConfiguration.getHandyBKUURL() != null) {
		%>
		<img src="assets/img/mobileBKU.png" alt="Sign via mobile BKU"/>
		<button type="submit" value="mobilebku" name="connector" id="mobilebku">Handy</button>
		<%
			}
		%>
		<%
			if (WebConfiguration.getKeystoreDefaultEnabled()) {
		%>
			<button type="submit" value="jks" name="connector"
				id="jks">Server Keystore</button>
		<%
			}
		%>
		<%
			if (WebConfiguration.getMOASSEnabled()) {
		%>
			<button type="submit" value="moa" name="connector"
				id="moa">MOA-SS</button>
		<%
			}
		%>
		
		<select name="locale" id="locale" size="3">
      		<option>EN</option>
      		<option>DE</option>
    	</select>
		
	</form>
	
	<p><small>Version: <%= PdfAsHelper.getVersion() %> - <%= PdfAsHelper.getSCMRevision() %></small></p>-->
	<footer>
	</footer>
</body>
</html>