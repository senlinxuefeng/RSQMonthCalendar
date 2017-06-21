/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var RE = {};

RE.currentSelection = {
    "startContainer": 0,
    "startOffset": 0,
    "endContainer": 0,
    "endOffset": 0};

RE.editor = document.getElementById('editor');
RE.body   = $(RE.editor);
RE.titleTag = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'];
RE.colors   = ['#e33737', '#e28b41', '#c8a732', '#209361', '#418caf', '#aa8773', '#999999', '#333333'];

RE.selection = new window.Selection(RE);
RE.util = window.Util;
RE.util.init(RE);

document.addEventListener("selectionchange", function() { RE.backuprange(); });

// 给原生端进行re-回调
var reCall = function (url) {
    window.location.href = url;
}

// 点击a不进行跳转
$(document).on('touchend.aHref', 'a[href]:not([data-bypass])', function (evt) {
    const href = $(this).prop('href');
    evt.preventDefault();
    reCall("re-link://" + encodeURIComponent(href));
});

// Initializations
// ios下，如果执行了这个回调，会导致 键盘自动退出
RE.callback = function() {
    if (!window.client.browser.iphone) {
        reCall("re-callback://" + encodeURI(RE.getHtml()));
    }
}
// 可编辑
RE.enable = function () {
    RE.editor.setAttribute('contenteditable', 'true');
}
// 不可编辑
RE.disable = function () {
    RE.editor.setAttribute('contenteditable', 'false');
}

RE.setHtml = function(contents) {
    contents = decodeURIComponent(contents.replace(/\+/g, '%20'));
    if (contents === '' || !contents) {
        contents = '<p><br></p>';
        RE.hasAppendEmptyLine = true;
    }
    if (!/<p><br><\/p>$/.test(contents)) {
        contents += '<p><br></p>';
        RE.hasAppendEmptyLine = true;
    }
    RE.editor.innerHTML = contents;
}

RE.getHtml = function() {
    var content = RE.editor.innerHTML;
    if (RE.hasAppendEmptyLine) { // 如果检测到末尾添加过空行
        content = content.replace(/<p><br><\/p>$/g, ''); // 替换末尾的 <p><br><\/p>
    }
    return content;
}

RE.getText = function() {
    return RE.editor.innerText;
}

RE.setBaseTextColor = function(color) {
    RE.editor.style.color  = color;
}

RE.setBaseFontSize = function(size) {
    RE.editor.style.fontSize = size;
}

