
var accessToken = 'your-access-token'

function login() {
    fetch('https://cnodejs.org/api/v1/accesstoken', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'accesstoken=' + accessToken
    })
        .then(function (response) {
            return response.json()
        })
        .then(function (result) {
            if (result.success) {
                console.log(result)
                updateAccountId(result.id)
                loadTopic()
            } else {
                console.error(result)
            }
        })
        .catch(function (e) {
            console.error(e)
        })
}

function loadTopic() {
    fetch('https://cnodejs.org/api/v1/topic/58eee565a92d341e48cfe7fc?accesstoken=' + accessToken + '&mdrender=true')
        .then(function (response) {
            return response.json()
        })
        .then(function (result) {
            if (result.success) {
                console.log(result)
                result.data.content = {
                    html: result.data.content
                }
                result.data.replies.forEach(function (reply) {
                    reply.content = {
                        html: reply.content
                    }
                })
                updateTopic(result.data)
            } else {
                console.error(result)
            }
        })
        .catch(function (e) {
            console.error(e)
        })
}

setTimeout(login, 500)