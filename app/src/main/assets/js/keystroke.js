/*
* @Author: qin yang
* @Date:   2016-08-02 10:28:55
* @Last Modified by:   qin yang
* @Last Modified time: 2017-01-03 14:50:57
*/

$(function () {
    var Keystroke = function () {
        this._init();
    };

    Keystroke.prototype._init = function () {
        this.editor = window.RE;
        this._initKeystrokeHandlers();
    };

    Keystroke.prototype._initKeystrokeHandlers = function () {
        this.editor.body.on('keydown.keystroke', this._backspaceAll.bind(this));
    };

    // 监听所有元素的删除事件
    Keystroke.prototype._backspaceAll = function (e) {
        if (e.which !== 8) return;
        var $rootBlock, $prevBlockEl, $unSelectionWrapper;
        $rootBlock = this.editor.selection.rootNodes().first();
        $prevBlockEl = $rootBlock.prev();

        if ($prevBlockEl.closest('.unselection-wrapper').length) {
            $unSelectionWrapper = $prevBlockEl.closest('.unselection-wrapper');
        } else if ($prevBlockEl.find('.unselection-wrapper').length) {
            $unSelectionWrapper = $prevBlockEl.find('.unselection-wrapper').last()
        }

        if ($unSelectionWrapper && this.editor.selection.rangeAtStartOf($rootBlock)) {
            this.editor.selection.setRangeAtStartOf($unSelectionWrapper);
            e.preventDefault();
        }
    };

    window.Keystroke = new Keystroke();
});