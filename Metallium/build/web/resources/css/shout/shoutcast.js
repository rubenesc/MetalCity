function createFilter(){
	
	var filter;
	$('.genrefilter input').each( function(index) {
		if(this.checked){
			if(filter!=null)
				filter = filter+"-"+this.id;
			else
				filter = this.id;
		}		
	});
	return filter
}
function getGenre(){
	var	path=unescape(window.location.pathname);
	try{
		var genre = path.substring(7);
		genre = escape(genre);
		return genre;
	}catch(err){
		try{
			var vars = [], params;
			params = path.split("/");
			var genre = params[2];
			genre = escape(genre);
			return genre;
		}catch(err){}
	}
}
function getSearchParma(){
	var	path=unescape(window.location.pathname);
	try{
		var kw = path.substring(16)
		kw = escape(kw);
		return kw;
	}catch(err){
		try{
			var vars = [], params;
			params = path.split("/");
			var kw = params[2];
			kw = escape(kw);
			return kw;
		}catch(err){}
	}
	
}
var pageName;
function setPageName(){
	var	path=unescape(window.location.pathname);
	var temp = new Array();
	temp = path.split('/');
	if("Internet-Radio" == temp[1])
		pageName = "search";
	if("radio" == temp[1])
		pageName = "genre";
}
	count = 10;
	strIndex = 0;
function sortresults(order,crit,page){
	var urlStr;
        
        setCookie("OrderBy",crit + "|" + order, 365);

	if(page=="hs"){
		 strIndex = 0;  // Initialized to start from 0
		 urlStr = "/ajax_dir";
		  //datatext ="order="+order+"&criteria="+crit+"&count="+count+"&strIndex="+strIndex;
		  datatext ="order="+order+"&criteria="+crit+"&strIndex=0&count=10&ajax=true";
	}
	else if(page == "gs"){
		 strIndex = 0;
		 //gen=$("#tableheadtext span").text();
		 var genre = getGenre();
		 kw = "&sgenre="+genre;
		 //datatext="order="+order+"&criteria="+crit+"&count="+count+"&strIndex="+strIndex+kw;
		 datatext="order="+order+"&mode="+crit+"&strIndex=0&count=10&ajax=true";
		 urlStr = "/genre-ajax/"+genre;
	}
	else if(page == "ks"){
		strIndex = 0;
		// kw=$("#tableheadtext span b").text();
		 kw = getSearchParma();
		 s = "&s="+kw;
		 urlStr = "/search-ajax/"+kw;
		 datatext= "order="+order+"&mode="+crit+"&strIndex=0&count=10&ajax=true";
		 var filter = createFilter();
		 if(filter!=null)
		 datatext+="&filter="+filter;
	}
	$.ajax({
		  type: "POST",
		  url: urlStr,
		  data: datatext,
		  success: function(html){
			$("#resulttable").html(html);
			thisMovie("basePlayer_V19");
			$(".playbutton").unbind('click');
			$(".playbutton").click(function(){
				//alert("comes here everytime?");
			station_id = $(this).attr("id");
			stnName = $(this).attr("name");
			$('div.dirlist').removeClass("dirbg");
			var obj = $(this).parents('.dirlist');
			obj.addClass("dirbg");

			return invokeSCPlayer();
			});
			$(".playbutton1").click(function(){
				$('div.dirlist').removeClass("dirbg");
				var obj = $(this).parents('.dirlist');
				obj.addClass("dirbg");
			});
			//com.aol.video.portal.Init();
			com.aol.video.portal.UI.onLoad();
			/*if(pageurl == "/keywordSearchDirectory.jsp")
				filterfunction("sort");*/
		   }
		});
	}
function homedir(val,page){
	if(prevhead == val.id){
		$(val).toggleClass("dirgenredown").addClass("dirgenreup");
		if($(val).hasClass('dirgenredown') == true){
			sortresults("desc", val.id, page);
		}
		else{
			sortresults("asc", val.id, page);
		}
	}else{
		$("#"+prevhead).removeClass("dirgenredown dirgenreup").parent().removeClass("dirheadbg");
		$(val).parent().addClass("dirheadbg");
		$(val).toggleClass("dirgenredown");
		prevhead = val.id;
		if($(val).hasClass('dirgenredown') == true){
			sortresults("desc", val.id, page);
		}
		else{
			sortresults("asc", val.id, page);
		}
	}

}
function updTotStCount(){
	var numfound = $(".numfound:last").attr('value');
	$("#totalstations").text(numfound);
}


