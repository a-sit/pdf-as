//
//A global status object
//
var global_status = new statusObject();

/*
 * Convert touch to mouse
 */

/*
 * picked from http://stackoverflow.com/questions/5186441/javascript-drag-and-drop-for-touch-devices
 *
 */
function touchHandler(event)
{
    // trick to add support for touch event to elements/widgets that do not support it 
    // by convetting convert touchevents into mouseevents

    // only apply this trick to ui-draggable elements
    if ( ! $(event.target).hasClass('ui-draggable') ) { 
        return;
    }   

    var touches = event.changedTouches,
        first = touches[0],
        type = ""; 

    switch(event.type) {
        case "touchstart": type = "mousedown"; break;
        case "touchmove":  type="mousemove"; break;    
        case "touchend":   type="mouseup"; break;
        default: return;
    }   

    // convert touchevents into mouseevents
    var simulatedEvent = document.createEvent("MouseEvent");
    simulatedEvent.initMouseEvent(type, true, true, window, 1,
                              first.screenX, first.screenY,
                              first.clientX, first.clientY, false,
                              false, false, false, 0/*left*/, null);

    first.target.dispatchEvent(simulatedEvent);
            event.preventDefault();
}

function init() {
	console.log("init touchconverter");
    if (Modernizr.touch){
        // faster links for touch devices
        // by wiring directly touchend event as if it was a click (and disabling click handler) 
        var links = document.getElementsByTagName("a");
        for (var i=0; i < links.length; i++) {
            var link = links[i];
            if ( link.href !== undefined && link.href !== '') {
                link.addEventListener("click", function(e) {
                  e.preventDefault();
                }); 
                link.addEventListener("touchend", function() {
                  document.location = this.href;                                                                                                                                                                                                                                
                }); 
            }   
        };  

       // listen to touch events and provide support to them where needed 
       document.addEventListener("touchstart", touchHandler, true);
       document.addEventListener("touchmove", touchHandler, true);
       document.addEventListener("touchend", touchHandler, true);
       document.addEventListener("touchcancel", touchHandler, true);    
    }   
}

$(document).ready(function(){
	
	init(); // initialize the touch converter
	window.lang = new Lang('en'); // set default language
	window.lang.dynamic('th', 'en.json'); // define language pack to load dynamically
	
	// check the language of the webapp , WEBAPP ONLY
	
	var webapp_language = window.parent.default_language;
	console.log("parent default lang: " + webapp_language);
	
	switchLanguage(webapp_language);
	
    $("#placeContinue").bind("click", function(evt) {
		
		$(window.parent.document).find("#SignStepButton").click();
	});
	
	$("#BackBox").on("click", function(evt){
		
		window.parent.GoBack();
	});
	
	$("#PlaceOnNewPage").on("click", function(evt) {
		
		$("#delSignature").click();
		console.log("setting placeonnewpage from " + window.parent.place_on_new_page);
		window.parent.place_on_new_page = true;
		console.log("to " + window.parent.place_on_new_page);
		$(window.parent.document).find("#SignStepButton").click();
		
	});
	
});

function switchLanguage(language)
{
	switch(language)
	{
	case "en":
		window.lang.change("en");
		break;
	case "de":
		window.lang.change("th");
		break;
		default:
			console.log("The viewer does not support the language '" + language + "'");
			break;
	}
}

//
//statusObject Definition
//
function statusObject() {
		this.pdf_url = null;
		this.redirect_url = null;
		this.connector = null;
		this.file_path = null;
		this.file = null;
		this.signature = null;
		this.running = false;
		this.applicationContext = '../../../../';
		
		this.setRunning = function(r) {
			this.running = (r === 'true');
		}
		
		this.getRunning = function() {
			return this.running;
		}
		
		this.setFile = function(f) {
			this.file = f;
		}
		
		this.getFile = function() {
			return this.file;
		}
		
		this.setPdfUlr = function(pdfurl) {
			this.pdf_url = pdfurl;
		}
		
		this.getPdfUrl = function() {
			return this.pdf_url;
		}
		
		this.setRedirectUrl = function(rurl) {
			this.redirect_url = rurl;
		}
		
		this.getRedirectUrl = function() {
			return this.redirect_url;
		}
		
		this.setConnector = function(con) {
			this.connector = con;
		}
		
		this.getConnector = function() {
			return this.connector;
		}
		
		this.setFilePath = function(f) {
			this.file_path = f;
		}
		
		this.getFilePath = function() {
			return this.file_path;
		}
		
		this.setSignature = function(s) {
			this.signature = s;
		}
		
		this.getSignature = function() {
			return this.signature;
		}
	}

