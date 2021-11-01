const express = require("express");
const uploadDpImageRouter = express.Router();
const getDpImageRouter = express.Router();
const {getDpController, uploadDpImageController} = require("../controllers/UserController");


uploadDpImageRouter.put('/', (req, res) => {
	uploadDpImageController(req, res)
	
})

getDpImageRouter.get('/', (req, res) => {
	getDpController(req, res)
	
})


module.exports = {uploadDpImageRouter, getDpImageRouter};