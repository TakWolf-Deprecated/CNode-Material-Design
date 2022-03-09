
function test() {
    fetch('https://cnodejs.org/api/v1/topic/58eee565a92d341e48cfe7fc?mdrender=true')
        .then(function (response) {
            return response.json()
        })
        .then(function (result) {
            if (result.success) {
                console.log(result)
                updateContent(result.data.content)
            } else {
                console.error(result)
            }
        })
        .catch(function (e) {
            console.error(e)
        })
}

setTimeout(test, 500)
