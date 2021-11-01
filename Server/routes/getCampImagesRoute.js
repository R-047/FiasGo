const express = require("express");
const getCampsImagesRouter = express.Router();
const {getCampsImages} = require("../controllers/MapsController");


getCampsImagesRouter.get('/', (req, res) => {
	getCampsImages(req, res);
	
})


module.exports = {getCampsImagesRouter};