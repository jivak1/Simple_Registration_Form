import {loadHeader, hideMenu} from "./tools.js" ;

function attatchEvents(){
    let loginButton = document.getElementById("loginButton") ;
    let emailField = document.getElementById("email") ;
    let passwordField = document.getElementById("password") ;
    let loginForm = document.getElementById("loginForm") ;
    let verificationField = null ;

    loginButton.addEventListener("click", loginEventLoader);

    function loginEventLoader(event){
        let loginCredentials = {} ;

        loginCredentials.email = emailField.value ;
        loginCredentials.password = passwordField.value ;

        if(verificationField !== null){
            loginCredentials.verificationToken = verificationField.value ;
        }else{
            loginCredentials.verificationToken = "" ;
        }

        fetch("http://localhost:8000/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"

            },
            body: JSON.stringify(loginCredentials)
        })
        .then(async response => {
            switch(response.status){
                case 200:
                    let responseText = await response.text();
                    let responseJson = JSON.parse(responseText);
                    let sessionVerificationToken = responseJson.sessionVerificationToken ;

                    sessionStorage.setItem("sessionVerificationToken", sessionVerificationToken) ;
                    sessionStorage.setItem("email", emailField.value) ;

                    window.location.href = "http://localhost:8080" ;
                    break ;
                case 400:
                    let errorDiv = document.createElement("div") ;
                        errorDiv.textContent = "Wrong email or password";
                        errorDiv.style.color = "red";
                        errorDiv.style.marginBottom = "5px";

                        loginForm.appendChild(errorDiv) ;

                        setTimeout(() => {
                            errorDiv.remove();
                        }, 1000);
                    break ;
                case 403:
                    if(document.getElementById("verificationCode") === null){
                        let labelVerification = document.createElement("label") ;
                        let inputVerification = document.createElement("input") ;

                        labelVerification.textContent = "Verification Code" ;

                        inputVerification.type = "text" ;
                        inputVerification.name = "verification-code" ;
                        inputVerification.id = "verificationCode"

                        loginForm.appendChild(labelVerification) ;
                        loginForm.appendChild(inputVerification) ;

                        let verificationCodeErrorDiv = document.createElement("div") ;

                        verificationCodeErrorDiv.textContent = "First verify your email with the verification code!" ;
                        verificationCodeErrorDiv.style.color = "red";
                        verificationCodeErrorDiv.id = "verificationError" ;

                        loginForm.appendChild(verificationCodeErrorDiv) ;

                        verificationField = document.getElementById("verificationCode") ;
                    }else{
                        document.getElementById("verificationError").textContent = "Wrong verification code, try again"
                    }

                    break ;
            }
        })
    }
}



loadHeader().then(hideMenu) ;;

attatchEvents() ;

