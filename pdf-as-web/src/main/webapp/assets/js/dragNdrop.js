
var file = null;

$(document).ready(function() {
	registerEventListeners();
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
	
	$("#dropzone").bind("dragenter", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		highlightDropzone();
	});
	
	$("#dropzone").bind("dragover", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		highlightDropzone(); // test

	});
	
	$("#dropzone").bind("dragleave", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		unhighlightDropzone();
	});
	
	$("#dropzone").bind("dragend", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		unhighlightDropzone();
	});
	
	$("#dropzone").bind("drop", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		unhighlightDropzone();
		var files = evt.originalEvent.dataTransfer.files;
		if(files == null || files.length === 0) {
			return;
		}
		file = files[0];
		previewFile();
	});
	
	$("#pdf-file").bind("change", function(evt) {
		file_event = evt;
		var files = evt.target.files;
		if(files == null || files.length === 0) {
			return;
		}
		file = files[0];
		previewFile(file);
	});
	
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
	$("#dropzone").css("background", "#D8FFD8");
}

//
//Changes color back to original, if the user stops dragging a file over the dropzone (or drops the file)
//
function unhighlightDropzone() {
	$("#dropzone").css("background", "#E8F4FF");
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
