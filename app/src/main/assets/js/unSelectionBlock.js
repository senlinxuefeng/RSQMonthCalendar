/*
* @Author: qin yang
* @Date:   2016-07-29 13:10:56
* @Last Modified by:   qin yang
* @Last Modified time: 2016-08-15 21:12:40
*/

$(function () {
    var UnSelectionBlock = function () {
        this._init();
    };
    UnSelectionBlock.className = {
        wrapper: 'unselection-wrapper',
        inlineWrapper: 'unselection-inline-wrapper',
        img: 'unselection-img',
        attach: 'unselection-attach',
        select: 'unselection-select',
        content: 'unselection-content',
        preview: 'unselection-attach-preview',
        _delete: 'unselection-attach-delete',
    };
    UnSelectionBlock.attr = {
        select: 'data-unselection-select',
        bucket: 'data-bucket',
        key: 'data-key-name',
        unique: 'data-unique-id',
        fileId: 'data-file-id',
        fileName: 'data-file-name',
        fileSrc: 'data-file-src',
        attach: 'data-attach',
        img: 'data-img',
        name: 'data-name'
    };
    UnSelectionBlock._tpl = {
        wrapper: '<p class="unselection-wrapper"></p>',
        img: '<img src="" alt="" />'
    };
    UnSelectionBlock._touchmove = false;
    UnSelectionBlock.getImgHtml = function (data) {
        var wrapper, $img;
        wrapper = UnSelectionBlock.getWrapper(data);
        wrapper.append(UnSelectionBlock._tpl.img);
        wrapper.attr(UnSelectionBlock.attr.img, true);
        if (data && data.file) {
            $img = wrapper.find('img');
            $img.attr('src', data.file.realPath);
            $img.attr('alt', data.file.name);
            $img.attr(UnSelectionBlock.attr.name, data.file.name);
            $img.attr(UnSelectionBlock.attr.bucket, data.bucket);
            $img.attr(UnSelectionBlock.attr.key, data.key || data.file.filePath);
            if (data.width) {
                $img.attr('width', data.width);
            }
            if (data.height) {
                $img.attr('height', data.height);
            }
            if (data.alt) {
                $img.attr('alt', data.alt);
            }
        }
        return $(document.createElement('div')).append(wrapper).html();
    };
    UnSelectionBlock.getWrapper = function (data) {
        var wrapper;
        data = data || { file: {} };
        wrapper = $(UnSelectionBlock._tpl.wrapper);
        wrapper.attr(UnSelectionBlock.attr.unique, UnSelectionBlock._guidGenerator());
        wrapper.attr(UnSelectionBlock.attr.fileId, data.file.id);
        return wrapper;
    };
    UnSelectionBlock._guidGenerator = function () {
        var S4 = function () {
            return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
        };
        return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    };

    UnSelectionBlock.prototype._init = function () {
        this.editor = window.RE;
        this._setEvent();
    }

    UnSelectionBlock.prototype._setEvent = function () {
        document.addEventListener('selectionchange', this._onSelectionChange.bind(this));
        $(document).on('keydown.unSelection', this._keyDownEvent.bind(this));
        this.editor.body.on('touchend.unSelection', '[data-attach="true"]', this._onAttachClick.bind(this));        
        this.editor.body.on('touchend.unSelection', '[data-img="true"]', this._onImgClick.bind(this));
        this.editor.body.on('touchend.unSelection', '.unselection-wrapper', this._onWrapperTouchEnd.bind(this));
        this.editor.body.on('touchmove.unSelection', '.unselection-wrapper', this._onWrapperTouchMove.bind(this));
    }

    UnSelectionBlock.prototype._onWrapperTouchEnd = function (e) {
        UnSelectionBlock._touchmove = false;
        e.preventDefault();
    }
    UnSelectionBlock.prototype._onWrapperTouchMove = function (e) {
        UnSelectionBlock._touchmove = true;
    }

    UnSelectionBlock.prototype._keyDownEvent = function (e) {
        if (e.which === 8) {
            this._deleteKeyDown = true;
        }
        if (this._selectedWrapper) {
            switch (e.which) {
                case 13:
                    this._skipToNextNewLine();
                    e.preventDefault();
                    break;
                case 40:
                case 39:
                    this._skipToNextLine();
                    e.preventDefault();
                    break;
                case 38:
                case 37:
                    this._skipToPrevLine();
                    e.preventDefault();
                    break;
                case 8:
                    this._delete();
                    e.preventDefault();
            }
            e.preventDefault();
        }
    }

    UnSelectionBlock.prototype._onAttachClick = function (e) {
        if (UnSelectionBlock._touchmove) return;
        var $wrapper, uniqueId, fileId, fileName, filsSrc;
        $wrapper = $(e.currentTarget);
        uniqueId = $wrapper.attr(UnSelectionBlock.attr.unique);
        fileId = $wrapper.attr(UnSelectionBlock.attr.fileId);
        fileName = $wrapper.find('[data-name]').attr('data-name');
        fileSrc = $wrapper.find('.unselection-attach-download').attr('href');
        this.editor.onAttachClick({
            uniqueId: uniqueId,
            fileId: fileId,
            fileName: fileName,
            fileSrc: fileSrc
        });
    }

    UnSelectionBlock.prototype._onImgClick = function (e) {
        if (UnSelectionBlock._touchmove) return;
        var $wrapper, uniqueId, fileId, fileName, filsSrc;
        $wrapper = $(e.currentTarget);
        uniqueId = $wrapper.attr(UnSelectionBlock.attr.unique);
        fileId = $wrapper.attr(UnSelectionBlock.attr.fileId);
        fileName = $wrapper.find('img').attr('data-name');
        fileSrc = $wrapper.find('img').attr('src');
        this.editor.onImgClick({
            uniqueId: uniqueId,
            fileId: fileId,
            fileName: fileName,
            fileSrc: fileSrc
        });
    }

    UnSelectionBlock.prototype._skipToNextNewLine = function () {
        var p, wrapper, range;
        p = document.createElement('p');
        p.innerHTML = '<br>';
        wrapper = this.editor.util.getRootNodeFromNode(this._selectedWrapper)
        wrapper = $(wrapper);
        wrapper.after(p);
        range = document.createRange();
        this.editor.selection.setRangeAtStartOf(p, range);
    }

    UnSelectionBlock.prototype._skipToNextLine = function () {
        var wrapper, nextSibling, range;
        wrapper = this._selectedWrapper[0];
        nextSibling = this.editor.util.getNextNode(wrapper);
        if (nextSibling) {
            range = document.createRange();
            this.editor.selection.setRangeAtStartOf(nextSibling, range);
        }      
    }

    UnSelectionBlock.prototype._skipToPrevLine = function () {
        var wrapper, previousSibling, range;
        wrapper = this._selectedWrapper[0];
        previousSibling = this.editor.util.getPrevNode(wrapper);
        if (previousSibling) {
            range = document.createRange()
            this.editor.selection.setRangeAtEndOf(previousSibling, range)
        }          
    }

    UnSelectionBlock.prototype._delete = function (wrapper) {
        var previousSibling, range;
        if (!wrapper) wrapper = this._selectedWrapper;
        if (wrapper) {
            previousSibling = this.editor.util.getPrevNode(wrapper);
            wrapper.remove();
            if (previousSibling) {
                range = document.createRange();
                this.editor.selection.setRangeAtEndOf(previousSibling, range);
            }
            this.editor.callback();
        }
    }

    UnSelectionBlock.prototype._deleteByUniqueId = function (uniqueId) {
        var wrapper = this.editor.body.find('[' + UnSelectionBlock.attr.unique + '="' + uniqueId + '"]');
        if (wrapper.length) {
            this._delete(wrapper);
        }
    }

    UnSelectionBlock.prototype._onSelectionChange = function (e) {
        var range, wrapper, wrapper1, wrapper2;
        range = this.editor.selection.range();
        if (range && range.endContainer) {
            wrapper1 = $(range.endContainer).closest('.' + UnSelectionBlock.className.wrapper)
            wrapper2 = $(range.endContainer).find('.' + UnSelectionBlock.className.wrapper).last()
            if (wrapper1.length) wrapper = wrapper1;
            if (wrapper2.length) wrapper = wrapper2;
            if (wrapper) {
                setTimeout((function (_this) {
                    return function () {
                        _this._selectWrapper(wrapper);
                    }
                })(this), 150);
            } else {
                if (this._selectedWrapper) {
                    this._selectCurrent(false);
                }
            }
        }
    }

    UnSelectionBlock.prototype._selectWrapper = function (wrapper) {
        if (window.client.browser.android && this._deleteKeyDown) {
            this._delete(wrapper);
            this._deleteKeyDown = false;
        } else {
            if ($.contains(this.editor.body[0], wrapper[0])) {
                this.editor.selection.clear();
                this._selectCurrent(false);
                this._selectedWrapper = wrapper;
                this._selectCurrent(true);
            }
        }
    }

    UnSelectionBlock.prototype._selectCurrent = function (type) {
        if (this._selectedWrapper) {
            if (type) {
                this._selectedWrapper.attr(UnSelectionBlock.attr.select, 'true');
            } else {
                this._selectedWrapper.removeAttr(UnSelectionBlock.attr.select);
                this._selectedWrapper = null;
            }
        }
    }

    window.unSelectionBlock = new UnSelectionBlock();
    window.UnSelectionBlock = UnSelectionBlock;
});