import {loadHeader, hideMenu} from "./tools.js" ;


function attatchEvents(){
    let registerButton = document.getElementById("registerButton") ;
    let registrationForm = document.getElementById("registrationForm")
    let usernameField = document.getElementById("username") ;
    let passwordField = document.getElementById("password") ;
    let confirmPasswordField = document.getElementById("confirmPassword")
    let emailField = document.getElementById("email") ;

    //add event listener
    registerButton.addEventListener("click", registerEventLoader) ;

    //event handler parses user input to json and fetches it to backend endpoint
    function registerEventLoader(event){
        let userToRegister = {} ;

        userToRegister.username = usernameField.value ;
        userToRegister.password = passwordField.value ;
        userToRegister.confirmPassword = confirmPasswordField.value ;
        userToRegister.email = emailField.value ;

        //fetch post request
        fetch("http://localhost:8000/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"

            },
            body: JSON.stringify(userToRegister)
        })
        .then(async response => {  //response handles http response from backend server
            switch(response.status){
                case 200: //on response 200 redirects user to index
                    window.location.href = "http://localhost:8080";
                    break ;
                case 400: //on 400 response displays validation errors
                    //parse json from response body
                    let responseText = await response.text();
                    let responseJson = JSON.parse(responseText);
                    let errors = responseJson.errors[0];

                    //creates error html elements and appends them to errors window container
                    let errorsWindow = document.createElement("div") ;
                    errorsWindow.className = "errors-window"
                    errors.forEach(element => {
                        let errorDiv = document.createElement("div") ;
                        errorDiv.textContent = element;
                        errorDiv.style.color = "red";
                        errorDiv.style.marginBottom = "5px";
                        errorsWindow.appendChild(errorDiv) ;
                    });
                    
                    //appends error window to registration form
                    registrationForm.appendChild(errorsWindow) ;

                    //removes malidation error mesages after 5 seconds
                    setTimeout(() => {
                        errorsWindow.remove();
                    }, 5000);
                    
                   
                    break ;
            }
        })
    }
}
//loads header
loadHeader().then(hideMenu) ;
//attatches events
attatchEvents() ;