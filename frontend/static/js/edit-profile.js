import {loadHeader, hideMenu} from "./tools.js" ;

function attatchEvents(){
    let editProfileButton = document.getElementById("editProfileButton") ;
    let editProfileForm = document.getElementById("editProfileForm")
    let usernameField = document.getElementById("username") ;
    let passwordField = document.getElementById("password") ;
    let confirmPasswordField = document.getElementById("confirmPassword")
    let emailField = document.getElementById("email") ;

    editProfileButton.addEventListener("click", editProfileEventLoader) ;

    function editProfileEventLoader(event){
        let userToUpdate = {} ;

        userToUpdate.username = usernameField.value ;
        userToUpdate.password = passwordField.value ;
        userToUpdate.confirmPassword = confirmPasswordField.value ;
        userToUpdate.emailToEdit = emailField.value ;
       

        if(sessionStorage.getItem("sessionVerificationToken") !== null){

            userToUpdate.sessionVerificationToken = sessionStorage.getItem("sessionVerificationToken") ;
        }else{

            userToUpdate.sessionVerificationToken = "" ;
        }

        if(sessionStorage.getItem("sessionVerificationToken") !== null){

            userToUpdate.email = sessionStorage.getItem("email") ;
        }else{

            userToUpdate.email = "" ;
        }

        fetch("http://localhost:8000/edit-profile", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"

            },
            body: JSON.stringify(userToUpdate)
        })
        .then(async response => {
            switch(response.status){
                case 200:
                    sessionStorage.setItem("email", emailField.value) ;

                    window.location.href = "http://localhost:8080";
                    break ;
                case 400:
                    let responseText = await response.text();
                    let responseJson = JSON.parse(responseText);
                    let errors = responseJson.errors[0];

                    let errorsWindow = document.createElement("div") ;
                    errorsWindow.className = "errors-window"
                    errors.forEach(element => {
                        let errorDiv = document.createElement("div") ;
                        errorDiv.textContent = element;
                        errorDiv.style.color = "red";
                        errorDiv.style.marginBottom = "5px";
                        errorsWindow.appendChild(errorDiv) ;
                    });
                    
                    
                    editProfileForm.appendChild(errorsWindow) ;

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