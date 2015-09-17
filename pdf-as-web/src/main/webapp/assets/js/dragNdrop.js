
// custom insertAt Stringmethod
String.prototype.insertAt=function(index, string) { 
  return this.substr(0, index) + string + this.substr(index);
}

var default_language = "en";
var file = null;

$(window).unload(function() {
    $("#uploadContinue").prop("disabled", true);
    $("#uploadContinueQuick").prop("disabled", true);
    $("#FileNamePreview").val("");
});

var mobile_success = false;
var local_success = false;
var keystore_success = false;
var place_on_new_page = false;

function registerEventListeners() {
	var locale = "EN";
	var connector = "mobilebku";
	
	$(document).bind("dragover", function(evt) {
		evt.preventDefault();
	});
	
	$(document).bind("drop", function(evt) {
		evt.preventDefault();
	});
	
	$("#borderBox").bind("dragenter", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		highlightDropzone();
	});
	
	$("#borderBox").bind("dragover", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		highlightDropzone();

	});
	
	$("#borderBox").bind("dragleave", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		unhighlightDropzone();
	});
	
	$("#borderBox").bind("dragend", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		unhighlightDropzone();
	});
	
	$("#borderBox").bind("drop", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		unhighlightDropzone();
		var files = evt.originalEvent.dataTransfer.files;
		if(files == null || files.length === 0) {
			return;
		}
		
		$("#FileNamePreview").val(files[0].name);
		$("uploadContinue").prop("disabled", true);
		$("uploadContinueQuick").prop("disabled", true);
		
		file = files[0];
		checkPDF(file);
	});
	
	$("#UploadStepButton").bind("click", function(evt) {
		
		toggleView("upload");
	});
	$("#PlaceStepButton").bind("click", function(evt) {
		
		place_on_new_page = false;
		toggleView("place");
		$("#iFrame").contents().find("#placeSignature").click();
	});
	$("#SignStepButton").bind("click", function(evt) {
		$("#methodContainer").show();
		$("#mobileSignOnFrame").hide();
		mobile_success = false;
		local_success = false;
		keystore_success = false;
		toggleView("sign");
		
		$("#DownloadResultButton").removeAttr("disabled");
		$("#DownloadResultButton").attr("title", "Download your Document");
		$("#DownloadResultButton").css("pointer-events", "auto");
		
		
		
		
	});
	$("#FinishStepButton").bind("click", function(evt) {
		
		toggleView("finish");
	});
	
	$("#uploadContinue").bind("click", function(evt) {
				
		$("#PlaceStepButton").click();
	});
	
	$("#uploadContinueQuick").bind("click", function(evt) {
		
		$("#iFrame").contents().find("#QuickSign").click();
		$("#iFrame").contents().find("#delSignature").click();
		place_on_new_page = false;
		$("#SignStepButton").click();
	});
	
	$("#DownloadResultButton").bind("click", function(evt) {
		
		$("#DownloadResultButton").attr("disabled", "disabled");
		$("#DownloadResultButton").attr("title", "The download is valid only once!");
		$("#DownloadResultButton").css("pointer-events", "none");
	});
	
	
	$("#LanguageDisplay").on("click", function(evt){
		
		toggleLanguage();
	});
	
	
	
	function toggleView(input)
	{
		
		$("#DropContainer").hide();
		$("#ViewContainer").hide();
		$("#SignContainer").hide();
		$("#DownloadContainer").hide();
		
		$("#UploadStepButton").removeClass("active");
		$("#PlaceStepButton").removeClass("active");
		$("#SignStepButton").removeClass("active");
		$("#FinishStepButton").removeClass("active");
		
		$("#UploadStepButton").removeClass("active");
		$("#PlaceStepButton").removeClass("active");
		$("#SignStepButton").removeClass("active");
		$("#FinishStepButton").removeClass("active");
		
		$("#uploadNavText").hide();
		$("#placeNavText").hide();
		$("#signNavText").hide();
		$("#downloadNavText").hide();
		
		$("#BackBox").removeAttr("disabled");
		$("#BackBox").css("pointer-events", "auto");
		
		
		switch(input)
		{
		case "upload":
			$("#DropContainer").show();
			$("#UploadStepButton").addClass("active");
			$("#PlaceStepButton").css("pointer-events", "none");
			$("#SignStepButton").css("pointer-events", "none");
			$("#FinishStepButton").css("pointer-events", "none");
			$("#uploadNavText").show();
			$("#BackBox").attr("disabled", "disabled");
			$("#BackBox").css("pointer-events", "none");
			break;
			
		case "place":
			$("#ViewContainer").show();
			$("#PlaceStepButton").addClass("active");
			$("#PlaceStepButton").css("pointer-events", "auto");
			$("#SignStepButton").css("pointer-events", "none");
			$("#FinishStepButton").css("pointer-events", "none");
			$("#placeNavText").show();
			break;
			
		case "sign":
			$("#SignContainer").show();
			$("#SignStepButton").addClass("active");
			$("#SignStepButton").css("pointer-events", "auto");
			$("#PlaceStepButton").css("pointer-events", "auto");
			$("#FinishStepButton").css("pointer-events", "none");
			$("#signNavText").show();
			break;
			
		case "finish":
			$("#DownloadContainer").show();
			$("#FinishStepButton").addClass("active");
			$("#SignStepButton").css("pointer-events", "auto");
			$("#downloadNavText").show();
			break;
		}
	}
	
	$("#pdf-file").bind("change", function(evt) {
		file_event = evt;
		var files = evt.target.files;
		if(files == null || files.length === 0) {
			return;
		}
		
		$("body").addClass("wait");
		
		$("#FileNamePreview").val(files[0].name);
		
		$("#uploadContinue").prop("disabled", true);
		$("#uploadContinueQuick").prop("disabled", true);
		$("#ContinueButtonText").hide();
		$("#MobileSpinner").show();

		
		file = files[0];
		checkPDF(file);
								
	});
	
	
	function checkPDF(to_check)
	{
		// Read the local file into a Uint8Array.
	    var fileReader = new FileReader();
	    
	    var is_pdf = true;
	    fileReader.onload = function webViewerChangeFileReaderOnload(evt) 
	    {
	       var buffer = evt.target.result;
		   var uint8Array = new Uint8Array(buffer);
		  		  
		   if(uint8Array[0] == 37 && // %
			  uint8Array[1] == 80 && // P
			  uint8Array[2] == 68 && // D
			  uint8Array[3] == 70	 // F 
		   )
		   {
			   console.log("File is PDF");
		   }
		   else
		   {
			   console.log("File is NOT PDF");
			   is_pdf = false;
		   }
		   
			setFeedbackView(is_pdf);		   
		   
	    };
	    
	    console.log("test if pdf..");
	    fileReader.readAsArrayBuffer(to_check);
	    
	}
	
	function setFeedbackView(is_pdf) // and load PDF if successful
	{
		
		if(!is_pdf) // if no pdf
		{
			console.log("setting Error Feedback");
			if($("#FormDefine").hasClass("has-success"))
			{
				$("#FormDefine").switchClass("has-success", "has-error");
			}
			else
			{
				$("#FormDefine").addClass("has-error");
			}
			
			$("#BadFeedback").show();
			$("#GoodFeedback").hide();
			
			$("#noPdfMessage").show();
			
			$("#FormDefine").css("margin-bottom", "2.2em");
						
			$("#uploadContinue").prop("disabled", true);
			$("#uploadContinueQuick").prop("disabled", true);
			$("#ContinueButtonText").show();
			$("#MobileSpinner").hide();

		}
		else // if it is pdf
		{
			console.log("setting Success Feedback");
			if($("#FormDefine").hasClass("has-error"))
			{
				$("#FormDefine").switchClass("has-error", "has-success");
			}
			else
			{
				$("#FormDefine").addClass("has-success");
			}
			
			$("#BadFeedback").hide();
			$("#GoodFeedback").show();
			
			$("#noPdfMessage").hide();
			
			$("#FormDefine").css("margin-bottom", "");
			
			//$("#uploadContinue").prop("disabled", false);
			//$("#uploadContinueQuick").prop("disabled", false);

			previewFile(file);
		} 
		
		//$("body").removeClass("wait");
	}
	
	$("input[name='connector']").bind("change", function(evt) {
		connector = this.value;
	});
	
	$("input[name='locale']").bind("change", function(evt) {
		locale = this.value;
	});
	
	$("#btnSign").bind("click", function(evt) {
		sign(file, connector, locale);
	});	
	
	$("#MobilePhoneSubmit").bind("click", function(evt) {
		
		$("#mobileBKU").click();
		mobile_success = true;
		$("body").addClass("wait");
		sign(file, connector, locale);
		
	});
		
	$("#LocalBKUSubmit").bind("click", function(evt) {
		
		$("#localBKU").click();	
		local_success = true;
		$("body").addClass("wait");
		sign(file, connector, locale);
	});
	
	$("#KeystoreSubmit").bind("click", function(evt) {
		
		$("#jks").click();	
		keystore_success = true;
		$("body").addClass("wait");
		sign(file, connector, locale);
	});
	
	$('[data-toggle="tooltip"]').tooltip();
}

