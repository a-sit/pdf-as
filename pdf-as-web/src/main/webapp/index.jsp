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
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    
</head>
<body>
 <header>
</header>

	<!-- Old sidebar -->
	<!-- 
		<div id="language" class="container">
			<h3 class="center">Language</h3>
				<fieldset>
				  <input type="radio" id="EN" name="locale" value="EN" checked><label for="EN">Englisch</label><br>
				  <input type="radio" id="DE" name="locale" value="DE"><label for="DE">Deutsch</label><br> 
				</fieldset>
		</div>
	-->
	
<div class="container col-lg-8 col-lg-offset-2">
	<!-- Header -->
<div class="row center">
	  <div class="" id="navBar">
	  <ul class="nav nav-wizard">
	    <li class="active" id="UploadStepButton"><a href="#"><span class="glyphicon glyphicon-open-file" aria-hidden="true"></span><span id="uploadNavText"> Upload</span></a></li>
	    <li id="PlaceStepButton" style="pointer-events:none;"><a href="#"><span class="glyphicon glyphicon-move" aria-hidden="true"></span><span id="placeNavText" style="display:none;"> Place&nbsp;&nbsp;</span></a></li>
	    <li id="SignStepButton" style="pointer-events:none;"><a href="#"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span><span id="signNavText" style="display:none;"> Sign&nbsp;&nbsp;&nbsp;&nbsp;</span></a></li>
	    <li id="FinishStepButton" style="pointer-events:none;"><a href="#"><span class="glyphicon glyphicon-save-file" aria-hidden="true"></span><span id="downloadNavText" style="display:none;"> Finish&nbsp;</span></a></li>
	  </ul>
	  </div>
	   <button id="BackBox" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-triangle-left" aria-hidden="true"></span></button>
 </div>
 <div id="OuterBackBox" style="display:none;">
 <div id="InnerBackBox" class="col-sm-1 col-md-1 col-md-offset-4 col-lg-1 col-lg-offset-4">
 <p >Back</p>
 </div>
 </div>

	
<!-- Main Switch Frame -->
<div class="row center">
	
		<div id="DropContainer">
			<div id="borderBox">
				<div class="row mainBox center">
					<div id="FormBox" class="">
						<h5 class="visible-lg-block"> Drop or select your file here</h5>
						<h5 class="hidden-lg">Upload your File</h5>
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
				            
				            <div id="ContinueButtonBox" class="col-md-12 center">
				            <button id="uploadContinue" class="btn btn-primary btn-lg btn-block" disabled="disabled" >Continue</button>
				            </div>				        
					</div>
				</div>
			</div>	
		</div>

		<div id="ViewContainer" style="display: none;">
			<div> <!--  class="center" -->
				<div id="ViewerExternToolbar" class="center pull-right">
				<button id="QuickSign" class="btn btn-primary">Quick Sign</button>
	            <button id="placeContinue" class="btn btn-success">Continue</button>
                </div>
             </div>
			<div id="content" class="">
			Loading your PDF, please wait...
			</div>
		</div>
		
		<div id="SignContainer" style="display: none;">
				<div class="row mainBox center">
					<div id="methodContainer" class="row center">
						<%
						if (WebConfiguration.getHandyBKUURL() != null) {
						%>
						
						<div class="methodChooseContainer">
							<div class="ImageBox" id="MobilePhoneSubmit"><img src="assets/img/mobileBKU.png" alt="Sign via mobile BKU"/>Mobile</div>
							<div style="display:none;"><input type="radio" id="mobileBKU" name="connector" value="mobilebku" checked></div>
						</div>
						<%
							}
						%>
						<%
						if (WebConfiguration.getLocalBKUURL() != null) {
						%>
						<div class="methodChooseContainer">
							<div class="ImageBox" id="LocalBKUSubmit"><img src="assets/img/onlineBKU.png" alt="Sign via local BKU" />Card</div>
							<div style="display:none;"><input type="radio" id="localBKU" name="connector" value="bku"></div>
						</div>
						<%
							}
						%>
						<!-- <%
						if (WebConfiguration.getOnlineBKUURL() != null) {
						%>
						<div class="methodChooseContainer">
							<div class="ImageBox"><img class="BKUImage" src="assets/img/onlineBKU.png" alt="Sign via online BKU"/></div>
							<div><button class="btn btn-primary">Online<br>BKU</button></div>
							<div style="display:none;"><input type="radio" id="onlineBKU" name="connector" value="onlinebku"></div>
						</div>
						<% 
							}
						%> -->
						<%
							if(WebConfiguration.getKeystoreDefaultEnabled()) {
						%>
						<div class="methodChooseContainer">
							<div class="ImageBox" id="KeystoreSubmit"><img class="BKUImage" src="assets/img/onlineBKU.png" alt="Sign via Server Keystore"/></div>
							<div style="display:none;"><input type="radio" id="jks" name="connector" value="jks"></div>
						</div>
						<%
							}
						%>  
					</div>
					<iframe id='mobileSignOnFrame' style="display:none;"></iframe>
				</div>
		</div>
		<div id="DownloadContainer" style="display: none;">
			<div id="btnSign" style="display:none">
				<h3 class="center">Sign Document</h3>
			</div>
			<div class="center" id="DownloadResultContainer">
			<input id="DownloadResultButton" value="Download your PDF" class="btn btn-success btn-lg">
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