$(document).ready(function(){
	setPageName();
	if (pageName == "genre"){
		updTotStCount();
	}else if (pageName == "search"){
		$('.genrefilter input').each( function(index) {
			 $(this).attr('checked', false);
		});
		populateStCount();
		var numfound = $(".numfound:first").attr('value');
		if(numfound==0)	{
			$('.genrefilter').remove();
		}
		didUmean();
		$(".genrefilter ul li input").click(function() {
			//   onFilterChange();
			filterUpdates(this);
		});  	
	}
	var url=document.location.href;
	if(url.indexOf("Internet-Radio")== -1 ){
		$(".dirheadlist1").addClass("dirgenredown");
		$(".dirheadlist").addClass("dirheadbg");
	}
if($("#listenershead").length == 1){
	prevhead = "listenershead";
}
else if($("#listenershead1").length == 1){
	prevhead = "listenershead1";
}
else{
	prevhead = "listenershead2";
}

$("#stationhead, #genrehead, #listenershead, #bitratehead, #typehead").click(function(){
	homedir(this,"hs");
});

$("#stationhead1, #genrehead1, #listenershead1, #bitratehead1, #typehead1").click(function(){
	homedir(this,"gs");
});

$("#stationhead2, #genrehead2, #listenershead2, #bitratehead2, #typehead2").click(function(){
	homedir(this,"ks");
});

populateGenres();
//filterfunction();
$(".playbutton").click(function(){
	$('div.dirlist').removeClass("dirbg");
	var obj = $(this).parents('.dirlist');
	obj.addClass("dirbg");

});
com.aol.video.portal.Init();	
com.aol.video.portal.UI.onLoad();
});

function didUmean(){
	var s =  getSearchParma();
	var urlStr = "/didUMean.jsp";
	var dataString ="ajax=true&s="+s;
	$.ajax({
		  type: "POST",
		  url: urlStr,
		  data: dataString,
		  success: function(html){
					$("#dumean").html(html);
			        }
		});
}


function populateStCount(){
	var dirList = 0;
	$('.dirlist').each( function(index) {
		dirList++;
	});
	var numfound = $(".numfound:last").attr('value');
	var showmoreTab = $('#showmorehome')[0];
	if( showmoreTab ){
		$("#stsize").text(dirList);
		$("#nfound").text(numfound);
	}else{
		$("#stsize").text(dirList);
		$("#nfound").text(dirList);
	}
}

function filterUpdates(elem){
	var filter = createFilter();
	strIndex = 0;
	$('.genrefilter input').each( function(index) {
		$(this).attr('disabled', true);
	});
	//kw=$("#tableheadtext span b").text();
	kw =  getSearchParma();
	var urlStr = "/search-ajax/"+kw;
	var dataString ="filter="+filter+"&ajax=true";
	var mode = $("#sort").attr('mode');
	var	order = $("#sort").attr('order');
	 if(mode!=null)
	   dataString+="&mode="+mode;
	if(order!=null)
	   dataString+="&order="+order;
	$.ajax({
	  type: "POST",
	  url:urlStr,
	  //data: "order="+order+"&mode="+mode+"&count=10&stIndex="+strIndex+"&s="+s,
	  data: dataString,
	  success: function(html){
				  $("#resulttable").html(html);
				  $('.genrefilter input').each( function(index) {
				     $(this).attr('disabled', false);
				   });
				   //com.aol.video.portal.Init();
				   com.aol.video.portal.UI.onLoad();
				  thisMovie("basePlayer_V19");
				  $(".playbutton").unbind('click');
					$(".playbutton").click(function(){
						$('div.dirlist').removeClass("dirbg");
						var obj = $(this).parents('.dirlist');
						obj.addClass("dirbg");

					station_id = $(this).attr("id");
					stnName = $(this).attr("name");
					return invokeSCPlayer();
					});
					populateStCount();
				}
	});
}
function showMorehome(){
	$("#showmorehome .showmorecenter span").html("<span class='ajaxloadersmall'></span>");
	var getcookies = document.cookie;

	if (getcookies == undefined || getcookies == null ||getcookies == '' )
    {
         order = "desc";
         crit =  "listenershead";
    } 
	else {
		if (getcookies.indexOf("OrderBy") != -1)
		    {
	
			pos1 = getcookies.indexOf("OrderBy")+"OrderBy".length+1;
	        pos2 = getcookies.indexOf(";",pos1);
	        
			if (pos2 == -1)
				pos2=getcookies.length;
			
			ordBy = unescape(document.cookie.substring(pos1,pos2));
	        crit=ordBy.substr(0,ordBy.indexOf("|"));
			order=ordBy.substr(ordBy.indexOf("|")+1);
		    }
			else {
			 order = "desc";
			 crit =  "listenershead";
			}
	}
	strIndex += 10;
	$.ajax({
		  type: "POST",
		  url: "/ajax_dir",

		  data: "order="+order+"&criteria="+crit+"&count=10&strIndex="+strIndex+"&ajax=true",
		  success: function(html){
			//$("#resulttable").append(html);
			$("#showmorehome").replaceWith(html);
			//$("#showmorehome").html("<span>show more</span>");
			$(".playbutton").unbind('click');
				$(".playbutton").click(function(){
					$('div.dirlist').removeClass("dirbg");
					var obj = $(this).parents('.dirlist');
					obj.addClass("dirbg");
				});
				$(".playbutton").click(invokePlayer);
				//com.aol.video.portal.Init();	
				com.aol.video.portal.UI.onLoad();				
		  }
		});
		//strIndex += 10;
	}

