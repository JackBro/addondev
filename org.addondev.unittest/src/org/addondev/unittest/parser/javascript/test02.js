

//function SourceFileRenamer(context)
//{
//	this.renamedSourceFiles = [];
//	this.context = context;
//	this.bps = [];
//	function ff()
//	{
//		var g=0;
//	}
//}
	
SourceFileRenamer.prototype.checkForRename = function(url, line, props)
{
	this.aaa=0;
	
	var sourceFile = this.context.sourceFileMap[url];
	if (sourceFile.isEval() || sourceFile.isEvent())
	{
		var segs = sourceFile.href.split('/');
		if (segs.length > 2)
		{
			if (segs[segs.length - 2] == "seq")
			{
				this.renamedSourceFiles.push(sourceFile);
				this.bps.push(props);
			}
		}
		this.context.dynamicURLhasBP = true;  // whether not we needed to rename, the dynamic sourceFile has a bp.
	}
	else
	{
	}
	
	function hh()
	{
		var gh=0;
	}
	
	return (this.renamedSourceFiles.length > 0);
};
//
//const updateViewOnShowHook = function()
//{
//    //Firebug.toggleBar(true);
//
//    //FirebugChrome.select(context.currentFrame, "script");
//
//    var stackPanel;// = context.getPanel("callstack");
//    //if (stackPanel)
//    //    stackPanel.refresh(context);
//
//    //context.chrome.focus();
//}

var BreakpointsTemplate = domplate(Firebug.Rep,
{
    tag:
        DIV({onclick: "$onClick"},
            FOR("group", "$groups",
                DIV({class: "breakpointBlock breakpointBlock-$group.name"},
                    H1({class: "breakpointHeader groupHeader"},
                        "$group.title"
                    ),
                    FOR("bp", "$group.breakpoints",
                        DIV({class: "breakpointRow"},
                            DIV({class: "breakpointBlockHead"},
                                INPUT({class: "breakpointCheckbox", type: "checkbox",
                                    _checked: "$bp.checked"}),
                                SPAN({class: "breakpointName"}, "$bp.name"),
                                TAG(FirebugReps.SourceLink.tag, {object: "$bp|getSourceLink"}),
                                IMG({class: "closeButton", src: "blank.gif"})
                            ),
                            DIV({class: "breakpointCode"}, "$bp.sourceLine")
                        )
                    )
                )
            )
        ),

    getSourceLink: function(bp)
    {
        return new SourceLink(bp.href, bp.lineNumber, "js");
    },

    onClick: function(event)
    {
        var panel = Firebug.getElementPanel(event.target);

        if (getAncestorByClass(event.target, "breakpointCheckbox"))
        {
            var sourceLink =
                getElementByClass(event.target.parentNode, "objectLink-sourceLink").repObject;

            panel.noRefresh = true;
            if (event.target.checked)
                fbs.enableBreakpoint(sourceLink.href, sourceLink.line);
            else
                fbs.disableBreakpoint(sourceLink.href, sourceLink.line);
            panel.noRefresh = false;
        }
        else if (getAncestorByClass(event.target, "closeButton"))
        {
            var sourceLink =
                getElementByClass(event.target.parentNode, "objectLink-sourceLink").repObject;

            panel.noRefresh = true;

            var head = getAncestorByClass(event.target, "breakpointBlock");
            var groupName = getClassValue(head, "breakpointBlock");
            if (groupName == "breakpoints")
                fbs.clearBreakpoint(sourceLink.href, sourceLink.line);
            else if (groupName == "errorBreakpoints")
                fbs.clearErrorBreakpoint(sourceLink.href, sourceLink.line);
            else if (groupName == "monitors")
            {
                fbs.unmonitor(sourceLink.href, sourceLink.line)
            }

            var row = getAncestorByClass(event.target, "breakpointRow");
            panel.removeRow(row);

            panel.noRefresh = false;
        }
    }
});

function ArrayEnumerator(array)
{
	this.index = 0;
	this.array = array;
	this.hasMoreElements = function()
	{
		//this.array2 = 0
		return (this.index < array.length);
	}
	this.getNext = function()
	{
		return this.array[++this.index];
	}
}