//
//Updates the selected File for the current Frame as well as
//for the parent frame if such exists
//This function is called if a new File is opened via the
//PDF.js Menu-Bar
//
function setFile(pdf_file) {
	if (window.self != window.parent) {
		window.parent.file = pdf_file;
	}
	global_status.setFile(pdf_file);
	removeSignature();
}

//
//Returns selected File of the parent Frame
//
function getFileFromParent() {
	if (window.self !== window.parent) {
		return window.parent.file;
	} else {
		return false;
	}
}

//
//Opens and displays a selected file.
//
function displayPdf() {
	var file = null;
	if(window.self !== window.parent) { //Take file selected in the parent if we are in an Iframe.
		file = getFileFromParent();
	} else {
		file = global_status.getFile();
	}
	if(!file) {
		return;
	}
	var fileReader = new FileReader();
	fileReader.onload = function(evt) {
		var buffer = evt.target.result;
		var uint8Array = new Uint8Array(buffer);
		PDFView.open(uint8Array, 0);
		registerSignaturePlacementEventHandlers();
	};
	fileReader.readAsArrayBuffer(file);
	console.log("now finished");
}


//
//Attaches event handlers to the signature placement icons
//
function registerSignaturePlacementEventHandlers() {
	$("#placeSignature").bind("click", placeSignature);
	$("#secondaryPlaceSignature").bind("click", placeSignature);
	$("#delSignature").bind("click", removeSignature);
	$("#secondaryDelSignature").bind("click", removeSignature);
	
	//
	//Catches events where PDF.js has finished rendering a new page.
	//PDF.js only renders a small subset of all pages of a PDF document
	//at a given time.
	//This causes a placed signature block to disappear,
	//if a user scrolls too far away from the page where the signature has
	//been placed. To address this issue the following function is used.
	//Every time a page is rendered, it is checked whether it is true that
	//a signature has been placed manually but is currently not in the DOM Tree.
	//In that case we have to check whether the page we placed the signature on
	//is in the DOM tree. If thats true, we can restore the signature on that page.
	//
	document.addEventListener('pagerendered', function(evt) {
		var page;
		if(!isSignatureInDomTree() && isSignaturePlaced()) {
			page = global_status.getSignature().page;
			if(isPageInDomTree(page)) {
				placeSignature(null, page, global_status.getSignature().sig[0].outerHTML);
			}
		
		}
		page = global_status.getSignature().page;
		// every time when a page is rendered we have to make the children clickthrough
		console.log("page is rendered: " + page);
		// disabled pointer events on given page
		$("#pageContainer" + page + " .textLayer").children().css("pointer-events", "none");
		$("#pageContainer" + page + " .annotationLayer").children().css("pointer-events", "none");
		
		// resize img signature to adapt to a resized window
		var current_scale = PDFViewerApplication.pdfViewer.currentScale;
		var sig_size = Math.floor(96 * current_scale);
		console.log("Signature resizing: " + sig_size);
		var image_source = global_status.applicationContext + "/visblock?r=" + sig_size.toString();	
		
		$("#img_signature").attr("src", image_source);
		
	});
}

//
//Checks whether a particular page is contained in the current DOM tree
//
function isPageInDomTree(p) {
	var page = $("#pageContainer" + p);
	if($(page).attr("data-loaded") === "true") {
		return true;
	} else {
		return false;
	}
}

