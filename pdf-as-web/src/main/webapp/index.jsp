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
</head>
<body>
 <header>
</header>

	<div id="sidebar">
		<!-- <div id="fileSelector" class="container">
			<h3 class="center">File Selector</h3>
			<div id="dropzone" class="center">
				Drag and Drop your Document here ...
			</div>
			  <div id="traditionalUpload">
				<p>... or select File here</p>
				<input type="file" name="pdf-file" id="pdf-file" accept="application/pdf">
			</div>
			
		</div>  -->

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
	
	
	<div id="main" class="container">
		<h3 class="center">Preview</h3>
		<div id="content" class="center">
			<div id="borderBox" class="center">
				<div>
					<p> Drop your file here</p>
					<p>... or select File here</p>
						<input type="file" name="pdf-file" id="pdf-file" accept="application/pdf">
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