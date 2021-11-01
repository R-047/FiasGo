const {updateUpvotesVictimsPostController, updateUpvotesServicesPostController} = require("../controllers/PostController");
const express = require("express");
const victimsUpvotesRouter = express.Router();
const servicesUpvotesRouter = express.Router();


victimsUpvotesRouter.post('/', (req, res) => {
	updateUpvotesVictimsPostController(req, res);
	
})

servicesUpvotesRouter.post('/', (req, res) => {
	updateUpvotesServicesPostController(req, res);
	
})


module.exports = {victimsUpvotesRouter, servicesUpvotesRouter};