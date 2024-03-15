//attatches header to all views + adds event handlers for logout button 
async function loadHeader(){
    //hetches header html and attatches it to the view
    await fetch("header.html")
    .then(response => response.text())
    .then(html => {
        let header = document.createElement("header") ;
        header.innerHTML = html;
        document.body.prepend(header);
    })

    let logoutButton = document.getElementById("logout") ;
    //adds event listener
    logoutButton.addEventListener("click", logoutEventLoader) ;

    //event handler clears session variables and logs out the user
    function logoutEventLoader(){
        sessionStorage.clear() ;

        window.location.href = "http://localhost:8080" ;
    }


}
//hides or displays elements based on user login
function hideMenu(){
    let home = document.getElementById("home") ;
    let proile = document.getElementById("profile") ;
    let shop = document.getElementById("shop") ;
    let register = document.getElementById("register") ;
    let login = document.getElementById("login") ;
    let logout = document.getElementById("logout") ;




    //checks session verification token and displays content based or user authentication
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
//export tools to other js scripts
export { loadHeader, hideMenu };

