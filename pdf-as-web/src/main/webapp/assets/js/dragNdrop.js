
$(document).ready(function() {
	registerEventListeners();
});

function registerEventListeners() {
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
		if(files == null) {
			return;
		}
		
		previewFile(files[0]);
	});
}

function previewFile(file) {
	var fr = new FileReader();
		
	fr.onload = function(file) {
		var buffer = fr.result;
		/*var int8View = new Uint8Array(buffer);
		var output = document.getElementById("output");
		output.innerHTML =
		int8View[0].toString(16)
		+ int8View[1].toString(16)
		+ int8View[2].toString(16)
		+ int8View[3].toString(16);*/
		displaypdf(buffer);
	};
	
	clearContentDiv();
	fr.readAsDataURL(file);
}

function sign() {
	/*var fd = new FormData();
	fd.append("pdf-file", files[0]);
	fd.append("source", "internal");
	fd.append("connector", "mobilebku");
	
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
	});*/
}

function highlightDropzone() {
	$("#dropzone").css("background", "#D8FFD8");
}

function unhighlightDropzone() {
	$("#dropzone").css("background", "#E8F4FF");
}

function displaypdf(datauri) {
	$("#content").append("<img src='assets/img/signature.png' alt='Signature' id='signature' draggable='true' style='position: absolute'>");
	$("#content").append("<iframe src=" + datauri + " width='800px' height='868px'></iframe>");
	
	$("#signature").draggable({
		drag: function() {
			
		},
		containment: "parent"
	});
}

function clearContentDiv() {
	$("#content").empty();
}
