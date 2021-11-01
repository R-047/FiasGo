const express = require("express");
const removeCampsRouter = express.Router();
const {takeDownCampController} = require("../controllers/MapsController");


removeCampsRouter.delete('/', (req, res) => {
	takeDownCampController(req, res);	
})


module.exports = {removeCampsRouter};