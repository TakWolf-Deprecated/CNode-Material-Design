
Vue.filter('relativeTimeSpan', function (time) {
    return window.formatBridge.getRelativeTimeSpanString(time);
});

Vue.filter('avatarCompat', function (url) {
    if (url && url.indexOf('//gravatar.com/avatar/') === 0) {
        return 'http:' + url;
    } else {
        return url;
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
        },

        openImageProxy: function (event) {
            if (event.target.nodeName === 'IMG') {
                if (event.target.parentNode.nodeName !== 'A') {
                    window.imageBridge.openImage(event.target.src);
                }
            }
        }

    }
});

function updateMessages(messages) {
    vmPage.messages = messages;
}

function markAllMessageRead() {
    vmPage.messages.forEach(function (message) {
       message.has_read = true;
    });
}
