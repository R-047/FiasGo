const express = require("express");
const setVictimsPostRouter = express.Router();
const getVictimsPostsRouter = express.Router();
const {getVictimsFeedsController, setVictimsPostController} = require("../controllers/PostController")




setVictimsPostRouter.post('/', (req, res) => {
	setVictimsPostController(req, res);
	
})

getVictimsPostsRouter.get('/', (req, res) => {
	getVictimsFeedsController(req, res)
})

module.exports = {setVictimsPostRouter, getVictimsPostsRouter};