if (typeof com === "undefined") {
    var com = {}
}
if (typeof com.aol === "undefined") {
    com.aol = {}
}
if (typeof com.aol.video === "undefined") {
    com.aol.video = {}
}
if (typeof com.aol.video.portal === "undefined") {
    com.aol.video.portal = {}
}
//jQuery.noConflict();
com.aol.video.portal.Init = function() {
	$(document.body).unbind('click');
    com.aol.video.portal.UI.onLoad();
    jQuery(document.body).click(com.aol.video.portal.Events.defaultClick);
    //jQuery(".surrogate .thumbnail").mouseenter(com.aol.video.portal.Events.fadeIn);
    //jQuery(".surrogate .thumbnail").mouseleave(com.aol.video.portal.Events.fadeOut);
	$(".surrogate .thumbnail").live("mouseenter", com.aol.video.portal.Events.fadeIn);
	$(".surrogate .thumbnail").live("mouseleave", com.aol.video.portal.Events.fadeOut);
};
com.aol.video.portal.UI = {
    onLoad: function() {
        boxHeight = jQuery("div.videoBody").height();
        jQuery("div.videoBody").height(boxHeight);
        jQuery(".metaLayer .curveTop").attr({
            style: "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',src='/media/popup_top.png');"
        });
        jQuery(".metaLayer .curveBottom").attr({
            style: "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',src='/media/popup_bottom.png');"
        });
        jQuery(".metaLayer .metaInner").attr({
            style: "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingmethod='scale',src='/media/popup_middle.png');"
        });
        if (isSearchPage == "true") {
            jQuery("#GH_search_field").attr("value", com.aol.video.portal.searchText);
        }
    }
};
com.aol.video.portal.Events = {
    fadeIn: function() {
        currentId = jQuery(this).attr("id");
        com.aol.video.portal.surDelay = setTimeout("jQuery('#'+currentId+' .metaLayer').fadeIn('medium')", 250)
    },
    fadeOut: function() {
        clearTimeout(com.aol.video.portal.surDelay);
        jQuery("#" + currentId + " .metaLayer").fadeOut("fast")
    }
};