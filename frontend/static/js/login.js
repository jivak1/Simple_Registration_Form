import {loadHeader, hideMenu} from "./tools.js" ;

function attatchEvents(){
    let loginButton = document.getElementById("loginButton") ;
    let emailField = document.getElementById("email") ;
    let passwordField = document.getElementById("password") ;
    let loginForm = document.getElementById("loginForm") ;
    let verificationField = null ;

    //add event listener
    loginButton.addEventListener("click", loginEventLoader);
    //event handler parses response body, makes fetch request to backend endpoint and backend http handles response
    function loginEventLoader(event){
        //parses user input to json
        let loginCredentials = {} ;

        loginCredentials.email = emailField.value ;
        loginCredentials.password = passwordField.value ;

        if(verificationField !== null){
            loginCredentials.verificationToken = verificationField.value ;
        }else{
            loginCredentials.verificationToken = "" ;
        }
        //fetch POST request to backend endpoint
        fetch("http://localhost:8000/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"

            },
            body: JSON.stringify(loginCredentials)
        })
        .then(async response => { //response handles backend HTTP response
            switch(response.status){
                case 200: //on 200 it parses session token and adds it to session storage together with the email
                    let responseText = await response.text();
                    let responseJson = JSON.parse(responseText);
                    let sessionVerificationToken = responseJson.sessionVerificationToken ;

                    sessionStorage.setItem("sessionVerificationToken", sessionVerificationToken) ;
                    sessionStorage.setItem("email", emailField.value) ;
                    //redirects to index(user is logged)
                    window.location.href = "http://localhost:8080" ;
                    break ;
                case 400://on 400 attatches wrong email or password error
                    let errorDiv = document.createElement("div") ;
                        errorDiv.textContent = "Wrong email or password";
                        errorDiv.style.color = "red";
                        errorDiv.style.marginBottom = "5px";

                        loginForm.appendChild(errorDiv) ;
                        //removes validation error fields after 1 sec
                        setTimeout(() => {
                            errorDiv.remove();
                        }, 1000);
                    break ;
                case 403: //on 403 it attatches email verification token input field
                    if(document.getElementById("verificationCode") === null){
                        let labelVerification = document.createElement("label") ;
                        let inputVerification = document.createElement("input") ;

                        labelVerification.textContent = "Verification Code" ;

                        inputVerification.type = "text" ;
                        inputVerification.name = "verification-code" ;
                        inputVerification.id = "verificationCode"
                        //appends verification code input field
                        loginForm.appendChild(labelVerification) ;
                        loginForm.appendChild(inputVerification) ;

                        let verificationCodeErrorDiv = document.createElement("div") ;

                        verificationCodeErrorDiv.textContent = "First verify your email with the verification code!" ;
                        verificationCodeErrorDiv.style.color = "red";
                        verificationCodeErrorDiv.id = "verificationError" ;
                        //appends verification code required error
                        loginForm.appendChild(verificationCodeErrorDiv) ;

                        verificationField = document.getElementById("verificationCode") ;
                    }else{ //displays error if verification code is wrong
                        document.getElementById("verificationError").textContent = "Wrong verification code, try again"
                    }

                    break ;
            }
        })
    }
}


//adds header
loadHeader().then(hideMenu) ;;
//attatches events for login button
attatchEvents() ;

