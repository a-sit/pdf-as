
$(document).ready(function() {
	registerEventListeners();
});

function registerEventListeners() {
	
	var file = null;
	var locale = "EN";
	var connector = "mobilebku";
	
	window.addEventListener("message", function receiveMessage(evt) {
		console.log("parent: message received");
		console.log(evt.data);
		var iframewindow = document.getElementById("iFrame");
		console.log(file);
		iframewindow.contentWindow.postMessage(file, "*");
		console.log("postmessage to child sent");
	}, false);
	
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
		
		//REMOVE
		alert("Locale");
		alert($("#iFrame").contents().find("#numPages").html());
		//REMOVE
	});
	
	$("#btnSign").bind("click", function(evt) {
		sign(file, connector, locale);
	});
}

function previewFile() {
	clearContentDiv();
	createIframe();
}

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
	
	$.ajax({
		url: "Sign",
		data: fd,
		processData: false,
		contentType: false,
		type: "POST",
		success: function(response) {
			$("html").empty();
			$("html").html(response);
		}
	});
}

function highlightDropzone() {
	$("#dropzone").css("background", "#D8FFD8");
}

function unhighlightDropzone() {
	$("#dropzone").css("background", "#E8F4FF");
}

function clearContentDiv() {
	$("#content").empty();
}

function createIframe() {
	$("#content").append("<iframe src='assets/js/pdf.js/web/viewer.html' height='863px' width='800px' id='iFrame'></iframe>");
}
