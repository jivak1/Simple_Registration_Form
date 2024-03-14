const express = require("express");
const http = require("http");
const fs = require("fs");
const path = require("path");
const app = express();
const port = process.env.PORT || 8080;


const server = http.createServer((req, res) => {
    console.log(`Request for ${req.url} by method ${req.method}`);

    if (req.method === "GET") {
        let filePath = "." + req.url ;
        if(!req.url.includes("static")){
            if (filePath == "./") {
                filePath = "./views/index.html";
            } else if(!req.url.includes(".html")) {
                filePath = "./views" + req.url + ".html";
            }else{
                filePath = "./views/header.html" ;
            }
        }

        const fileExt = path.extname(filePath);
        let contentType = "text/html";

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

        fs.readFile(filePath, (error, content) => {
            if (error) {
                if (error.code == "ENOENT") {
                    res.writeHead(404, { "Content-Type": "text/html" });
                    res.end(`<h1>Error 404: ${filePath} not found</h1>`, "utf-8");
                } else {
                    res.writeHead(500);
                    res.end(`Sorry, check with the site admin for error: ${error.code} ..\n`);
                }
            } else {
                res.writeHead(200, { "Content-Type": contentType });
                res.end(content, "utf-8");
            }
        });
    }
});

server.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
});