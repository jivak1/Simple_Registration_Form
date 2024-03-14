
async function loadHeader(){
    await fetch("header.html")
    .then(response => response.text())
    .then(html => {
        let header = document.createElement("header") ;
        header.innerHTML = html;
        document.body.prepend(header);
    })

    let logoutButton = document.getElementById("logout") ;

    logoutButton.addEventListener("click", logoutEventLoader) ;

    function logoutEventLoader(){
        sessionStorage.clear() ;

        window.location.href = "http://localhost:8080" ;
    }


}

function hideMenu(){
    let home = document.getElementById("home") ;
    let proile = document.getElementById("profile") ;
    let shop = document.getElementById("shop") ;
    let register = document.getElementById("register") ;
    let login = document.getElementById("login") ;
    let logout = document.getElementById("logout") ;





    if(sessionStorage.getItem("sessionVerificationToken") === null){
        home.style.display = "none" ;
        shop.style.display = "none" ;
        logout.style.display = "none"
        proile.style.display = "none" ;
    }else{
        register.style.display = "none" ;
        login.style.display = "none" ;
    }
}

export { loadHeader, hideMenu };

