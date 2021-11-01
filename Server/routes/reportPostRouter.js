const express = require("express");
const reportPostRouter = express.Router();
const {reportPostController} = require("../controllers/ReportsController")

reportPostRouter.post("/", (req, res)=> {
	reportPostController(req, res)
})

module.exports = {reportPostRouter}