const express = require("express");
const registerRouter = express.Router();
const {registerUser} = require("../models/registerUser")

registerRouter.post((req, res) => {
	const username = req.body.name;
	const email = req.body.email;
	const password = req.body.password;
	const phone_number = req.body.ph_number;
	registerUser(username, email, password, phone_number)
})

module.exports = registerRouter;