//
//Creates an PDF.js instance within the PDF preview section and displays
//the user selected PDF document.
//
function previewFile() {
	clearContentDiv();
	createIframe();
}

// from "Einfach Signieren"
function addDataToForm(data, form) {
	var ret = [];
	   for (var d in data) {
		   if (typeof data[d] != "undefined") {
			   	var input = document.createElement('input');
			   	input.type = 'hidden';
			   	input.name = d;
			   	input.value = data[d];
			   	form.appendChild(input);
		   }
	   }
}


//
//Calls the sign servlet with the provided file and the provided connector and locale
//
function sign(file, connector, locale) {
	if(file == null) {
		alert("No file selected");
		return
	}
	/*var fd = new FormData();
	fd.append("source", "internal");
	fd.append("pdf-file", file);
	fd.append("connector", connector);
	fd.append("locale", locale); */
	
	/*var parameters = {
			source: "internal",
			pdf-file: file,
			connector: connector,
			locale: local,
			sig-pos-x: null,
			sig-pos-y: null,
			sig-pos-p: null
	}; */
	
	var form = document.createElement('form');
	form.method = "POST";
	form.action = "Sign";
	form.target = "mobileSignOnFrame";
	form.enctype = "multipart/form-data";
	
	var input = document.createElement('input');
   	input.type = 'hidden';
   	input.name = "source";
   	input.value = "internal";
   	form.appendChild(input);
   	
   	// used beneath
   	function uint8ToString(buf) {
   	    var i, length, out = '';
   	    for (i = 0, length = buf.length; i < length; i += 1) {
   	        out += String.fromCharCode(buf[i]);
   	    }
   	    return out;
   	}
   	
   	// starting the asynchronous things
   	
   	begin_convert(file);
   	
   	// convert file to b64 string
   	
   	function begin_convert(file)
	{
		// Read the local file into a Uint8Array.
	    var fileReader = new FileReader();
	    
	    fileReader.onload = function webViewerChangeFileReaderOnload(evt) 
	    {
	       var buffer = evt.target.result;
		   var uint8Array = new Uint8Array(buffer);
		  
		   
		   var b64encoded = btoa(String.fromCharCode.apply(null, uint8Array));
		   
		   var result = uint8ToString(b64encoded);
		  		  
		 
		   continueFormCreation(result);
			   
		   
	    };
	    
	    console.log("converting to b64..");
	    fileReader.readAsArrayBuffer(file);
	    
	}
   	
   	
   	function continueFormCreation(result)
   	{
	   	
	   	input = document.createElement('input');
	   	input.type = 'hidden';
	   	input.name = "pdf-file-b64";
	   	input.value = result; // former: file
	   	form.appendChild(input);
	   	
	   	input = document.createElement('input');
	   	input.type = 'hidden';
	   	input.name = "connector";
	   	input.value = connector;
	   	form.appendChild(input);
	   	
	   	input = document.createElement('input');
	   	input.type = 'hidden';
	   	input.name = "locale";
	   	input.value = locale;
	   	form.appendChild(input);
			
		ifr = document.getElementById('iFrame').contentWindow;
		if(ifr.isSignaturePlaced()) { //Signature has been manually placed
			
			console.log("signature manually placed: x: y:" + ifr.global_status.getSignature().posx + ", " + ifr.global_status.getSignature().posy)
			/*fd.append("sig-pos-x", ifr.global_status.getSignature().posx);
			fd.append("sig-pos-y", ifr.global_status.getSignature().posy);
			fd.append("sig-pos-p", ifr.global_status.getSignature().page); */
			
			/*parameters.sig-pos-x = ifr.global_status.getSignature().posx;
			parameters.sig-pos-y = ifr.global_status.getSignature().posy;
			parameters.sig-pos-p = ifr.global_status.getSignature().page; */
			
			input = document.createElement('input');
		   	input.type = 'hidden';
		   	input.name = "sig-pos-x";
		   	input.value = ifr.global_status.getSignature().posx;
		   	form.appendChild(input);
		   	
		   	input = document.createElement('input');
		   	input.type = 'hidden';
		   	input.name = "sig-pos-y";
		   	input.value = ifr.global_status.getSignature().posy;
		   	form.appendChild(input);
		   	
		   	input = document.createElement('input');
		   	input.type = 'hidden';
		   	input.name = "sig-pos-p";
		   	input.value = ifr.global_status.getSignature().page;
		   	form.appendChild(input);
		}
		else if(place_on_new_page)
		{
			console.log("signature will be placed on a new page");
			/*fd.append("sig-pos-p", "new"); */
		/*	parameters.sig-pos-p = "new"; */
			
			input = document.createElement('input');
		   	input.type = 'hidden';
		   	input.name = "sig-pos-p";
		   	input.value = "new";
		   	form.appendChild(input);
		   	
			place_on_new_page = false;
		}
		else
		{
			console.log("signature will be placed on bottom");
		}
			
		document.body.appendChild(form);
		$("#methodContainer").hide();
		$("#mobileSignOnFrame").show();
		form.submit();
	
   	}

/*	$.ajax({
		url: "Sign",
		data: fd,
		processData: false,
		contentType: false,
		type: "POST",
		xhrFields: {
		      withCredentials: true
		},
		success: function(response) {
			console.log("hello there, response is: " + response);
			
			if(mobile_success)
			{
				console.log("mobile success...now switch html");
				$("#methodContainer").hide();
				$("#mobileSignOnFrame").show();
				$("#mobileSignOnFrame").contents().find('html').empty();
				$("#mobileSignOnFrame").contents().find('html').html(response);
				mobile_success = false;
				
			}
			else if(local_success)
			{
				console.log("local success...now switch html, nothing for now");
				$("#methodContainer").hide();
				$("#mobileSignOnFrame").show();
				$("#mobileSignOnFrame").contents().find('html').empty();
				$("#mobileSignOnFrame").contents().find('html').html(response);
				local_success = false;
			}
			else if(keystore_success)
			{
				/*console.log("keystore success...now switch html"); // put this into success function in document ready()
				$("#DownloadResultButton").attr("href", response);
				
				var insertIndex = file.name.indexOf(".pdf");
				
				var download_name = file.name.insertAt(insertIndex, "_signed");
				$("#DownloadResultButton").attr("download", download_name);
				
				$("#FinishStepButton").click(); */ /*
				$("#methodContainer").hide();
				$("#mobileSignOnFrame").show();
				$("#mobileSignOnFrame").contents().find('html').empty();
				$("#mobileSignOnFrame").contents().find('html').html(response);
				keystore_success = false;
			}
			else
			{
				$("#methodContainer").show();
				$("#mobileSignOnFrame").hide();
			} 
			
			// switch the html of the iframe
			
			
			$("body").removeClass("wait");
			
			console.log("finished switching html");
			
			
			
			//$("html").empty();
			//$("html").html(response);
			/*
			$("#fade").remove();
			$("#popup").remove();
			var fade_div = "<div id='fade' class='black_overlay'></div>";
			var popup_div = "<div id='popup' class='white_content'><a href='javascript:void(0)' id='closelink'>Close</a><div id='resp'></div></div>"
			$("body").append(fade_div);
			$("body").append(popup_div);
			$("#resp").html(response);
			$("#closelink").bind("click", function(evt) {
				$("#fade").remove();
				$("#popup").remove();
			});*/  /*
		}
	}); */
}