//
//Checks whether a signature block is contained in the current DOM tree
//
function isSignatureInDomTree() {
	var signature = $(".cl_signature");
	if(signature.length > 0) {
		return true;
	} else {
		return false;
	}
}

//
//Checks if a signature has been manually placed by a user 
//
function isSignaturePlaced() {
	return global_status.getSignature() != null
}

//
//Places signature given by parameter s on the page provided by the page_to_place parameter or on the current page if no such
//parameter is provided. If the parameter s is omitted a default signature is placed.
//

var last_left;
var last_top;

function placeSignature(evt, page_to_place, s) {
	
	//check if properties are already assigned
	
	// get properties (if assigned) from last placement
	
	var left_pos;
	var top_pos;
		
	if(typeof last_left != 'undefined')
	{
		left_pos = last_left;
		top_pos = last_top;
	}
	else // otherwise set default position
	{
		left_pos = "30%";
		top_pos = "20%";
	}
	
	var current_scale = PDFViewerApplication.pdfViewer.currentScale;
	var sig_size = Math.floor(96 * current_scale);
	//sig_size = 300;
	console.log("Signature resolution: " + sig_size);
	var image_source = global_status.applicationContext + "/visblock?r=" + sig_size.toString();
	var defaultSignature = "<img src='" + image_source + "' alt='Signature' id='img_signature' class='cl_signature' draggable='true' style='position: absolute; z-index:4; " +
	"cursor:move; left:" + left_pos + "; top:" + top_pos + ";'>";

	if (typeof page_to_place === 'undefined') { page_to_place = PDFView.page;}
	if (typeof s === 'undefined') { s = defaultSignature}
	
	if(isSignaturePlaced() && isSignatureInDomTree()) {
		removeSignature();
	} 

	$("#pageContainer" + page_to_place).prepend(s);
	global_status.setSignature({
		page: (s === defaultSignature) ? page_to_place : global_status.getSignature().page,
		posx: (s === defaultSignature) ? "0" : global_status.getSignature().posx,
		posy: (s === defaultSignature) ? "0" : global_status.getSignature().posy,
		sig: $(".cl_signature")
	});
	updateSignaturePosition(global_status.getSignature());
	makeSignatureDraggable(global_status.getSignature());
	
	$("#img_signature").on("mousedown", function(evt) {
		evt.stopPropagation();
	});
	
	$("#img_signature").on("mousedown", function(evt){
		
		if($(this).closest(".textLayer").children().css("pointer-events") !== "none" && 
				$(this).closest(".annotationLayer").children().css("pointer-events") !== "none")
		{			
			$(this).closest(".textLayer").children().css("pointer-events", "none");
			$(this).closest(".annotationLayer").children().css("pointer-events", "none");
			
			return;
		}
		
	});
	
	$(".page").on("mousedown", function(evt){
				
		// check if there is already pointerevents: none on all page children, if not -> apply
		
		if($(this).find(".textLayer").children().css("pointer-events") !== "none" && 
				$(this).find(".annotationLayer").children().css("pointer-events") !== "none")
		{			
			$(this).find(".textLayer").children().css("pointer-events", "none");
			$(this).find(".annotationLayer").children().css("pointer-events", "none");
			return;
		}
		
		if($(this).find("#img_signature").length)
		{
			
			// let the signature jump to the event position
			$("#img_signature").css("left", evt.offsetX + "px");
			$("#img_signature").css("top", evt.offsetY + "px");
			
			// update position
			updateSignaturePosition(global_status.getSignature());	
			
			// start dragging signature
			$("#img_signature").trigger(evt);
			
		}
	});
	
	 $("#pageFitOption").click();
 	 $("#scaleSelect").change();
	
	
}

