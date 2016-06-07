
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

Vue.filter('tabName', function (topic) {
    if (topic.top) {
        return '置顶';
    } else {
        switch (topic.tab) {
            case 'share':
                return '分享';
            case 'ask':
                return '问答';
            case 'job':
                return '招聘';
            default:
                return '全部';
        }
    }
});

var vmPage = new Vue({
    el: '#page',
    data: {
        userId: '',
        topic: {},
        positionMap: {}
    },
    methods: {

        collectTopic: function (topic) {
            if (topic.is_collect) {
                window.topicBridge.decollectTopic(topic.id);
            } else {
                window.topicBridge.collectTopic(topic.id);
            }
        },

        upReply: function (reply) {
            window.topicBridge.upReply(JSON.stringify(reply));
        },

        at: function (reply, positionMap) {
            window.topicBridge.at(JSON.stringify(reply), positionMap[reply.id]);
        },

        openUser: function (loginName) {
            window.topicBridge.openUser(loginName);
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

function updateTopicAndUserId(topic, userId) {
    vmPage.topic = topic;
    vmPage.userId = userId;
    var positionMap = {};
    if (topic.replies) {
        for (var i = 0; i < topic.replies.length; i++) {
            var reply = topic.replies[i];
            positionMap[reply.id] = i;
        }
    }
    vmPage.positionMap = positionMap;
}

function updateTopicCollect(isCollect) {
    if (vmPage.topic) {
        vmPage.topic.is_collect = isCollect;
    }
}

function updateReply(reply) {
    if (vmPage.topic && vmPage.topic.replies) {
        for (var i = 0; i < vmPage.topic.replies.length; i++) {
            var replyN = vmPage.topic.replies[i];
            if (replyN.id === reply.id) {
                vmPage.topic.replies.splice(i, 1, reply);
                break;
            }
        }
    }
}

function appendReply(reply) {
    if (vmPage.topic) {
        if (!vmPage.topic.replies) {
            vmPage.topic.replies = [];
        }
        vmPage.topic.replies.push(reply);
    }
}
