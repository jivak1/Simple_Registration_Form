import {loadHeader, hideMenu} from "./tools.js" ;


function attatchEvents(){
    let registerButton = document.getElementById("registerButton") ;
    let registrationForm = document.getElementById("registrationForm")
    let usernameField = document.getElementById("username") ;
    let passwordField = document.getElementById("password") ;
    let confirmPasswordField = document.getElementById("confirmPassword")
    let emailField = document.getElementById("email") ;

    registerButton.addEventListener("click", registerEventLoader) ;

    function registerEventLoader(event){
        let userToRegister = {} ;

        userToRegister.username = usernameField.value ;
        userToRegister.password = passwordField.value ;
        userToRegister.confirmPassword = confirmPasswordField.value ;
        userToRegister.email = emailField.value ;

        fetch("http://localhost:8000/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"

            },
            body: JSON.stringify(userToRegister)
        })
        .then(async response => {
            switch(response.status){
                case 200:
                    window.location.href = "http://localhost:8080";
                    break ;
                case 400:
                    let responseText = await response.text();
                    let responseJson = JSON.parse(responseText);
                    let errors = responseJson.errors[0];

                    console.log(errors) ;

                    let errorsWindow = document.createElement("div") ;
                    errorsWindow.className = "errors-window"
                    errors.forEach(element => {
                        let errorDiv = document.createElement("div") ;
                        errorDiv.textContent = element;
                        errorDiv.style.color = "red";
                        errorDiv.style.marginBottom = "5px";
                        errorsWindow.appendChild(errorDiv) ;
                    });
                    
                    
                    registrationForm.appendChild(errorsWindow) ;

                    setTimeout(() => {
                        errorsWindow.remove();
                    }, 5000);
                    
                   
                    break ;
            }
        })
    }
}

loadHeader().then(hideMenu) ;

attatchEvents() ;