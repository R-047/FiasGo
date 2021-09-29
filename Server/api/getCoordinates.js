const { json } = require("body-parser");
const express = require("express");
const getCoordinates = express.Router();
const {getHospitalsCoordinates} = require("../controllers/hereMapsApiConf")

getCoordinates.get("/", async (req, res) => {
	const users_location_lat = req.query.lat;
	const users_location_long = req.query.long;
	let data = await getHospitalsCoordinates("https://discover.search.hereapi.com/v1/discover?at=12.9716,77.5946&limit=10&q=hospitals&in=countryCode:IND");
	data = JSON.parse(JSON.stringify(data));
	const hospitals = data.items.map((hospital) => {
		return {
			"title":hospital.title,
			"lat":hospital.position.lat,
			"long":hospital.position.lng,
			"address":hospital.address.label,
			"distance":hospital.distance,
			"contact":hospital.contacts ? hospital.contacts[0].phone:"no contacts"
		}
	})

	JSON.stringify(hospitals);
	res.json(hospitals);
	//getHospitals()
	//getCamps()
	//get the users location and 
	//give the coordinates for hospitals and cammps near to the users
})

module.exports = getCoordinates;