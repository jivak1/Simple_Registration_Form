import {loadHeader, hideMenu} from "./tools.js" ;

function attatchEvents(){
    let editProfileButton = document.getElementById("editProfileButton") ;
    let editProfileForm = document.getElementById("editProfileForm")
    let usernameField = document.getElementById("username") ;
    let passwordField = document.getElementById("password") ;
    let confirmPasswordField = document.getElementById("confirmPassword")
    let emailField = document.getElementById("email") ;

    //adds event listener
    editProfileButton.addEventListener("click", editProfileEventLoader) ;
    //event listener renders user input to json, makes PUT request and handles backend HTTP response
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
        //fetch PUT request to backend endpoint
        fetch("http://localhost:8000/edit-profile", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"

            },
            body: JSON.stringify(userToUpdate)
        })
        .then(async response => { //handles response and displays validation errors if any
            switch(response.status){
                case 200:
                    //session storage email is updated(if its the same as before there is no bug, it just puts the same one)
                    sessionStorage.setItem("email", emailField.value) ;

                    //redirect to index if user edit is complete
                    window.location.href = "http://localhost:8080";
                    break ;
                case 400: //on 400 it displays validation errors
                    let responseText = await response.text();
                    let responseJson = JSON.parse(responseText);
                    let errors = responseJson.errors[0];
                    //appends validation errors to errors window container
                    let errorsWindow = document.createElement("div") ;
                    errorsWindow.className = "errors-window"
                    errors.forEach(element => {
                        let errorDiv = document.createElement("div") ;
                        errorDiv.textContent = element;
                        errorDiv.style.color = "red";
                        errorDiv.style.marginBottom = "5px";
                        errorsWindow.appendChild(errorDiv) ;
                    });
                    
                    //appends errors window to edit form
                    editProfileForm.appendChild(errorsWindow) ;
                    //removes validation error fields after  seconds
                    setTimeout(() => {
                        errorsWindow.remove();
                    }, 5000);
                    
                   
                    break ;
            }
        })
    }
}
//adds header
loadHeader().then(hideMenu) ;
//attatches events
attatchEvents() ;