function searchOnKeyword(){
	var seach_keyword = $("#GH_search_field").val();
	if(seach_keyword == ""){
         $("#GH_search_field").val("Search by Artist, Genre or Station");
         return;
    }
	seach_keyword = trimString(seach_keyword);
	while(seach_keyword.indexOf("\\") != -1){
    	seach_keyword = seach_keyword.replace("\\","");
    }
	window.location = "/Internet-Radio/"+escape(seach_keyword);
}

function validateSearchKeyword(){
	var seach_keyword = $("#GH_search_field").val();
	seach_keyword = trimString(seach_keyword);
	if("" == seach_keyword || null == seach_keyword){
		alert("Enter Search Keyword");
		return false;
	}
	if("Search for SHOUTcast Music" == seach_keyword){
		alert("Enter Search Keyword");
		return false;
	}
	return true;
}

function trimString(str){return str.replace(/^\s+|\s+$/g, '') ;}


function showMoreSearch(){
	$("#showmorehome .showmorecenter span").html("<span class='ajaxloadersmall'></span>");
	strIndex += 10;
	//	var s = $('#tableheadtext span b').text();
	var s =  getSearchParma();
	var urlStr = "/search-ajax/"+s;
	var mode = $("#sort").attr('mode');
	var	order = $("#sort").attr('order');
	var	filter = $("#filter").attr('value');
    var dataString ="strIndex="+strIndex+"&count="+count+"&ajax=true";
    if(mode!=null)
	   dataString+="&mode="+mode;
	if(order!=null)
	   dataString+="&order="+order;
	if(filter!=null)
	   dataString+="&filter="+filter;
    $.ajax({
		  type: "POST",
		  url: urlStr,
		  //data: "order="+order+"&mode="+mode+"&count=10&stIndex="+strIndex+"&s="+s,
		  data: dataString,
		  success: function(html){
					//$("#resulttable").append(html);
					$("#showmorehome").replaceWith(html);
			        populateStCount();
			        $(".playbutton").unbind('click');
					$(".playbutton").click(function(){
						$('div.dirlist').removeClass("dirbg");
						var obj = $(this).parents('.dirlist');
						obj.addClass("dirbg");

					});
					$(".playbutton").click(invokePlayer);
					//com.aol.video.portal.Init();
					com.aol.video.portal.UI.onLoad();
			}
		});
	}
