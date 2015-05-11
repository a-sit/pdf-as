window.addEventListener("message", function (evt) {
	var goto_last_page_on_render = false;
	var file = evt.data;
	var fileReader = new FileReader();
	var current_page = null;
	console.log("child:" + file);
	
	function rendered(evt) {
		console.log(evt);
		if(goto_last_page_on_render) {
			document.getElementById("lastPage").click();
			return;
		}
		showSignatureBlock();
		console.log(evt);
		current_page = PDFView.page;
		document.removeEventListener('textlayerrendered', rendered);
		document.addEventListener('textlayerrendered', showSignatureBlock);
	}
	
	window.addEventListener('pagechange', showSignatureBlock);
	document.addEventListener('textlayerrendered', rendered);

	fileReader.onload = function(evt) {
		var buffer = evt.target.result;
		var uint8Array = new Uint8Array(buffer);
		PDFView.open(uint8Array, 0);
	};
	
	function showSignatureBlock(evt) {
		console.log(evt);
		if(PDFViewerApplication.pdfViewer.getPageView(PDFViewerApplication.page - 1).renderingState === RenderingStates.RUNNING) {
			return;
		}
		
		console.log(document.readyState);
		$(".img_signature").remove();
		$("#pageContainer"+PDFView.page).prepend("<img src='../../../img/signature.png' alt='Signature' id='img_signature' class='cl_signature' draggable='true' style='position: absolute; z-index:4;'>");
		$(".cl_signature").draggable({
			drag: function() {
				
			},
			containment: "parent"
		});
	}
	
	fileReader.readAsArrayBuffer(file);
}, false);





