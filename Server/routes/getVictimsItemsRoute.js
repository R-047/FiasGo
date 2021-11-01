const {getVictimsItemsController}  =require("../controllers/SupportsControllers");
const express = require("express")
const getVictimsItemRouter = express.Router();

getVictimsItemRouter.get("/", (req, res) => {
	getVictimsItemsController(req, res);
})

module.exports = {getVictimsItemRouter}