function showMoreGenre(){
	$("#showmorehome .showmorecenter span").html("<span class='ajaxloadersmall'></span>");
	strIndex += 10;
	//var s = $('#tableheadtext h2 span').text();
	var s = getGenre();
	var mode = $("#sort").attr('mode');
	var	order = $("#sort").attr('order');
    var dataString ="strIndex="+strIndex+"&count="+count+"&ajax=true";
    if(mode!=null)
	   dataString+="&mode="+mode;
	if(order!=null)
	   dataString+="&order="+order;
	var	urlStr = "/genre-ajax/"+s;
    $.ajax({
		  type: "POST",
		  url: urlStr,
		  //data: "order="+order+"&mode="+mode+"&count=10&stIndex="+strIndex+"&s="+s,
		  data: dataString,
		  success: function(html){
			//$("#resulttable").append(html);
			$("#showmorehome").replaceWith(html);
			//$("#showmorehome").html("<span>show more</span>");
			$(".playbutton").unbind('click');
				$(".playbutton").click(function(){
						$('div.dirlist').removeClass("dirbg");
						var obj = $(this).parents('.dirlist');
						obj.addClass("dirbg");
				});
				$(".playbutton").click(invokePlayer);
				//com.aol.video.portal.Init();
				com.aol.video.portal.UI.onLoad();
		  }
		});
	}
var filterfunction = function(orderby){
		if(orderby){
			$('.genrefilter ul li').each( function(index) {
				$(this).removeClass('showClass');
				$(this).removeClass('hideClass');
			});
		}
		var filtstr = $(".filt:last").attr('value');
		if(filtstr!=null){
			if(filtstr.search("b")!=-1){
				$("#broadband").parent('li').addClass('showClass');
			}else if(!$("#broadband").parent('li').hasClass('showClass')){
				$("#broadband").parent('li').addClass('hideClass');
			}if(filtstr.search("1")!=-1){
				$("#128").parent('li').addClass('showClass');
			}else if(!$("#128").parent('li').hasClass('showClass')){
				$("#128").parent('li').addClass('hideClass');
			}
			if(filtstr.search("2")!=-1){
				$("#28").parent('li').addClass('showClass');
			}else if(!$("#28").parent('li').hasClass('showClass')){
				$("#28").parent('li').addClass('hideClass');
			}
			if(filtstr.search("a")!=-1){
				$("#aac").parent('li').addClass('showClass');
			}else if(!$("#aac").parent('li').hasClass('showClass')){
				$("#aac").parent('li').addClass('hideClass');
			}
			if(filtstr.search("m")!=-1){
				$("#mp3").parent('li').addClass('showClass');
			}else if(!$("#mp3").parent('li').hasClass('showClass')){
				$("#mp3").parent('li').addClass('hideClass');
			}
		}
	}

	
/*params for filter station*/
	var flagNP;	
	//var flag28;
	var flag128;
	var flagBB;
	var flagAAC;
	var flagMP3;
	var typelength;
	var bitlength;
	var kw;

function onFilterChange(){
	var n = $(".genrefilter input:checked").length;
	if(n==0){
		$('#resulttable .dirlist').each(function(index, domEle) {
		$(this).css("display","block");
	  });
	}else{
	flagNP=$("#nowplaying").is(':checked');	
	//flag28=$("#28").is(':checked');
	flag128=$("#128").is(':checked');
	flagBB=$("#broadband").is(':checked');
	flagAAC=$("#aac").is(':checked');
	flagMP3=$("#mp3").is(':checked');
	typelength = $("#type input:checked").length;
	bitlength =$("#bit input:checked").length;
	//kw = $('#tableheadtext span b').text();
	kw =  getSearchParma();
		$('#resulttable .dirlist').each(function(index, domEle) {
			if(filterStation(domEle)){
				$(this).css("display","block");
			}else{
				$(this).css("display","none");
			}
		});
	}
}

