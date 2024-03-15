const express = require("express");
const http = require("http");
const fs = require("fs");
const path = require("path");
const app = express();
const port = process.env.PORT || 8080; 

//creates http server 
const server = http.createServer((req, res) => {
    console.log(`Request for ${req.url} by method ${req.method}`);
    //checks every request method(only get methods, because the frontend server purpose is to delegate user actions to the backend)
    if (req.method === "GET") {
        let filePath = "." + req.url ;
        if(!req.url.includes("static")){ //non static resources first
            if (filePath == "./") {
                filePath = "./views/index.html";
            } else if(!req.url.includes(".html")) {
                filePath = "./views" + req.url + ".html";
            }else{
                filePath = "./views/header.html" ; //serves header.html since its an edge case
            }
        }

        //gets ext name
        const fileExt = path.extname(filePath);
        //default content type is html
        let contentType = "text/html";

        //switches file extentions and sets content type acordingly
        switch (fileExt) {
            case ".css":
                contentType = "text/css";
                break;
            case ".js":
                contentType = "application/javascript";
                break;
            case ".jpg":
                contentType = "image/jpg";
                break;
            case ".ico":
                contentType = "image/x-icon";
                break;
        }
        //sends resources to user
        fs.readFile(filePath, (error, content) => {
            if (error) {
                if (error.code == "ENOENT") { //if resource doesnt exist it replaces it with error 404 element in the response
                    res.writeHead(404, { "Content-Type": "text/html" });
                    res.end(`<h1>Error 404: ${filePath} not found</h1>`, "utf-8");
                }
            } else {
                //if resource is found it is sent in the response
                res.writeHead(200, { "Content-Type": contentType });
                res.end(content, "utf-8");
            }
        });
    }
});

//listens on port for requests
server.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
});