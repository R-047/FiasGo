const { json } = require("body-parser");
const express = require("express");
const getCamps = express.Router();
const setCamps = express.Router();
const getUsersCamps = express.Router();
const {getCoordinatesController, hostNewCampController, getUsersCampsController} = require("../controllers/MapsController")


getCamps.get("/", (req, res) => {
	getCoordinatesController(req, res);
})

setCamps.post('/', (req, res) => {
	hostNewCampController(req, res);
})

getUsersCamps.get('/', (req, res) => {
	getUsersCampsController(req, res);
})



module.exports = {getCamps, setCamps, getUsersCamps};