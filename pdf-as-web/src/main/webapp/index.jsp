<%@page import="at.gv.egiz.pdfas.web.config.WebConfiguration"%>
<%@page import="at.gv.egiz.pdfas.web.helper.PdfAsHelper"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>PDF-Signatur</title>
     <link rel="stylesheet" href="assets/css/style.css">    
    
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/jquery-ui.min.js"></script>
	<script src="assets/js/pdf.js/build/pdf.js"></script>
	<script src="assets/js/dragNdrop.js"></script>
	<script src="assets/js/jquery-lang.js"></script>
	<script src="assets/js/js.cookie.js"></script>
	
	<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <script src="assets/bootstrap/js/bootstrap.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-nav2/dist/bootstrap-nav-wizard.css">
    <link rel="stylesheet" href="assets/css/flag-icon.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    
    <!-- Google Font -->
	<link href='https://fonts.googleapis.com/css?family=Roboto+Condensed' rel='stylesheet' type='text/css'>
	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    
</head>
<body>
 <header>
</header>

<div class="container col-lg-8 col-lg-offset-2">
	<!-- Header -->
	<div class="row" id="LanguageSwitchContainer">
	<h2 id="PageTitle" class="pull-left" lang="en">PDF-Signature Online</h2>
	<h4 id="LanguageDisplay" class="pull-right"><span class="label label-info"><span class="flag-icon flag-icon-de"></span> DE</span></h4>
	</div>
<div class="row center">
	  <div class="" id="navBar">
	  <ul class="nav nav-wizard">
	    <li class="active" id="UploadStepButton"><a href="#" title="Upload your Document" data-toggle="tooltip"><span class="glyphicon glyphicon-open-file" aria-hidden="true"></span><span id="uploadNavText" class="hidden-xs" lang="en"> Upload</span></a></li>
	    <li id="PlaceStepButton" style="pointer-events:none;"><a href="#" data-toggle="tooltip" title="Place your Signature"><span class="glyphicon glyphicon-move" aria-hidden="true"></span><span id="placeNavText" style="display:none;" class="hidden-xs" lang="en"> Place</span></a></li>
	    <li id="SignStepButton" style="pointer-events:none;"><a href="#" data-toggle="tooltip" title="Sign your Document"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span><span id="signNavText" style="display:none;" class="hidden-xs" lang="en"> Sign</span></a></li>
	    <li id="FinishStepButton" style="pointer-events:none;"><a href="#" data-toggle="tooltip" title="Download your signed Document"><span class="glyphicon glyphicon-save-file" aria-hidden="true"></span><span id="downloadNavText" style="display:none;" class="hidden-xs" lang="en"> Finish</span></a></li>
	  </ul>
	  </div>
 </div>
	
<!-- Main Switch Frame -->
<div class="row center">
		<div id="DropContainer">
			<div id="borderBox" class="center">
				<div class="mainBox center">
					<div id="FormBox" class="">
						<h5 class="visible-lg-block" lang="en">Drop or select your file here</h5>
						<h5 class="hidden-lg" lang="en">Upload your File</h5>
							<div id="FormDefine" class="form-group has-feedback">
					            <div class="input-group">
					                <span class="input-group-btn">
					                    <span class="btn btn-primary btn-file" lang="en">
					                     Browse... <input type="file" name="pdf-file" id="pdf-file" accept="application/pdf">
					                    </span>
					                </span>
					                
					                <input id= "FileNamePreview" type="text" class="form-control" readonly>				                
					                <span id="BadFeedback" class="glyphicon glyphicon-remove form-control-feedback" style="display: none;" aria-hidden="true"></span>
					                <span id="GoodFeedback" class="glyphicon glyphicon-ok form-control-feedback" style="display: none;" aria-hidden="true"></span>
					            </div>
					            
					            <div id="fileTypeErrorMessage" class="pull-right">
					            <p id="noPdfMessage" style="display:none;" lang="en"><span class="glyphicon glyphicon-alert" aria-hidden="true"></span> The file type must be PDF!</p>
					            </div>

				            </div>	
				            
				            <div id="ContinueButtonBox" class="col-md-12">
				            <button id="uploadContinue" class="btn btn-success btn-lg btn-block center" disabled="disabled" title="Place your Signature by yourself">
				            <p id="ContinueButtonText" lang="en">Continue</p><i style="display:none;" id="MobileSpinner" class="fa fa-spinner fa-pulse fa-lg"></i>
				            </button>
				            <button id="uploadContinueQuick" class="btn btn-default btn-lg btn-block" disabled="disabled" title="Signature will be placed at the bottom of your document" lang="en">Quick Sign</button>				           
				            </div>							       	        
					</div>
				</div>
			</div>	
		</div>

		<div id="ViewContainer" style="display: none;">
			<div id="content" class="" lang="en">
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
							<div class="ImageBox" id="MobilePhoneSubmit" lang="en"><img src="assets/img/mobileBKU.png" alt="Sign via mobile BKU"/>Mobile</div>
							<div style="display:none;"><input type="radio" id="mobileBKU" name="connector" value="mobilebku" checked></div>
						</div>
						<%
							}
						%>
						<%
						if (WebConfiguration.getLocalBKUURL() != null) {
						%>
						<div class="methodChooseContainer">
							<div class="ImageBox" id="LocalBKUSubmit" lang="en"><img src="assets/img/onlineBKU.png" alt="Sign via local BKU" />Card</div>
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
			<div id="DownloadResultContainer">
				<p align="center" id="ResultInfoText" lang="en">You can download your signed document here:</p>
					<a id="DownloadResultButton" class="btn btn-success btn-lg" lang="en">Download</a>
			</div>
		
		</div>	
	</div>
	<div class="row">
	</div>
</div>
	<footer>
	</footer>
</body>
</html>