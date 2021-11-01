const {postCommentsController, getPostCommentsController, deletePostCommentsController} = require("../controllers/PostController");
const express = require("express");
const postCommentsRouter = express.Router();
const getCommentsRouter = express.Router();
const deleteCommentsRouter = express.Router();


postCommentsRouter.post('/', (req, res) => {
	postCommentsController(req, res);
	
})

getCommentsRouter.get('/', (req, res) => {
	getPostCommentsController(req, res);
	
})

deleteCommentsRouter.delete('/', (req, res) => {
	deletePostCommentsController(req, res);
})

module.exports = {postCommentsRouter, getCommentsRouter, deleteCommentsRouter};
