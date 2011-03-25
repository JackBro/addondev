/*

highlight v3

Highlights arbitrary terms.

<http://johannburkard.de/blog/programming/javascript/highlight-javascript-text-higlighting-jquery-plugin.html>

MIT license.

Johann Burkard
<http://johannburkard.de>
<mailto:jb@eaio.com>

*/

/* 

modified for incremental search at 2011
konkon

*/

/// <reference path="jquery-1.5.min.js"/>

jQuery.fn.incrementalSearch = function (pat) {
    function innerHighlight(node, pat, cindex) {
        var skip = 0;
        if (node.nodeType == 3) {
            var pos = node.data.toUpperCase().indexOf(pat);
            if (pos >= 0) {
                if (jQuery.fn.incrementalSearchtmpindex === undefined) {

                    //jQuery.fn.highlighttmpnode=node.parentNode;
                    jQuery.fn.incrementalSearchtmpindex = cindex;
                    jQuery.fn.incrementalSearchtmppos = pos;

                    var spannode = document.createElement('span');
                    spannode.className = 'highlight';
                    var middlebit = node.splitText(pos);
                    var endbit = middlebit.splitText(pat.length);
                    var middleclone = middlebit.cloneNode(true);
                    spannode.appendChild(middleclone);
                    middlebit.parentNode.replaceChild(spannode, middlebit);


                    skip = 1;
                    exi = 1;
                } else {
                    if (jQuery.fn.incrementalSearchtmpindex < cindex) {
                        if (pos >= 0) {
                            //jQuery.fn.highlighttmpnode=node.parentNode;
                            jQuery.fn.incrementalSearchtmppos = pos;
                            jQuery.fn.incrementalSearchtmpindex = cindex;
                            var spannode = document.createElement('span');
                            spannode.className = 'highlight';
                            var middlebit = node.splitText(pos);
                            var endbit = middlebit.splitText(pat.length);
                            var middleclone = middlebit.cloneNode(true);
                            spannode.appendChild(middleclone);
                            middlebit.parentNode.replaceChild(spannode, middlebit);
                            exi = 1;
                        }
                        skip = 1;
                    } else

                        if (jQuery.fn.incrementalSearchtmpindex == cindex) {
                            if (jQuery.fn.incrementalSearchInc == true) {
                                pos = node.data.toUpperCase().indexOf(pat, jQuery.fn.incrementalSearchtmppos);
                            } else {
                                pos = node.data.toUpperCase().indexOf(pat, jQuery.fn.incrementalSearchtmppos + 1);
                            }
                            if (pos >= 0) {
                                //jQuery.fn.highlighttmpnode=node.parentNode;
                                jQuery.fn.incrementalSearchtmppos = pos;
                                jQuery.fn.incrementalSearchtmpindex = cindex;

                                var spannode = document.createElement('span');
                                spannode.className = 'highlight';
                                var middlebit = node.splitText(pos);
                                var endbit = middlebit.splitText(pat.length);
                                var middleclone = middlebit.cloneNode(true);
                                spannode.appendChild(middleclone);
                                middlebit.parentNode.replaceChild(spannode, middlebit);

                                skip = 1;
                                exi = 1;
                            }
                            skip = 1;
                        }
                }
            } else {
                //skip = 1;
            }
        }
        else if (node.nodeType == 1 && node.childNodes && !/(script|style)/i.test(node.tagName)) {
            for (var i = 0; i < node.childNodes.length; ++i) {
                if (exi == 1) {
                    jQuery.fn.incrementalSearchfind = true;
                    //jindex = 0;
                    //jQuery.fn.highlighttmpindex = undefined;
                    break;
                }
                i += innerHighlight(node.childNodes[i], pat, jindex);
                jindex++;
            }
        }
        return skip;
    }
    if (jQuery.fn.incrementalSearchfind === undefined || jQuery.fn.incrementalSearchfind == false) {
        jQuery.fn.incrementalSearchtmpindex = undefined;
    }
    jQuery.fn.incrementalSearchInc = false;
    if (jQuery.fn.incrementalSearchpat === undefined) {
        jQuery.fn.incrementalSearchInc = true;
        jQuery.fn.incrementalSearchpat = pat;
    } else if (jQuery.fn.incrementalSearchpat != pat) {
        jQuery.fn.incrementalSearchInc = true;
        jQuery.fn.incrementalSearchpat = pat;
    } else {
        jQuery.fn.incrementalSearchInc = false;
    }
    jQuery.fn.incrementalSearchfind = false;

    var jindex = 0;
    return this.each(function () {
        exi = 0;
        innerHighlight(this, pat.toUpperCase(), jindex);
        jindex++;
        if (exi == 1) {
            //jindex = 0;
            //jQuery.fn.highlighttmpindex = undefined;
            
            //var fooOffset = $('.highlight').offset(); // fooの表示位置を取得
            //$(document).scrollTop(fooOffset.top); // fooの位置までスクロール
            return;
        }

    });
};

jQuery.fn.removeHighlight = function () {
    return this.find("span.highlight").each(function () {
        this.parentNode.firstChild.nodeName;
        with (this.parentNode) {
            replaceChild(this.firstChild, this);
            normalize();
        }
    }).end();
};