RE.setPadding = function(left, top, right, bottom) {
  RE.editor.style.paddingLeft = left;
  RE.editor.style.paddingTop = top;
  RE.editor.style.paddingRight = right;
  RE.editor.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function(color) {
    document.body.style.backgroundColor = color;
}

RE.setBackgroundImage = function(image) {
    RE.editor.style.backgroundImage = image;
}

RE.setWidth = function(size) {
    RE.editor.style.minWidth = size;
}

RE.setHeight = function(size) {
    RE.editor.style.height = size;
}

RE.setTextAlign = function(align) {
    RE.editor.style.textAlign = align;
}

RE.setVerticalAlign = function(align) {
    RE.editor.style.verticalAlign = align;
}

RE.setPlaceholder = function(placeholder) {
    RE.editor.setAttribute("placeholder", placeholder);
}

RE.undo = function() {
    document.execCommand('undo', false, null);
}

RE.redo = function() {
    document.execCommand('redo', false, null);
}

RE.setBold = function() {
    document.execCommand('bold', false, null);
}

RE.setItalic = function() {
    document.execCommand('italic', false, null);
}

RE.setSubscript = function() {
    document.execCommand('subscript', false, null);
}

RE.setSuperscript = function() {
    document.execCommand('superscript', false, null);
}

RE.setStrikeThrough = function() {
    document.execCommand('strikeThrough', false, null);
}

RE.setUnderline = function() {
    document.execCommand('underline', false, null);
}

RE.setBullets = function() {
    document.execCommand('InsertUnorderedList', false, null);
}

RE.setNumbers = function() {
    document.execCommand('InsertOrderedList', false, null);
}

RE.setTextColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('foreColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setTextBackgroundColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('hiliteColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setFontSize = function(fontSize){
    document.execCommand("fontSize", false, fontSize);
}

RE.setHeading = function(heading) {
    document.execCommand('formatBlock', false, '<h'+heading+'>');
}

RE.setIndent = function() {
    document.execCommand('indent', false, null);
}

RE.setOutdent = function() {
    document.execCommand('outdent', false, null);
}

RE.setJustifyLeft = function() {
    document.execCommand('justifyLeft', false, null);
}

RE.setJustifyCenter = function() {
    document.execCommand('justifyCenter', false, null);
}

RE.setJustifyRight = function() {
    document.execCommand('justifyRight', false, null);
}

RE.setBlockquote = function() {
    document.execCommand('formatBlock', false, '<blockquote>');
}

RE.setInsertUserTo = function(id,text) {
    var html = '<a href="javascript:;" data-id="'+ id +'" class="rui-mention" data-mention="true">'+ text +'</a>';
    document.execCommand('insertHTML', false, html);
}

RE.insertImage = function(url, fileName, key, bucketName, id, picWidth, picHeight) {
    RE.restorerange();
    var range = this.selection.range();
    range.deleteContents();
    this.selection.range(range);
    var html = window.UnSelectionBlock.getImgHtml({
        bucket: bucketName,
        key: key,
        file: {
            id: id,
            realPath: url,
            name: fileName
        },
        width: picWidth,
        height: picHeight,
        alt: fileName
    });
    var wrapper = $(html);
    var rootNode = this.selection.rootNodes().last();
    if (rootNode && rootNode.length) {
        rootNode.after(wrapper);
        var $emptyLine = $('<p><br></p>');
        wrapper.after($emptyLine);
        // this.selection.setRangeAfter(wrapper.find('img'), range);
        this.selection.setRangeAtStartOf($emptyLine);
    }
    RE.callback();
}

RE.insertHTML = function(html) {
    RE.restorerange();
    document.execCommand('insertHTML', false, html);
}

RE.insertText = function (text) {
    RE.restorerange();
    if (typeof text !== 'string') text = "";
    document.execCommand('insertText', false, text);
}

RE.lineBreak = function () {
    RE.restorerange();
    document.execCommand('insertParagraph', false);
} 

RE.setInsertUnorderedList = function() {
    document.execCommand('insertUnorderedList',false,null);
}

RE.setInsertOrderedList = function() {
    document.execCommand('insertOrderedList',false,null);
}

RE.insertLink = function(url, title) {
    RE.restorerange();
    var sel = document.getSelection();
    if (sel.toString().length == 0) {
        document.execCommand("insertHTML",false,"<a href='"+url+"'>"+title+"</a>");
    } else if (sel.rangeCount) {
       var el = document.createElement("a");
       el.setAttribute("href", url);
       el.setAttribute("title", title);

       var range = sel.getRangeAt(0).cloneRange();
       range.surroundContents(el);
       sel.removeAllRanges();
       sel.addRange(range);
   }
    RE.callback();
}

RE.setTodo = function(text) {
    var html = '<input type="checkbox" name="'+ text +'" value="'+ text +'"/> &nbsp;';
    document.execCommand('insertHTML', false, html);
}

RE.prepareInsert = function() {
    RE.backuprange();
}

RE.backuprange = function(){
    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
      var range = selection.getRangeAt(0);
      RE.currentSelection = {
          "startContainer": range.startContainer,
          "startOffset": range.startOffset,
          "endContainer": range.endContainer,
          "endOffset": range.endOffset};
    }
}

RE.restorerange = function(){
    var selection = window.getSelection();
    selection.removeAllRanges();
    var range = document.createRange();
    range.setStart(RE.currentSelection.startContainer, RE.currentSelection.startOffset);
    range.setEnd(RE.currentSelection.endContainer, RE.currentSelection.endOffset);
    selection.addRange(range);
}

RE.enabledEditingItems = function(e) {
    var items = [];
    if (document.queryCommandState('bold')) {
        items.push('bold');
    }
    if (document.queryCommandState('italic')) {
        items.push('italic');
    }
    if (document.queryCommandState('subscript')) {
        items.push('subscript');
    }
    if (document.queryCommandState('superscript')) {
        items.push('superscript');
    }
    if (document.queryCommandState('strikeThrough')) {
        items.push('strikeThrough');
    }
    if (document.queryCommandState('underline')) {
        items.push('underline');
    }
    if (document.queryCommandState('insertOrderedList')) {
        items.push('orderedList');
    }
    if (document.queryCommandState('insertUnorderedList')) {
        items.push('unorderedList');
    }
    if (document.queryCommandState('justifyCenter')) {
        items.push('justifyCenter');
    }
    if (document.queryCommandState('justifyFull')) {
        items.push('justifyFull');
    }
    if (document.queryCommandState('justifyLeft')) {
        items.push('justifyLeft');
    }
    if (document.queryCommandState('justifyRight')) {
        items.push('justifyRight');
    }
    if (document.queryCommandState('insertHorizontalRule')) {
        items.push('horizontalRule');
    }
    var formatBlock = document.queryCommandValue('formatBlock');
    if (formatBlock.length > 0 && RE.titleTag.indexOf(formatBlock.toLowerCase()) >= 0) {
        items.push(formatBlock);
    }
    // 如果formatBlock不是titleTag(h1-h6),那么就标记为普通文本
    if (RE.titleTag.indexOf(formatBlock.toLowerCase()) === -1) {
        items.push('normal');
    }

    var selection = document.getSelection();
    if (selection.rangeCount) {
        var range     = selection.getRangeAt(0);
        var startNode = range.startContainer;
        var node      = startNode.nodeName !== '#text' ? startNode : $(startNode).parent()[0];
        var color = window.getComputedStyle(node, null).getPropertyValue('color');
        color = color.indexOf('#') >= 0 ? color : RE.convertRgbToHex(color);
        if (color && RE.colors.indexOf(color) >= 0) {
            items.push('color_' + color.replace('#', ''));
        }
    }
    reCall("re-state://" + encodeURI(items.join(",")));
}
RE.convertRgbToHex = function (rgb) {
    var re = /rgb\((\d+),\s?(\d+),\s?(\d+)\)/g;
    var match = re.exec(rgb);
    if (!match) return '';

    var rgbToHex = function (r, g, b) {
        var componentToHex = function (c) {
            var hex = c.toString(16);
            return hex.length === 1 ? '0' + hex : hex;
        };
        return '#' + componentToHex(r) + componentToHex(g) + componentToHex(b);
    };

    return rgbToHex(match[1] * 1, match[2] * 1, match[3] * 1);
}

RE.focus = function() {
    var range = document.createRange();
    range.selectNodeContents(RE.editor);
    range.collapse(false);
    var selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    RE.editor.focus();
}

RE.blurFocus = function() {
    RE.editor.blur();
}

RE.removeFormat = function() {
    execCommand('removeFormat', false, null);
}

// 把选中的设置成普通文字
RE.setCommonTxt = function () {
    RE.selection.range();
    var $rootNodes = RE.selection.rootNodes();
    RE.selection.save();
    $rootNodes.each(function (i, node) {
        var $node = $(node);
        if ($node.is('blockquote') || $node.is('p') || $node.is('pre, table') || $node.is('#editor')) {
            return;
        }
        var contents = $node.contents();
        $('<p/>').append(contents).replaceAll($node);
    });
    RE.selection.restore();
}

// 可编辑
RE.setEnable = function () {
    RE.editor.setAttribute('contenteditable', 'true');
}
// 不可编辑
RE.setDisable = function () {
    RE.editor.setAttribute('contenteditable', 'false');
}

RE.onChange = function (e, now) {
    var _now = $.trim(now);
    if (_now === '') {
        this.body.trigger('empty');
    }
}
RE.onEmpty = function () {
    RE.setHtml('<p><br/></p>');
}
// 当点击附件的时候调用
RE.onAttachClick = function (data) {
    var uniqueId, fileId, fileName, fileSrc;
    uniqueId = encodeURIComponent(data.uniqueId);
    fileId = encodeURIComponent(data.fileId);
    fileName = encodeURIComponent(data.fileName);
    fileSrc = encodeURIComponent(data.fileSrc);
    reCall("re-attach://" + "uniqueId=" + uniqueId + "&" + "fileId=" + fileId + "&" + "fileName=" + fileName + "&" + "fileSrc=" + fileSrc);
};

// 通过uniqueId来删除附件
RE.deleteAttach = function (uniqueId) {
    if (uniqueId) {
        window.unSelectionBlock._deleteByUniqueId(uniqueId);
    }
};

RE.onImgClick = function (data) {
    var uniqueId, fileId, fileName, fileSrc;
    uniqueId = encodeURIComponent(data.uniqueId);
    fileId = encodeURIComponent(data.fileId);
    fileName = encodeURIComponent(data.fileName);
    fileSrc = encodeURIComponent(data.fileSrc);
    reCall("re-img://" + "uniqueId=" + uniqueId + "&" + "fileId=" + fileId + "&" + "fileName=" + fileName + "&" + "fileSrc=" + fileSrc);
};

RE.deleteImg = function (uniqueId) {
    if (uniqueId) {
        window.unSelectionBlock._deleteByUniqueId(uniqueId);
    }
};

// Event Listeners
RE.editor.addEventListener("input", RE.callback);
RE.body.on("change", RE.onChange.bind(RE));
RE.body.on('empty', RE.onEmpty.bind(RE));
RE.editor.addEventListener("keyup", function(e) {
    var KEY_LEFT = 37, KEY_RIGHT = 39;
    if (e.which == KEY_LEFT || e.which == KEY_RIGHT) {
        RE.enabledEditingItems(e);
    }
});
RE.editor.addEventListener("click", RE.enabledEditingItems);

RE.body.on('focus', function() {
    var $this = $(this);
    $this.data('before', $this.html());
    setTimeout(function () {
        if (!window.client.browser.iphone) {
            reCall("re-editor://focus");
        }
    }, 0);
    return $this;
}).on('blur keyup paste input', function() {
    var $this = $(this);
    var before = $this.data('before');
    var now    = $this.html();
    if (before !== now) {
        $this.data('before', now);
        $this.trigger('change', now);
    }
    return $this;
}).on('blur', function () {
    setTimeout(function () {
        if (!window.client.browser.iphone) {
            reCall("re-editor://blur");
        }
    }, 0);
}).on('keydown', function (e) {
    if (e.which === 50 || e.which === 229) { // 等于229的时候，是百度，搜狗输入法，敲击@键的值
        setTimeout(function () {
            var range = RE.selection.range();
            var $container = RE.selection.containerNode();
            if (range && $container.length) {
                var text = $container.text();
                var charAtEndOffset = text.charAt(range.endOffset - 1);
                if (charAtEndOffset === '@') {
                    var beforeCharCode = text.charCodeAt(range.endOffset - 2);
                    if (isNaN(beforeCharCode) || beforeCharCode === 32 || beforeCharCode === 64 || beforeCharCode > 255) { // 32是空格, 64是@, 大于255是中文
                        document.execCommand('delete', false);
                        reCall("re-at://at");
                    }
                }
            }
        }, 100);
    }
})