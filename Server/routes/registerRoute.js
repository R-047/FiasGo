const express = require("express");
const registerRouter = express.Router();
const {userRegister} = require("../controllers/UserController")

registerRouter.post("/", (req, res) => {
	userRegister(req, res)
})

module.exports = registerRouter;