
function setFileForParent(file) {
	console.log("SettingFileFOrparent");
	if (window.self != window.parent) {
		window.parent.file = file;
	}
}

window.addEventListener("message", handlePostMessage, false);

function handlePostMessage(evt) {
	var file = evt.data;
	console.log(file)
	var signature_placed_manually = false;
	var fileReader = new FileReader();
	
	fileReader.onload = function(evt) {
		var buffer = evt.target.result;
		var uint8Array = new Uint8Array(buffer);
		PDFView.open(uint8Array, 0);
		$("#placeSignature").bind("click", placeSignature);
		$("#secondaryPlaceSignature").bind("click", placeSignature);
		$("#delSignature").bind("click", removeSignature);
		$("#secondaryDelSignature").bind("click", removeSignature);
	};
	
	fileReader.readAsArrayBuffer(file);
}

function isSignaturePlaced() {
	var signature = $(".cl_signature");
	if(signature.length > 0) {
		return true;
	} else {
		return false;
	}
}

function placeSignature(evt) {
	var current_scale = PDFViewerApplication.pdfViewer.currentScale;
	var sig_size = Math.floor(96 * current_scale);
	var current_page = PDFView.page;
	
	if(isSignaturePlaced()) {
		removeSignature();
	} 
	
	$("#pageContainer"+current_page).prepend("<img src='http://localhost:8080/pdf-as-web/visblock?r=" + sig_size.toString() + "' data-pos-x='0' data-pos-y='0' data-page='" + current_page + "' alt='Signature' id='img_signature' class='cl_signature' draggable='true' style='position: absolute; z-index:4; cursor:move'>");
	makeSignatureDraggable($(".cl_signature"));
}

function makeSignatureDraggable(signature) {
	var current_page = PDFView.page;
	var canvas_height = $("#page" + current_page.toString()).attr("height");
	var current_scale = PDFViewerApplication.pdfViewer.currentScale;

	signature.draggable({
		drag: function() {
			var $this = $(this);
			var thisPos = $this.position({my: "left bottom"});
			var parentPos = $this.parent().position();			
			var x = thisPos.left - parentPos.left;
			var y = thisPos.top - parentPos.top;
			$(this).attr("data-pos-x", (Math.floor(x / current_scale / (4.0/3.0))).toString()); //width shrink again by 4/3?
			$(this).attr("data-pos-y", Math.floor((parseInt(canvas_height) + parentPos.top - (thisPos.top + parentPos.top)) / current_scale / (4.0/3.0)).toString()); //height shrink again by 4/3?
		},
		containment: "parent"
	});			
}

function removeSignature() {
	if(isSignaturePlaced()) {
		$(".cl_signature").remove();
	}
}

$(document).ready(function() {
	//http://localhost:8080/pdf-as-web/assets/js/pdf.js/web/viewer.html?connector=jks&pdfurl=http://www.example.net/pdf.pdf
	$.urlParam = function(name){
		var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
		if(!results) {
		   return null;
		} else {
		   return results[1] || 0;
		}
	}
	
	var connector = $.urlParam("connector");
	var pdfurl = $.urlParam("pdfurl");
	
	if(!connector || !pdfurl) {
		return;
	} else {
		var fd = new FormData();
		fd.append("source", "internal");
		fd.append("pdf-url", pdfurl);
		fd.append("connector", connector);

		$.ajax({
			url: "http://localhost:8080/pdf-as-web/Sign",
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
});