//
//Changes color if the user drags a file over the dropzone
//
function highlightDropzone() {
	$("#borderBox").css("background", "#D8FFD8");
}

//
//Changes color back to original, if the user stops dragging a file over the dropzone (or drops the file)
//
function unhighlightDropzone() {
	$("#borderBox").css("background", "#E8F4FF");
}

//
//Removes all content that is inside of the PDF Preview section
//
function clearContentDiv() {
	$("#content").empty();
}

//
//Inserts an Iframe with a PDF.js instance into the PDF Preview section
//
function createIframe() {
	$("#content").append("<iframe src='assets/js/pdf.js/web/viewer.html' height='863px' width='800px' id='iFrame'></iframe>");
}

$(document).ready(function() {
	window.lang = new Lang('en'); // set default language
	window.lang.dynamic('th', 'assets/js/langpack/th.json'); // define language pack to load dynamically
	
	// check for language cookie
	
	var cookie_language = Cookies.get("language");
	
	if(cookie_language && cookie_language !== default_language)
	{
		toggleLanguage();
	}
	
	registerEventListeners();

});

function respond(arg, success) { // called when user successfully signed with mobile / card
	 //document.getElementById("demo").innerHTML = arg;
		console.log("recieved response after server contact, arg: " + arg + ", success: " + success);
		
		
		if(!success)
		{
			window.alert("Server Response Error, please try again");
			$("#SignStepButton").click();
			return;
		}
		
		$("#DownloadResultButton").attr("href", arg);
		
		var insertIndex = file.name.indexOf(".pdf");
		
		var download_name = file.name.insertAt(insertIndex, "_signed");
		$("#DownloadResultButton").attr("download", download_name);
		$("#methodContainer").show();
		$("#mobileSignOnFrame").hide();
		$("#FinishStepButton").click();
		
		
		
};


function toggleLanguage()
{
	if(default_language === "de")
	{
		$("#LanguageDisplay").html
		(
				"<span class='label label-info'><span class='flag-icon flag-icon-de'></span> DE</span>"
		);
		
		default_language = "en";
		window.lang.change('en');
		
		if($("#iFrame").get(0) != null) // check if ready 
		{
			$("#iFrame").get(0).contentWindow.switchLanguage("en");
		}
	}
	else
	{
		$("#LanguageDisplay").html
		(
				"<span class='label label-info'><span class='flag-icon flag-icon-gb'></span> EN</span>"
		);
		
		default_language = "de";
		window.lang.change('th');
		
		if($("#iFrame").get(0) != null)
		{
			$("#iFrame").get(0).contentWindow.switchLanguage("de");
		}
		
	}
	
	// set Cookie
	Cookies.set("language", default_language);
}

function GoBack() {
	
	if($("#PlaceStepButton").hasClass("active"))
	{
		$("#UploadStepButton").click();
	}
	else if($("#SignStepButton").hasClass("active"))
	{
		$("#PlaceStepButton").click();
	}
	else if($("#FinishStepButton").hasClass("active"))
	{
		
		$("#SignStepButton").click();
	}
	
}