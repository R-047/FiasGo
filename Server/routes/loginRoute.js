const express = require("express");
const loginRouter = express.Router();
const {checkForUser} = require("../controllers/checkForUser");

loginRouter.post("/", (req, res) => {
	const email = req.body.email;
	const pwd = req.body.password;
	if(checkForUser(email, pwd)){
		req.session.email = "alby@gmail.com"
		res.send("user found, login successsful")
	}else{
		res.send("user not found") }

})

module.exports = loginRouter;