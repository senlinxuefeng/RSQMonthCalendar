/*
* @Author: qin yang
* @Date:   2016-07-08 12:19:09
* @Last Modified by:   qin yang
* @Last Modified time: 2017-01-04 11:44:52
*/

$(function () {
    var Client = function () {
        this.setIphoneCss();
    };

    Client.prototype.browser = (function () {
        var ua, iphone, android;
        ua = navigator.userAgent;
        iphone = /iphone|ipad/i.test(ua);
        android = /android/i.test(ua);
        if (iphone) {
            return {
                iphone: true
            };
        } else if (android) {
            return {
                android: true
            };
        } else {
            return {};
        }
    })();

    // 专门给iphone添加一些css
    Client.prototype.setIphoneCss = function () {
        if (this.browser.iphone) {
            $('body').addClass('iphone');
            $('head').append('<link rel="stylesheet" type="text/css" href="css/iphone.css" />');
        }
    };

    window.client = new Client();
})