function filterStation(t)
{
	var bitrate = $(t).contents().filter('.dirbitrate').text();
	var type = $(t).contents().filter('.dirtype').text();
	var nowPlg = $(t).contents().filter('.stationcol').contents().filter('.playingtext').contents().filter('span').text(); 
	var resultNP=true;
	var resultBit=false;
	var resultType=false;
	if(flagNP && ( nowPlg.toLowerCase().match(kw.toLowerCase())==null)){
		resultNP=false;
	}
	if((bitlength!=0) && resultNP){
		/*if(flag28 && bitrate<=28){
			if((typelength==0 || flagAAC) && type=="AAC+")
			resultBit=true;
			else if( (typelength==0 ||flagMP3 )&& type=="MP3")
			resultBit=true;
		} else */
		if(flag128 && bitrate<=127 ){
			if( (typelength==0 || flagAAC) && type=="AAC+")
				resultBit=true;
			else if( (typelength==0 ||flagMP3 ) && type=="MP3")
				resultBit=true;
		} else if(flagBB && bitrate > 127){
			if( (typelength==0 || flagAAC) && type=="AAC+")
			resultBit=true;
			else if( (typelength==0 ||flagMP3 ) && type=="MP3")
			resultBit=true;
		}
	return resultBit;
	}
	
    if(resultNP && typelength !=0&&bitlength==0){
	      if(flagAAC && type=="AAC+")
		  resultType=true;
		  else if(flagMP3 && type=="MP3")
		  resultType=true;
		  
		  return resultType;
	}
	
	return resultNP ;
}
function populateGenres(){
prevgenre = '';
$("#radiopicker ul ul").css("display","none");
	$(".arrowup").click(function(e){
		prigenre = $(this).parents().find("a").eq(0).text();
		targetidsec ="#"+ $(this).parents().attr("id")+"_sec";
		populate(prigenre,$(this).parents().attr("id"));
		$(this).toggleClass("arrowup").addClass("arrowdown");
		return false;
	});
}

function populate(prigenre,primaryid){
        if($(targetidsec).children().length == 0){
                $("#"+primaryid).find("a").eq(0).append("<span class='ajaxloadergenre'></span>");
                $.ajax({
                  type: "POST",
                  url: "/genre.jsp",
                  data: "genre="+encodeURIComponent(prigenre)+"&id="+primaryid,
                  success: function(html){
                        $(targetidsec).html(html);
                        $("#"+primaryid).find("span").remove();
                  }
                });
                $(targetidsec).slideToggle("2000");
                }
        else{
                        if($(targetidsec).is(':hidden')){
                        $(targetidsec).slideToggle("2000");
                        }
                else{
                        $(targetidsec).slideToggle("2000");
                        }
                }
                return false;
        }


function populate1(primarygenre,primaryid){
        targetidsec ="#"+primaryid+"_sec";
        $(targetidsec).html('<p class="ajaxloadersmall"></p>');
        $.ajax({
          type: "POST",
          url: "/genre.jsp",
          data: "genre="+encodeURIComponent(primarygenre)+"&id="+primaryid,
          success: function(html){
                $(targetidsec).html(html);
                                        $(targetidsec).slideToggle("2000");
                                        $("#"+primaryid).find("div").toggleClass("arrowup").addClass("arrowdown");
          }
        });

        return false;
}


    $(document).ready(function() {
			$('#tabs li').click(showContent);
      });
		var prevId="tab1";
		var  curId;
		var showPrev = "#firstdiv";
		var showDiv = "#firstdiv";
		var id;
		var preid;
		
        $("#tab1").css("display","block");
 
		var showContent = function (){
	    
		
		curId = this.id;
		if(curId == "tab1") {showDiv =  "#firstdiv";}
		else if(curId == "tab2") { showDiv =  "#seconddiv";}
		else if(curId == "tab3") {showDiv =  "#thirddiv"; }
		else if(curId == "tab4") {showDiv =  "#fourthdiv"; }
		
         id = "#"+curId+ " .image";
		 preid = "#"+prevId+ " .selected";
 
		 var id1 = "#"+curId+ " .left_round";
		 var preid1 = "#"+prevId+ " .selected1";
 
		 var id2 = "#"+curId+ " .right_round";
		 var preid2 = "#"+prevId+ " .selected2";//2
 
		  $(preid).addClass('image');
          $(preid).removeClass('selected');
 
		  $(id).addClass('selected');
          $(id).removeClass('image');
 
          $(preid1).addClass('left_round');
          $(preid1).removeClass('selected1');
 
		  $(id1).addClass('selected1');
          $(id1).removeClass('left_round');
 
 
		  $(preid2).addClass('right_round');
          $(preid2).removeClass('selected2');
 
		  $(id2).addClass('selected2');
          $(id2).removeClass('right_round');
		  
 
 
		  $(showPrev).css("display","none");
	      $(showDiv).css("display","block");
		  prevId = curId;
		  showPrev = showDiv;
}
var stnName = '';
var play = 1;
var confirm_play=0;
var station_id = '';
var slide_flag = 1;

