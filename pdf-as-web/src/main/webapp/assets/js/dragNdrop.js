
var file = null;

$(document).ready(function() {
	registerEventListeners();
});

$(window).unload(function() {
    console.log("REFRESHED");
    $("#uploadContinue").prop("disabled", true);
    $("#FileNamePreview").val("");
});

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
		
		file = files[0];
		checkPDF(file);
	});
	
	$("#UploadStepButton").bind("click", function(evt) {
		
		toggleView("upload");
	});
	$("#PlaceStepButton").bind("click", function(evt) {
		
		toggleView("place");
	});
	$("#SignStepButton").bind("click", function(evt) {
		
		toggleView("sign");
	});
	$("#FinishStepButton").bind("click", function(evt) {
		
		toggleView("finish");
	});
	
	$("#uploadContinue").bind("click", function(evt) {
				
		$("#PlaceStepButton").click();
	});
	
	
	$("#placeContinue").bind("click", function(evt) {

		$("#SignStepButton").click();
	});
	
	$("#placeSignatureExtern").bind("click", function(evt) {
		
		$("#iFrame").contents().find("#placeSignature").click();
	});
	
	$("#delSignatureExtern").bind("click", function(evt) {
		
		$("#iFrame").contents().find("#delSignature").click();
	})
	
	function toggleView(input)
	{
		console.log("toggleView : " + input);
		
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
		
		
		switch(input)
		{
		case "upload":
			$("#DropContainer").show();
			$("#UploadStepButton").addClass("active");
			$("#PlaceStepButton").prop("style", "pointer-events:none;");
			$("#SignStepButton").prop("style", "pointer-events:none;");
			$("#FinishStepButton").prop("style", "pointer-events:none;");
			$("#uploadNavText").show();
			break;
			
		case "place":
			$("#ViewContainer").show();
			$("#PlaceStepButton").addClass("active");
			$("#PlaceStepButton").prop("style", "");
			$("#SignStepButton").prop("style", "pointer-events:none;");
			$("#FinishStepButton").prop("style", "pointer-events:none;");
			$("#placeNavText").show();
			break;
			
		case "sign":
			$("#SignContainer").show();
			$("#SignStepButton").addClass("active");
			$("#SignStepButton").prop("style", "");
			$("#FinishStepButton").prop("style", "pointer-events:none;");
			$("#signNavText").show();
			break;
			
		case "finish":
			$("#DownloadContainer").show();
			$("#FinishStepButton").addClass("active");
			$("#SignStepButton").prop("style", "");
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
		
		$("#FileNamePreview").val(files[0].name);
		$("uploadContinue").prop("disabled", true);

		
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
		   
		    console.log("setting view now..");
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
			
			$("#uploadContinue").prop("disabled", true);

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
			
			$("#uploadContinue").prop("disabled", false);

			previewFile(file);
		} 
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
}

//
//Creates an PDF.js instance within the PDF preview section and displays
//the user selected PDF document.
//
function previewFile() {
	clearContentDiv();
	createIframe();
}

//
//Calls the sign servlet with the provided file and the provided connector and locale
//
function sign(file, connector, locale) {
	if(file == null) {
		alert("No file selected");
		return
	}
	var fd = new FormData();
	fd.append("source", "internal");
	fd.append("pdf-file", file);
	fd.append("connector", connector);
	fd.append("locale", locale);
	
	ifr = document.getElementById('iFrame').contentWindow;
	if(ifr.isSignaturePlaced()) { //Signature has been manually placed
		fd.append("sig-pos-x", ifr.global_status.getSignature().posx);
		fd.append("sig-pos-y", ifr.global_status.getSignature().posy);
		fd.append("sig-pos-p", ifr.global_status.getSignature().page);
	}

	$.ajax({
		url: "Sign",
		data: fd,
		processData: false,
		contentType: false,
		type: "POST",
		xhrFields: {
		      withCredentials: true
		},
		success: function(response) {
			$("html").empty();
			$("html").html(response);
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
			});*/
		}
	});
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
