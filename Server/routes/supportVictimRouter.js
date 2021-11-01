const express = require("express");
const supportVictimRouter = express.Router();
const getPostSupportRouter = express.Router();
const {supportVictimController,getPostSupportsControllers} = require("../controllers/SupportsControllers");


supportVictimRouter.post('/', (req, res) => {
	supportVictimController(req, res)
})

getPostSupportRouter.get('/', (req, res) => {
	getPostSupportsControllers(req, res)
})


module.exports = {supportVictimRouter, getPostSupportRouter};