function getPlayersCookie() {
var playersCookie=[];
var pos1='';
var pos2='';
var popup_status = '';

if (document.cookie.indexOf("Settings") != -1)
{
 pos1 = document.cookie.indexOf("Settings")+"Settings".length+1;
 pos2 = document.cookie.indexOf(";",pos1);
 if (pos2 == -1)
 {
  pos2 = document.cookie.indexOf(",",pos1);
  if (pos2 == -1) {
      pos2 = document.cookie.length;
  }
 }
 message= document.cookie.substring(pos1,pos2);
 var word=message.split("|");
 playersCookie = word[0].split("~");  
}
if (playersCookie[1] == '' )
    playersCookie[1] = 'stretched';
 
return playersCookie[1];
}




$(document).ready(function() {
thisMovie("basePlayer_V19");
$(".playbutton").click(function(){
station_id = $(this).attr("id");
stnName = $(this).attr("name");
//$(".dirlist").removeClass("dirbg");
//$(this).parent().parent().addClass("dirbg");
return invokeSCPlayer();
});

$(".featured_stations").click(function(){
station_id = $(this).attr("id");
stnName = $(this).attr("name");
return invokeSCPlayer();
});

$(".secgen_a").click(function(){
station_id = $(this).attr("id");
stnName = $(this).attr("name");
return invokeSCPlayer();
});

$('.popUpoutPlayer').click(function() {
openPopoutPlayer();

if(slide_flag == 0){
  $(".bottom-bar").animate({bottom:'-=75px'},1500);
  $("#GF_").css("height",$("#GF_").height()-65+'px');
slide_flag = 1;
confirm_play=0;
}
});
});

var invokePlayer = function(){
station_id = $(this).attr("id");
return invokeSCPlayer();
}

function invokeSCPlayer(){
var player_cookie = getPlayersCookie();
if(player_cookie == "stretched"){
  if(getCookie('cookie_popup_status') == "popup")
   {
   openPopoutPlayer();
   }
  else{
  	try{
		invokeBottomPlayer(station_id);
	}catch(e){ }
    openStretchedPlayer();
  }
  return false;
}
else if(player_cookie == "popup"){
   //alert("invokePopupPlayer("+station_id+","+play+")");
   openPopoutPlayer();
   return false;
}
else if(player_cookie == "others"){
return true; 
}
else{
  if(getCookie('cookie_popup_status') == "popup")
  {
   openPopoutPlayer();
   return false;
   }
  else{
    openStretchedPlayer();  
    try{
		invokeBottomPlayer(station_id);
	}catch(e){ }
  }
  return false;
}
}

function openPopoutPlayer()
{
   setCookie('cookie_popup_status','popup',365);
   stnName = $('#'+station_id).attr("name");
   var popup_status = window.open("/shoutcast_popup_player?station_id="+station_id+"&play_status="+play+"&stn="+stnName,"mywindow","location=0,status=0,toolbar=0,menubar=0,scrollbars=0,width=400,height=135");
   popup_status.focus();
}

function openStretchedPlayer(){
  if(slide_flag == 1){
    $("#bottom-bar").css("visibility","visible");
    $("#bottom-bar").animate({bottom:'+=75'},1500);
    $("#GF_").css("height",$("#GF_").height()+65+'px');
    slide_flag = 0;
    confirm_play=1; 
   }
}

function setPlayStatus(val){
    play=0;
}

/********* GET COOKIE STARTS *********/
function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=");
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 ;
    c_end=document.cookie.indexOf(";",c_start);
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end));
    } 
  }
return ""
}
/********* GET COOKIE END  *********/

/********* SET COOKIE STARTS  *********/
function setCookie(c_name,value,expiredays)
{
var exdate=new Date();
exdate.setDate(exdate.getDate()+expiredays);
document.cookie=c_name+ "=" +escape(value)+((expiredays==null) ? "" : "; path=/ ; expires="+exdate.toUTCString());
}
/********* SET COOKIE END  *********/

/********* DELETE COOKIE STARTS  *********/
function deletecookie()
{
    //alert("inside del cookie");
    var d = new Date();
    document.cookie = "v0=1;expires=" + d.toGMTString() + ";" + ";";

    //alert(document.cookie);
}
/********* DELETE COOKIE END  *********/

function savePlayersCookie(plyname) {
     var ckSettings =    "Player~"+plyname+"|"+"Bandwidth~ALL"+"|"+"Codec~ALL";
     var date = new Date();
     date.setTime(date.getTime()+(365*24*60*60*1000));
     var expires = "; expires="+date.toGMTString();
     document.cookie = "Settings"+"="+ckSettings+"; path=/"+expires;         
}



