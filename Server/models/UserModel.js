const {getClient} = require('../Utils/DBConfig');
const { getUsersCamps } = require('./MapsModel');
const {getUsersVictimsPost, getUsersServicesPost} = require('./PostModel');
const {getUserSupports} = require('./SupportsModel')

const authuser = async (email, password) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select user_id, user_name, user_email, ph_num, profile_pic, user_status  from users where user_email = '${email}' and password = '${password}'`);
		await client.end();
		res(result)		
	})
}

const registerUser = async (username, email, password, phone_num, join_date_time, profile_pic) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`insert into users(user_name,password,user_email,profile_pic,join_date_time,ph_num) values('${username}','${password}','${email}','${null}','${join_date_time}','${phone_num}')`);
		await client.end();
		res(result)		
	})

}


const updateUserDpImageName = async (user_id, image_name) =>{
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`update users set profile_pic = '${image_name}' where user_id = ${user_id}`);
		await client.end();
		res(result)		
	})
}


const getUserDpPath = async (user_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select profile_pic from users where user_id = ${user_id}`);
		await client.end();
		res(result)		
	})
}



const updateUserStatus = async (user_id, status) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
	const result = await client.query(`update users set user_status = '${status}' where user_id = ${user_id}`);
		await client.end();
		res(result)	
	})
}

const getUserActivites = async (user_id) => {
	return new Promise(async (res, rej) => {
		let arr_of_obj = [];
	const users_victims_posts = await getUsersVictimsPost(user_id);
	const users_service_posts = await getUsersServicesPost(user_id)
	const users_supports = await getUserSupports(user_id)
	const users_camps = await getUsersCamps(user_id);
	users_victims_posts.forEach(element => {
		element.type = "RFH"
		element.date = element.date.toString()
		arr_of_obj.push(element)
	});
	users_service_posts.forEach(element => {
		element.type = "SERVICE"
		element.date = element.date.toString()
		arr_of_obj.push(element)
	})
	if(users_supports.rows!==null){
		users_supports.rows.forEach(element => {
			element.type = "SUPPORT"
			element.date = element.date.toString()
			arr_of_obj.push(element)
		})
	}
	users_camps.forEach(element => {
		element.type = "CAMP"
		element.date = element.opening_date.toString()
		arr_of_obj.push(element)
	})

	arr_of_obj.sort(function (a, b) {
		return new Date(b.date) - new Date(a.date);
	      });

	// console.log(arr_of_obj);
	res(arr_of_obj)
	})
}

const getUsers = async() => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select user_id, user_name, user_email, ph_num, profile_pic, join_date_time, user_status  from users`);
		await client.end();
		res(result)		
	})
}






module.exports = {authuser, registerUser, updateUserDpImageName, getUserDpPath, getUserActivites, updateUserStatus, getUsers}