function getpost() {
    fetch('/api/posts').then(response => {
        return response.json()
    }).then(posts => {
        posts.forEach(post => {
            createDiv(post.title, post.body)
        });
    })
}


function createDiv(title, text) {
    let divElement = document.createElement("div")
    let textElement = document.createTextNode(`${title}: ${text}`)
    divElement.appendChild(textElement)
    document.getElementById("main-block-container").appendChild(divElement)
}