window.onbeforeunload = confirmPlay;
function confirmPlay(){
var player_cookie = getPlayersCookie();
  if(confirm_play == 1 && (player_cookie == "" || player_cookie == '' || player_cookie == undefined )){
   if (confirm("Music will stop playing. Continue listening in popup player? \nNote: You can get rid of this message by changing your player preference by clicking on Settings under the Help menu.")) {
        savePlayersCookie("popup");
        if (!/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
        	openPopoutPlayer();
        }
        confirm_play == 0;
   }else{
   savePlayersCookie("stretched");
   }
  }
}

$(document).ready(function() {
$("#GH_search_field").click(function(){
//alert($("#GH_search_field").val() );
if($("#GH_search_field").value != "Search by station , Artist or Genres"){
//alert("same");

}
});
});

function searchBox2(){if($("GH_search_field").value == ""){$("GH_search_field").value ="Search for Station, Genre";}}
function retainFieldStatus(status, id){if($(id).value == status){$(id).value = "";}}
var readyStatus = 1;
var httpRequester=newXMLHttpRequest();
function newXMLHttpRequest(){
	var xmlreq=false;
	if(window.XMLHttpRequest){
		xmlreq=new XMLHttpRequest();
	}else if(window.ActiveXObject){
		try{
			xmlreq=new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e1){
			try{
				xmlreq=new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e2){}
		}
	}
	return xmlreq;
}

function changeCaptcha(){
	var urlString="/getMagicCode.jsp";
	if(readyStatus==1){
		if(httpRequester){
			readyStatus=0;
			httpRequester.open("POST",urlString,true);
			httpRequester.onreadystatechange=function(){
				if(httpRequester.readyState==4){
					document.getElementById('mcverify').value = httpRequester.responseText;
					document.getElementById('captcha_image').src = "/captchImage?mc="+httpRequester.responseText;
					readyStatus=1;
				}
			}
			httpRequester.send(null);
		}
	}
}

function validateAbuseForm(){
    var valid = true;
    if(document.abuse_form.email.value==""){
            alert("Please enter the required email Id");
            valid=false;
            document.abuse_form.email.focus();
    }
    else if(!validateEmail(document.abuse_form.email.value) ){
            alert("Please enter the valid email Id");
            valid=false;
            document.abuse_form.email.focus();
    }
    else if(document.abuse_form.reason.value==""){
            alert("Please select the required reason");
            valid=false;
            document.abuse_form.reason.focus();
    }
    return valid;
}


function submitReportAbuseForm()
{
        var urlString="/formReportAbuse.jsp";urlString+="?email="+document.abuse_form.email.value;urlString+="&reason="+document.abuse_form.reason.value;urlString+="&desc="+document.abuse_form.desc.value;

        if(readyStatus==1)
        {
                if(httpRequester){
                        readyStatus=0;
                        httpRequester.open("POST",urlString,true);
                        httpRequester.onreadystatechange=function()
                        {
                                if(httpRequester.readyState==4)
                                {
                                        readyStatus=1;
                                        var status=httpRequester.responseText;
                                        if(status.search("SUCCESS")!=-1)
                                        {
                                                alert("Thank you for sending us your feedback. We will get back to you soon.");
                                                location.href="/";
                                        }
                                        else
                                        {
                                                alert("Sorry, your form could not be submitted. Please try again later.");
                                        }
                                }
                        }
                                httpRequester.send(null);
                }
        }
}

/* Function to get_secondaryGenre(); */
function get_secondaryGenre(elem,sec_gen)
{
	var urlString="/getSecondaryGenre.jsp?pri_genre="+escape(elem);
	if(sec_gen != ""){
		urlString+="&sec_gnr="+escape(sec_gen);
	}
	if(readyStatus==1)
    {
		if(httpRequester)
		{
			readyStatus=0;
			httpRequester.open("POST",urlString,true);
			httpRequester.onreadystatechange=function()
			{
				if(httpRequester.readyState==4)
				{
					readyStatus=1;
					document.getElementById("div_secgenre").innerHTML=httpRequester.responseText;
				}
			}
			httpRequester.send(null);
		}
	}
}


/* shoutcast.js end*/