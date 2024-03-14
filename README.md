**Please oped the README file and read it as code!**

Here is my solution to the registration form task, it consists of three parts:
1)A node.js frontend server that serves views to the user and makes fetch api calls to the backend server trough event listeners(js scripts inside the views).
2)A java backend server that recieves HTTP requests on his endpoints, takes care of validation, login logic, email verification and persists user information to the sql server trough JPA.
3)A MySQL server that stores user information

1) The frontend server includes several directories and files:
   - server.js file that creates an http server in order to serve static resources and views upon GET requests
   - static directory that contains all static resources
     . images directory that contains the background image and all future image resources
     . js directory that contains all js scripts, that attatch event handlers, handle http requests from the backend server, display validation errors and redirext the user if needed
        ~ All scripts attatch the header, so I will not be including it in further script specification
        ~ register.js attatches event listeners, sends a fetch api request to the backend and displays validation errors.
        ~ login.js attatches event listeners on the login view and sends user information to the backend server. Upon a 200 response it saves session token and email to the session storage and redirects to index(when the session token is stored the user has succesfully logged in). Upon 403 response it adds an additional email verification token field. Upon a 400 request it displays all validation errors.
        ~ edit-profile.js attatches event listeners, sends a fetch api request ot the backend server (including the session verification token), displays validation errors and updates stored in the session email if its changed.
     . styles directory that has the css for all html elements
   - views directory that contains all views, that are sent to the user
2) The backend server includes two main directoris main and test. The server logic is inside the main and unit tests are inside the test
   - Main class creates an http server and routes all http requests to its three current endpoints - login, register and eddit-user
   - controllers directory incudes all controllers that handle incoming http requests
     . Register controller handles two types of requests - "POST" and "OPTIONS".
       ~ When an OPTIONS request is recieved it sends a 204 response with the needed CORS headers(for demo purposes both frontend and backend endoints are on the same origin - localhost). This information is valid for all other controllers, so I will be not incuding it in further controllers.
       ~ When a POST request is recieved the request body (type json sent from the frontend) it gets parsed to a local JSONObject, the input is validated and if all user information is correct a new user is created and persisted to the DB, otherwise the appropriate respose is sent, including all validation errors. Email verification email is sent as well.
     . Login controller handles login attempts, verifies user information, including email verification token and returns the appropriate HTTP response.
       ~ When a POST request is recieved the controller verifies user information, including email verification token and returns the appropriate HTTP response. If the response is 200 the response body includes a session verification token, that can be used to validate further requests from the logged in user.
     . Edit user controller handles PUT requests.
       ~ When a PUT request is made to update user information the controller validates user information(incuding validating the user session token), persists it if there are no validation errors and returns the appropriate HTTP response.
   - email directory includes the Email sender class, that sends account confirmation emails trough jakarta mail api with google as smtp server host.
   - model directory includes two entities - Base Entity and User Entitty, that are annotated with the appropriate JPA annotations
     . Base entity, that is inherited by all other entities and includes Id incrementation  logic
     . User entity, that contains all user fielsd, that are persisted to the bd, including a active flag, that is set to true when the email is verified and the current session validation token, that is sent to the user in their most recent login.
   - service directory includes the User service, that takes care of CRUD opperations to the DB trough JPA entity manager
   - utils directory includes utility class HTTPUtils, that provides additional CORS header attatchment and request body parsing logic
   - validation directory includes two classes - ValidationError and InputValidator
     . ValidationError represents each validation error
     . InputValidator validates all input and returns a list of validation errors (list might be empty if there are no errors)
3) The MySQL DB server contains all user information and serves it to the backend surver upon request. It contains all JPA anotated fields.

In order to start my application you shold: 
   1) Go to the frontend directory with terminal and input the command: npm start
   2) Go to the backend directory and start the backend server with the command: javac Main.java && java Main
      - make sure to set up you sql server before launch of the backend
      - input DB password in the persistance.xml
      - input correct gmail credentials for the confirmation email sender inside the EmailSender class(including app password).
      