//
//Makes a placed signature block draggable and keep track of
//the position of the signature block within the page where it has been placed.
//
function makeSignatureDraggable(signature) {
	signature.sig.draggable({
		drag: function() {
			updateSignaturePosition(signature)
		},
		containment: "parent"
	});			
	
	$(window).on("touchmove", function(e) {
		
		// only if we are on the page we prevent the default scroll
		var target = e.target;
				
		if($(target).hasClass("textLayer") || $(target).parents('.left').length || $(target).parents('.page').length)
		{
			e.preventDefault();
		}
	});
	
	$("#img_signature").on("contextmenu", function(e) {
		e.preventDefault();
	});
}

//
//Update the position of the signature block, e.g. if the signature block is dragged. 
//The position is is saved in the html attributes 'data-pos-x' and 'data-pos-y' of the 
//signature element.
//
function updateSignaturePosition(signature) {
	var page = signature.page;
	var canvas_height = $("#page" + page.toString()).attr("height");
	var current_scale = PDFViewerApplication.pdfViewer.currentScale;
	var thisPos = $(signature.sig).position();
	var x;
	var y;
	
	x = thisPos.left;
	y = thisPos.top; 


	signature.posx = Math.floor(x / current_scale / (4.0/3.0)).toString();
	signature.posy = Math.floor((parseInt(canvas_height) - (y)) / current_scale / (4.0/3.0)).toString();
		
	
	//console.log("last x: last y: " + $("#img_signature").css("left") + " " + $("#img_signature").css("top"));
	
	last_left = $("#img_signature").css("left");
	last_top = $("#img_signature").css("top");
}

//
//Remove a manually placed signature block.
//
function removeSignature() {
	if(isSignaturePlaced()) {
		$(".cl_signature").remove();
		global_status.setSignature(null);
	}
}

$(document).ready(function() {
	if(window.self !== window.parent) { //We are in an Iframe and return
		return;
	}
	
	//
	//Function that return the value of the provided HTML get parameter
	//
	$.urlParam = function(name){
		var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
		if(!results) {
		   return null;
		} else {
		   return decodeURIComponent(results[1]) || decodeURI(0);
		}
	}
	
	global_status.setConnector($.urlParam("connector"));
	global_status.setPdfUlr($.urlParam("pdfurl"));
	global_status.setFilePath($.urlParam("file"));
	console.log("Status:", global_status);
	global_status.setRunning($.urlParam("running"));
	
	if(global_status.getFilePath()) { //if a filepath is given via the file HTML get parameter, try to fetch the file and display it
		fetchFile(global_status.getFilePath()).then(function(result) {
			setFile(result);
			displayPdf();
		}, function() {
			alert("Failed loading PDF");
		});	
	} else if(global_status.getConnector() && global_status.getPdfUrl()) { //if connector and pdfUrl is given, start the sign process straight away
		quickSign(global_status.getConnector(), global_status.getPdfUrl());
		return;
	}
	
	addConnector();
	addConnectorEventHandler();
	setConnector(global_status);
	addSignButton();
	addSignButtonEventHandler(global_status);
	registerSignaturePlacementEventHandlers();
	
});

//
//Fetches the file given via filepath from the server.
//Returns a promise that is being resolved if the file could
//be successfully fetched, and rejected otherwise.
//
function fetchFile(filepath) {
	return new Promise(function(resolve, reject) {
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function(){
			if (this.readyState == 4 && this.status == 200) {
				resolve(this.response);
			} else if (this.readyState == 4 && this.status != 200) {
				reject();
			}
		}
		xhr.open('GET', filepath);
		xhr.responseType = 'blob';
		xhr.send();    
	});
}

//
//Adds a drop down menu to the PDF.js menu bar where a user can select
//between different sign methods
//
function addConnector() {
	var sign_method_html = "<span class='dropdownToolbarButton'><select id='connectorSelect' title='Sign Method' tabindex='24' style='min-width: 166px;'>"
		+ "<option id='mobilebku' value='mobilebku'>Handy</option>"
		+ "<option id='bku' value='bku'>Local BKU</option>"
		+ "<option id='obku' value='onlinebku'>Online BKU</option>"
		+ "<option id='jks' value='jks'>Keystore</option>"
		+ "</select></span>";	
	$("#toolbarViewerRight").prepend(sign_method_html);
}

