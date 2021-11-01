const express = require("express");
const setServicesPostRouter = express.Router();
const getServicesPostsRouter = express.Router();
const {setServicesPostController, getServicesFeedsController} = require("../controllers/PostController")




setServicesPostRouter.post('/', (req, res) => {
	setServicesPostController(req, res);
	
})

getServicesPostsRouter.get('/', (req, res) => {
	getServicesFeedsController(req, res)
})

module.exports = {setServicesPostRouter, getServicesPostsRouter};