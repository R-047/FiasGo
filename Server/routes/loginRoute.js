const express = require("express");
const loginRouter = express.Router();
const {userAuth} = require("../controllers/UserController")

loginRouter.post("/", (req, res) => {
	userAuth(req, res);
})

module.exports = loginRouter;