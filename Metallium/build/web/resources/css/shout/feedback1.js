// fbLink v1.0d

var _fBr=encodeURIComponent(window.location.href);
var _fBh=362;
var _fBw=452;
var _fByt=(((screen.height-_fBh)/2)-100);
var _fBxl=((screen.width-_fBw)/2);


function _fBsG () {
	var _chG = typeof 's_channel' == 'undefined'? s_channel : '' ;
	return _chG;
}

function _fBsH () {
	var _chH = 'channel' in s_265? s_265.channel : "";
	_chH = _chH=='undefined'?'':_chH;
	return _chH;
}

function fBch() {
	var _ch = typeof s_265 == 'undefined'?_fBsG():_fBsH();
	return _ch;
}


function fBo(_sid){
	var _fBhref = 'http://feedback.aol.com/rs/rs.php?sid='+_sid;
	window.open(_fBhref+'&referer='+_fBr+'&ch='+fBch(),'feedback','width='+_fBw+',height='+_fBh+',screenX='+_fBxl+',screenY='+_fByt+',top='+_fByt+',left='+_fBxl+',resizable=yes,copyhistory=yes,scrollbars=no');
	return false;
}

function fBo2(_sid){
	var _fBhref = 'http://feedback.aol.com/rs/rs.php?sid='+_sid;
	window.open(_fBhref+'&referer='+_fBr+'&ch='+fBch(),'feedback','width='+_fBw+',height='+_fBh+',screenX='+_fBxl+',screenY='+_fByt+',top='+_fByt+',left='+_fBxl+',resizable=yes,copyhistory=yes,scrollbars=no');
}

function openFBHelp(href) {
	var _fBHelph=520;
	var _fBHelpw=794;
	var _fBHelpyt=(((screen.height-_fBHelph)/2)-100);
	var _fBHelpxl=((screen.width-_fBHelpw)/2);
	var href2=(href.indexOf('?')==-1?href+'?':href+'&');
    
	window.open(href2+'referer='+_fBr+'&ch='+fBch(),'feedback_help','width='+_fBHelpw+',height='+_fBHelph+',screenX='+_fBHelpxl+',screenY='+_fBHelpyt+',top='+_fBHelpyt+',left='+_fBHelpxl+',resizable=yes,copyhistory=yes,scrollbars=no');
}

function openStandard(href) {
	window.open(href,'','screenX=0,screenY=0,top=0,left=0,location=yes,toolbar=yes,resizable=yes,copyhistory=yes,scrollbars=yes');
}