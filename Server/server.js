const express = require("express");
const cookieParser = require("cookie-parser");
const expressSession = require("express-session");
const {Authenticate} = require("./middlewares/authenticate");
const bodyParser = require("body-parser");
const app = express();

//utility middlewares
app.use(cookieParser());
app.use(expressSession({secret:"key"}));
app.use(bodyParser.urlencoded({
	extended: true
}))



app.use("/login", require("./routes/loginRoute"));
app.use(Authenticate);
app.use('/testRoute', require("./routes/testRoute"));
app.use('/register', require("./routes/registerRoute"));




//apis
app.use('/api/getCoordinates', require("./api/getCoordinates"));
app.use('/api/getNews', require("./api/getNews"));



//login
//registration
//co-ordinates of maps
//victims posts
//service posts
//upload victims post
//uplooad service post
//get news
//Users Activity

app.listen(3000, () => console.log("server running on port 3000"))