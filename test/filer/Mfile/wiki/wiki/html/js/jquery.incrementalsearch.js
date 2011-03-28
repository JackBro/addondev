
/// <reference path="jquery-1.5.min.js"/>

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

(function (jQuery) {

    var selelems = [];
    var curselelem;
    var curselelemoffset;
    var incSearchElemIndex = -1;
    var incSearchPos = -1;
    var SEL_HIGHLIGHT = 'selecthighlight';
    var CUR_SEL_HIGHLIGHT = 'curselecthighlight';
    var INC_HIGHLIGHT = 'inchighlight';

    jQuery.fn.searchReset = function () {
        jQuery(this).removeHighlight();
        incSearchElemIndex = -1;
        incSearchPos = -1;
    };

    jQuery.fn.highlight = function (pat) {
        selelems = [];

        var isstring = typeof pat == 'string';
        if (isstring) {
            pat = pat.toUpperCase();
        }

        function innerHighlight(node, pat) {
            var skip = 0;
            if (node.nodeType == 3) {
                var pos;
                var matchlen = 0;
                if (isstring) {
                    pos = node.data.toUpperCase().indexOf(pat);
                    matchlen = pat.length;
                } else {
                    var res = node.data.match(pat);
                    if (res) {
                        pos = RegExp.index;
                        matchlen = res[0].length;
                    } else {
                        pos = -1;
                    }
                }
                if (pos >= 0) {
                    var spannode = document.createElement('span');
                    spannode.className = SEL_HIGHLIGHT;
                    var middlebit = node.splitText(pos);
                    var endbit = middlebit.splitText(matchlen);
                    var middleclone = middlebit.cloneNode(true);
                    spannode.appendChild(middleclone);
                    middlebit.parentNode.replaceChild(spannode, middlebit);
                    skip = 1;

                    selelems.push(spannode);
                }
            }
            else if (node.nodeType == 1 && node.childNodes && !/(script|style)/i.test(node.tagName)) {
                for (var i = 0; i < node.childNodes.length; ++i) {
                    i += innerHighlight(node.childNodes[i], pat);
                }
            }
            return skip;
        }
        return this.each(function () {
            innerHighlight(this, isstring ? pat.toUpperCase() : pat);
        });
    };



    jQuery.fn.search = function (pat, prev, offset) {
        offset = jQuery.extend({ left: 0, top: 0 }, offset);

        if (jQuery(this).find('.' + CUR_SEL_HIGHLIGHT).length) {
            //if (selelems.length>0){
            nextSearch(jQuery(this), prev);
        } else {
            //alert("search firstSearch prev= " + prev);
            firstSearch(jQuery(this), pat, prev, offset);
        }
    };

    function firstSearch(target, pat, prev, startoffset) {

        target.removeHighlight().highlight(pat);

        if (selelems.length > 0) {
            //var ary = selelems;
            var len = selelems.length;
            var h = jQuery(selelems[0]).height();
            var elem;

            for (var i = 0; i < len; i++) {
                var elemoffset = jQuery(selelems[i]).offset();

                if ((elemoffset.top >= startoffset.top - h / 2 && elemoffset.left >= startoffset.left)
                    || elemoffset.top >= startoffset.top - h / 2) {
                    elem = selelems[i];
                    if (prev && i > 0) {
                        elem = ary[i - 1];
                    }
                    break;
                }
            }
            if (elem) {
                curselelem = elem;
                jQuery(elem).addClass(CUR_SEL_HIGHLIGHT);
            }
        }
    };

    function nextSearch(target, prev) {
        if (!curselelem || selelems.length == 0) return;

        var isfind = true;
        target.find('span.' + SEL_HIGHLIGHT).each(function () {

            var index = jQuery.inArray(curselelem, selelems);
            if (index >= 0 && index < selelems.length) {

                var elem = selelems[index];
                if (prev) {
                    if (index > 0) {
                        elem = selelems[index - 1];
                    } else {
                        isfind = false;
                    }
                } else {
                    if (index == selelems.length - 1) {
                        isfind = false;
                    } else {
                        elem = selelems[index + 1];
                    }
                }
                curselelem = elem;

                if (isfind) {
                    for (var i = 0; i < selelems.length; i++) {
                        if (jQuery(selelems[i]).hasClass(CUR_SEL_HIGHLIGHT)) {
                            jQuery(selelems[i]).removeClass(CUR_SEL_HIGHLIGHT);
                            break;
                        }
                    }
                    curselelem = elem;
                    jQuery(curselelem).addClass(CUR_SEL_HIGHLIGHT);
                }

                return false;
            }
        });
    };

    jQuery.fn.incrementalSearch = function (pat, startoffset) {
        selelems = [];

        if ($('.' + CUR_SEL_HIGHLIGHT).length) {
            startoffset = $('.' + CUR_SEL_HIGHLIGHT).offset();
            jQuery(this).searchReset();
        } else {
            startoffset = jQuery.extend({ left: 0, top: 0 }, startoffset);
        }

        var isstring = typeof pat == 'string';
        if (isstring) {
            pat = pat.toUpperCase();
        }

        function createSpannode(node, pos, length) {
            var spannode = document.createElement('span');
            spannode.className = INC_HIGHLIGHT; // CUR_SEL_HIGHLIGHT; // 'highlight2';
            var middlebit = node.splitText(pos);
            var endbit = middlebit.splitText(length);
            var middleclone = middlebit.cloneNode(true);
            spannode.appendChild(middleclone);
            middlebit.parentNode.replaceChild(spannode, middlebit);
            return spannode;
        }



        function innerHighlight(node, pat, cindex) {
            var skip = 0;
            if (node.nodeType == 3) {
                //var pos = node.data.toUpperCase().indexOf(pat);
                var pos;
                var matchlen = 0;
                if (isstring) {
                    pos = node.data.toUpperCase().indexOf(pat);
                    matchlen = pat.length;
                } else {
                    var res = node.data.match(pat);
                    if (res) {
                        pos = RegExp.index;
                        matchlen = res[0].length;
                    } else {
                        pos = -1;
                    }
                }
                if (pos >= 0) {
                    if (incSearchElemIndex < 0) {
                        var spannode = createSpannode(node, pos, matchlen);
                        var h = jQuery(spannode).height();
                        var elemoffset = jQuery(spannode).offset();
                        //if (elemoffset.left >= startoffset.left && elemoffset.top >= startoffset.top) {
                        if ((elemoffset.top >= startoffset.top - h / 2 && elemoffset.left >= startoffset.left)
                            || elemoffset.top >= startoffset.top - h / 2) {
                            incSearchElemIndex = cindex;
                            incSearchPos = pos;
                            curselelem = spannode;
                            exi = 1;
                        } else {
                            spannode.parentNode.firstChild.nodeName;
                            with (spannode.parentNode) {
                                replaceChild(spannode.firstChild, spannode);
                                normalize();
                            }
                        }
                        skip = 1;
                    } else {
                        if (incSearchElemIndex < cindex) {
                            incSearchElemIndex = cindex;
                            incSearchPos = pos;
                            curselelem = createSpannode(node, pos, matchlen);

                            exi = 1;
                            skip = 1;
                        } else if (incSearchElemIndex == cindex) {
                            if (isstring) {
                                pos = node.data.toUpperCase().indexOf(pat, incSearchPos);
                                matchlen = pat.length;
                            } else {
                                var res = node.data.match(pat);
                                if (res) {
                                    pos = RegExp.index;
                                    matchlen = res[0].length;
                                } else {
                                    pos = -1;
                                }
                            }
                            //pos = node.data.toUpperCase().indexOf(pat, incSearchPos);
                            if (pos >= 0) {
                                incSearchElemIndex = cindex;
                                incSearchPos = pos;
                                curselelem = createSpannode(node, pos, matchlen);

                                exi = 1;
                            }
                            skip = 1;
                        }
                    }
                }
            }
            else if (node.nodeType == 1 && node.childNodes && !/(script|style)/i.test(node.tagName)) {
                for (var i = 0; i < node.childNodes.length; ++i) {
                    if (exi == 1) {
                        jQuery.fn.incrementalSearchfind = true;
                        break;
                    }
                    i += innerHighlight(node.childNodes[i], pat, jindex);
                    jindex++;
                }
            }
            return skip;
        }

        var jindex = 0;
        return this.each(function () {
            exi = 0;
            innerHighlight(this, pat.toUpperCase(), jindex);
            jindex++;
            if (exi == 1) {
                return;
            }
        });
    };

    jQuery.fn.removeHighlight = function () {
        //return this.find("span.highlight").each(function () {
        return this.find('span.' + SEL_HIGHLIGHT + ', span.' + CUR_SEL_HIGHLIGHT + ', span.' + INC_HIGHLIGHT).each(function () {
            this.parentNode.firstChild.nodeName;
            with (this.parentNode) {
                replaceChild(this.firstChild, this);
                normalize();
            }
        }).end();
    };
})(jQuery);