//
//Adds change event handler to the connector select field.
//
function addConnectorEventHandler() {
	$("#connectorSelect").bind("change", function(evt) {
		global_status.setConnector($("#connectorSelect").val());
	});	
}

//
//Adds a button to the PDF.js menu bar, that allows the user to start the sign process
//
function addSignButton() {
	var sign_button_html = "<button id='sign' class='toolbarButton sign hiddenLargeView' title='Sign document' tabindex='30' data-l10n-id='Sign Document'>"
		+ "<span data-l10n-id='sign_document_label'>Sign document</span>"
		+ "</button>";
	$("#toolbarViewerRight").prepend(sign_button_html);	
}

//
//Attaches event Handler to the sign button.
//
function addSignButtonEventHandler(status) {
	$("#sign").bind("click", function(evt) {
		sign(status);
	});
}

//
//Selects a particular connector
//
function setConnector(statusObj) {
	if(!statusObj) {
		return;
	}
	var default_connector = "mobilebku";
	var con = statusObj.getConnector();
	if(!con || $("#connectorSelect #" + con).length === 0) {
		$("#connectorSelect").val(default_connector);
		statusObj.setConnector(default_connector);
	} else {
		$("#connectorSelect").val(statusObj.getConnector());
	}
}

//
//If pdf-url and connector are provided by means of HTML get parameters
//the document is signed straight away by calling the Sign servlet
//
function quickSign(connector, pdfurl) {
	var fd = new FormData();
	fd.append("source", "internal");
	fd.append("connector", connector);
	fd.append("pdf-url", pdfurl);
	
	var signUrl = global_status.applicationContext + "/Sign";
	
	$.ajax({
		url: signUrl,
		data: fd,
		processData: false,
		contentType: false,
		xhrFields: {
		      withCredentials: true
		},
		type: "POST",
		success: function(response) {
			console.log("hello there, response is: " + response);
			$("#DownloadResultButton").prop("href", response);
			$("#FinishStepButton").click();
			//$("html").empty();
			//$("html").html(response);
		}
	});	
}

//
//function that is called once the sign button has been pressed.
//takes a statusobject as it's only parameter, which can be used
//to decide what action should happen. Currently it only calls
//the sign servlet.
//
function sign(statusObj) {
	var file = statusObj.getFile();
	if(!file) {
		alert("No File Opened");
		return;
	}
	var fd = new FormData();
	fd.append("source", "internal");
	var method = "POST";
	var signUrl = global_status.applicationContext + "/Sign";
	
	if(global_status.getRunning()) {
		signUrl = global_status.applicationContext + "/signNow";
		method = "GET";
		fd = null;
		
		if(isSignaturePlaced()) {
			signUrl = signUrl + "?sigPosX=" + encodeURIComponent(global_status.getSignature().posx);
			signUrl = signUrl + "&sigPosY=" + encodeURIComponent(global_status.getSignature().posy);
			signUrl = signUrl + "&sigPosP=" + encodeURIComponent(global_status.getSignature().page);
		}
	} else {
		fd.append("connector", global_status.getConnector());
		fd.append("pdf-file", global_status.getFile());
		if(isSignaturePlaced()) {
			fd.append("sig-pos-x", global_status.getSignature().posx);
			fd.append("sig-pos-y", global_status.getSignature().posy);
			fd.append("sig-pos-p", global_status.getSignature().page);
		}
	}	
	
	console.log("Data:", fd);
	
	$.ajax({
		url: signUrl,
		data: fd,
		processData: false,
		contentType: false,
		type: method,
		xhrFields: {
		      withCredentials: true
		},
		success: function(response) {
			console.log("hello there, response is: " + response);
			$("#DownloadResultButton").prop("href", response);
			$("#FinishStepButton").click();
			
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
			});
			if(typeof onAnmeldeSubmit == 'function') {
				onAnmeldeSubmit();
			}*/
		}
	});		
}
