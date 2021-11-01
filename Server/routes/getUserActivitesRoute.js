const express = require("express");
const getUserActivitiesRouter = express.Router();
const {getUserActivitiesController} = require("../controllers/UserController");


getUserActivitiesRouter.get('/', (req, res) => {
	getUserActivitiesController(req, res);	
})


module.exports = {getUserActivitiesRouter};