const {UseHereMaps} = require("../Utils/hereMapsApiConf")
const {getCampsModel, postCampsModel, takeDownCamp, getUsersCamps} = require("../models/MapsModel")
const {CampImageUpload} = require("../Utils/configStorageEngine")
const path = require("path");
const {getRootDir} = require("../Utils/getRootDir")

const getCoordinatesController = async (req, res) => {
	const users_location_lat = req.query.lat;
	const users_location_long = req.query.long;
	let hospitals_data = await UseHereMaps(`https://discover.search.hereapi.com/v1/discover?at=${users_location_lat},${users_location_long}&limit=10&q=hospitals&in=countryCode:IND`);
	hospitals_data = JSON.parse(JSON.stringify(hospitals_data));
	const hospitals = hospitals_data.items.map((hospital) => {
		return {
			"title":hospital.title,
			"lat":hospital.position.lat,
			"long":hospital.position.lng,
			"address":hospital.address.label,
			"distance":hospital.distance,
			"contact":hospital.contacts ? hospital.contacts[0].phone:"no contacts"
		}
	})
	let camps_data = await getCampsModel(users_location_lat, users_location_long);

	JSON.stringify(hospitals);
	let payload = {"error":"null"};
	payload.HospitalList = hospitals;
	payload.CampsList = camps_data;
	const json_payload = JSON.stringify(payload)
	res.json(JSON.parse(json_payload));

}


const hostNewCampController = (req, res) => {
	CampImageUpload(req, res, async (err) =>  {
		if (err) {
			return res.end("Error uploading file.");
		}else{
			const camp_images = []
				req.files.forEach(element => {
					camp_images.push(element.filename)
				});
				// camp_name: any, person_gov_id: any, openin_date: any, closing_date: any, time_from: any, time_till: any, user_id: any, desc: any, camp_lat: any, camp_long: any, camp_location_name: any, contact: any, camp_images_names: any
			const user_id = req.body.user_id;
			const camp_name = req.body.camp_name;
			const person_gov_id = req.body.id;
			const openin_date = req.body.opening_date;
			const closing_date = req.body.closing_date;
			const time_from = req.body.time_from;
			const time_till = req.body.time_till;
			const camp_desc = req.body.camp_desc;
			const camp_lat = req.body.camp_lat;
			const camp_long = req.body.camp_long;
			const contact = req.body.contact;
			const address_result = await UseHereMaps(`https://revgeocode.search.hereapi.com/v1/revgeocode?at=${camp_lat}%2C${camp_long}&lang=en-US`)
			//const location_name = address_result.items[0].address.county +","+address_result.items[0].address.state
			const location_name = address_result.items[0].address.label
			await postCampsModel(camp_name, person_gov_id, openin_date, closing_date, time_from, time_till, user_id, camp_desc, camp_lat, camp_long, location_name, contact, camp_images) ? res.json(JSON.parse('{"status":"Camp hosted successfuly","error":"null"}')) : res.json(JSON.parse('{"status":"Camp not hosted","error":"FileNotUploaded"}'));
			
			
		}	
	});
}

const getCampsImages = async (req, res) => {
	const imgFile = req.query.imageName;
	res.sendFile(path.join(getRootDir(), "Uploads", "CampImages", imgFile));
}

const takeDownCampController = async (req, res) => {
	const camp_id = req.body.camp_id;
	await takeDownCamp(camp_id) ? res.json(JSON.parse('{"status":"Camp removed successfully","error":"null"}')) : res.json(JSON.parse('{"status":"Camp not removed","error":"error occured"}'))
	
}

const getUsersCampsController = async (req, res) => {
	const user_id = req.query.user_id
	const result = await getUsersCamps(user_id);
	res.json(JSON.parse(JSON.stringify(result)));
}



module.exports= {getCoordinatesController, hostNewCampController, getCampsImages, takeDownCampController, getUsersCampsController}