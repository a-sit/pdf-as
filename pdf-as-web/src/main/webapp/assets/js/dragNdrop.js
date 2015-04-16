
$(document).ready(function() {
	registerEventListeners();
});

function registerEventListeners() {
	
	var file;
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
		previewFile(file);
	});
	
	$("#pdf-file").bind("change", function(evt) {
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

function previewFile(file) {
	var fr = new FileReader();
		
	fr.onload = function(file) {
		var buffer = fr.result;
		var uint8array = new Uint8Array(buffer);
		displaypdf(uint8array);
	};
	
	clearContentDiv();
	fr.readAsArrayBuffer(file);
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

function displaypdf(uint8array) {
	$("#content").append("<img src='assets/img/signature.png' alt='Signature' id='signature' draggable='true' style='position: absolute'>");
	$("#content").append("<canvas id='pdf-preview'></canvas>");
	
	
	PDFJS.getDocument(uint8array).then(function(__pdf) {
		var pdf = __pdf;
		var last_page = pdf.numPages;
		
		pdf.getPage(last_page).then(renderPage);
		
	});
	
	$("#signature").draggable({
		drag: function() {
			
		},
		containment: "parent"
	});
}

function renderPage(page) {
	var viewport = page.getViewport(1);
	var canvas = document.getElementById("pdf-preview");
	var context = canvas.getContext('2d');
	canvas.height = 868;
	canvas.width = 800;
	
	page.render({
		canvasContext: context,
		viewport: viewport
	});
	
}


