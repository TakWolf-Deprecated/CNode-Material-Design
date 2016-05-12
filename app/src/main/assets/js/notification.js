
var MINUTE = 60 * 1000;
var HOUR = 60 * MINUTE;
var DAY = 24 * HOUR;
var WEEK = 7 * DAY;
var MONTH = 31 * DAY;
var YEAR = 12 * MONTH;

Vue.filter('recentlyTimeText', function (time) {
    var diff = Date.parse(new Date()) - Date.parse(new Date(time));
    if (diff > YEAR) {
        return Math.floor(diff / YEAR) + "年前";
    } else if (diff > MONTH) {
        return Math.floor(diff / MONTH) + "个月前";
    } else if (diff > WEEK) {
        return Math.floor(diff / WEEK) + "周前";
    } else if (diff > DAY) {
        return Math.floor(diff / DAY) + "天前";
    } else if (diff > HOUR) {
        return Math.floor(diff / HOUR) + "小时前";
    } else if (diff > MINUTE) {
        return Math.floor(diff / MINUTE) + "分钟前";
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

var vmList = new Vue({
    el: '#list',
    data: {
        messages: []
    },
    methods: {
        
        openTopic: function (topic_id) {
            window.notificationBridge.openTopic(topic_id);
        },
        
        openUser: function (loginname) {
            window.notificationBridge.openUser(loginname);
        }
        
    }
});

vmList.$watch('messages', function () {
    $('.markdown-text img').click(function () {
        var img = $(this);
        if (img.parent('a').length <= 0) {
            window.imageBridge.openImage(img.attr('src'));
        }
    });
});

function updateMessages(messages) {
    vmList.messages = messages;
}

function markAllMessageRead() {
    vmList.messages.forEach(function (message) {
       message.has_read = true;
    });
}
