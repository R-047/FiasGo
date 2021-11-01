const express = require("express");
const getPostImagesRouter = express.Router();
const {getPostsImages} = require("../controllers/PostController");


getPostImagesRouter.get('/', (req, res) => {
	getPostsImages(req, res);
	
})


module.exports = {getPostImagesRouter};