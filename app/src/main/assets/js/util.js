/*
* @Author: qin yang
* @Date:   2016-07-29 13:56:08
* @Last Modified by:   qin yang
* @Last Modified time: 2016-07-29 15:44:50
*/

(function ($) {
	var Util = function () {}

	Util.prototype.init = function (editor) {
		this.editor = editor;
	}

	Util.prototype.getRootNodeFromNode = function (node) {
		var $node;
		$node = $(node);
    	while (!$node.parent().is(this.editor.body)) {
    		$node = $node.parent();
    	}
    	return $node[0];
	}
	Util.prototype.getNextNode = function (node) {
		var $node, $next;
		$node = $(node);
	    $next = $node.next();
	    if (!$next.length) {
	    	$node = $node.parent();
	    	while (!$next.length && !$node.is(this.editor.body)) {
	    		$next = $node.next();
	    		$node = $node.parent();
		    }
	    }
	    
	    return $next[0];
	}
	Util.prototype.getPrevNode = function (node) {
		var $node, $prev;
		$node = $(node);
	    $prev = $node.prev();
	    if (!$prev.length) {
	    	$node = $node.parent();
	    	while (!$prev.length && !$node.is(this.editor.body)) {
	    		$prev = $node.prev();
	    		$node = $node.parent();
	    	}
	    }
	    return $prev[0];
	}
	Util.prototype.getNodeLength = function (node) {
		node = $(node)[0];
		switch (node.nodeType) {
			case 7:
			case 10:
				return 0;
			case 3:
			case 8:
				return node.length;
			default:
				return node.childNodes.length;
		}
	}
	Util.prototype.isEmptyNode = function (node) {
		$node = $(node);
		return $node.is(':empty') || (!$node.text() && !$node.find(':not(br, span, div)').length);
	}
	window.Util = new Util();
})(jQuery);