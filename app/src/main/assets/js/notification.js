
var MINUTE = 60 * 1000;
var HOUR = 60 * MINUTE;
var DAY = 24 * HOUR;
var WEEK = 7 * DAY;
var MONTH = 31 * DAY;
var YEAR = 12 * MONTH;

Vue.filter('recentlyTimeText', function (time) {
    var offset = Date.parse(new Date()) - Date.parse(new Date(time));
    if (offset > YEAR) {
        return Math.floor(offset / YEAR) + "年前";
    } else if (offset > MONTH) {
        return Math.floor(offset / MONTH) + "个月前";
    } else if (offset > WEEK) {
        return Math.floor(offset / WEEK) + "周前";
    } else if (offset > DAY) {
        return Math.floor(offset / DAY) + "天前";
    } else if (offset > HOUR) {
        return Math.floor(offset / HOUR) + "小时前";
    } else if (offset > MINUTE) {
        return Math.floor(offset / MINUTE) + "分钟前";
    } else {
        return "刚刚";
    }
});

Vue.filter('messageAction', function (message) {
    if (message.type === 'at') {
        if (message.reply && message.reply.id) {
            return '在回复中@了您';
        } else {
            return '在话题中@了您';
        }
    } else {
        return '回复了您的话题';
    }
});

var vmPage = new Vue({
    el: '#page',
    data: {
        messages: []
    },
    methods: {
        
        openTopic: function (topicId) {
            window.notificationBridge.openTopic(topicId);
        },
        
        openUser: function (loginName) {
            window.notificationBridge.openUser(loginName);
        }
        
    }
});

vmPage.$watch('messages', function () {
    $('.markdown-text img').unbind('click').click(function () {
        var img = $(this);
        if (img.parent('a').length <= 0) {
            window.imageBridge.openImage(img.attr('src'));
        }
    });
});

function updateMessages(messages) {
    vmPage.messages = messages;
}

function markAllMessageRead() {
    vmPage.messages.forEach(function (message) {
       message.has_read = true;
    });
}
